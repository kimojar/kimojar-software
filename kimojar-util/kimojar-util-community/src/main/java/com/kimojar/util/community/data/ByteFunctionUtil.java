/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.data
 * FILE ByteFunctionUtil.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-07-07
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
package com.kimojar.util.community.data;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.function.Function;

/**
 * @author KiMoJar
 * @date 2022-07-07
 */
public class ByteFunctionUtil {

	/**
	 * 字符串解码器：将字节码解码为字符串，使用ISO-8859-1字符集
	 */
	public static Function<byte[], String> StringDecoder_ISO88591 = payload -> {
		return new String(payload, Charset.forName("ISO-8859-1"));
	};

	/**
	 * 字符串解码器：将字节码解码为字符串，使用UTF-8字符集
	 */
	public static Function<byte[], String> StringDecoder_UTF8 = payload -> {
		return new String(payload, Charset.forName("UTF-8"));
	};

	/**
	 * 数字解码器：将字节码解码为数字，使用BIG_ENDIAN字节序
	 */
	public static Function<byte[], Long> NumberDecoder_Big = payload -> {
		return ByteUtil.bytesToLong(payload, ByteOrder.BIG_ENDIAN);
	};

	/**
	 * 数字解码器：将字节码解码为数字，使用LITTLE_ENDIAN字节序
	 */
	public static Function<byte[], Long> NumberDecoder_Little = payload -> {
		return ByteUtil.bytesToLong(payload, ByteOrder.LITTLE_ENDIAN);
	};

	/**
	 * 获取一个布尔解码器：将字节码指定bit位解码为布尔值
	 * 
	 * @param bitPosi 指定bit位
	 * @return
	 */
	public static Function<byte[], Boolean> getBooleanDecoder(int bitPosi) {
		return payload -> {
			return ByteUtil.getBitAt(payload, 0, bitPosi);
		};
	}

	/**
	 * 字符串编码器：将字符串编码为字节码，使用ISO-8859-1字符集
	 */
	public static Function<String, byte[]> StringCoder_ISO88591 = data -> {
		return data.getBytes(Charset.forName("ISO-8859-1"));
	};

	/**
	 * 字符串编码器：将字符串编码为字节码，使用UTF-8字符集
	 */
	public static Function<String, byte[]> StringCoder_UTF8 = data -> {
		return data.getBytes(Charset.forName("UTF-8"));
	};

	/**
	 * 数字编码器：将数字编码为字节数组，数组长度为1
	 */
	public static Function<Byte, byte[]> ByteNumberCoder = data -> {
		return new byte[] { data };
	};

	/**
	 * 数字编码器：将数字编码为字节数组，数组长度为2，使用BIG_ENDIAN字节序
	 */
	public static Function<Short, byte[]> ShortNumberCoder_Big = data -> {
		return ByteUtil.shortToBytes(data, ByteOrder.BIG_ENDIAN);
	};

	/**
	 * 数字编码器：将数字编码为字节数组，数组长度为2，使用LITTLE_ENDIAN字节序
	 */
	public static Function<Short, byte[]> ShortNumberCoder_Little = data -> {
		return ByteUtil.shortToBytes(data, ByteOrder.LITTLE_ENDIAN);
	};

	/**
	 * 数字编码器：将数字编码为字节数组，数组长度为4，使用BIG_ENDIAN字节序
	 */
	public static Function<Short, byte[]> IntNumberCoder_Big = data -> {
		return ByteUtil.intToBytes(data, ByteOrder.BIG_ENDIAN);
	};

	/**
	 * 数字编码器：将数字编码为字节数组，数组长度为4，使用LITTLE_ENDIAN字节序
	 */
	public static Function<Short, byte[]> IntNumberCoder_Little = data -> {
		return ByteUtil.intToBytes(data, ByteOrder.LITTLE_ENDIAN);
	};

	/**
	 * 数字编码器：将数字编码为字节数组，数组长度为8，使用BIG_ENDIAN字节序
	 */
	public static Function<Short, byte[]> LongNumberCoder_Big = data -> {
		return ByteUtil.longToBytes(data, ByteOrder.BIG_ENDIAN);
	};

	/**
	 * 数字编码器：将数字编码为字节数组，数组长度为8，使用LITTLE_ENDIAN字节序
	 */
	public static Function<Short, byte[]> LongNumberCoder_Little = data -> {
		return ByteUtil.longToBytes(data, ByteOrder.LITTLE_ENDIAN);
	};

	/**
	 * 获取一个布尔编码器：将指定字节的指定bit位设置后编码为字节
	 * 
	 * @param payload 指定字节
	 * @param bitPosi 指定bit位
	 * @return
	 */
	public static Function<Boolean, byte[]> getBooleanCoder(byte[] payload, int bitPosi) {
		return data -> {
			ByteUtil.setBitAt(payload, 0, bitPosi, data);
			return payload;
		};
	}

}
