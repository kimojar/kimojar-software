/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7.socket.oio
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
package com.kimojar.util.community.plc.siemens.s7.socket.oio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kimojar.util.community.plc.siemens.s7.telegram.COTP;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7;
import com.kimojar.util.community.plc.siemens.s7.telegram.TPKT;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7Constant.ErrorCode;
import com.kimojar.util.community.plc.siemens.s7.DataBlock;

/**
 * 基于java.net而实现的PLC Server.
 * <li>由于java.net包提供的socket实现是阻塞的,其在性能和开发便捷上都有很大的问题,所以其主要目的只是学习参考使用</li>
 * <li>仅实现{@link AreaType#DataBlock}类型数据的读写,其它数据类型尚未实现</li>
 * 
 * @author KiMoJar
 */
public class S7Server {

	public static final Logger logger = LoggerFactory.getLogger(S7Server.class);

	private String serverIP;
	private int serverPort;
	private ServerSocket socketTCP;

	/**
	 * PDU长度(字节数)<br>
	 * <li>默认为480.</li>
	 * <li>TPKT\packet-data\TPDU-data.表示了S7协议部分的所有长度.</li>
	 * <li>通过发送S7_SC报文后得到的反馈报文S7_CC的最后一个字得到,也就是通过PDU Size Negotiated得到.</li>
	 */
	private int pduLength = 480;

	private ConcurrentHashMap<String, Socket> clientMap = new ConcurrentHashMap<String, Socket>();
	private ConcurrentHashMap<String, BufferedInputStream> readerMap = new ConcurrentHashMap<String, BufferedInputStream>();
	private ConcurrentHashMap<String, OutputStream> writerMap = new ConcurrentHashMap<String, OutputStream>();
	private ConcurrentHashMap<Integer, DataBlock> dataBlockMap = new ConcurrentHashMap<Integer, DataBlock>();

	public S7Server(String serverIP, int serverPort, int pduLength) {
		this.serverIP = serverIP;
		this.serverPort = (serverPort <= 0 ? TPKT.TPKT_PORT : serverPort);
		this.pduLength = (pduLength <= 0 ? this.pduLength : pduLength);
	}

