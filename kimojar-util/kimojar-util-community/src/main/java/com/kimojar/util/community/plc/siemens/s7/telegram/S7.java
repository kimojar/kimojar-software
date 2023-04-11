/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7.telegram
 * FILE S7.java
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
public class S7 {

	public static final int PduHeaderLength10 = 10;
	public static final int PduHeaderLength12 = 12;
	public static final int S7scPduLength = 0x12;
	public static final int S7ccPduLength = 0x14;
	// public static final int SingleReadPacketLength = 4 + 3 + 24;// packet header + tpdu header + pdu
	public static final int SingleReadAckPduLengthExceptData = PduHeaderLength12 + 2 + 4;
	public static final int SingleReadAckPacketLengthExceptData = 4 + 3 + SingleReadAckPduLengthExceptData;
	public static final int SingleWritePduLengthExceptData = PduHeaderLength10 + 14 + 4;
	// public static final int SingleWritePacketLegnthExceptData = 4 + 3 + SingleWritePduLengthExceptData;
	public static final int SingleWriteAckPacketLength = 4 + 3 + 15;// packet header + tpdu header + pdu
	public static final int MinimumPduLength = 10;

	private byte[] pdu;
	private byte[] pduHeader;
	private byte[] pduPara;
	private byte[] pduData;

	private S7(byte[] pdu) {
		this.pdu = pdu;
		int paraLength = ByteUtil.getWordAt(pdu, 6);
		int dataLength = ByteUtil.getWordAt(pdu, 8);
		switch(pdu[1]){
			case S7_HEADER_ROSCTR_ACK:
			case S7_HEADER_ROSCTR_ACKDATA:
				this.pduHeader = Arrays.copyOfRange(pdu, 0, PduHeaderLength12);
				this.pduPara = Arrays.copyOfRange(pdu, PduHeaderLength12, PduHeaderLength12 + paraLength);
				this.pduData = Arrays.copyOfRange(pdu, PduHeaderLength12 + paraLength, PduHeaderLength12 + paraLength + dataLength);
				break;
			default:
				this.pduHeader = Arrays.copyOfRange(pdu, 0, PduHeaderLength10);
				this.pduPara = Arrays.copyOfRange(pdu, PduHeaderLength10, PduHeaderLength10 + paraLength);
				this.pduData = Arrays.copyOfRange(pdu, PduHeaderLength10 + paraLength, PduHeaderLength10 + paraLength + dataLength);
				break;
		}
	}

	/**
	 * 分析pdu是否合法,以及根据分析结果来决定是否返回一个S7实例.
	 * <li>如果返回值不为null,则说明参数pdu是合法的,以及返回的S7实例也是合法的.</li>
	 * 
	 * @param pdu 待分析的pdu
	 * @return 分析失败则返回null;分析成功则返回参数pdu所代表的S7实例
	 */
	public static S7 analyzePdu(byte[] pdu) {

		if(pdu == null || pdu.length < MinimumPduLength)
			return null;
		if(S7_HEADER_PROTOCOLID != pdu[0])
			return null;
		if(pdu[1] < S7_HEADER_ROSCTR_JOB || pdu[1] > S7_HEADER_ROSCTR_USERDATA)
			return null;
		int parameterLength = ByteUtil.getWordAt(pdu, 6);
		int dataLength = ByteUtil.getWordAt(pdu, 8);
		switch(pdu[1]){
			case S7_HEADER_ROSCTR_ACK:
			case S7_HEADER_ROSCTR_ACKDATA:
				if(pdu.length != PduHeaderLength12 + parameterLength + dataLength)
					return null;
				break;
			default:
				if(pdu.length != PduHeaderLength10 + parameterLength + dataLength)
					return null;
				break;
		}
		return new S7(pdu);
	}

	/**
	 * 创建一个请求建立通讯的S7报文
	 * 
	 * @param pduLength excepted PDU Length
	 * @return
	 */
	public static S7 createS7SC(int pduLength) {
		byte[] pdu = {
		// S7 HEADER
		S7_HEADER_PROTOCOLID, // S7_PROTOCOLID
		S7_HEADER_ROSCTR_JOB, // S7_ROSCTR
		(byte) 0x00, (byte) 0x00, // S7_REDUNDANCYID
		(byte) 0x00, (byte) 0x00, // S7_PDUREFERENCE
		(byte) 0x00, (byte) 0x08, // S7_PARALENGTH
		(byte) 0x00, (byte) 0x00, // S7_DATALENGTH
		// S7 PARA
		S7_PARA_FUNCODE_SERVICESC, // S7_FUNCTIONCODE
		(byte) 0x00, // S7_RESERVED
		(byte) 0x00, (byte) 0x01, // S7_CALLING
		(byte) 0x00, (byte) 0x01, // S7_CALLED
		(byte) 0x01, (byte) 0xE0// S7_PDULENGTH,默认为480 bytes
		};
		if(pduLength > 0)
			ByteUtil.setWordAt(pdu, 16, pduLength);
		return new S7(pdu);
	}

