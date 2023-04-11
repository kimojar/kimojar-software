/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.data
 * FILE ByteArray.java
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
package com.kimojar.util.community.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供byte列表和byte数组的转换,以及元素的增改操作.
 * <li>
 * 原本可以使用{@link java.util.Arrays#asList(Object...)}得到一个byte数组的列表集合,进而通过{@link java.util.ArrayList#addAll(java.util.Collection)}来实现向byte列表中添加byte数组,
 * 但是由于使用Arrays.asList()将byte数组转为列表集合时,每一个byte元素需要在前面添加类型转换,否则编译器会认为得到的是一个int列表,这降低了代码可读性,为了解决该问题诞生了本类.
 * 
 * <pre>
 * 
 * List<Byte> byteList = Arrays.asList((byte) 3, (byte) 10);
 * List<Integer> intList = Arrays.asList(3, 10);
 * </pre>
 * 
 * </li>
 * 
 * @author KiMoJar
 */
public class ByteArray {

	private ArrayList<Byte> list;

	public ByteArray() {
		list = new ArrayList<Byte>();
	}

	public ByteArray(int size) {
		list = new ArrayList<Byte>(size);
	}

	public ByteArray(byte[] bytes) {
		list = new ArrayList<Byte>(bytes.length);
		for(byte element : bytes)
			list.add(element);
	}

	/**
	 * 将两个byte数组合并为一个
	 * 
	 * @param bytes1
	 * @param bytes2
	 * @return
	 */
	public static byte[] unitBytes(byte[] bytes1, byte[] bytes2) {
		byte[] unitByteArray = new byte[bytes1.length + bytes2.length];
		System.arraycopy(bytes1, 0, unitByteArray, 0, bytes1.length);
		System.arraycopy(bytes2, 0, unitByteArray, bytes1.length, bytes2.length);
		return unitByteArray;
	}

	/**
	 * 将N个byte数组合并为一个
	 * 
	 * @param byteArrays
	 * @return
	 */
	public static byte[] unitBytes(byte[]... byteArrays) {
		int unitByteArrayLength = 0;// unit byte array length
		for(int i = 0; i < byteArrays.length; i++)
			unitByteArrayLength += byteArrays[i].length;
		byte[] unitByteArray = new byte[unitByteArrayLength];
		int countLength = 0;
		for(int i = 0; i < byteArrays.length; i++) {
			byte[] byteArray = byteArrays[i];
			System.arraycopy(byteArray, 0, unitByteArray, countLength, byteArray.length);
			countLength += byteArray.length;
		}
		return unitByteArray;
	}

	/**
	 * 向ByteArray的byte列表中添加一个或多个byte元素
	 * 
	 * @param elements int元素,将会被转为byte元素
	 */
	public void add(int... elements) {
		for(int element : elements) {
			list.add((byte) element);
		}
	}

	/**
	 * 向ByteArray的byte列表中添加一个或多个byte元素
	 * 
	 * @param elements byte元素
	 */
	public void add(byte... elements) {
		for(Byte element : elements) {
			list.add(element);
		}
	}

	/**
	 * 获取下标之前的byte数组,不包括下标处的元素.
	 * 
	 * @param index
	 * @return
	 */
	public byte[] toArrayBefore(int index) {
		List<Byte> data = list.subList(0, index);
		byte[] byteArray = new byte[data.size()];
		int i = 0;
		for(byte element : data) {
			byteArray[i] = element;
			i++;
		}
		return byteArray;
	}

	/**
	 * 将ByteArray的byte列表中指定下标的byte元素替换为指定的byte元素
	 * 
	 * @param index ByteArray的byte列表中的指定下标
	 * @param element 替换的int元素,将会被转为byte元素
	 */
	public void replace(int index, int element) {
		list.remove(index);
		list.add(index, (byte) element);
	}

	/**
	 * 将ByteArray的byte列表中指定下标的byte元素替换为指定的byte元素
	 * 
	 * @param index index ByteArray的byte列表中的指定下标
	 * @param element 替换的byte元素
	 */
	public void replace(int index, byte element) {
		list.remove(index);
		list.add(index, element);
	}

	public void clear() {
		list.clear();
	}

	/**
	 * 获取ByteArray中的byte元素数量
	 * 
	 * @return
	 */
	public int size() {
		return list.size();
	}

	/**
	 * 返回ByteArray所表示的byte列表的byte数组形式
	 * 
	 * @return byte列表的byte数组形式
	 */
	public byte[] toArray() {
		byte[] byteArray = new byte[list.size()];
		int i = 0;
		for(byte element : list) {
			byteArray[i] = element;
			i++;
		}
		return byteArray;
	}

	/**
	 * 返回ByteArray所表示的byte列表的字符串形式
	 * 
	 * @return byte列表的字符串形式 1 2 3 4 5 6
	 */
	public String toByteString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(byte element : list)
			stringBuilder.append(element + " ");
		return stringBuilder.toString().trim();
	}

	/**
	 * 返回ByteArray所表示的byte列表的字符串形式
	 * 
	 * @return byte列表的字符串形式 [1][2][3][4][5][6]
	 */
	public String toSocketString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(byte element : list)
			stringBuilder.append("[" + element + "]");
		return stringBuilder.toString();
	}

	/**
	 * 返回ByteArray所表示的byte列表的无符号十进制的字符串形式
	 * 
	 * @return byte列表的无符号十进制的字符串形式
	 */
	public String toUnsignedDecimalString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(Byte element : list)
			stringBuilder.append((element & 0xFF) + " ");
		return stringBuilder.toString();
	}

	/**
	 * 返回ByteArray所表示的byte列表的无符号十六进制的字符串形式
	 * 
	 * @return byte列表的无符号十六进制的字符串形式
	 */
	public String toUnsignedHexString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(Byte element : list)
			stringBuilder.append(Integer.toHexString(element & 0xFF) + " ");
		return stringBuilder.toString();
	}

	/**
	 * 根据参数所表示的byte字符串得到ByteArray实例
	 * 
	 * @return byte字符串形式 1 2 3 4 5 6
	 */
	public static ByteArray fromByteString(String byteString) {
		ByteArray byteArray = new ByteArray();
		for(String s : byteString.trim().split(" "))
			byteArray.add(Byte.parseByte(s));
		return byteArray;
	}
}
