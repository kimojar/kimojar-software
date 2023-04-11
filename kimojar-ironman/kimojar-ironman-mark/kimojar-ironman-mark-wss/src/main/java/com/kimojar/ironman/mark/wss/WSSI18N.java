/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wss
 * PACKAGE com.kimojar.ironman.mark.wss
 * FILE WSSI18N.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-01-30
 * ==============================================================================
 * Copyright (C) 2023
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
package com.kimojar.ironman.mark.wss;

import com.kimojar.util.common.i18n.Internationalization;

/**
 * @author KiMoJar
 * @date 2023-01-30
 */
public enum WSSI18N implements Internationalization {

	MarkName("WebService Server"),
	MarkDescription("WebService Server can simulate a WebService node."),
	JPanel_Screen(0),
	// north panel
	JLabel_Address("Address"),
	JTextField_Address("http://123.123.123.123:9999/KiMoJarWebService"),
	JLabel_Protocol("Protocol"),
	JComboBox_Protocol(0),
	JLabel_Namespace("Namespace"),
	JTextField_Namespace("http://com.kimojar.ironman.mark.wss/"),
	JLabel_ReturnName("Return"),
	JTextField_ReturnName("return"),
	JButton_Publish("Publish"),
	JButton_Stop("Stop"),
	JButton_Browse("Browse"),
	// central panel
	JCheckBox_ServerAEnable("Enable"),
	JLabel_ServerAReturnDelay("ReturnDelay(ms)"),
	JTextField_ServerAReturnDelay("0"),
	JLabel_ServerAMethodName("MethodName"),
	JTextField_ServerAMethodName("serverA"),
	JLabel_ServerAReturnData("ReturnData"),
	JTextArea_ServerAReturnData("invoke Server A success!"),
	JLabel_ServerAReceivedData("ReceivedData"),
	JTextArea_ServerAReceivedData(0),
	JCheckBox_ServerBEnable("Enable"),
	JLabel_ServerBReturnDelay("ReturnDelay(ms)"),
	JTextField_ServerBReturnDelay("0"),
	JLabel_ServerBMethodName("MethodName"),
	JTextField_ServerBMethodName("serverB"),
	JLabel_ServerBReturnData("ReturnData"),
	JTextArea_ServerBReturnData("invoke Server B success!"),
	JLabel_ServerBReceivedData("ReceivedData"),
	JTextArea_ServerBReceivedData(0),
	JCheckBox_ServerCEnable("Enable"),
	JLabel_ServerCReturnDelay("ReturnDelay(ms)"),
	JTextField_ServerCReturnDelay("0"),
	JLabel_ServerCMethodName("MethodName"),
	JTextField_ServerCMethodName("serverC"),
	JLabel_ServerCReturnData("ReturnData"),
	JTextArea_ServerCReturnData("invoke Server C success!"),
	JLabel_ServerCReceivedData("ReceivedData"),
	JTextArea_ServerCReceivedData(0),
	TitledBorder_ServerA("ServerA"),
	TitledBorder_ServerB("ServerB"),
	TitledBorder_ServerC("ServerC"),
	JPanel_ServerA(0),
	JPanel_ServerB(0),
	JPanel_ServerC(0),
	;

	public int code;// 标准编码
	public int icode;// 编码传递时需要转化的编码，整形
	public String message;// 编码对应的默认描述
	public String scode;// 编码传递时需要转化的编码，字符型

	private WSSI18N(int code) {
		this(code, "", "", 0);
	}

	private WSSI18N(String message) {
		this(0, message, "", 0);
	}
	
	private WSSI18N(int code, String message) {
		this(code, message, "", 0);
	}

	private WSSI18N(int code, String message, String scode) {
		this(code, message, scode, 0);
	}

	private WSSI18N(int code, String message, int icode) {
		this(code, message, "", icode);
	}

	private WSSI18N(int code, String message, String scode, int icode) {
		this.code = code;
		this.message = message;
		this.scode = scode;
		this.icode = icode;
	}

	public WSSI18N getByCode(int code) {
		for(WSSI18N one : WSSI18N.values())
			if(one.code == code)
				return one;
		return null;
	}

	public WSSI18N getBySCode(String scode) {
		for(WSSI18N one : WSSI18N.values())
			if(one.scode == scode)
				return one;
		return null;
	}

	public WSSI18N getByICode(int icode) {
		for(WSSI18N one : WSSI18N.values())
			if(one.icode == icode)
				return one;
		return null;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String getPropertyKey() {
		return this.name();
	}

	@Override
	public String getDefaultMessage() {
		return message;
	}

}