	/**
	 * 创建一个确认建立通讯的S7报文
	 * 
	 * @param pduLength negotiated PDU Length
	 * @param errorClassCode error class & error code
	 * @return
	 */
	public static S7 createS7CC(int pduLength, ErrorClassCode errorClassCode) {
		byte[] pdu = {
		// S7 HEADER
		S7_HEADER_PROTOCOLID, // S7_PROTOCOLID
		S7_HEADER_ROSCTR_ACK, // S7_ROSCTR
		(byte) 0x00, (byte) 0x00, // S7_REDUNDANCYID
		(byte) 0x00, (byte) 0x00, // S7_PDUREFERENCE
		(byte) 0x00, (byte) 0x08, // S7_PARALENGTH
		(byte) 0x00, (byte) 0x00, // S7_DATALENGTH
		(byte) 0x00, // S7_ERRORCLASS
		(byte) 0x00, // S7_ERRORCODE
		// S7 PARA
		S7_PARA_FUNCODE_SERVICESC, // S7_FUNCTIONCODE
		(byte) 0x00, // S7_RESERVED
		(byte) 0x00, (byte) 0x01, // S7_CALLING
		(byte) 0x00, (byte) 0x01, // S7_CALLED
		(byte) 0x01, (byte) 0xE0// S7_PDULENGTH,默认为480 bytes
		};
		if(pduLength > 0)
			ByteUtil.setWordAt(pdu, 18, pduLength);
		if(errorClassCode != null)
			ByteUtil.setWordAt(pdu, 10, errorClassCode.key);
		return new S7(pdu);
	}

	/**
	 * 以字节为基本单位,从指定DB块和指定字节地址读取指定数目字节.单item读取.
	 * 
	 * @param dbNumber 要读取的DB块
	 * @param address 读取的字节起始地址
	 * @param amount 读取的字节数
	 * @return
	 */
	public static S7 createS7ReadDB(int dbNumber, int address, int amount) {
		byte[] pdu = {
		// S7 HEADER
		S7_HEADER_PROTOCOLID, // S7_PROTOCOLID
		S7_HEADER_ROSCTR_JOB, // S7_ROSCTR
		(byte) 0x00, (byte) 0x00, // S7_REDUNDANCYID
		(byte) 0xAA, (byte) 0x00, // S7_PDUREFERENCE
		(byte) 0x00, (byte) 0x0E, // S7_PARALENGTH
		(byte) 0x00, (byte) 0x00, // S7_DATALENGTH
		// S7 PARA
		S7_PARA_FUNCODE_SERVICEREAD, // S7_FUNCTIONCODE
		(byte) 0x01, // S7_ITEMCOUNT
		(byte) 0x12, // S7_VARIABLESPECIFICATION
		(byte) 0x0A, // S7_LENGTHOFREMAININGBYTES
		S7_PARA_VARSPECSYNTAXID_S7ANY, // S7_VARIABLESPECIFICATIONSYNTAXID
		(byte) 0x02, // S7_TRANSPORTSIZE
		(byte) 0x00, (byte) 0x00, // S7_READAMOUNT
		(byte) 0x00, (byte) 0x00, // S7_DATABLOCKNUMBER
		S7_PARA_AREA_DB, // S7_AREATYPE
		(byte) 0x00, (byte) 0x00, (byte) 0x00// S7_ADDRESS
		};
		ByteUtil.setWordAt(pdu, 16, amount);
		ByteUtil.setWordAt(pdu, 18, dbNumber);
		pdu[21] = (byte) (address >> 16 & 0xFF);// address most HI
		pdu[22] = (byte) (address >> 8 & 0xFF);// address better HI
		pdu[23] = (byte) (address & 0xFF);// address LO
		return new S7(pdu);
	}

