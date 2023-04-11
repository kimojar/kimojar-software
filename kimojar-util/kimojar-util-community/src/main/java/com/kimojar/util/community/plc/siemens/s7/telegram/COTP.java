/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7.telegram
 * FILE COTP.java
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

import java.util.Arrays;

import com.kimojar.util.community.data.ByteArray;
import com.kimojar.util.community.data.ByteUtil;

/**
 * @author KiMoJar
 * @date 2020-04-01
 */
public class COTP {

	public static final byte COTP_TPDUTYPE_CR = (byte) 0xE0;
	public static final byte COTP_TPDUTYPE_CC = (byte) 0xD0;
	public static final byte COTP_TPDUTYPE_DC = (byte) 0xC0;
	public static final byte COTP_TPDUTYPE_DR = (byte) 0x80;
	public static final byte COTP_TPDUTYPE_DT = (byte) 0xF0;
	public static final byte COTP_TPDUTYPE_AK = (byte) 0x60;
	public static final byte COTP_TPDUTYPE_EA = (byte) 0x20;
	public static final byte COTP_TPDUTYPE_RJ = (byte) 0x50;
	public static final byte COTP_TPDUTYPE_ER = (byte) 0x70;
	public static final int CotpccTpduLength = 0x12;
	public static final int CotpdtTpduHeaderLength = 0x03;

	private byte[] tpdu;

	private COTP(byte[] tpdu) {
		this.tpdu = tpdu;
	}

	/**
	 * 分析tpdu是否合法,以及根据分析结果来决定是否返回一个COTP实例.
	 * <li>如果返回值不为null,则说明参数tpdu是合法的,以及返回的COTP实例也是合法的.</li>
	 * 
	 * @param tpdu 待分析的tpdu
	 * @return 分析失败则返回null;分析成功则返回参数tpdu所代表的COTP实例
	 */
	public static COTP analyzeTpdu(byte[] tpdu) {
		if(tpdu == null || tpdu.length < 2)
			return null;
		switch(tpdu[1]){
			case COTP_TPDUTYPE_CR:
			case COTP_TPDUTYPE_CC:
			case COTP_TPDUTYPE_DR:
			case COTP_TPDUTYPE_DC:
			case COTP_TPDUTYPE_DT:
			case COTP_TPDUTYPE_AK:
			case COTP_TPDUTYPE_EA:
			case COTP_TPDUTYPE_RJ:
			case COTP_TPDUTYPE_ER:
				break;
			default:
				return null;
		}
		return new COTP(tpdu);
	}

	public static COTP createCOTPCR(int destTSAP) {
		byte[] tpdu = {
		// COTP HEADER
		(byte) 0x11, // COTP_LENGTHINDICATOR
		COTP_TPDUTYPE_CR, // COTP_TPDUTYPE
		(byte) 0x00, (byte) 0x00, // COTP_DESTINATIONREFERENCE
		(byte) 0x00, (byte) 0x01, // COTP_SOURCEREFERENCE
		(byte) 0x00, // COTP_CLASSOPTIONS
		(byte) 0xC0, (byte) 0x01, (byte) 0x0A, // COTP_PARA_TPDUSIZE
		(byte) 0xC1, (byte) 0x02, (byte) 0x01, (byte) 0x00, // COTP_PARA_SOURCETSAP
		(byte) 0xC2, (byte) 0x02, (byte) 0x01, (byte) 0x02// COTP_PARA_DESTINATIONTSAP
		};
		ByteUtil.setWordAt(tpdu, 16, destTSAP);
		return new COTP(tpdu);
	}

	public static COTP createCOTPCC() {
		byte[] tpdu = {
		// COTP HEADER
		(byte) 0x11, // COTP_LENGTHINDICATOR
		COTP_TPDUTYPE_CC, // COTP_TPDUTYPE
		(byte) 0x00, (byte) 0x01, // COTP_DESTINATIONREFERENCE
		(byte) 0x00, (byte) 0x01, // COTP_SOURCEREFERENCE
		(byte) 0x00, // COTP_CLASSOPTIONS
		(byte) 0xC0, (byte) 0x01, (byte) 0x0A, // COTP_PARA_TPDUSIZE
		(byte) 0xC1, (byte) 0x02, (byte) 0x01, (byte) 0x00, // COTP_PARA_SOURCETSAP
		(byte) 0xC2, (byte) 0x02, (byte) 0x01, (byte) 0x02// COTP_PARA_DESTINATIONTSAP
		};
		return new COTP(tpdu);
	}

	public static COTP createCOTPDT(S7 s7) {
		byte[] pdu = s7.getPdu();
		ByteArray tpdu = new ByteArray();
		tpdu.add((byte) 0x02);// COTP_LENGTHINDICATOR
		tpdu.add(COTP_TPDUTYPE_DT);// COTP_TPDUTYPE
		tpdu.add((byte) 0x80);// COTP_TPDUNR&EOT
		tpdu.add(pdu);// TPKT_PAYLOAD
		return new COTP(tpdu.toArray());
	}

	public byte[] getTpduHeader() {
		return Arrays.copyOfRange(tpdu, 0, getLengthIndicator() & 0xFF + 1);
	}

	public byte[] getTpduData() {
		return Arrays.copyOfRange(tpdu, (getLengthIndicator() & 0xFF) + 1, tpdu.length);
	}

	public byte[] getTpdu() {
		return tpdu;
	}

	public byte getLengthIndicator() {
		return tpdu[0];
	}

	public byte getTpduType() {
		return tpdu[1];
	}
}
