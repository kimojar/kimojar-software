/**
 * ==============================================================================
 * PROJECT kimojar-ironman-jarvis
 * PACKAGE com.kimojar.ironman.jarvis
 * FILE JarvisCode.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-10
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
package com.kimojar.ironman.jarvis;

import com.kimojar.util.common.i18n.Internationalization;

/**
 * 国际化编码
 * 
 * @author KiMoJar
 * @date 2022-12-10
 */
public enum JarvisI18N implements Internationalization {

	LoadMark(100, "load mark: {}"),
	FindVisionProvider(101, "find Jarvis vision provider: {}"),
	NoVisionProvider(102, "can not find Jarvis vision provider, Jarvis UI will not display"),
	LoadVision(103, "load Jarvis vision: {} at {}"),
	LoadMarkScreen(104, "load mark screen: {} at {}"),
	;

	public int code;// 标准编码
	public String message;// 编码对应的默认描述
	public String scode;// 编码传递时需要转化的编码，字符型
	public int icode;// 编码传递时需要转化的编码，整形

	private JarvisI18N(int code) {
		this(code, "", "", 0);
	}
	
	private JarvisI18N(String message) {
		this(0, message, "", 0);
	}
	
	private JarvisI18N(int code, String message) {
		this(code, message, "", 0);
	}

	private JarvisI18N(int code, String message, String scode) {
		this(code, message, scode, 0);
	}

	private JarvisI18N(int code, String message, int icode) {
		this(code, message, "", icode);
	}

	private JarvisI18N(int code, String message, String scode, int icode) {
		this.code = code;
		this.message = message;
		this.scode = scode;
		this.icode = icode;
	}

	public JarvisI18N getByCode(int code) {
		for(JarvisI18N one : JarvisI18N.values())
			if(one.code == code)
				return one;
		return null;
	}

	public JarvisI18N getBySCode(String scode) {
		for(JarvisI18N one : JarvisI18N.values())
			if(one.scode == scode)
				return one;
		return null;
	}

	public JarvisI18N getByICode(int icode) {
		for(JarvisI18N one : JarvisI18N.values())
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