	public static S7 createS7ReadDBAck(byte[] readData) {
		byte[] pduExceptData = {
		// S7 HEADER
		S7_HEADER_PROTOCOLID, // S7_PROTOCOLID
		S7_HEADER_ROSCTR_ACKDATA, // S7_ROSCTR
		(byte) 0x00, (byte) 0x00, // S7_REDUNDANCYID
		(byte) 0xAA, (byte) 0x00, // S7_PDUREFERENCE
		(byte) 0x00, (byte) 0x02, // S7_PARALENGTH
		(byte) 0x00, (byte) 0x00, // S7_DATALENGTH,根据数据长度浮动
		(byte) 0x00, // S7_ERRORCLASS
		(byte) 0x00, // S7_ERRORCODE
		// S7 PARA
		S7_PARA_FUNCODE_SERVICEREAD, // S7_FUNCTIONCODE
		(byte) 0x01, // S7_ITEMCOUNT
		// S7 DATA
		S7_DATA_RETURNCODE_SUCCESS, // S7_RETURNCODE
		(byte) 0x04,// S7_TRANSPORTSIZE
		};
		ByteUtil.setWordAt(pduExceptData, 8, 4 + readData.length);
		ByteArray pdu = new ByteArray();
		pdu.add(pduExceptData);
		pdu.add(ByteUtil.shortToBytes((short) readData.length, ByteOrder.BIG_ENDIAN));// S7_RETURNDATALENGTHA
		pdu.add(readData);
		return new S7(pdu.toArray());
	}

	public static S7 createS7WriteDB(int dbNumber, int address, byte[] data) {
		byte[] pduExceptData = {
		// S7 HEADER
		S7_HEADER_PROTOCOLID, // S7_PROTOCOLID
		S7_HEADER_ROSCTR_JOB, // S7_ROSCTR
		(byte) 0x00, (byte) 0x00, // S7_REDUNDANCYID
		(byte) 0xAB, (byte) 0x00, // S7_PDUREFERENCE
		(byte) 0x00, (byte) 0x0E, // S7_PARALENGTH
		(byte) 0x00, (byte) 0x00, // S7_DATALENGTH,根据数据长度浮动
		// S7 PARA
		S7_PARA_FUNCODE_SERVICEWRITE, // S7_FUNCTIONCODE
		(byte) 0x01, // S7_ITEMCOUNT
		(byte) 0x12, // S7_VARIABLESPECIFICATION
		(byte) 0x0A, // S7_LENGTHOFTHEREAMININGBYTES
		S7_PARA_VARSPECSYNTAXID_S7ANY, // S7_VARIABLESPECIFICATIONSYANTAXID
		(byte) 0x02, // S7_TRANSPORTSIZE
		(byte) 0x00, (byte) 0x00, // S7_WRITEAMOUNT,根据数据长度浮动
		(byte) 0x00, (byte) 0x00, // S7_DBNUMBER,根据数据浮动
		S7_PARA_AREA_DB, // S7_AREATYPE
		(byte) 0x00, (byte) 0x00, (byte) 0x00, // S7_ADDRESS,根据数据浮动
		// S7 DATA
		(byte) 0x00, // S7_RETURNCODE
		(byte) 0x04, // S7_TRANSPORTSIZE
		(byte) 0x00, (byte) 0x00// S7_LENGTH,根据数据浮动
		};
		ByteUtil.setWordAt(pduExceptData, 8, 4 + data.length);
		ByteUtil.setWordAt(pduExceptData, 16, data.length);
		ByteUtil.setWordAt(pduExceptData, 18, dbNumber);
		pduExceptData[21] = (byte) (address >> 16 & 0xFF);// address most HI
		pduExceptData[22] = (byte) (address >> 8 & 0xFF);// address better HI
		pduExceptData[23] = (byte) (address & 0xFF);// address LO
		int dataTransportSize = data.length << 3;
		ByteUtil.setWordAt(pduExceptData, 26, dataTransportSize);
		ByteArray pdu = new ByteArray();
		pdu.add(pduExceptData);
		pdu.add(data);
		return new S7(pdu.toArray());
	}

