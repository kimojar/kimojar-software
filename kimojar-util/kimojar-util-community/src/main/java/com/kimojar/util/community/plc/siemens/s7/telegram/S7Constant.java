/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7.telegram
 * FILE S7Constant.java
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
package com.kimojar.util.community.plc.siemens.s7.telegram;

/**
 * @author KiMoJar
 * @date 2020-04-09
 */
public class S7Constant {

	/**
	 * 连接PLC的类型.该值用于计算得到Remote TSAP.
	 * <li>PG (the programming console)</li>
	 * <li>OP (the Siemens HMI panel)</li>
	 * <li>S7 Basic (a generic data transfer connection)</li>
	 * <li>It's possible but it's not mandatory to specify the connection type.</li>
	 * <li>PLC Server and PLC Partner (in passive mode) accept any value for Local and Remote TSAP.</li>
	 * 
	 * @author KiMoJar
	 */
	public enum ConnectionType {

		PG(0x01), OP(0x02), S7Basic(0x03);

		private int value;

		private ConnectionType(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}
	}

	public enum CPUType {

		S7RTX(100), S7200(200), S7300(300), S7400(400), S71200(1200), S71500(1500);

		private int value;

		private CPUType(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}
	}

	public enum ErrorCode {

		NoError(0, "OK"),
		// socket错误
		SetSocketParaError(1, "Set socket parameter exception"),
		BindSocketAddressError(2, "Bind socket address exception"),
		// socket客户端错误
		InvalidClientIPOrPort(11, "Invalid client ip or port"),
		ServerIPUnreachable(12, "Server ip is unreachable"),
		ServerPortUnreachable(13, "Server port is unreachable"),
		ConnectToServerTimeout(14, "Connect to server timeout"),
		ConnectToServerError(15, "Connect to server exception"),
		GetSocketIOStreamError(16, "Get socket input stream or output stream exception"),
		SocketIsNotReady(17, "Socket is not ready"),
		// socket服务端错误
		InvalidServerIPOrPort(20, "Invalid server ip or port"),
		ServerListeningError(21, "Server listening error(Address already in use)"),
		// 报文错误
		ReceivedTPKTHeaderError(30, "Receive TPKT header timeout"),
		InvalidTPKTHeader(31, "Invalid TPKT header"),
		ReceivedTPKTDataError(32, "Receive TPKT data timeout"),
		InvalidTPKT(33, "Invalid TPKT telegram"),
		InvalidCOTP(34, "Invalid COTP telegram"),
		UnknownCOTP(35, "Invalid COTP telegram"),
		InvalidS7(36, "Invalid S7 telegram"),
		// 建立连接错误
		SendCOTPCRError(40, "Send COTP CR exception"),
		ReceiveCOTPCCError(41, "Receive COTP CC timeout"),
		COTPCCLengthError(42, "Wrong length of COTP CC"),
		COTPCCTpducodeError(43, "Wrong tpdu code of COTP CC"),
		ReceiveCompleteCOTPCCError(44, "Reveive complete COTP CC timeout"),
		// 建立通讯错误
		SendS7SCError(50, "Send S7 SC exception"),
		ReceiveS7CCError(51, "Receive S7 CC timeout"),
		S7CCLengthError(52, "Wrong length of S7 CC"),
		S7CCTpducodeError(53, "Wrong tpdu code of S7 CC"),
		ReceiveCompleteS7CCError(54, "Reveive complete S7 CC timeout"),
		S7CCPdulengthError(55, "Wrong pdu length of S7 CC"),
		ConnectToPLCError(56, "Connect to PLC exceprion"),
		// 读取数据错误
		SendS7RError(60, "Send S7 Read exception"),
		ReceiveS7RAError(61, "Receive S7 RA timeout"),
		ReceiveCompleteS7RAError(62, "Reveive complete S7 RA timeout"),
		InvalidReadAddress(63, "Invalid read address"),
		// 写入数据错误
		SendS7WError(70, "Send S7 Write exception"),
		ReceiveS7WAError(71, "Receive S7 WA timeout"),
		ReceiveCompleteS7WAError(72, "Reveive complete S7 WA timeout"),
		InvalidWriteAddress(73, "Invalid write address"),
		// 发送/接收sokcet数据错误
		SendMessageError(80, "Send message exception"),
		ReceiveMessageError(81, "Receive message exception"),
		ReceiveMessageTimeout(82, "Receive message timeout"),
		// S7通用ClassCode错误
		ClasscodeIndicateError(1000, "Class Code indicate error"),
		ClasscodeIndicate8104Error(1001, "Class Code indicate 'Function refused by CPU'"),
		// S7通用ReturnCode错误
		ReturncodeIndicateError(2000, "Return Code indicate error"),
		ReturncodeIndicate05Error(2001, "Return Code indicate 'Invalid address,out of range'"),
		ReturncodeIndicate0AError(2002, "Return Code indicate 'Object does not exist'"),
		;

		private int key;
		private String value;

		private ErrorCode(int key, String value) {
			this.key = key;
			this.value = value;
		}

		public int key() {
			return this.key;
		}

		public String value() {
			return this.value;
		}
	}

	/**
	 * 数据区域的类型.
	 * <li>Counter=0x1C=28</li>
	 * <li>Timer=0x1D=29</li>
	 * <li>Input=PE=0x81=129</li>
	 * <li>Output=PA=0x82=130</li>
	 * <li>Marker=0x83=131</li>
	 * <li>DataBlock=0x84=132</li>
	 * 
	 * @author KiMoJar
	 */
	public enum AreaType {

		Counter(0x1C), Timer(0x1D), Input(0x81), Output(0x82), Marker(0x83), DataBlock(0x84);

		private int value;

		private AreaType(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}
	}

	/**
	 * Transport Size
	 * 
	 * @author KiMoJar
	 * @date 2020-04-13
	 */
	public enum ElementType {

		Bit(0x01, 1),
		Byte(0x02, 1),
		Char(0x03, 1),
		Word(0x04, 2),
		Int(0x05, 2),
		DWord(0x06, 4),
		DInt(0x07, 4),
		Real(0x08, 4),
		// Date(0x09, 0), Tdo(0x0A, 0), Time(0x0B, 0), S5Time(0x0C, 0), DT(0x0F, 0),
		// Counter(0x1C, 2), Timer(0x1D, 2), IECCounter(0x1E, 2), IECTimer(0x1F, 2), HSCounter(0x20, 2),
		;

		private int key;
		private int byteSize;

		private ElementType(int key, int byteSize) {
			this.key = key;
			this.byteSize = byteSize;
		}

		public int key() {
			return this.key;
		}

		public int byteSize() {
			return this.byteSize;
		}
	}

	public enum ValueType {

		BYTE(10), SHORT(20), INTEGER(30), LONG(40), DOUBLE(50), STRING(60);

		private int value;

		private ValueType(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}
	}

	/**
	 * S7协议读取或写入数据时,返回的报文的返回码
	 * 
	 * @author KiMoJar
	 */
	public enum ReturnCode {

		Success(0xFF),
		InvalidAddress_AddressOutOfRange(0x05),
		ObjectDoesNotExit(0x0A),
		Unknown(0x00);

		private int value;

		private ReturnCode(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}

		public static ReturnCode getReturnCode(int value) {
			for(ReturnCode code : ReturnCode.values()) {
				if(code.value() == value)
					return code;
			}
			return Unknown;
		}
	}
}
