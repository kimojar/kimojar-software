/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7
 * FILE DataBlock.java
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

import java.util.Arrays;

/**
 * @author KiMoJar
 */
public class DataBlock {

	public static final int MaxDataBlockLength = 65536;
	private int dataBlockNumber;
	private byte[] data = new byte[65536];

	public DataBlock(int dataBlockNumber) {
		this.dataBlockNumber = dataBlockNumber;
	}

	public synchronized byte[] getDBB(int byteAddress, int amount) {
		return Arrays.copyOfRange(data, byteAddress, byteAddress + amount);
	}

	public synchronized void setDBB(int byteAddress, byte... data) {
		for(int i = 0; i < data.length; i++)
			this.data[byteAddress + i] = data[i];
	}

	public synchronized boolean getDBX(int byteAddress, int bitAddress) {
		byte byt = this.data[byteAddress];
		String[] bitArray = new String[8];
		for(int i = 0; i < 8; i++)
			bitArray[i] = (byt >> i & 0x01) + "";
		return bitArray[bitAddress].equals("1") ? true : false;
	}

	public synchronized void setDBX(int byteAddress, int bitAddress, boolean value) {
		byte byt = this.data[byteAddress];
		String[] bitArray = new String[8];
		String bitString = "";
		for(int i = 0; i < 8; i++)
			bitArray[i] = (byt >> i & 0x01) + "";
		bitArray[bitAddress] = value ? "1" : "0";
		for(int j = 7; j >= 0; j--)
			bitString += bitArray[j];
		this.data[byteAddress] = (byte) Integer.parseInt(bitString, 2);
	}

	public int getDataBlockNumber() {
		return this.dataBlockNumber;
	}

}