	public static S7 createS7WriteDBAck() {
		byte[] pdu = {
		// S7 HEADER
		S7_HEADER_PROTOCOLID, // S7_PROTOCOLID
		S7_HEADER_ROSCTR_ACKDATA, // S7_ROSCTR
		(byte) 0x00, (byte) 0x00, // S7_REDUNDANCYID
		(byte) 0xAB, (byte) 0x00, // S7_PDUREFERENCE
		(byte) 0x00, (byte) 0x02, // S7_PARALENGTH
		(byte) 0x00, (byte) 0x01, // S7_DATALENGTH,根据数据长度浮动
		(byte) 0x00, // S7_ERRORCLASS
		(byte) 0x00, // S7_ERRORCODE
		// S7 PARA
		S7_PARA_FUNCODE_SERVICEWRITE, // S7_FUNCTIONCODE
		(byte) 0x01, // S7_ITEMCOUNT
		// S7 DATA
		S7_DATA_RETURNCODE_SUCCESS,// S7_RETURNCODE
		};
		return new S7(pdu);
	}

	public byte[] getPdu() {
		return pdu;
	}

	public byte[] getPduHeader() {
		return pduHeader;
	}

	public ErrorClassCode getErrorClassCode() {
		return ErrorClassCode.getErrorClassCode(ByteUtil.getUnsignValue(pduHeader[10], pduHeader[11]));
	}

	public byte getErrorClass() {
		return pduHeader[10];
	}

	public byte getErrorCode() {
		return pduHeader[11];
	}

	public byte[] getPduPara() {
		return pduPara;
	}

	public byte getFunctionCode() {
		return pduPara[0];
	}

	public int getUnsignedItemCount() {
		return pduPara[1] & 0xFF;
	}

	public int getUnsignedPduLength() {
		return ByteUtil.getWordAt(pduPara, 6);
	}

	public byte getTransportSize() {
		return pduPara[5];
	}

	public int getUnsignedReadAmount() {
		return ByteUtil.getWordAt(pduPara, 6);
	}

	public int getUnsignedDBNumber() {
		return ByteUtil.getWordAt(pduPara, 8);
	}

	public byte getAreaType() {
		return pduPara[10];
	}

	public int getUnsignedAddress() {
		return ByteUtil.getUnsignValue(pduPara[11], pduPara[12], pduPara[13]);
	}

	public byte[] getPduData() {
		return pduData;
	}

	public byte getReturnCode() {
		return pduData[0];
	}

	public int getUnsignedWriteDataLength() {
		return ByteUtil.getWordAt(pduData, 2);
	}

	public byte[] getWriteDataBytes() {
		return Arrays.copyOfRange(pduData, 4, pduData.length);
	}

	public enum ErrorClassCode {

		Unknown(0xFFFF, "Unknown error"),
		NoError(0x0000, "No error"),
		;

		private int key;
		private String value;

		private ErrorClassCode(int key, String value) {
			this.key = key;
			this.value = value;
		}

		public int key() {
			return this.key;
		}

		public String value() {
			return this.value;
		}

		public static ErrorClassCode getErrorClassCode(int key) {
			for(ErrorClassCode ecc : ErrorClassCode.values()) {
				if(ecc.key() == key)
					return ecc;
			}
			return Unknown;
		}
	}

