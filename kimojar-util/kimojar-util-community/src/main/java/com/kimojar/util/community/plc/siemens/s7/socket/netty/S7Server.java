/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7.socket.netty
 * FILE S7Server.java
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
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kimojar.util.community.plc.siemens.s7.telegram.TPKT;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7Constant.ErrorCode;
import com.kimojar.util.community.plc.siemens.s7.DataBlock;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author KiMoJar
 * @date 2020-04-09
 */
public class S7Server {

	public static final Logger logger = LoggerFactory.getLogger(S7Server.class);

	private String serverName;
	private EventLoopGroup acceptorGroup;
	private EventLoopGroup clientGroup;
	private ServerBootstrap serverBoot;
	private ChannelFuture serverFuture;
	private S7ServerHandler serverHandler;
	private String serverIP;
	private int serverPort;
	private ConcurrentHashMap<Integer, DataBlock> dataBlockMap;

	private Status status = Status.Stop;

	public enum Status {
		Stop, Running, Error,
		;
	}

	public S7Server() {
		this(null, 0);
	}

	/**
	 * S7服务端构造函数.
	 * 
	 * @param serverIP
	 * @param serverPort
	 */
	public S7Server(String serverIP, int serverPort) {
		this.serverIP = serverIP;
		this.serverPort = (serverPort <= 0 ? TPKT.TPKT_PORT : serverPort);
		this.dataBlockMap = new ConcurrentHashMap<Integer, DataBlock>();
	}

	/**
	 * S7服务端监听.
	 * 
	 * @return
	 */
	public ErrorCode listen() {
		ErrorCode lastErrorCode = ErrorCode.NoError;
		if(serverBoot != null)
			destroy();
		// 检测服务端地址是否可用
		InetSocketAddress serverSocketAddress = new InetSocketAddress(serverIP, serverPort);
		if(serverSocketAddress.isUnresolved()) {
			logger.error("Invalid server ip or port. ServerIP:{},ServerPort:{}.", serverIP, serverPort);
			status = Status.Error;
			return lastErrorCode = ErrorCode.InvalidServerIPOrPort;
		}
		// 初始化服务端
		acceptorGroup = new NioEventLoopGroup();
		clientGroup = new NioEventLoopGroup();
		serverBoot = new ServerBootstrap();
		serverHandler = new S7ServerHandler(dataBlockMap);
		// 引导服务端
		serverBoot.group(acceptorGroup, clientGroup);
		serverBoot.channel(NioServerSocketChannel.class);
		serverBoot.childHandler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(serverHandler);
			}
		});
		try {
			serverFuture = serverBoot.bind(serverSocketAddress).sync().addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess())
						logger.info("Server({}:{}) listening successful.", serverIP, serverPort);
					else
						logger.error("Server({}:{}) listening failed.", serverIP, serverPort);
				}
			});
			/**
			 * 阻塞listen()线程,直到通道被关闭
			 * serverFuture.channel().closeFuture().sync();
			 */
		} catch(Exception e) {
			logger.error("Server({}:{}) listening exception.", serverIP, serverPort, e);
			status = Status.Error;
			return lastErrorCode = ErrorCode.ServerListeningError;
		}
		status = Status.Running;
		return lastErrorCode;
	}

	/**
	 * S7服务端,断开通讯.
	 */
	public void destroy() {
		if(serverFuture != null) {
			/*
			 * 日志记录在前,因为服务端主动关闭连接,也会先触发serverHandler的channelInactive(),
			 * 为了区别是客户端主动断开连接还是服务端主动断开连接,所以在此处先记录出服务端shutdown日志,
			 * 表示服务端主动断开连接,以区别于客户端主动断开连接情况.
			 */
			logger.info("Server({}:{}) shutdown.", serverIP, serverPort);
			serverFuture.channel().close().addListener(ChannelFutureListener.CLOSE);
			serverFuture.awaitUninterruptibly();
			acceptorGroup.shutdownGracefully();
			clientGroup.shutdownGracefully();
			serverFuture = null;
			serverBoot = null;
		}
		status = Status.Stop;
	}

	/**
	 * 获取当前连接到服务端的客户端数量
	 * 
	 * @return 连接的客户端数量
	 */
	public int activeClientNum() {
		return serverHandler.getClientMap().size();
	}

	public DataBlock addDataBlock(int dbNumber) {
		if(!dataBlockMap.containsKey(dbNumber))
			dataBlockMap.put(dbNumber, new DataBlock(dbNumber));
		return dataBlockMap.get(dbNumber);
	}

	/**
	 * @return the serverIP
	 */
	public String getServerIP() {
		return serverIP;
	}

	/**
	 * @param serverIP the serverIP to set
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

}
