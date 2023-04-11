/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7
 * FILE S7Event.java
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
package com.kimojar.util.community.plc.siemens.s7;

import com.kimojar.util.community.plc.siemens.s7.telegram.S7Constant.ErrorCode;

/**
 * @author KiMoJar
 */
public class S7Event {

	private byte[] data;// S7_Read读取到的数据
	private ErrorCode errorcode;// S7通讯得到的错误码
	private int cost;// S7通讯耗时
	private String detial;// S7通讯详细描述

	public S7Event() {
		this.data = new byte[] {};
	}

	public S7Event(double startTime, ErrorCode errorcode) {
		this.data = new byte[] {};
		this.cost = (int) (System.currentTimeMillis() - startTime);
		this.errorcode = errorcode;
	}

	public S7Event(byte[] data, double startTime, ErrorCode errorcode) {
		this.data = data == null ? new byte[] {} : data;
		this.cost = (int) (System.currentTimeMillis() - startTime);
		this.errorcode = errorcode;
	}

	public S7Event(byte[] data, double startTime, ErrorCode errorcode, String detial) {
		this.data = data == null ? new byte[] {} : data;
		this.cost = (int) (System.currentTimeMillis() - startTime);
		this.errorcode = errorcode;
		this.detial = detial;
	}

	public byte[] getData() {
		return data;
	}

	public ErrorCode getErrorcode() {
		return errorcode;
	}

	public int getCost() {
		return cost;
	}

	public String getDetial() {
		return detial;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setErrorcode(ErrorCode errorcode) {
		this.errorcode = errorcode;
	}

	public void setCost(double startTime) {
		this.cost = (int) (System.currentTimeMillis() - startTime);
	}

}