	public static final byte S7_HEADER_PROTOCOLID = 0x32;
	public static final byte S7_HEADER_ROSCTR_JOB = 0x01;
	public static final byte S7_HEADER_ROSCTR_ACK = 0x02;
	public static final byte S7_HEADER_ROSCTR_ACKDATA = 0x03;
	public static final byte S7_HEADER_ROSCTR_USERDATA = 0x07;
	public static final byte S7_PARA_FUNCODE_SERVICECPU = 0x00;
	public static final byte S7_PARA_FUNCODE_SERVICESC = (byte) 0xF0;
	public static final byte S7_PARA_FUNCODE_SERVICEREAD = 0x04;
	public static final byte S7_PARA_FUNCODE_SERVICEWRITE = 0x05;
	public static final byte S7_PARA_FUNCODE_FUNCRD = 0x1A;
	public static final byte S7_PARA_FUNCODE_FUNCDB = 0x1B;
	public static final byte S7_PARA_FUNCODE_FUNCDE = 0x1C;
	public static final byte S7_PARA_FUNCODE_FUNCSU = 0x1D;
	public static final byte S7_PARA_FUNCODE_FUNCUL = 0x1E;
	public static final byte S7_PARA_FUNCODE_FUNCEU = 0x1F;
	public static final byte S7_PARA_FUNCODE_FUNCPI = 0x28;
	public static final byte S7_PARA_FUNCODE_FUNCPS = 0x29;
	public static final byte S7_PARA_VARSPECSYNTAXID_S7ANY = 0x10;
	public static final byte S7_PARA_VARSPECSYNTAXID_PBCID = 0x13;
	public static final byte S7_PARA_VARSPECSYNTAXID_ALARMLOCKFREESET = 0x15;
	public static final byte S7_PARA_VARSPECSYNTAXID_ALARMINDSET = 0x16;
	public static final byte S7_PARA_VARSPECSYNTAXID_ALARMACKSET = 0x19;
	public static final byte S7_PARA_VARSPECSYNTAXID_ALARMQUERYREQSET = 0x1A;
	public static final byte S7_PARA_VARSPECSYNTAXID_NOTIFYINDSET = 0x1C;
	public static final byte S7_PARA_VARSPECSYNTAXID_NCK = (byte) 0x82;
	public static final byte S7_PARA_VARSPECSYNTAXID_NCKMETRIC = (byte) 0x83;
	public static final byte S7_PARA_VARSPECSYNTAXID_NCKINCH = (byte) 0x84;
	public static final byte S7_PARA_VARSPECSYNTAXID_DRIVEESANY = (byte) 0xA2;
	public static final byte S7_PARA_VARSPECSYNTAXID_1200SYM = (byte) 0xB2;
	public static final byte S7_PARA_VARSPECSYNTAXID_DBREAD = (byte) 0xB0;
	public static final byte S7_PARA_TRANSPORTSIZE_BIT = 0x01;
	public static final byte S7_PARA_TRANSPORTSIZE_BYTE = 0x02;
	public static final byte S7_PARA_TRANSPORTSIZE_CHAR = 0x03;
	public static final byte S7_PARA_TRANSPORTSIZE_WORD = 0x04;
	public static final byte S7_PARA_TRANSPORTSIZE_INT = 0x05;
	public static final byte S7_PARA_TRANSPORTSIZE_DWORD = 0x06;
	public static final byte S7_PARA_TRANSPORTSIZE_DINT = 0x07;
	public static final byte S7_PARA_TRANSPORTSIZE_REAL = 0x08;
	public static final byte S7_PARA_TRANSPORTSIZE_DATE = 0x09;
	public static final byte S7_PARA_TRANSPORTSIZE_TOD = 0x0A;
	public static final byte S7_PARA_TRANSPORTSIZE_TIME = 0x0B;
	public static final byte S7_PARA_TRANSPORTSIZE_S5TIME = 0x0C;
	public static final byte S7_PARA_TRANSPORTSIZE_DT = 0x0F;
	public static final byte S7_PARA_TRANSPORTSIZE_COUNTER = 0x1C;
	public static final byte S7_PARA_TRANSPORTSIZE_TIMER = 0x1D;
	public static final byte S7_PARA_TRANSPORTSIZE_IECCOUNTER = 0x1E;
	public static final byte S7_PARA_TRANSPORTSIZE_IECTIMER = 0x1F;
	public static final byte S7_PARA_TRANSPORTSIZE_HSCOUNTER = 0x20;
	public static final byte S7_PARA_AREA_DATARECORD = 0x01;
	public static final byte S7_PARA_AREA_SYSINFO = 0x03;
	public static final byte S7_PARA_AREA_SYSFLAGS = 0x05;
	public static final byte S7_PARA_AREA_ANAIN = 0x06;
	public static final byte S7_PARA_AREA_ANAOUT = 0x07;
	public static final byte S7_PARA_AREA_P = (byte) 0x80;
	public static final byte S7_PARA_AREA_INPUTS = (byte) 0x81;
	public static final byte S7_PARA_AREA_OUTPUTS = (byte) 0x82;
	public static final byte S7_PARA_AREA_FLAGS = (byte) 0x83;
	public static final byte S7_PARA_AREA_DB = (byte) 0x84;
	public static final byte S7_PARA_AREA_DI = (byte) 0x85;
	public static final byte S7_PARA_AREA_LOCAL = (byte) 0x86;
	public static final byte S7_PARA_AREA_V = (byte) 0x87;
	public static final byte S7_PARA_AREA_COUNTER = 0x1C;
	public static final byte S7_PARA_AREA_TIMER = 0x1D;
	public static final byte S7_PARA_AREA_COUNTER200 = 0x1E;
	public static final byte S7_PARA_AREA_TIMER200 = 0x1F;
	public static final byte S7_DATA_RETURNCODE_SUCCESS = (byte) 0xFF;
	public static final byte S7_DATA_RETURNCODE_OBJECTNOTEXIST = 0x0A;
}
