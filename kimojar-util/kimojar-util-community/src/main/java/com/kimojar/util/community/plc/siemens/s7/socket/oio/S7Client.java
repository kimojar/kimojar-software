/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7.socket.oio
 * FILE S7Client.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-11-14
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
package com.kimojar.util.community.plc.siemens.s7.socket.oio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kimojar.util.community.plc.siemens.s7.S7Event;
import com.kimojar.util.community.plc.siemens.s7.telegram.COTP;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7.ErrorClassCode;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7Constant.AreaType;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7Constant.ConnectionType;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7Constant.ErrorCode;
import com.kimojar.util.community.plc.siemens.s7.telegram.TPKT;
import com.kimojar.util.community.data.ByteArray;
import com.kimojar.util.community.data.ByteUtil;

/**
 * <p>
 * 该类主要是研究学习使用,使用oio实现s7需要特别小心脏数据问题<br>
 * 如果需要扩展到实践中,为了提高效率,建议去除trace级别的日志记录以提高读写速度.<br>
 * 我做过测试,s7Client.readDB(1, 0, 2000, 200),循环读1000次:<br>
 * 存在trace日志记录时,trace日志级别,也就是记录出trace日志,耗费40458ms;<br>
 * 存在trace日志记录时,debug日志级别,也就是存在trace但是不记录出来,耗费4201ms;<br>
 * 不存在trace日志记录时,耗费1236ms.<br>
 * <br>
 * s7Client.writeDB(1, 0, 10字节, 200),循环写10000次:<br>
 * 存在trace日志记录时,trace日志级别,也就是记录出trace日志,耗费2361ms;<br>
 * 存在trace日志记录时,debug日志级别,也就是存在trace但是不记录出来,耗费971ms;<br>
 * 不存在trace日志记录时,耗费909ms.<br>
 * <br>
 * 所以底层通讯尽量少使用日志记录,尽量使用简单的返回值.
 * </p>
 * <li></li>
 * 
 * @author KiMoJar
 * @date 2020-04-13
 */
public class S7Client {

	public static final Logger logger = LoggerFactory.getLogger(S7Client.class);

	private String clientIP;
	private int clientPort;
	private String serverIP;
	private int serverPort;
	private int rack;// 机架号
	private int slot;// 槽号
	private InputStream reader;
	private OutputStream writer;
	private Socket socketTCP;

	/**
	 * 连接PLC的连接类型.详细参见:{@link ConnectionType}
	 */
	private ConnectionType connType;

	/**
	 * PDU长度(字节数)<br>
	 * <li>默认为480.</li>
	 * <li>TPKT\packet-data\TPDU-data.表示了S7协议部分的所有长度.</li>
	 * <li>通过发送S7_SC报文后得到的反馈报文S7_CC的最后一个字得到,也就是通过PDU Size Negotiated得到.</li>
	 */
	private int pduLength = 0;

	/**
	 * 单次能够读取的数据量的最大值.<br>
	 * <li>singleReadMaxSize = {@link S7Client#pduLength} - {@link S7Client#S7_READ_ACK_PDULENGTH_EXCEPTDATA}.</li>
	 * <li>在建立通讯阶段,确认pduSize时确定.</li>
	 */
	private int singleReadMaxPermitLength = 200;

	/**
	 * 单次能够写入的数据量的最大值.<br>
	 * <li>singleWriteMaxSize = {@link S7Client#pduLength} - {@link S7Client#S7_WRITE_PDULENGTH_EXCEPTDATA}.</li>
	 * <li>在建立通讯阶段,确认pduSize时确定.</li>
	 */
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
	 * 连接服务端.
	 * 
	 * @return
	 */
	public ErrorCode connect() {
		// 判断是否已建立连接
		ErrorCode lastErrorCode = ErrorCode.NoError;
		if(socketTCP != null)
			disconnect();
		socketTCP = new Socket();
		// 初始化客户端地址
		InetSocketAddress clientSocketAddress;
		if(clientIP == null || clientIP.isEmpty())
			clientSocketAddress = new InetSocketAddress(clientPort);
		else
			clientSocketAddress = new InetSocketAddress(clientIP, clientPort);
		if(clientSocketAddress.isUnresolved()) {
			logger.error("Invalid client IP or Port.ClientIP:{},ClientPort:{}.", clientIP, clientPort);
			return lastErrorCode = ErrorCode.InvalidClientIPOrPort;
		}
		// 初始化服务端地址
		InetSocketAddress serverSocketAddress = new InetSocketAddress(serverIP, serverPort);
		if(serverSocketAddress.isUnresolved()) {
			logger.error("Invalid server IP or Port.ServerIP:{},ServerPort:{}.", serverIP, serverPort);
			return lastErrorCode = ErrorCode.InvalidServerIPOrPort;
		}
		// 设置socket连接参数,考虑参数可配置化
		try {
			socketTCP.setReuseAddress(true);
			socketTCP.setSendBufferSize(1024);
			socketTCP.setReceiveBufferSize(1024);
			socketTCP.setSoTimeout(1000);
			socketTCP.setTcpNoDelay(true);// 对释放端口绑定无效
			socketTCP.setSoLinger(true, 0);// 保证socket关闭后能够立即释放端口绑定(逗留0s)
		} catch(Exception e) {
			logger.error("Set socket parameter failed.", e);
			return lastErrorCode = ErrorCode.SetSocketParaError;
		}
		// 绑定socket客户端地址
		try {
			socketTCP.bind(clientSocketAddress);
		} catch(Exception e) {
			logger.error("Bind SocketAddress failed.", e);
			return lastErrorCode = ErrorCode.BindSocketAddressError;
		}
		// 连接到socket服务端地址
		try {
			socketTCP.connect(serverSocketAddress, 5000);
			clientIP = socketTCP.getLocalAddress().getHostAddress();
			clientPort = socketTCP.getLocalPort();
		} catch(SocketTimeoutException e) {
			logger.error("Connect to server failed, server ip {} is unreachable.", serverIP, e);
			return lastErrorCode = ErrorCode.ServerIPUnreachable;
		} catch(ConnectException e) {
			logger.error("Connect to server failed, server port {} is unreachable.", serverPort, e);
			return lastErrorCode = ErrorCode.ServerPortUnreachable;
		} catch(IOException e) {
			logger.error("Connect to server failed.", e);
			return lastErrorCode = ErrorCode.ConnectToServerError;
		}
		// 获取socket通讯io流
		try {
			reader = socketTCP.getInputStream();
			writer = socketTCP.getOutputStream();
		} catch(Exception e) {
			logger.error("Get socket IOStream failed.", e);
			return lastErrorCode = ErrorCode.GetSocketIOStreamError;
		}
		// 建立连接
		if((lastErrorCode = establishConnection()) != ErrorCode.NoError)
			return lastErrorCode;
		// 建立通讯
		if((lastErrorCode = establishCommunication(pduLength)) != ErrorCode.NoError)
			return lastErrorCode;
		// PLC连接成功.
		logger.info("Connect to PLC({}:{} -> {}:{}).", clientIP, clientPort, serverIP, serverPort);
		return lastErrorCode;
	}

