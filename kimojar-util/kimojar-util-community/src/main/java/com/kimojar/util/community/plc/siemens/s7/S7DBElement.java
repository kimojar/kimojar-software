/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7
 * FILE S7DBElement.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-06-24
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

import org.apache.commons.lang3.Validate;

import com.kimojar.util.community.data.AbstractDataElement;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7Constant.ElementType;

/**
 * 比如：堆垛机调度在线状态、线控任务号
 * 
 * @see {@link AbstractDataElement}
 * @author KiMoJar
 * @param <D> data:数据元素所表达的数据类型
 * @date 2022-06-24
 */
public class S7DBElement<D> extends AbstractDataElement<byte[], D> {

	/** 数据元素所属DB块 */
	private int dbNumber;
	/** 数据元素起始字节地址 */
	private int startByte;
	/** 数据元素起始bit位，如果有的话 */
	private int startBit;
	/** 表示数据元素的单元数据类型 */
	private ElementType unit;
	/** 表达数据元素的单元数据类型长度 */
	private int unitLength;

	public S7DBElement(String name, String desc, int dbNumber, int startByte, int startBit, ElementType unit, int unitLength) {
		super(name, desc);
		Validate.exclusiveBetween(0, 65536, dbNumber);
		this.dbNumber = dbNumber;
		Validate.inclusiveBetween(0, 65535, startByte);
		this.startByte = startByte;
		if(unit == ElementType.Bit) {
			Validate.inclusiveBetween(0, 7, startBit);
			this.startBit = startBit;
		} else
			this.startBit = 0;
		Validate.inclusiveBetween(0, 65535, unitLength);
		this.unitLength = unitLength;
	}

	/**
	 * 获取原始数据的字节长度
	 * 
	 * @return
	 */
	public int getPayloadLength() {
		return (unit == ElementType.Bit) ? unit.byteSize() : (unit.byteSize() * this.unitLength);
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DB").append(dbNumber).append(".");
		switch(unit){
			case Bit:
				builder.append("DBX").append(startByte).append(".").append(startBit);
				break;
			case Byte:
				builder.append("DBB").append(startByte);
				break;
			case Char:
				builder.append("DBB").append(startByte);
				break;
			case Word:
				builder.append("DBW").append(startByte);
				break;
			case DWord:
				builder.append("DBD").append(startByte);
				break;
			default:
				break;
		}
		builder.append("=").append(data);
		return builder.toString();
	}

	/**
	 * 获取数据元素所属的DB块
	 * 
	 * @return
	 */
	public int getDBNumber() {
		return dbNumber;
	}

	/**
	 * 获取数据元素的起始字节
	 * 
	 * @return
	 */
	public int getStartByte() {
		return startByte;
	}

	/**
	 * 获取数据元素的起始bit位
	 * 
	 * @return
	 */
	public int getStartBit() {
		return startBit;
	}

	/**
	 * 获取数据元素的计量单位：bit、byte、char、word、dword
	 * 
	 * @return
	 */
	public ElementType getUnit() {
		return unit;
	}

	/**
	 * 获取数据元素的计量单位数量
	 * 
	 * @return
	 */
	public int getUnitLength() {
		return unitLength;
	}
}
