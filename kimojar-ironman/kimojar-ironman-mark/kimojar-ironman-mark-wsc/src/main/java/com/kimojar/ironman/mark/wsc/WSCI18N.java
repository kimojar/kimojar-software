/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wsc
 * PACKAGE com.kimojar.ironman.mark.wsc
 * FILE WSCI18N.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-02-03
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
package com.kimojar.ironman.mark.wsc;

import com.kimojar.util.common.i18n.Internationalization;

/**
 * @author KiMoJar
 * @date 2023-02-03
 */
public enum WSCI18N implements Internationalization {

	MarkName("WebService Client"),
	MarkDescription("WebService Client can invoke a WebService Server."),
	JPanel_Screen(0),
	// north panel
	JLabel_Address("Address"),
	JTextField_Address("http://123.123.123.123:9999/KiMoJarWebService"),
	JLabel_Protocol("Protocol"),
	JComboBox_Protocol(0),
	JLabel_FunctionName("FunctionName"),
	JTextField_FunctionName("serverA"),
	JLabel_FunctionPara("FunctionPara"),
	JTextField_FunctionPara("para"),
	JLabel_Namespace("Namespace"),
	JTextField_Namespace("http://com.kimojar.ironman.mark.wss/"),
	JLabel_FunctionResponse("FunctionResponse"),
	JTextField_FunctionResponse("serverAResponse"),
	JLabel_InvokeTimeout("InvokeTimeout(ms)"),
	JTextField_InvokeTimeout("5000"),
	JLabel_ConnectionType("ConnectionType"),
	JRadioButton_SoapConnection("SoapConnection"),
	JRadioButton_HttpConnection("HttpConnection"),
	JButton_Invoke("Invoke"),
	JButton_InvokeMore("More..."),
	// central panel
	JLabel_RequestData("RequestData"),
	JTextArea_RequestData("Hello web service server, I'm client."),
	JLabel_ResponseData("ResponseData"),
	JTextArea_ResponseData(0),
	// text
	Text_InvokeWebServiceServerSuccess("invoke successed."),
	Text_InvokeWebServiceServerFailed("invoke failed."),
	Text_InvokeWebServiceServerFailedCode("failed code: "),
	Text_InvokeWebServiceServerFailedReason("failed reason: "),
	Text_ToBeContinue("to be continue..."),
	Text_InvokeCost("invoke cost: "),
	;

	public int code;// 标准编码
	public int icode;// 编码传递时需要转化的编码，整形
	public String message;// 编码对应的默认描述
	public String scode;// 编码传递时需要转化的编码，字符型

	private WSCI18N(int code) {
		this(code, "", "", 0);
	}
	
	private WSCI18N(String message) {
		this(0, message, "", 0);
	}

	private WSCI18N(int code, String message) {
		this(code, message, "", 0);
	}

	private WSCI18N(int code, String message, String scode) {
		this(code, message, scode, 0);
	}

	private WSCI18N(int code, String message, int icode) {
		this(code, message, "", icode);
	}

	private WSCI18N(int code, String message, String scode, int icode) {
		this.code = code;
		this.message = message;
		this.scode = scode;
		this.icode = icode;
	}

	public WSCI18N getByCode(int code) {
		for(WSCI18N one : WSCI18N.values())
			if(one.code == code)
				return one;
		return null;
	}

	public WSCI18N getBySCode(String scode) {
		for(WSCI18N one : WSCI18N.values())
			if(one.scode == scode)
				return one;
		return null;
	}

	public WSCI18N getByICode(int icode) {
		for(WSCI18N one : WSCI18N.values())
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
