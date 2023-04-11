/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7.socket.netty
 * FILE S7ClientHandler.java
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
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kimojar.util.community.plc.siemens.s7.S7Event;
import com.kimojar.util.community.plc.siemens.s7.telegram.TPKT;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7Constant.ErrorCode;
import com.kimojar.util.community.data.ByteArray;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author KiMoJar
 * @date 2020-04-09
 */
public class S7ClientHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(S7ClientHandler.class);

	private ChannelHandlerContext ctx;
	private InetSocketAddress server;
	private StringBuffer receivedMessage = new StringBuffer();
	private Charset charset = CharsetUtil.ISO_8859_1;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		this.server = (InetSocketAddress) ctx.channel().remoteAddress();
		InetSocketAddress client = (InetSocketAddress) ctx.channel().localAddress();
		String threadName = "S7 Client(" + client.getAddress().getHostAddress() + ":" + client.getPort()
		+ "->" + server.getAddress().getHostAddress() + ":" + server.getPort() + ")";
		Thread.currentThread().setName(threadName);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf receivedByteBuf = (ByteBuf) msg;
		byte[] receivedBytes = new byte[receivedByteBuf.readableBytes()];
		receivedByteBuf.readBytes(receivedBytes);
		receivedByteBuf.release();
		ByteArray receivedByteArray = new ByteArray(receivedBytes);
		logger.trace("Server({}:{}) message received:{}", server.getAddress().getHostAddress(), server.getPort(), receivedByteArray.toUnsignedDecimalString());
		receivedMessage.append(new String(receivedBytes, charset));
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.trace("Server({}:{}) disconnected", server.getAddress().getHostAddress(), server.getPort());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.trace("Server({}:{}) exception:{}", server.getAddress().getHostAddress(), server.getPort(), cause.toString());
	}

	public void sendMessage(byte[] sendBytes) {
		final ByteArray sendByteArray = new ByteArray(sendBytes);
		ByteBuf sendByteBuf = Unpooled.copiedBuffer(sendBytes);
		/*
		 * 发送前要判断通道是否可用,否则发送报异常,而且不是InterruptedException异常.
		 * 抛出的是java.security.PrivilegedActionException: java.nio.channels.ClosedChannelException.
		 * 由于该异常不会由ctx.writeAndFlush(sendByteBuf).sync()触发,所以无法在此处捕获,只能判断通道状态.
		 */
		if(!ctx.channel().isOpen() || !ctx.channel().isActive()) {
			logger.trace("Server({}:{}) message send canceled(because of chanel is disconnect):{}", server.getAddress().getHostAddress(), server.getPort(), sendByteArray.toUnsignedDecimalString());
			return;
		}
		try {
			ctx.writeAndFlush(sendByteBuf).sync().addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess())
						logger.trace("Server({}:{}) message send:{}", server.getAddress().getHostAddress(), server.getPort(), sendByteArray.toUnsignedDecimalString());
					else
						logger.trace("Server({}:{}) message send failed:{}", server.getAddress().getHostAddress(), server.getPort(), sendByteArray.toUnsignedDecimalString());
				}
			});
		} catch(InterruptedException e) {
			logger.trace("Server({}:{}) message send exception:{}", server.getAddress().getHostAddress(), server.getPort(), sendByteArray.toUnsignedDecimalString());
		}
	}

	public S7Event receiveMessage() {
		long startTime = System.currentTimeMillis();
		while((System.currentTimeMillis() - startTime) < 2000)// TODO 超时时间可以参数化
		{
			if(receivedMessage.length() >= TPKT.PacketHeaderLength) {
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
					return new S7Event(packet, startTime, ErrorCode.NoError);
				}
			}
		}
		System.out.println(System.currentTimeMillis() - startTime);
		return new S7Event(startTime, ErrorCode.ReceiveMessageTimeout);
	}

	public boolean isConnect() {
		if(ctx == null)
			return false;
		return ctx.channel().isOpen() && ctx.channel().isActive();
	}
}
