/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7.socket.netty
 * FILE S7Client.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-11-13
 * ==============================================================================
 * Copyright (C) 2022
 * KiMoJar All rights reserved
 * ==============================================================================
 * This project is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or under
 * EPL Eclipse Public License 1.0.
 * 
 * This means that you have to chose in advance which take before you import
 * the library into your project.
 * 
 * This project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE whatever license you
 * decide to adopt.
 * ==============================================================================
 */
package com.kimojar.util.community.plc.siemens.s7.socket.netty;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kimojar.util.community.data.ByteArray;
import com.kimojar.util.community.plc.siemens.s7.DataBlock;
import com.kimojar.util.community.plc.siemens.s7.S7Event;
import com.kimojar.util.community.plc.siemens.s7.telegram.COTP;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7.ErrorClassCode;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7Constant.ConnectionType;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7Constant.ErrorCode;
import com.kimojar.util.community.plc.siemens.s7.telegram.TPKT;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 基于netty而实现的PLC Client.
 * <li>由于netty提供的socket实现是非阻塞的,其在响应速度上无法满足高度读取需求,以及控制粒度太粗,所以其主要目的只是学习参考使用</li>
 * <li>20200424,经过改造后,netty实现了同步,响应速度还不错,虽然控制粒度也还稍微有点粗,但也满足基本使用,
 * 并且通过netty实现s7客户端,解决了oio实现s7客户端存在的脏数据风险问题.</li><br>
 * 如果需要扩展到实践中,为了提高效率,建议去除trace级别的日志记录以提高读写速度.<br>
 * 我做过测试,s7Client.readDB(1, 0, 2000, 200),循环读1000次:<br>
 * 存在trace日志记录时,trace日志级别,也就是记录出trace日志,耗费8133ms,8806ms;<br>
 * 存在trace日志记录时,debug日志级别,也就是存在trace但是不记录出来,耗费3880ms,3771ms;<br>
 * 不存在trace日志记录时,耗费3021ms,3106ms,3006ms.<br>
 * <br>
 * s7Client.writeDB(1, 0, 10字节, 200),循环写10000次:<br>
 * 存在trace日志记录时,trace日志级别,也就是记录出trace日志,耗费5736ms,5551ms;<br>
 * 存在trace日志记录时,debug日志级别,也就是存在trace但是不记录出来,耗费3086ms;<br>
 * 不存在trace日志记录时,耗费2900ms.<br>
 * 
 * @author KiMoJar
 * @date 2020-04-09
 */
public class S7Client {

	public static final Logger logger = LoggerFactory.getLogger(S7Client.class);

	private EventLoopGroup acceptorGroup;
	private Bootstrap clientBoot;
	private S7ClientHandler clientHandler;
	private ChannelFuture clientFuture;

	private String clientIP;
	private int clientPort;
	private String serverIP;
	private int serverPort;
	private ConnectionType connType;
	private int rack;
	private int slot;
	private int pduLength;
	private int singleReadMaxPermitLength = 200;
	private int singleWriteMaxPermitLength = 200;

	/**
	 * S7客户端构造函数,以Program方式作为客户端,系统自动选择clientIP和clientPort.默认PDULength为480.详细参见:{@link S7Client#S7Client(ConnectionType, int, String, int, String, int, int, int)}
	 * 
	 * @param serverIP 服务端IP.
	 * @param serverPort 服务端端口.通常为102,若小于等于0则默认为102.
	 * @param rack CPU机架号.
	 * @param slot CPU槽号.
	 */
	public S7Client(String serverIP, int serverPort, int rack, int slot) {
		this(ConnectionType.PG, null, 0, serverIP, serverPort, rack, slot, 480);
	}

