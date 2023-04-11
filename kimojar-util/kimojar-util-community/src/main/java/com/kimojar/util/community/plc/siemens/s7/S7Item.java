/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7
 * FILE S7Item.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-05-05
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 简单理解为PLC的DB块读取参数，目前仅支持以字节为最小粒度来读写
 * 
 * @author KiMoJar
 * @date 2022-05-05
 */
@Data
@AllArgsConstructor
@Builder
public class S7Item {

	/** item唯一标识 */
	private String id;
	/** item名称，可按照一定规则解析 */
	private String name;
	/** item要读取或者写入的数据块 */
	private int dbNumer;
	/** item读取起始字节地址 */
	private int startByte;
	/** item读取或者写入的字节数 */
	private int rwByteCount;
	/** item读取起始设备号，可选项 */
	private int startCode;
	/** item读取的设备的步长，以字节为单位计算 */
	private int step;
	/** item是否启用 */
	private boolean enable;
	/** item属性 */
	private Map<String, String> properties;

	/**
	 * id=name=UUID，enable=ture，others is not set
	 */
	public S7Item() {
		this.id = UUID.randomUUID().toString();
		this.name = id;
		this.enable = true;
	}

	/**
	 * id=name=UUID，enable=ture
	 * 
	 * @param dbNumber DB块
	 * @param startByte 起始字节
	 * @param rwByteCount 读取或写入字节数
	 */
	public S7Item(int dbNumber, int startByte, int rwByteCount) {
		this();
		this.dbNumer = dbNumber;
		this.startByte = startByte;
		this.rwByteCount = rwByteCount;
	}

	public void addProperty(String key, String value) {
		if(properties == null)
			properties = new HashMap<>();
		this.properties.put(key, value);
	}
}
