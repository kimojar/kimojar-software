/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7.socket.netty
 * FILE S7ServerHandler.java
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
package com.kimojar.util.community.plc.siemens.s7.socket.netty;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kimojar.util.community.plc.siemens.s7.telegram.COTP;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7;
import com.kimojar.util.community.plc.siemens.s7.telegram.TPKT;
import com.kimojar.util.community.data.ByteArray;
import com.kimojar.util.community.plc.siemens.s7.DataBlock;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * S7通讯的服务端报文处理器
 * 
 * @author KiMoJar
 * @date 2020-04-09
 */
public class S7ServerHandler extends ChannelInboundHandlerAdapter {

	public static final Logger logger = LoggerFactory.getLogger(S7ServerHandler.class);

	private ConcurrentHashMap<ChannelHandlerContext, InetSocketAddress> clientMap;
	private ConcurrentHashMap<Integer, DataBlock> dataBlockMap;
	private ConcurrentHashMap<ChannelHandlerContext, StringBuffer> clientMessageMap;
	private Charset charset = CharsetUtil.ISO_8859_1;

	public S7ServerHandler(ConcurrentHashMap<Integer, DataBlock> dataBlockMap) {
		this.clientMap = new ConcurrentHashMap<ChannelHandlerContext, InetSocketAddress>();
		this.dataBlockMap = dataBlockMap;
		this.clientMessageMap = new ConcurrentHashMap<ChannelHandlerContext, StringBuffer>();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress server = (InetSocketAddress) ctx.channel().localAddress();
		InetSocketAddress client = (InetSocketAddress) ctx.channel().remoteAddress();// 相对于服务端的远程端,就是客户端
		clientMap.put(ctx, client);
		clientMessageMap.put(ctx, new StringBuffer());
		String threadName = "S7 Server(" + server.getAddress().getHostAddress() + ":" + server.getPort()
		+ "->" + client.getAddress().getHostAddress() + ":" + client.getPort() + ")";
		Thread.currentThread().setName(threadName);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress client = clientMap.get(ctx);
		logger.info("Client({}:{}) connected.", client.getAddress().getHostAddress(), client.getPort());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		InetSocketAddress client = clientMap.get(ctx);
		ByteBuf receivedByteBuf = (ByteBuf) msg;
		byte[] receivedBytes = new byte[receivedByteBuf.readableBytes()];
		receivedByteBuf.readBytes(receivedBytes);
		receivedByteBuf.release();
		ByteArray receivedByteArray = new ByteArray(receivedBytes);
		logger.trace("Client({}:{}) message received:{}", client.getAddress().getHostAddress(), client.getPort(), receivedByteArray.toUnsignedDecimalString());

		StringBuffer receivedMessage = clientMessageMap.get(ctx);
		receivedMessage.append(new String(receivedBytes, charset));
		while(receivedMessage.length() >= TPKT.PacketHeaderLength) {
			byte[] packetHeader = receivedMessage.substring(0, TPKT.PacketHeaderLength).getBytes(charset);
			int packetLength = TPKT.analyzePacketLength(packetHeader);
			if(packetLength == 0)// invalid packet header
			{
				/*
				 * 如果packet header校验失败,删除一个字节,而不是删除4个字节,最大限度增强脏数据处理能力.
				 * 因为脏数据通常是发生在tcp报文之间,而不是tcp报文数据内部.
				 * receivedMessage.delete(0, TPKT.PacketHeaderLength);
				 */
				receivedMessage.delete(0, 1);
				continue;
			} else if(receivedMessage.length() >= packetLength)// S7通讯要建立在服务端不会主动发送消息的前提下,否则很难保证效率,因为得将每个接收报文完全解析
			{
				byte[] packet = receivedMessage.substring(0, packetLength).getBytes(charset);
				receivedMessage.delete(0, packetLength);
				TPKT tpkt = TPKT.analyzePacket(packet);
				if(tpkt != null) {
					COTP cotp = COTP.analyzeTpdu(tpkt.getPacketData());
					if(cotp != null) {
						switch(cotp.getTpduType()){
							case COTP.COTP_TPDUTYPE_CR:
								sendMessage(ctx, TPKT.createTPKT(COTP.createCOTPCC()).getPacket());
								break;
							case COTP.COTP_TPDUTYPE_DT: {
								S7 s7 = S7.analyzePdu(cotp.getTpduData());// pdu=tpdu_data=cotp payload
								if(s7 != null) {
									switch(s7.getFunctionCode()){
										case S7.S7_PARA_FUNCODE_SERVICESC:
											sendMessage(ctx, TPKT.createTPKT(COTP.createCOTPDT(S7.createS7CC(480, null))).getPacket());// TODO paramlize the pdu length
											break;
										case S7.S7_PARA_FUNCODE_SERVICEREAD:
											readAck(ctx, s7);
											break;
										case S7.S7_PARA_FUNCODE_SERVICEWRITE:
											writeAck(ctx, s7);
											break;
										default:
											logger.trace("Client({}:{}) requesting function({}) not support", client.getAddress().getHostAddress(), client.getPort(), s7.getFunctionCode());
											break;
									}
								} else
									logger.trace("Client({}:{}) message received is a invalid S7 telegram", client.getAddress().getHostAddress(), client.getPort());
								break;
							}
							default:
								logger.trace("Client({}:{}) message received is a unknown COTP telegram", client.getAddress().getHostAddress(), client.getPort());
								break;
						}
					} else
						logger.trace("Client({}:{}) message received is a invalid COTP telegram", client.getAddress().getHostAddress(), client.getPort());
				} else
					logger.trace("Client({}:{}) message received is a invalid TPKT telegram", client.getAddress().getHostAddress(), client.getPort());
			} else
				break;
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress client = clientMap.get(ctx);
		clientMap.remove(ctx);
		clientMessageMap.remove(ctx);
		logger.info("Client({}:{}) disconnected.", client.getAddress().getHostAddress(), client.getPort());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		InetSocketAddress client = clientMap.get(ctx);
		logger.error("Client({}:{}) error.", client.getAddress().getHostAddress(), client.getPort(), cause);
	}

	/**
	 * 向指定客户端发送S7_Read_Ack报文
	 * 
	 * @param ctx 指定客户端通道上下文
	 * @param s7 客户端发送的S7_Read报文实例
	 */
	private void readAck(ChannelHandlerContext ctx, S7 s7) {
		// TODO 暂时只支持单item读取
		if(s7.getUnsignedItemCount() == 1) {
			int readAmount = s7.getUnsignedReadAmount();
			int dbNumber = s7.getUnsignedDBNumber();
			int address = s7.getUnsignedAddress() >> 3;
			if(!dataBlockMap.containsKey(dbNumber))// TODO 数据区不存在
				dataBlockMap.put(dbNumber, new DataBlock(dbNumber));
			byte[] readData = dataBlockMap.get(dbNumber).getDBB(address, readAmount);
			sendMessage(ctx, TPKT.createTPKT(COTP.createCOTPDT(S7.createS7ReadDBAck(readData))).getPacket());
		}
	}

	/**
	 * 向指定客户端发送S7_Write_Ack报文
	 * 
	 * @param ctx 指定客户端通道上下文
	 * @param s7 客户端发送的S7_Write报文实例
	 */
	private void writeAck(ChannelHandlerContext ctx, S7 s7) {
		// TODO 暂时只支持单item读取
		if(s7.getUnsignedItemCount() == 1) {
			int dbNumber = s7.getUnsignedDBNumber();
			int address = s7.getUnsignedAddress() >> 3;
			if(!dataBlockMap.containsKey(dbNumber))// TODO 数据区不存在
				dataBlockMap.put(dbNumber, new DataBlock(dbNumber));
			dataBlockMap.get(dbNumber).setDBB(address, s7.getWriteDataBytes());
			sendMessage(ctx, TPKT.createTPKT(COTP.createCOTPDT(S7.createS7WriteDBAck())).getPacket());
		}
	}

	/**
	 * 向指定客户端发送消息
	 * 
	 * @param ctx 客户端通道上下文
	 * @param msgBytes 需要发送的消息的字节数组
	 */
	private synchronized void sendMessage(ChannelHandlerContext ctx, byte[] msgBytes) {
		final InetSocketAddress client = clientMap.get(ctx);
		final ByteArray msgByteArray = new ByteArray(msgBytes);
		ByteBuf msg = Unpooled.copiedBuffer(msgBytes);
		ctx.writeAndFlush(msg).addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess())
					logger.trace("Client({}:{}) message send:{}", client.getAddress().getHostAddress(), client.getPort(), msgByteArray.toUnsignedDecimalString());
				else
					logger.trace("Client({}:{}) message send failed:{}", client.getAddress().getHostAddress(), client.getPort(), msgByteArray.toUnsignedDecimalString());
			}
		});
	}

	/**
	 * 获取客户端列表
	 * 
	 * @return
	 */
	public ConcurrentHashMap<ChannelHandlerContext, InetSocketAddress> getClientMap() {
		return clientMap;
	}

}