	/**
	 * S7客户端构造函数,以Program方式作为客户端.默认PDULength为480.详细参见:{@link S7Client#S7Client(ConnectionType, int, String, int, String, int, int, int)}
	 * 
	 * @param clientIP 客户端IP.
	 * @param clientPort 客户端端口.
	 * @param serverIP 服务端IP.
	 * @param serverPort 服务端端口.通常为102,若小于等于0则默认为102.
	 * @param rack CPU机架号.
	 * @param slot CPU槽号.
	 */
	public S7Client(String clientIP, int clientPort, String serverIP, int serverPort, int rack, int slot) {
		this(ConnectionType.PG, clientIP, clientPort, serverIP, serverPort, rack, slot, 480);
	}

	/**
	 * S7客户端构造函数,以Program方式作为客户端.详细参见:{@link S7Client#S7Client(ConnectionType, int, String, int, String, int, int, int)}
	 * 
	 * @param clientIP 客户端IP.
	 * @param clientPort 客户端端口.
	 * @param serverIP 服务端IP.
	 * @param serverPort 服务端端口.通常为102,若小于等于0则默认为102.
	 * @param rack CPU机架号.
	 * @param slot CPU槽号.
	 * @param pduLength PDU期望长度.
	 */
	public S7Client(String clientIP, int clientPort, String serverIP, int serverPort, int rack, int slot, int pduLength) {
		this(ConnectionType.PG, clientIP, clientPort, serverIP, serverPort, rack, slot, pduLength);
	}

	/**
	 * S7客户端构造函数.
	 * 
	 * @param connType 连接PLC的连接类型.若为null则默认为PG的类型.
	 * @param clientIP 客户端IP.
	 * @param clientPort 客户端端口.
	 * @param serverIP 服务端IP.
	 * @param serverPort 服务端端口.通常为102,若小于等于0则默认为102.
	 * @param rack CPU机架号.
	 * @param slot CPU槽号.
	 * @param pduLength pdu期望长度
	 */
	public S7Client(ConnectionType connType, String clientIP, int clientPort, String serverIP, int serverPort, int rack, int slot, int pduLength) {
		this.connType = (connType == null ? ConnectionType.PG : connType);
		this.clientIP = clientIP;
		this.clientPort = clientPort < 0 ? 0 : clientPort;
		this.serverIP = serverIP;
		this.serverPort = (serverPort <= 0 ? TPKT.TPKT_PORT : serverPort);
		this.rack = rack;
		this.slot = slot;
		this.pduLength = pduLength;
	}

