/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7.telegram
 * FILE TPKT.java
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
package com.kimojar.util.community.plc.siemens.s7.telegram;

import java.nio.ByteOrder;
import java.util.Arrays;

import com.kimojar.util.community.data.ByteArray;
import com.kimojar.util.community.data.ByteUtil;

/**
 * @author KiMoJar
 * @date 2020-04-01
 */
public class TPKT {

	public static final byte TPKT_VERSION = 0x03;
	public static final byte TPKT_RESERVED = 0x00;
	public static final int PacketHeaderLength = 4;
	/**
	 * TPKT(RFC-1006)指定了使用102端口作为通讯端口.
	 */
	public static final short TPKT_PORT = 102;

	private byte[] packet;

	private TPKT(byte[] packet) {
		this.packet = packet;
	}

	/**
	 * 分析一个packet header是否有效.
	 * 
	 * @param packetHeader 待分析的packet header
	 * @return true=有效,false=无效
	 */
	public static boolean analyzePacketHeader(byte[] packetHeader) {
		if(packetHeader == null || packetHeader.length != PacketHeaderLength)
			return false;
		if(TPKT_VERSION != packetHeader[0])
			return false;
		if(TPKT_RESERVED != packetHeader[1])
			return false;
		return true;
	}

	/**
	 * 从packet header中分析出packet的长度.
	 * <li>在分析packet长度前,会先分析packet header是否合法,如果不合法则返回0,合法则分析出packet长度.</li>
	 * 
	 * @param packetHeader 待分析的packet header
	 * @return packet的长度.0表示分析失败,由于packet header不合法
	 */
	public static int analyzePacketLength(byte[] packetHeader) {
		if(!analyzePacketHeader(packetHeader))
			return 0;
		return ByteUtil.getWordAt(packetHeader, 2);
	}

	/**
	 * 从packet header中分析出packet data的长度.
	 * <li>在分析packet data长度前,会先分析packet header是否合法,如果不合法则返回0,合法则分析出packet data长度.</li>
	 * <li>此方法也可以理解为分析出TPDU的长度.</li>
	 * <li>packet = packet header + packet data.</li>
	 * <li>packet data length = packet length - packet header length.</li>
	 * 
	 * @param packetHeader 待分析的packet header
	 * @return packet data的长度.0表示分析失败,由于packet header不合法
	 */
	public static int analyzePacketDataLength(byte[] packetHeader) {
		int packetLength = analyzePacketLength(packetHeader);
		if(packetLength == 0)
			return 0;
		return packetLength - PacketHeaderLength;
	}

	/**
	 * 分析packet是否合法,以及根据分析结果来决定是否返回一个TPKT实例.
	 * <li>如果返回值不为null,则说明参数packet是合法的,以及返回的TPKT实例也是合法的.</li>
	 * 
	 * @param packet 待分析的packet
	 * @return 分析失败则返回null;分析成功则返回参数packet所代表的TPKT实例
	 */
	public static TPKT analyzePacket(byte[] packet) {
		if(packet == null || packet.length < PacketHeaderLength)
			return null;
		int packetLength = analyzePacketLength(Arrays.copyOfRange(packet, 0, PacketHeaderLength));
		if(packetLength == 0)
			return null;
		if(packetLength != packet.length)
			return null;
		return new TPKT(packet);
	}

	/**
	 * 分析packet是否合法,以及根据分析结果来决定是否返回一个TPKT实例.
	 * <li>如果返回值不为null,则说明参数packet是合法的,以及返回的TPKT实例也是合法的.</li>
	 * 
	 * @param packetHeader 待分析的packet header
	 * @param packetData 待分析的packet data
	 * @return 分析失败则返回null;分析成功则返回参数packet所代表的TPKT实例
	 */
	public static TPKT analyzePacket(byte[] packetHeader, byte[] packetData) {
		byte[] packet = ByteArray.unitBytes(packetHeader, packetData);
		if(packet == null || packet.length < PacketHeaderLength)
			return null;
		int packetLength = analyzePacketLength(packetHeader);
		if(packetLength == 0)
			return null;
		if(packetLength != packet.length)
			return null;
		return new TPKT(packet);
	}

	public static COTP analyzeTpdu(byte[] packet) {
		TPKT tpkt = TPKT.analyzePacket(packet);
		if(tpkt != null)
			return COTP.analyzeTpdu(tpkt.getPacketData());
		return null;
	}

	public static S7 analyzePdu(byte[] packet) {
		COTP cotp = analyzeTpdu(packet);
		if(cotp != null)
			return S7.analyzePdu(cotp.getTpduData());
		return null;
	}

	public static TPKT createTPKT(COTP cotp) {
		byte[] tpdu = cotp.getTpdu();
		ByteArray packet = new ByteArray();
		// TPKT HEADER
		packet.add(TPKT_VERSION);// TPKT_VERSION
		packet.add(TPKT_RESERVED);// TPKT_RESERVED
		packet.add(ByteUtil.shortToBytes((short) (tpdu.length + PacketHeaderLength), ByteOrder.BIG_ENDIAN));// TPKT_LENGTH
		// TPKT PAYLOAD
		packet.add(tpdu);

		return new TPKT(packet.toArray());
	}

	/**
	 * 获取packet header.
	 * 
	 * @return packet header byte array
	 */
	public byte[] getPacketHeader() {
		return Arrays.copyOfRange(packet, 0, PacketHeaderLength);
	}

	/**
	 * 获取packet data.
	 * 
	 * @return packet data byte array
	 */
	public byte[] getPacketData() {
		return Arrays.copyOfRange(packet, PacketHeaderLength, packet.length);
	}

	public byte[] getPacket() {
		return packet;
	}
}