	public ErrorCode listen() {
		ErrorCode lastErrorCode = ErrorCode.NoError;
		if(socketTCP != null)
			destory();
		// 1初始化监听地址
		InetSocketAddress serverSocketAddress = new InetSocketAddress(serverIP, serverPort);
		if(serverSocketAddress.isUnresolved()) {
			logger.error("Invalid server IP or Port.ServerIP:{},ServerPort:{}.", serverIP, serverPort);
			return lastErrorCode = ErrorCode.InvalidServerIPOrPort;
		}
		// 2设置socket属性并绑定socket地址
		try {
			socketTCP = new ServerSocket();// 其构造方法有异常抛出,所以在此处初始化
			socketTCP.setReuseAddress(true);
			socketTCP.setReceiveBufferSize(1024);
			socketTCP.setSoTimeout(0);// A timeout of zero is interpreted as an infinite timeout
			socketTCP.bind(serverSocketAddress);// 尽量最后调用,否则有些设置无效
		} catch(IOException e) {
			logger.error("Bind SocketAddress failed.", e);
			return lastErrorCode = ErrorCode.BindSocketAddressError;
		}
		// 3服务端监听,等待客户端连接
		Thread listener = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					try {
						Socket client = socketTCP.accept();
						String clinetIP = client.getInetAddress().getHostAddress();
						int clientPort = client.getPort();
						String clientId = clinetIP + ":" + clientPort;
						if(!clientMap.containsKey(clientId)) {
							clientMap.put(clientId, client);
							readerMap.put(clientId, new BufferedInputStream(client.getInputStream()));
							writerMap.put(clientId, client.getOutputStream());
							createClientDisposer(clientId);
						}
						logger.debug("Client({}) is connected.", clientId);
						Thread.sleep(50);
					} catch(IOException | InterruptedException e) {
						logger.error("Socket server listening error.", e);
					}
				}
			}
		}, "S7-Server-ClientListener");
		listener.start();
		logger.info("PLC({}:{}) is listening for connection.", serverIP, serverPort);

		return lastErrorCode;
	}

	private void createClientDisposer(final String clientId) {
		Thread clientDisposer = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					try {
						Socket client = clientMap.get(clientId);
						if(client.isClosed() || client.isInputShutdown() || client.isOutputShutdown()) {
							clientMap.remove(clientId);
							logger.info("Client({}) is closed.", clientId);
							break;
						}
						try {
							client.sendUrgentData(0xFF);
						} catch(IOException e) {
							clientMap.remove(clientId);
							logger.info("Client({}) is disconnected.", clientId);
							break;
						}
						BufferedInputStream reader = readerMap.get(clientId);
						byte[] packet_header = new byte[4];// packet_header=packet - packet_data
						if(receiveData(reader, packet_header, 0, packet_header.length)) {
							int tpduLength = TPKT.analyzePacketDataLength(packet_header);
							if(tpduLength != 0) {
								byte[] tpdu = new byte[tpduLength];// tpdu=packet_data=tpkt payload
								if(receiveData(reader, tpdu, 0, tpdu.length)) {
									COTP cotp = COTP.analyzeTpdu(tpdu);
									if(cotp != null) {
										switch(cotp.getTpduType()){
											case COTP.COTP_TPDUTYPE_CR:
												establishConnection(clientId, cotp);
												break;
											case COTP.COTP_TPDUTYPE_DR:
												break;
											case COTP.COTP_TPDUTYPE_DT: {
												S7 s7 = S7.analyzePdu(cotp.getTpduData());// pdu=tpdu_data=cotp payload
												if(s7 != null) {
													switch(s7.getFunctionCode()){
														case S7.S7_PARA_FUNCODE_SERVICESC:
															establishCommunication(clientId);
															break;
														case S7.S7_PARA_FUNCODE_SERVICEREAD:
															read(clientId, s7);
															break;
														case S7.S7_PARA_FUNCODE_SERVICEWRITE:
															write(clientId, s7);
															break;
														default:
															break;
													}
												}
												break;
											}
											default:
												break;
										}
									}
								}
							}
						}
						Thread.sleep(5);
					} catch(Exception e) {
						logger.error("Client({}) occured error while dispose message.{}", clientId, e);
					}
				}
			}
		}, "S7-Server-ClientDisposer-" + clientId);
		clientDisposer.start();
	}

	/**
	 * 向指定客户端回复连接确认报文COTP_CC
	 * 
	 * @param clientId 客户端id
	 * @param cotp cotp报文实例
	 */
	private void establishConnection(String clientId, COTP cotp) {
		byte[] cotp_cc = TPKT.createTPKT(COTP.createCOTPCC()).getPacket();// 连接确认报文
		try {
			writerMap.get(clientId).write(cotp_cc, 0, cotp_cc.length);
		} catch(IOException e) {
			logger.error("Send COTP_CC to client({}) failed.{}", clientId, e);
		}
	}

	private void establishCommunication(String clientId) {
		byte[] s7_cc = TPKT.createTPKT(COTP.createCOTPDT(S7.createS7CC(480, null))).getPacket();// 通讯确认报文
		try {
			writerMap.get(clientId).write(s7_cc, 0, s7_cc.length);
		} catch(IOException e) {
			logger.error("Send S7_CC to client({}) failed.{}", clientId, e);
		}
	}

	private void read(String clientId, S7 s7) {
		try {
			int readAmount = s7.getUnsignedReadAmount();
			int dbNumber = s7.getUnsignedDBNumber();
			int address = s7.getUnsignedAddress();
			if(!dataBlockMap.containsKey(dbNumber))
				dataBlockMap.put(dbNumber, new DataBlock(dbNumber));
			byte[] readData = dataBlockMap.get(dbNumber).getDBB(address, readAmount);
			byte[] s7_read_ack = TPKT.createTPKT(COTP.createCOTPDT(S7.createS7ReadDBAck(readData))).getPacket();// 读取反馈报文
			writerMap.get(clientId).write(s7_read_ack, 0, s7_read_ack.length);
		} catch(Exception e) {
			logger.error("Send S7_CC to client({}) failed.{}", clientId, e);
		}
	}

	private void write(String clientId, S7 s7) {
		try {
			int dbNumber = s7.getUnsignedDBNumber();
			int address = s7.getUnsignedAddress();
			if(!dataBlockMap.containsKey(dbNumber))
				dataBlockMap.put(dbNumber, new DataBlock(dbNumber));
			dataBlockMap.get(dbNumber).setDBB(address, s7.getWriteDataBytes());// TODO 需要一个返回值
			byte[] s7_write_ack = TPKT.createTPKT(COTP.createCOTPDT(S7.createS7WriteDBAck())).getPacket();// 读取反馈报文
			writerMap.get(clientId).write(s7_write_ack, 0, s7_write_ack.length);
		} catch(Exception e) {
			logger.error("Send S7_CC to client({}) failed.{}", clientId, e);
		}
	}

	/**
	 * 等待可以不受阻塞地从输入流读取(或跳过)的估计字节数达到预期的字节数.
	 * 
	 * @param size 预期的字节数.
	 * @param timeout 预期的超时时间.
	 * @return 等待结果.
	 */
	private boolean waitData(BufferedInputStream reader, int size, int timeout) {
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
	private boolean receiveData(BufferedInputStream reader, byte[] buffer, int pos, int size) {
		int readCount = 0;
		if(waitData(reader, size, 1000)) {
			try {
				readCount = reader.read(buffer, pos, size);
			} catch(Exception e) {
				logger.error("Read data from remote({}:{}) exception.", serverIP, serverPort, e);
				return false;
			}
		}
		return readCount == size;
	}

	public void destory() {

	}

	public static void main(String[] args) {
		S7Server ss = new S7Server("123.123.123.123", 102, 0);
		ss.listen();
	}
}