	/**
	 * S7客户端连接服务端
	 * 
	 * @return
	 */
	public ErrorCode connect() {
		ErrorCode lastErrorCode = ErrorCode.NoError;
		if(clientBoot != null)
			disconnect();
		InetSocketAddress clientSocketAddress;
		// 初始化客户端地址
		if(clientIP == null || clientIP.isEmpty())
			clientSocketAddress = new InetSocketAddress(clientPort);// 自选一个本地ip和端口
		else
			clientSocketAddress = new InetSocketAddress(clientIP, clientPort);
		if(clientSocketAddress.isUnresolved()) {
			logger.error("Invalid client ip or port.ClientIP:{},ClientPort:{}.", clientIP, clientPort);
			return lastErrorCode = ErrorCode.InvalidClientIPOrPort;
		}
		// 初始化服务端地址
		InetSocketAddress serverSocketAddress = new InetSocketAddress(serverIP, serverPort);
		if(serverSocketAddress.isUnresolved()) {
			logger.error("Invalid server IP or Port.ServerIP:{},ServerPort:{}.", serverIP, serverPort);
			return lastErrorCode = ErrorCode.InvalidServerIPOrPort;
		}
		// 初始化客户端
		if(acceptorGroup == null)
			acceptorGroup = new NioEventLoopGroup();
		clientBoot = new Bootstrap();
		clientHandler = new S7ClientHandler();
		// 引导客户端
		clientBoot.group(acceptorGroup);
		clientBoot.channel(NioSocketChannel.class);
		clientBoot.remoteAddress(serverSocketAddress);
		clientBoot.localAddress(clientSocketAddress);
		clientBoot.option(ChannelOption.SO_REUSEADDR, true);
		clientBoot.option(ChannelOption.SO_LINGER, 0);
		clientBoot.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(clientHandler);
			}
		});
		try {
			clientFuture = clientBoot.connect().sync().addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess()) {
						InetSocketAddress local = (InetSocketAddress) future.channel().localAddress();
						clientIP = local.getAddress().getHostAddress();
						clientPort = local.getPort();
						logger.info("Socket to PLC({}:{} -> {}:{}) established.", clientIP, clientPort, serverIP, serverPort);
					} else
						logger.error("Socket to PLC({}:{} -> {}:{}) established failed.", clientIP, clientPort, serverIP, serverPort);
				}
			});
		} catch(Exception e)// TODO 地址绑定失败,会在这里捕获异常!而且clientFuture不会被赋值
		{
			logger.error("Socket to PLC({}:{} -> {}:{}) established exception.", clientIP, clientPort, serverIP, serverPort, e);
			return lastErrorCode = ErrorCode.ConnectToServerError;
		}
		int destTSAP = (connType.value() << 8) + (rack * 0x20) + slot;
		if(TPKT.analyzeTpdu(sendMessage(TPKT.createTPKT(COTP.createCOTPCR(destTSAP)).getPacket()).getData()) != null) {
			S7 s7cc = TPKT.analyzePdu(sendMessage(TPKT.createTPKT(COTP.createCOTPDT(S7.createS7SC(pduLength))).getPacket()).getData());
			if(s7cc != null) {
				pduLength = s7cc.getUnsignedPduLength();
				singleReadMaxPermitLength = pduLength - S7.SingleReadAckPduLengthExceptData;
				singleWriteMaxPermitLength = pduLength - S7.SingleWritePduLengthExceptData;
				logger.info("Connect to PLC({}:{} -> {}:{}).", clientIP, clientPort, serverIP, serverPort);
			} else
				return lastErrorCode = ErrorCode.InvalidS7;
		} else
			return lastErrorCode = ErrorCode.InvalidCOTP;
		return lastErrorCode;
	}

	private S7Event sendMessage(byte[] msg) {
		clientHandler.sendMessage(msg);
		return clientHandler.receiveMessage();
	}

	/**
	 * 读取DB块数据.
	 * <li>DB8.DBB100,1000</li>
	 * <li>DB8.DBB100.1000</li>
	 * <li>DB8.DBW100,500</li>
	 * <li>db8,w100.500</li>
	 * 
	 * @param item 需要读取的数据信息
	 * @param singleReadMaxBytes 单次读取最大字节数
	 * @return
	 */
	public S7Event readDB(String item, int singleReadMaxBytes) {
		String[] itemInfo = item.replaceAll("DB", "").replaceAll("db", "").trim().split(",|\\.");
		int dbNumber = Integer.parseInt(itemInfo[0]);
		int start = Integer.parseInt(itemInfo[1].substring(1, itemInfo[1].length()));
		int amountBytes = 0;
		if(itemInfo[1].toUpperCase().contains("B"))
			amountBytes = Integer.parseInt(itemInfo[2]);
		else if(itemInfo[1].toUpperCase().contains("W"))
			amountBytes = Integer.parseInt(itemInfo[2]) * 2;
		else
			amountBytes = 0;
		return readDB(dbNumber, start, amountBytes, singleReadMaxBytes);
	}

	/**
	 * 读取DB块数据.
	 * <li>以单个item读取,且单位数据长度为byte.</li>
	 * <li>如果需要读取的数据长度超过指定的单次最大读取数据长度,就会拆分为多次读取.</li>
	 * <li>单次读取最大数据长度应该在允许范围内,其范围为[1 ~ {@link S7Client#singleReadMaxPermitLength}],否则使用允许范围的最大值.</li>
	 * 
	 * @param dbNumber 需要读取的数据块.
	 * @param start 需要读取的数据的起始位置.
	 * @param amountBytes 需要读取的字节数.
	 * @param singleReadMaxBytes 单次读取的数据的最大字节数,可以通过指定该值来限定每次读取的最大字节数.如果不在允许范围内,否则使用允许范围的最大值.
	 * @return 返回读取到的信息.
	 */
	public S7Event readDB(int dbNumber, int start, int amountBytes, int singleReadMaxBytes) {
		double startTime = System.currentTimeMillis();
		if((start + amountBytes) > DataBlock.MaxDataBlockLength) {
			if(logger.isTraceEnabled())
				logger.trace("Invalid read address:DB{}.DBB{},{}bytes", dbNumber, start, amountBytes);
			return new S7Event(startTime, ErrorCode.InvalidReadAddress);
		}
		byte[] resultData = new byte[amountBytes];// 用于存储读取返回的数据(需要的数据,去除报文的其他部分)

		singleReadMaxBytes = (singleReadMaxBytes < 1 || singleReadMaxBytes > singleReadMaxPermitLength) ? singleReadMaxPermitLength : singleReadMaxBytes;
		int leftBytes = amountBytes;
		int offset = 0;
		int readCount = 0;// 读取次数计数

		while(leftBytes > 0)// 每循环一次,就会读取一次
		{
			// 计算单次读取字节数
			int singleReadBytes = (leftBytes > singleReadMaxBytes) ? singleReadMaxBytes : leftBytes;// 单次读取的字节数
			int address = start << 3;// start<<3 = start*8 = byte address;start前三位bit = bit address;按byte为单位读取,所以bit address始终为0
			byte[] packet = TPKT.createTPKT(COTP.createCOTPDT(S7.createS7ReadDB(dbNumber, address, singleReadBytes))).getPacket();
			// 发送读取请求报文,并获取反馈报文
			S7Event ackEvent = sendMessage(packet);
			if(ackEvent.getErrorcode() != ErrorCode.NoError) {
				if(logger.isTraceEnabled())
					logger.trace("Send S7_READ failed.");
				return new S7Event(startTime, ackEvent.getErrorcode());
			}
			if(logger.isTraceEnabled())
				logger.trace("Send S7_READ:{}", new ByteArray(packet).toUnsignedDecimalString());
			// 验证服务端返回的读取结果报文
			byte[] ackPacket = ackEvent.getData();
			S7 s7ReadAck = TPKT.analyzePdu(ackPacket);
			if(s7ReadAck == null) {
				if(logger.isTraceEnabled())
					logger.trace("Invalid S7 telegram:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
				return new S7Event(startTime, ErrorCode.InvalidS7);
			}
			// 获取读取结果报文中的异常反馈信息
			ErrorClassCode ecc = s7ReadAck.getErrorClassCode();
			if(ecc != ErrorClassCode.NoError) {
				if(logger.isTraceEnabled())
					logger.trace("The S7_Read_ACK indicated error occured.ErrorClassCode:{},ErrorDesc:{}", ecc, ecc.value());
				return new S7Event(startTime, ErrorCode.ClasscodeIndicateError);
			}
			// 从读取结果报文中解析出报文的返回码,判断反馈是否正确
			try {
				/*
				 * 获取returncode要try catch,
				 * 是因为机场试验线PLC是S7400,连接槽号用2和3都能连通,
				 * 但是2能读取数据,但是3读取的返回数据没有pdu para和pdu data,
				 * 然而returncode包含于pad data,所以获取returncode会报异常.
				 */
				byte returnCode = s7ReadAck.getReturnCode();
				if(returnCode != S7.S7_DATA_RETURNCODE_SUCCESS) {
					if(logger.isTraceEnabled())
						logger.trace("The S7_Read_ACK indicated error occured.ReturnCode:{},ErrorDesc:{}", returnCode, "");// TODO 完善return code
					return new S7Event(startTime, ErrorCode.ReturncodeIndicateError);
				}
			} catch(Exception e) {
				if(logger.isTraceEnabled())
					logger.trace("The S7_Read_ACK indicated error occured.ReturnCode:{},ErrorDesc:{}", null, "S7_Read_ACK contains no pdu data(certain no returncode)");// TODO 完善return code
				return new S7Event(startTime, ErrorCode.ReturncodeIndicateError);
			}
			if(logger.isTraceEnabled())
				logger.trace("Received S7_READ_ACK:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
			// 将读取到的部分数据存储
			byte[] readData = new byte[singleReadBytes];
			System.arraycopy(ackPacket, S7.SingleReadAckPacketLengthExceptData, readData, 0, singleReadBytes);
			System.arraycopy(readData, 0, resultData, offset, singleReadBytes);
			if(logger.isTraceEnabled())
				logger.trace("Read from DB{}.DBB{},{}, cost {}ms at No.{}. Value:{}", dbNumber, start, singleReadBytes, System.currentTimeMillis() - startTime, readCount + 1,
				new ByteArray(readData).toByteString());
			// 准备读取剩余部分数据
			offset += singleReadBytes;
			leftBytes -= singleReadBytes;
			start += singleReadBytes;
			readCount++;
		}
		return new S7Event(resultData, startTime, ErrorCode.NoError);
	}

	/**
	 * 写入DB块数据.
	 * <li>读取地址格式:
	 * <li>DB52,DBW0,1</li>
	 * <li>DB52.DBW0.1</li>
	 * <li>DB52,DBB0,1</li>
	 * <li>DB52.DBB0.1</li>
	 * <li>52,B0,1</li>
	 * <li>52.B0.1</li>
	 * </li>
	 * 
	 * @param item 读取地址.
	 * @param data 需要写入的数据.
	 * @param singleWriteMaxBytes 单次写入最大字节数
	 * @return 返回写入的结果.
	 */
	public S7Event writeDB(String item, byte[] data, int singleWriteMaxBytes) {
		String[] itemInfo = item.replace("db", "").replace("DB", "").split("\\.|,");
		int dbNumber = Integer.parseInt(itemInfo[0]);
		int start = Integer.parseInt(itemInfo[1].substring(1, itemInfo[1].length()));

		return writeDB(dbNumber, start, data, singleWriteMaxBytes);
	}

	/**
	 * 写入数据区数据.
	 * <li>以单个item写入,且单位数据长度为byte.</li>
	 * <li>如果需要写入的数据长度过指定的单次最大写入数据长度,就会拆分为多次读取.</li>
	 * <li>单次写入最大数据长度应该在允许范围内,其范围为[1 ~ ({@link S7Client#pduLength} - {@link S7Client#singleWriteMaxPermitLength})],否则使用允许范围的最大值.</li>
	 * 
	 * @param dbNumber 需要写入的数据块.只针对DataBlock类型的数据有效.
	 * @param start 需要写入的数据的起始位置.
	 * @param data 需要写入的数据.
	 * @param singleWriteMaxBytes 单次最大写入数据长度.
	 * @return 返回写入的结果.
	 */
	public S7Event writeDB(int dbNumber, int start, byte[] data, int singleWriteMaxBytes) {
		double startTime = System.currentTimeMillis();
		if((start + data.length) > DataBlock.MaxDataBlockLength) {
			if(logger.isTraceEnabled())
				logger.trace("Invalid write address:DB{}.DBB{},{}bytes", dbNumber, start, data.length);
			return new S7Event(startTime, ErrorCode.InvalidWriteAddress);
		}
		singleWriteMaxBytes = (singleWriteMaxBytes < 1 || singleWriteMaxBytes > singleWriteMaxPermitLength) ? singleWriteMaxPermitLength : singleWriteMaxBytes;
		int leftBytes = data.length;
		int offset = 0;
		int writeCount = 0;// 写入次数计数

		while(leftBytes > 0)// 每循环一次,就会读取一次
		{
			// 计算单次写入字节数
			int singleWriteBytes = (leftBytes > singleWriteMaxBytes) ? singleWriteMaxBytes : leftBytes;// 单次写入的数据量
			int address = start << 3;// start<<3 = start*8 = byte address;start前三位bit = bit address;按byte为单位读取,所以bit address始终为0
			byte[] writeData = new byte[singleWriteBytes];
			System.arraycopy(data, offset, writeData, 0, singleWriteBytes);
			byte[] packet = TPKT.createTPKT(COTP.createCOTPDT(S7.createS7WriteDB(dbNumber, address, writeData))).getPacket();
			// 发送写入请求报文
			S7Event ackEvent = sendMessage(packet);
			if(ackEvent.getErrorcode() != ErrorCode.NoError) {
				if(logger.isTraceEnabled())
					logger.trace("Send S7_WRITE failed.");
				return new S7Event(startTime, ackEvent.getErrorcode());
			}
			if(logger.isTraceEnabled())
				logger.trace("Send S7_WRITE:{}", new ByteArray(packet).toUnsignedDecimalString());
			// 验证服务端返回的写入结果报文
			byte[] ackPacket = ackEvent.getData();
			S7 s7WriteAck = TPKT.analyzePdu(ackPacket);
			if(s7WriteAck == null) {
				if(logger.isTraceEnabled())
					logger.trace("Invalid S7 telegram:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
				return new S7Event(startTime, ErrorCode.InvalidS7);
			}
			// 获取读取结果报文中的异常反馈信息
			ErrorClassCode ecc = s7WriteAck.getErrorClassCode();
			if(ecc != ErrorClassCode.NoError) {
				if(logger.isTraceEnabled())
					logger.trace("The S7_Write_ACK indicated error occured.ErrorClassCode:{},ErrorDesc:{}", ecc, ecc.value());
				return new S7Event(startTime, ErrorCode.ClasscodeIndicateError);
			}
			// 从读取结果报文中解析出报文的返回码,判断反馈是否正确
			byte returnCode = s7WriteAck.getReturnCode();
			if(returnCode != S7.S7_DATA_RETURNCODE_SUCCESS) {
				if(logger.isTraceEnabled())
					logger.trace("The S7_Write_ACK indicated error occured.ReturnCode:{},ErrorDesc:{}", returnCode, "");// TODO 完善return code
				return new S7Event(startTime, ErrorCode.ReturncodeIndicateError);
			}
			if(logger.isTraceEnabled()) {
				logger.trace("Received S7_Write_ACK:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
				logger.trace("Write to DB{}.DBB{},{}, cost {}ms at No.{}. Value:{}", dbNumber, start, singleWriteBytes, System.currentTimeMillis() - startTime, writeCount + 1,
				new ByteArray(writeData).toByteString());
			}
			// 准备写入剩余部分数据
			offset += singleWriteBytes;
			leftBytes -= singleWriteBytes;
			start += singleWriteBytes;
			writeCount++;
		}
		return new S7Event(startTime, ErrorCode.NoError);
	}

	public boolean isConnect() {
		if(clientHandler == null)
			return false;
		return clientHandler.isConnect();
	}

	/**
	 * 断开与服务端的连接
	 */
	public void disconnect() {
		if(clientFuture != null) {
			clientFuture.channel().close().addListener(ChannelFutureListener.CLOSE);
			clientFuture.awaitUninterruptibly();
			acceptorGroup.shutdownGracefully();
			clientFuture = null;
			clientHandler = null;
			clientBoot = null;
			logger.info("Disonnect from PLC({}:{} -> {}:{}).", clientIP, clientPort, serverIP, serverPort);
		}
	}

	public String getClientIP() {
		return this.clientIP;
	}

	public int getClientPort() {
		return this.clientPort;
	}

	public String getServerIP() {
		return this.serverIP;
	}

	public int getServerPort() {
		return this.serverPort;
	}

	public int getPduLength() {
		return this.pduLength;
	}
}