	/**
	 * 获取与PLC建立连接后,协商达成一致的PDU长度.
	 * <li>应该在连接建立,通讯建立后,且建立成功,才能获取到正确的PUD长度.</li>
	 * 
	 * @return
	 */
	public int getPDULength() {
		return pduLength;
	}

	/**
	 * 建立与PLC的连接.
	 * <li>向服务端发送连接请求报文,并接收和验证服务端返回的连接确认报文.</li>
	 * 
	 * @return 连接建立成功,返回ture,失败返回false.
	 */
	private ErrorCode establishConnection() {
		ErrorCode lastErrorCode = ErrorCode.NoError;
		// 发送连接请求报文
		try {
			int destTSAP = (connType.value() << 8) + (rack * 0x20) + slot;// ((connType.value() << 8) + (rack * 0x20) + slot) & 0x0000FFFF;
			byte[] cotpcrPacket = TPKT.createTPKT(COTP.createCOTPCR(destTSAP)).getPacket();
			writer.write(cotpcrPacket, 0, cotpcrPacket.length);
			writer.flush();
			logger.trace("Send COTP_CR:{}", new ByteArray(cotpcrPacket).toUnsignedDecimalString());
		} catch(Exception e) {
			logger.trace("Send COTP_CR failed.", e);
			return lastErrorCode = ErrorCode.SendCOTPCRError;
		}
		// 接收packet header
		byte[] ackPacketHeader = new byte[TPKT.PacketHeaderLength];
		if(!receiveData(ackPacketHeader, 0, ackPacketHeader.length)) {
			logger.trace("Receive packet header timeout.");
			return lastErrorCode = ErrorCode.ReceivedTPKTHeaderError;
		}
		// 验证packet header是否有效,后续读取多长的packet data依赖于这一步,否则会出现readDBInFixedLength()的问题
		int ackPakcetLength = TPKT.analyzePacketLength(ackPacketHeader);
		if(ackPakcetLength == 0) {
			logger.trace("Invalid packet header:{}", new ByteArray(ackPacketHeader).toUnsignedDecimalString());
			return lastErrorCode = ErrorCode.InvalidTPKTHeader;
		}
		// 接收packet data
		byte[] ackPacketData = new byte[ackPakcetLength - TPKT.PacketHeaderLength];
		if(!receiveData(ackPacketData, 0, ackPacketData.length)) {
			logger.trace("Receive packet data timeout.");
			return lastErrorCode = ErrorCode.ReceivedTPKTDataError;
		}
		// 合并得到完整packet
		byte[] ackPacket = ByteArray.unitBytes(ackPacketHeader, ackPacketData);
		// 验证服务端返回的连接确认报文
		COTP cotpcc = TPKT.analyzeTpdu(ackPacket);
		if(cotpcc == null) {
			logger.trace("Invalid COTP telegram:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
			return lastErrorCode = ErrorCode.InvalidCOTP;
		}
		logger.trace("Received COTP_CC:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
		return lastErrorCode;
	}

	/**
	 * 建立与PLC的通讯.
	 * <li>向服务端发送建立通讯请求报文,并接收和验证服务端返回的通讯确认报文.</li>
	 * 
	 * @param negotiatedPDULength 期望与PLC协商达成的PDU Length,取值范围[480-4096],超出范围取边界值.
	 * @return 通讯建立成功,返回ture,失败返回false.
	 */
	private ErrorCode establishCommunication(int negotiatedPDULength) {
		ErrorCode lastErrorCode = ErrorCode.NoError;
		// 发送通讯请求报文
		try {
			negotiatedPDULength = negotiatedPDULength > 4096 ? 4096 : negotiatedPDULength;
			negotiatedPDULength = negotiatedPDULength < 480 ? 480 : negotiatedPDULength;
			byte[] s7scPacket = TPKT.createTPKT(COTP.createCOTPDT(S7.createS7SC(negotiatedPDULength))).getPacket();
			writer.write(s7scPacket, 0, s7scPacket.length);
			writer.flush();
			logger.trace("Send S7_SC:{}", new ByteArray(s7scPacket).toUnsignedDecimalString());
		} catch(Exception e) {
			logger.trace("Send S7_SC failed.", e);
			return lastErrorCode = ErrorCode.SendS7SCError;
		}
		// 接收packet header
		byte[] ackPacketHeader = new byte[TPKT.PacketHeaderLength];
		if(!receiveData(ackPacketHeader, 0, ackPacketHeader.length)) {
			logger.trace("Receive packet header timeout.");
			return lastErrorCode = ErrorCode.ReceivedTPKTHeaderError;
		}
		// 验证packet header是否有效,后续读取多长的packet data依赖于这一步,否则会出现readDBInFixedLength()的问题
		int ackPakcetLength = TPKT.analyzePacketLength(ackPacketHeader);
		if(ackPakcetLength == 0) {
			logger.trace("Invalid packet header:{}", new ByteArray(ackPacketHeader).toUnsignedDecimalString());
			return lastErrorCode = ErrorCode.InvalidTPKTHeader;
		}
		// 接收packet data
		byte[] ackPacketData = new byte[ackPakcetLength - TPKT.PacketHeaderLength];
		if(!receiveData(ackPacketData, 0, ackPacketData.length)) {
			logger.trace("Receive packet data timeout.");
			return lastErrorCode = ErrorCode.ReceivedTPKTDataError;
		}
		// 合并得到完整packet
		byte[] ackPacket = ByteArray.unitBytes(ackPacketHeader, ackPacketData);
		// 验证服务端返回的通讯确认报文
		S7 s7cc = TPKT.analyzePdu(ackPacket);
		if(s7cc == null) {
			logger.trace("Invalid S7 telegram:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
			return lastErrorCode = ErrorCode.InvalidS7;
		}
		// 获取通讯确认报文中的异常反馈信息
		ErrorClassCode ecc = s7cc.getErrorClassCode();
		if(ecc != ErrorClassCode.NoError) {
			logger.trace("The S7_CC indicated error occured.ErrorClassCode:{},ErrorDesc:{}", ecc, ecc.value());
			return lastErrorCode = ErrorCode.ClasscodeIndicateError;
		}
		// 获取通讯确认报文反馈的pdu length
		pduLength = s7cc.getUnsignedPduLength();
		if(pduLength < 0) {
			logger.trace("The S7_CC negotiated wrong pdu size {}.", pduLength);
			return lastErrorCode = ErrorCode.S7CCPdulengthError;
		}
		singleReadMaxPermitLength = pduLength - S7.SingleReadAckPduLengthExceptData;
		singleWriteMaxPermitLength = pduLength - S7.SingleWritePduLengthExceptData;
		logger.trace("Received S7_CC:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
		return lastErrorCode;
	}

	/**
	 * 等待可以不受阻塞地从输入流读取(或跳过)的估计字节数达到预期的字节数.
	 * 
	 * @param size 预期的字节数.
	 * @param timeout 预期的超时时间.
	 * @return 等待结果.
	 */
	private boolean waitData(int size, int timeout) {
		byte[] temp;
		int availableDataSize = 0;
		long beginTime = System.currentTimeMillis();
		boolean expired = false;
		try {
			while(availableDataSize < size && !expired) {
				availableDataSize = reader.available();// 可以不受阻塞地从输入流读取(或跳过)的估计字节数
				expired = (System.currentTimeMillis() - beginTime) > timeout;
				// if timeout,clean the buffered bytes
				if(expired && availableDataSize > 0) {
					temp = new byte[size];
					reader.read(temp, 0, availableDataSize);
				}
			}
		} catch(Exception e) {
			temp = null;
			logger.error("Wait data from remote({}:{}) exception.", serverIP, serverPort, e);
			return false;
		}
		return !expired;
	}

	/**
	 * 不受阻塞地从输入流读取指定的字节数,存到指定字节数组的指定位置.
	 * 
	 * @param buffer 存储读取字节的数组
	 * @param pos 存储字节的起始位置
	 * @param size
	 * @return 如果读取到的字节数和指定读取的字节数相等,则返回true,否则返回false,读取异常也返回false.
	 */
	private boolean receiveData(byte[] buffer, int pos, int size) {
		int readCount = 0;
		if(waitData(size, 2000)) {
			try {
				readCount = reader.read(buffer, pos, size);
			} catch(Exception e) {
				logger.error("Read data from remote({}:{}) exception.", serverIP, serverPort, e);
				return false;
			}
		}
		return readCount == size;
	}

	/**
	 * 用于测试目的
	 */
	private void reconnect() {
		disconnect();
		connect();
	}

	/**
	 * 与服务端断开连接,并释放相关资源.
	 */
	public void disconnect() {
		try {
			if(reader != null) {
				reader.close();
				reader = null;
			}
			if(writer != null) {
				writer.close();
				writer = null;
			}
			if(socketTCP != null) {
				socketTCP.close();
				socketTCP = null;
			}
		} catch(Exception e) {
			logger.error("Disconnect PLC exception:{}", e);
			e.printStackTrace();
		}
		logger.info("Disconnect from PLC({}:{}).", serverIP, serverPort);
	}

	/**
	 * 判断PLC连接是否断开
	 * <li>发送一个测试字节0x00,通过是否发送成功来判断socket连接是否正常</li>
	 * 
	 * @return true=连接断开了,false=连接正常
	 */
	public boolean isConnect() {
		if(socketTCP == null)
			return false;
		else {
			try {
				/*
				 * 或者可以发送紧急数据报
				 * socketTCP.sendUrgentData(0x00);
				 */
				writer.write(0x00);
				writer.flush();
				return true;
			} catch(IOException e) {
				return false;
			}
		}
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
			// 发送读取请求报文
			try {
				writer.write(packet, 0, packet.length);
				writer.flush();
				logger.trace("Send S7_READ:{}", new ByteArray(packet).toUnsignedDecimalString());
			} catch(Exception e) {
				logger.trace("Send S7_READ failed.", e);
				return new S7Event(startTime, ErrorCode.SendS7RError);
			}
			// 接收packet header
			byte[] ackPacketHeader = new byte[TPKT.PacketHeaderLength];
			if(!receiveData(ackPacketHeader, 0, ackPacketHeader.length)) {
				logger.trace("Receive packet header timeout.");
				return new S7Event(startTime, ErrorCode.ReceivedTPKTHeaderError);
			}
			// 验证packet header是否有效,后续读取多长的packet data依赖于这一步,否则会出现readDBInFixedLength()的问题
			int ackPakcetLength = TPKT.analyzePacketLength(ackPacketHeader);
			if(ackPakcetLength == 0) {
				logger.trace("Invalid packet header:{}", new ByteArray(ackPacketHeader).toUnsignedDecimalString());
				return new S7Event(startTime, ErrorCode.InvalidTPKTHeader);
			}
			// 接收packet data
			byte[] ackPacketData = new byte[ackPakcetLength - TPKT.PacketHeaderLength];
			if(!receiveData(ackPacketData, 0, ackPacketData.length)) {
				logger.trace("Receive packet data timeout.");
				return new S7Event(startTime, ErrorCode.ReceivedTPKTDataError);
			}
			// 合并得到完整packet
			byte[] ackPacket = ByteArray.unitBytes(ackPacketHeader, ackPacketData);
			// 验证服务端返回的读取结果报文
			S7 s7ReadAck = TPKT.analyzePdu(ackPacket);
			if(s7ReadAck == null) {
				logger.trace("Invalid S7 telegram:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
				return new S7Event(startTime, ErrorCode.InvalidS7);
			}
			// 获取读取结果报文中的异常反馈信息
			ErrorClassCode ecc = s7ReadAck.getErrorClassCode();
			if(ecc != ErrorClassCode.NoError) {
				logger.trace("The S7_Read_ACK indicated error occured.ErrorClassCode:{},ErrorDesc:{}", ecc, ecc.value());
				return new S7Event(startTime, ErrorCode.ClasscodeIndicateError);
			}
			// 从读取结果报文中解析出报文的返回码,判断反馈是否正确
			byte returnCode = s7ReadAck.getReturnCode();
			if(returnCode != S7.S7_DATA_RETURNCODE_SUCCESS) {
				logger.trace("The S7_Read_ACK indicated error occured.ReturnCode:{},ErrorDesc:{}", returnCode, "");// TODO 完善return code
				return new S7Event(startTime, ErrorCode.ReturncodeIndicateError);
			}
			logger.trace("Received S7_READ_ACK:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
			// 将读取到的部分数据存储
			byte[] readData = new byte[singleReadBytes];
			System.arraycopy(ackPacket, S7.SingleReadAckPacketLengthExceptData, readData, 0, singleReadBytes);
			System.arraycopy(readData, 0, resultData, offset, singleReadBytes);
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
	 * @deprecated 该方法其实是{@link S7Client#readDB(int, int, int, int)}的原版,由于以计算出来的固定长度来接收服务端反馈报文的方式存在鲁棒性不足问题,
	 *             所以更名后抛弃该方法,不删除原因仅仅是留作研究学习使用.例如只读取一个字节,KSEC PLC模拟器会在读取反馈报文中反馈两个字节,虽然不应该反馈两个字节,
	 *             但是反馈报文是个合法的packet,所以程序应该有这样的鲁棒性,只要合法的packet就应该接收,只是只取其反馈的第一个字节.
	 */
	public S7Event readDBInFixedLength(int dbNumber, int start, int amountBytes, int singleReadMaxBytes) {
		double startTime = System.currentTimeMillis();
		byte[] bytes = new byte[amountBytes];// 用于存储读取返回的数据(需要的数据,去除报文的其他部分)

		singleReadMaxBytes = (singleReadMaxBytes < 1 || singleReadMaxBytes > singleReadMaxPermitLength) ? singleReadMaxPermitLength : singleReadMaxBytes;
		int leftBytes = amountBytes;
		int offset = 0;
		int readCount = 0;// 读取次数计数

		while(leftBytes > 0)// 每循环一次,就会读取一次
		{
			// 计算单次读取字节数
			int singleReadBytes = (leftBytes > singleReadMaxBytes) ? singleReadMaxBytes : leftBytes;// 单次读取的字节数
			int address = start << 3;// start<<3 = start*8 = byte address;start前三位bit = bit address;按byte为单位读取,所以bit address始终为0
			byte[] s7ReadPacket = TPKT.createTPKT(COTP.createCOTPDT(S7.createS7ReadDB(dbNumber, address, singleReadBytes))).getPacket();
			// 发送读取请求报文
			try {
				writer.write(s7ReadPacket, 0, s7ReadPacket.length);
				writer.flush();
				logger.trace("Send S7_READ:{}", new ByteArray(s7ReadPacket).toUnsignedDecimalString());
			} catch(Exception e) {
				logger.trace("Send S7_READ failed.", e);
				return new S7Event(startTime, ErrorCode.SendS7RError);
			}
			// 接收服务端返回的读取结果报文
			byte[] s7ReadAckPacket = new byte[S7.SingleReadAckPacketLengthExceptData + singleReadBytes];// 用于存储读取返回packet.
			if(!receiveData(s7ReadAckPacket, 0, s7ReadAckPacket.length)) {
				logger.trace("Receive S7_Read_ACK timeout.");
				return new S7Event(startTime, ErrorCode.ReceiveS7RAError);
			}
			// 验证服务端返回的读取结果报文
			S7 s7readack = TPKT.analyzePdu(s7ReadAckPacket);
			if(s7readack == null) {
				logger.trace("Invalid S7 telegram:{}", new ByteArray(s7ReadAckPacket).toUnsignedDecimalString());
				return new S7Event(startTime, ErrorCode.InvalidS7);
			}
			// 获取读取结果报文中的异常反馈信息
			ErrorClassCode ecc = s7readack.getErrorClassCode();
			if(ecc != ErrorClassCode.NoError) {
				logger.trace("The S7_Read_ACK indicated error occured.ErrorClassCode:{},ErrorDesc:{}", ecc, ecc.value());
				return new S7Event(startTime, ErrorCode.ClasscodeIndicateError);
			}
			// 从读取结果报文中解析出报文的返回码,判断反馈是否正确
			byte returnCode = s7readack.getReturnCode();
			if(returnCode != S7.S7_DATA_RETURNCODE_SUCCESS) {
				logger.trace("The S7_Read_ACK indicated error occured.ReturnCode:{},ErrorDesc:{}", returnCode, "");// TODO 完善return code
				return new S7Event(startTime, ErrorCode.ReturncodeIndicateError);
			}
			logger.trace("Received S7_READ_ACK:{}", new ByteArray(s7ReadAckPacket).toUnsignedDecimalString());
			// 将读取到的部分数据存储
			byte[] readData = new byte[singleReadBytes];
			System.arraycopy(s7ReadAckPacket, S7.SingleReadAckPacketLengthExceptData, readData, 0, singleReadBytes);
			System.arraycopy(readData, 0, bytes, offset, singleReadBytes);
			logger.trace("Read from DB{}.DBB{},{}, cost {}ms at No.{}. Value:{}", dbNumber, start, singleReadBytes, System.currentTimeMillis() - startTime, readCount + 1,
			new ByteArray(readData).toByteString());
			// 准备读取剩余部分数据
			offset += singleReadBytes;
			leftBytes -= singleReadBytes;
			start += singleReadBytes;
			readCount++;
		}
		return new S7Event(bytes, startTime, ErrorCode.NoError);
	}

	/**
	 * 读取数据区数据.
	 * <li>如果需要读取的数据长度超过指定的单次最大读取数据长度,就会拆分为多次读取.</li>
	 * <li>单次读取最大数据长度应该在允许范围内,其范围为[1 ~ {@link S7Client#singleReadMaxPermitLength}],否则使用允许范围的最大值.</li>
	 * 
	 * @param areaType 需要读取的数据类型.
	 * @param dbNumber 需要读取的数据块.只针对DataBlock类型的数据有效.
	 * @param start 需要读取的数据的起始位置.
	 * @param amount 需要读取的数据量.
	 * @param singleReadMaxLength 单次读取的数据的最大字节数,可以通过指定该值来限定每次读取的最大字节数.如果不在允许范围内,否则使用允许范围的最大值.
	 * @return 返回读取到的信息.
	 * @deprecated 读取counter,timer等尚不完善,存在一定bug,所以将读取DB,counter等拆分开来.{@link S7Client#readDB(int, int, int, int)}
	 */
	public S7Event readArea(AreaType areaType, int dbNumber, int start, int amount, int singleReadMaxLength) {
		double startTime = System.currentTimeMillis();
		byte[] s7_read = {
		(byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1f, // TPKE Header
		(byte) 0x02, (byte) 0xF0, (byte) 0x80, // COTP Header
		(byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x05,
		(byte) 0x00, (byte) 0x00, (byte) 0x0E, (byte) 0x00, (byte) 0x00, // S7 Header
		(byte) 0x04, (byte) 0x01, (byte) 0x12, (byte) 0x0A, (byte) 0x10, (byte) 0x02, (byte) 0x00,
		(byte) 0x00, (byte) 0x84, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00// S7 Parameter
		};// packet length is 31
		byte[] s7_read_ack = new byte[pduLength + 7];// 用于存储读取返回packet.7=packet去除pdu的长度
		byte[] bytes = new byte[amount];// 用于存储读取返回的数据(需要的数据,去除报文的其他部分)

		singleReadMaxLength = (singleReadMaxLength < 1 || singleReadMaxLength > singleReadMaxPermitLength) ? singleReadMaxPermitLength : singleReadMaxLength;
		int elementSize = (areaType == AreaType.Counter || areaType == AreaType.Timer) ? 2 : 1;// 单元数据的尺寸.If we are addressing Timers or counters the element size is 2
		int singleReadMaxElements = singleReadMaxLength / elementSize;// 每次能读取的最大数据量
		int leftElements = amount;// 剩余的需要读取的数据量
		int offset = 0;
		int readCount = 0;// 读取次数计数
		int position = start;// 仅用于日志

		while(leftElements > 0)// 每循环一次,就会读取一次
		{
			int singleReadElements = (leftElements > singleReadMaxElements) ? singleReadMaxElements : leftElements;// 单次读取的数据量
			int singleReadLength = singleReadElements * elementSize;// 单次读取的数据的字节数
			int address = (areaType == AreaType.Counter || areaType == AreaType.Timer) ? start : start << 3;// start<<3 = start*8.adjusts Start and word length

			s7_read[22] = (areaType == AreaType.Counter || areaType == AreaType.Timer) ? (byte) areaType.value() : (byte) 0x02;// set transport size
			ByteUtil.setWordAt(s7_read, 23, singleReadElements);// set read count
			if(areaType == AreaType.DataBlock)
				ByteUtil.setWordAt(s7_read, 25, dbNumber);// set read DB number
			s7_read[27] = (byte) areaType.value();// set read area type
			s7_read[28] = (byte) (address >> 16 & 0x0FF);// address most HI
			s7_read[29] = (byte) (address >> 8 & 0x0FF);// address better HI
			s7_read[30] = (byte) (address & 0x0FF);// address LO

			try {
				writer.write(s7_read, 0, s7_read.length);
				writer.flush();
				logger.trace("Send S7_READ:{}", new ByteArray(s7_read).toSocketString());
			} catch(Exception e) {
				logger.error("Send S7_READ failed.", e);
				reconnect();
				return new S7Event(startTime, ErrorCode.SendS7RError);
			}
			// 获取反馈报文的前7个字节
			if(!receiveData(s7_read_ack, 0, 7)) {
				logger.error("Receive the first seven bytes of the S7_Read_ACK timeout.");
				return new S7Event(startTime, ErrorCode.ReceiveS7RAError);
			}
			// 根据获取的前7个字节,验证S7_Read_ACK报文长度是否正确
			int packetLength = ByteUtil.getWordAt(s7_read_ack, 2);// TPKT\packet-header\packet length
			/*
			 * modified by kimojar,20191214.
			 * 通常读取或写入失败时,通常长度都是不对的,也就是在此处就会校验出结果来了,
			 * 导致无法根据errorclass,errorcode以及returncode来判别出具体的错误信息,所以将此处注释
			 * if(packetLength != singleReadLength + S7Constant.S7_READ_ACK_PACKETLENGTH_EXCEPTDATA)
			 * {
			 * logger.error("The length {} of S7_Read_ACK packet is invalid which should be {}.", packetLength, singleReadLength + S7Constant.S7_READ_ACK_PACKETLENGTH_EXCEPTDATA);
			 * return null;
			 * }
			 */
			// 获取缓冲区中S7_Read_ACK报文的剩余字节
			if(!receiveData(s7_read_ack, 7, packetLength - 7)) {
				logger.error("Receive the left bytes of the S7_Read_ACK timeout.");
				return new S7Event(startTime, ErrorCode.ReceiveCompleteS7RAError);
			}
			// 从S7_Read_ACK报文中解析出报文的错误码,判断反馈是否正确
			byte errorClass = s7_read_ack[17];
			byte errorCode = s7_read_ack[18];
			if(errorClass != 0 || errorCode != 0) {
				logger.error("The S7_CC indicated error occured.errorClass:{},errorCode:{},errorDesc:{}", errorClass, errorCode, "");
				return new S7Event(startTime, ErrorCode.ClasscodeIndicateError);
			}
			// 从S7_Read_ACK报文中解析出报文的返回码,判断反馈是否正确
			byte returnCode = s7_read_ack[21];
			if(returnCode != S7.S7_DATA_RETURNCODE_SUCCESS) {
				logger.error("The returnCode of S7_Read_ACK indicated error occured.returnCode:{},errorDesc:{}", returnCode, "");
				return new S7Event(startTime, ErrorCode.ReturncodeIndicateError);
			}
			logger.trace("Received S7_READ_ACK:{}", new ByteArray(s7_read_ack).toSocketString());
			// 将读取到的部分数据存储
			System.arraycopy(s7_read_ack, 25, bytes, offset, singleReadLength);
			// 准备读取剩余部分数据
			offset += singleReadLength;
			leftElements -= singleReadElements;
			start += singleReadLength;
			readCount++;
		}
		logger.trace("Read from {}{}.DBX{},{} cost {}ms use {} times. Value:{}", areaType, dbNumber, position, amount, System.currentTimeMillis() - startTime, readCount,
		new ByteArray(bytes).toByteString());
		return new S7Event(bytes, startTime, ErrorCode.NoError);
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
			try {
				writer.write(packet, 0, packet.length);
				writer.flush();
				logger.trace("Send S7_WRITE:{}", new ByteArray(packet).toUnsignedDecimalString());
			} catch(Exception e) {
				logger.trace("Send S7_WRITE failed.", e);
				reconnect();
				return new S7Event(startTime, ErrorCode.SendS7WError);
			}
			// 接收packet header
			byte[] ackPacketHeader = new byte[TPKT.PacketHeaderLength];
			if(!receiveData(ackPacketHeader, 0, ackPacketHeader.length)) {
				logger.trace("Receive packet header timeout.");
				return new S7Event(startTime, ErrorCode.ReceivedTPKTHeaderError);
			}
			// 验证packet header是否有效,后续读取多长的packet data依赖于这一步,否则会出现readDBInFixedLength()的问题
			int ackPakcetLength = TPKT.analyzePacketLength(ackPacketHeader);
			if(ackPakcetLength == 0) {
				logger.trace("Invalid packet header:{}", new ByteArray(ackPacketHeader).toUnsignedDecimalString());
				return new S7Event(startTime, ErrorCode.InvalidTPKTHeader);
			}
			// 接收packet data
			byte[] ackPacketData = new byte[ackPakcetLength - TPKT.PacketHeaderLength];
			if(!receiveData(ackPacketData, 0, ackPacketData.length)) {
				logger.trace("Receive packet data timeout.");
				return new S7Event(startTime, ErrorCode.ReceivedTPKTDataError);
			}
			// 合并得到完整packet
			byte[] ackPacket = ByteArray.unitBytes(ackPacketHeader, ackPacketData);
			// 验证服务端返回的写入结果报文
			S7 s7WriteAck = TPKT.analyzePdu(ackPacket);
			if(s7WriteAck == null) {
				logger.trace("Invalid S7 telegram:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
				return new S7Event(startTime, ErrorCode.InvalidS7);
			}
			// 获取读取结果报文中的异常反馈信息
			ErrorClassCode ecc = s7WriteAck.getErrorClassCode();
			if(ecc != ErrorClassCode.NoError) {
				logger.trace("The S7_Write_ACK indicated error occured.ErrorClassCode:{},ErrorDesc:{}", ecc, ecc.value());
				return new S7Event(startTime, ErrorCode.ClasscodeIndicateError);
			}
			// 从读取结果报文中解析出报文的返回码,判断反馈是否正确
			byte returnCode = s7WriteAck.getReturnCode();
			if(returnCode != S7.S7_DATA_RETURNCODE_SUCCESS) {
				logger.trace("The S7_Write_ACK indicated error occured.ReturnCode:{},ErrorDesc:{}", returnCode, "");// TODO 完善return code
				return new S7Event(startTime, ErrorCode.ReturncodeIndicateError);
			}
			logger.trace("Received S7_Write_ACK:{}", new ByteArray(ackPacket).toUnsignedDecimalString());
			logger.trace("Write to DB{}.DBB{},{}, cost {}ms at No.{}. Value:{}", dbNumber, start, singleWriteBytes, System.currentTimeMillis() - startTime, writeCount + 1,
			new ByteArray(writeData).toByteString());
			// 准备写入剩余部分数据
			offset += singleWriteBytes;
			leftBytes -= singleWriteBytes;
			start += singleWriteBytes;
			writeCount++;
		}
		return new S7Event(startTime, ErrorCode.NoError);
	}

	/**
	 * 写入数据区数据.
	 * <li>如果需要写入的数据长度过指定的单次最大写入数据长度,就会拆分为多次读取.</li>
	 * <li>单次写入最大数据长度应该在允许范围内,其范围为[1 ~ ({@link S7Client#pduLength} - {@link S7Client#singleWriteMaxPermitLength})],否则使用允许范围的最大值.</li>
	 * 
	 * @param areaType 需要写入的数据类型.
	 * @param dbNumber 需要写入的数据块.只针对DataBlock类型的数据有效.
	 * @param start 需要写入的数据的起始位置.
	 * @param data 需要写入的数据.
	 * @param singleWriteMaxLength 单次最大写入数据长度.
	 * @return 返回写入的结果.
	 * @deprecated 写入counter,timer等尚不完善.
	 */
	public S7Event writeArea(AreaType areaType, int dbNumber, int start, byte[] data, int singleWriteMaxLength) {
		double startTime = System.currentTimeMillis();
		byte[] s7_write = {
		(byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x23, // TPKT Header
		(byte) 0x02, (byte) 0xF0, (byte) 0x80, // COTP Header
		(byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x05,
		(byte) 0x00, (byte) 0x00, (byte) 0x0E, (byte) 0x00, (byte) 0x00, // S7 Header
		(byte) 0x05, (byte) 0x01, (byte) 0x12, (byte) 0x0A, (byte) 0x10, (byte) 0x02, (byte) 0x00,
		(byte) 0x00, (byte) 0x84, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // S7 Parameter
		(byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00// S7 Data
		};// packet length is 35
		byte[] s7_write_ack = new byte[pduLength + 7];// 用于存储读取返回packet.7=packet去除pdu的长度

		singleWriteMaxLength = (singleWriteMaxLength < 1 || singleWriteMaxLength > singleWriteMaxPermitLength) ? singleWriteMaxPermitLength : singleWriteMaxLength;
		int elementSize = (areaType == AreaType.Counter || areaType == AreaType.Timer) ? 2 : 1;// 单元数据的长度.If we are addressing Timers or counters the element size is 2
		int singleWriteMaxElements = singleWriteMaxLength / elementSize;// 每次能写入的最大数据量
		int leftElements = data.length;// 剩余的需要写入的数据量
		int offset = 0;
		int writeCount = 0;// 写入次数计数
		int length;
		int position = start;// 仅用于日志

		while(leftElements > 0)// 每循环一次,就会读取一次
		{
			int singleWriteElements = (leftElements > singleWriteMaxElements) ? singleWriteMaxElements : leftElements;// 单次写入的数据量
			int singleWriteLength = singleWriteElements * elementSize;// 单次写入数据的字节数
			int address = (areaType == AreaType.Counter || areaType == AreaType.Timer) ? start : start << 3;// start<<3 = start*8.adjusts Start and word length
			length = (areaType == AreaType.Counter || areaType == AreaType.Timer) ? singleWriteLength : singleWriteLength << 3;// requestSize<<3 = requestSize*8.

			ByteUtil.setWordAt(s7_write, 2, singleWriteLength + S7.SingleWritePduLengthExceptData + COTP.CotpdtTpduHeaderLength + TPKT.PacketHeaderLength);// set packet length
			ByteUtil.setWordAt(s7_write, 15, singleWriteLength + 4);// set data length
			s7_write[17] = (byte) 0x05;// set function as write
			s7_write[22] = (areaType == AreaType.Counter || areaType == AreaType.Timer) ? (byte) areaType.value() : (byte) 0x02;// set transport size
			ByteUtil.setWordAt(s7_write, 23, singleWriteElements);// set write count
			if(areaType == AreaType.DataBlock)
				ByteUtil.setWordAt(s7_write, 25, dbNumber);// set write DB number
			s7_write[27] = (byte) areaType.value();// set write area type
			s7_write[28] = (byte) (address >> 16 & 0x0FF);// address most HI
			s7_write[29] = (byte) (address >> 8 & 0x0FF);// address better HI
			s7_write[30] = (byte) (address & 0x0FF);// address LO
			ByteUtil.setWordAt(s7_write, 33, length);// set data length by size

			byte[] writePacket = new byte[singleWriteLength + S7.SingleWritePduLengthExceptData + COTP.CotpdtTpduHeaderLength + TPKT.PacketHeaderLength];
			System.arraycopy(s7_write, 0, writePacket, 0, 35);
			System.arraycopy(data, offset, writePacket, 35, singleWriteLength);

			try {
				writer.write(writePacket, 0, writePacket.length);
				writer.flush();
				logger.trace("Send S7_WRITE:{}", new ByteArray(writePacket).toSocketString());
			} catch(Exception e) {
				logger.error("Send S7_WRITE failed.", e);
				reconnect();
				return new S7Event(startTime, ErrorCode.SendS7WError);
			}

			// 获取反馈报文的前7个字节
			if(!receiveData(s7_write_ack, 0, 7)) {
				logger.error("Receive the first seven bytes of the S7_Read_ACK failed.");
				return new S7Event(startTime, ErrorCode.ReceiveS7WAError);
			}
			// 根据获取的前7个字节,验证S7_Write_ACK报文长度是否正确
			int packetLength = ByteUtil.getWordAt(s7_write_ack, 2);// TPKT\packet-header\packet length
			/*
			 * modified by kimojar,20191214.
			 * 通常读取或写入失败时,长度都是不对的,也就是在此处就会校验出结果来了,
			 * 导致无法根据errorclass,errorcode以及returncode来判别出具体的错误信息,所以将此处注释
			 * if(packetLength != S7Constant.S7_WRITE_ACK_PACKETLENGTH)
			 * {
			 * logger.error("The length {} of s7_write_ack packet is invalid which should be {}.", packetLength, 22);
			 * return false;
			 * }
			 */
			// 获取缓冲区中S7_Read_ACK报文的剩余字节
			if(!receiveData(s7_write_ack, 7, packetLength - 7)) {
				logger.error("Receive the left bytes of the s7_write_ack failed.");
				return new S7Event(startTime, ErrorCode.ReceiveCompleteS7WAError);
			}
			// 从s7_write_ack报文中解析出报文的错误码,判断反馈是否正确
			byte errorClass = s7_write_ack[17];
			byte errorCode = s7_write_ack[18];
			if(errorClass != 0 || errorCode != 0) {
				logger.error("The s7_write_ack indicated error occured.errorClass:{},errorCode:{},errorDesc:{}", errorClass, errorCode, "");
				return new S7Event(startTime, ErrorCode.ClasscodeIndicateError);
			}
			// 从s7_write_ack报文中解析出报文的返回码,判断反馈是否正确
			byte returnCode = s7_write_ack[21];
			if(returnCode != (byte) 0xFF) {
				logger.error("The returnCode of s7_write_ack {} indicated error occured.returnCode:{},errorDesc:{}", returnCode, "");
				return new S7Event(startTime, ErrorCode.ReturncodeIndicateError);
			}
			logger.trace("Received s7_write_ack:{}", new ByteArray(s7_write_ack).toSocketString());
			// 准备写入剩余部分数据
			offset += singleWriteLength;
			leftElements -= singleWriteElements;
			start += singleWriteLength;
			writeCount++;
		}
		logger.trace("Write to {}{}.{},{} cost {}ms use {} times. Value:{}", areaType, dbNumber, position, data.length, (int) (System.currentTimeMillis() - startTime), writeCount,
		new ByteArray(data).toSocketString());
		return new S7Event(startTime, ErrorCode.NoError);
	}

	public String getClientIP() {
		return clientIP;
	}

	public int getClinetPort() {
		return clientPort;
	}

	public String getServerIP() {
		return serverIP;
	}

	public int getServerPort() {
		return serverPort;
	}

}
