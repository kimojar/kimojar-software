/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.data
 * FILE ByteUtil.java
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

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.Validate;

/**
 * @author KiMoJar
 * @date 2022-07-07
 */
public class ByteUtil {

	/**
	 * 将一个整数拆分为一个字节数组
	 * <li>如果拆分出来的字节数组的需要长度超过对应的整数的最大可拆分长度,则取对应的整数的最大可拆分长度
	 * <li>如果拆分出来的字节数组的需要长度小于对应的整数的最大可拆分长度,则实际拆分出来的字节数组将从最高字节开始忽略
	 * 
	 * @param value 需要拆分的整数,最大为long,也可以是int,short.
	 * @param bytesLength 拆分出来的字节数组的需要长度,应小于等于8,因为整数最大为long,顶多占用8个字节.如果拆分int,则应不大于4;如果拆分short,则应不大于2.
	 * @param byteOrder 拆分出来的字节数组的字节序,取值BIG_ENDIAN或LITTLE_ENDIAN.如果字节序为null,则抛出npe
	 * @return 拆分出来的字节数组.
	 */
	public static byte[] numberToBytes(long value, int bytesLength, ByteOrder byteOrder) {
		Validate.notNull(byteOrder, "byteorder can not be null");
		Validate.inclusiveBetween(0, 8, bytesLength, "bytesLength should between 0 and 8");
		byte[] bytes = new byte[bytesLength];
		for(int i = 0; i < bytesLength; i++) {
			int offset;
			if(byteOrder.equals(ByteOrder.LITTLE_ENDIAN))
				offset = i * 8;
			else
				offset = (bytesLength - i - 1) * 8;// 右移多少个字节,多少位
			bytes[i] = (byte) (value >> offset & 0xFF);
		}
		return bytes;
	}

	/**
	 * 将一个整数拆分为一个字节数组
	 * <li>如果拆分出来的字节数组的需要长度超过对应的整数的最大可拆分长度,则取对应的整数的最大可拆分长度
	 * <li>如果拆分出来的字节数组的需要长度小于对应的整数的最大可拆分长度,则实际拆分出来的字节数组将从最高字节开始忽略
	 * 
	 * @param value 需要拆分的整数,最大为long,也可以是int,short.
	 * @param byteOrder 拆分出来的字节数组的字节序,取值BIG_ENDIAN或LITTLE_ENDIAN.如果字节序为null,则抛出npe
	 * @return 拆分出来的字节数组.
	 */
	public static byte[] longToBytes(long value, ByteOrder byteOrder) {
		Validate.notNull(byteOrder, "byteorder can not be null");
		byte[] bytes = new byte[8];
		for(int i = 0; i < 8; i++) {
			int offset;
			if(byteOrder.equals(ByteOrder.LITTLE_ENDIAN))
				offset = i * 8;
			else
				offset = (8 - i - 1) * 8;// 右移多少个字节,多少位
			bytes[i] = (byte) (value >> offset & 0xFF);
		}
		return bytes;
	}

	/**
	 * 将一个整数拆分为一个字节数组字符串.
	 * <li>字符串形式如:[1] [-3] [76] [28].
	 * <li>拆分规则同{@link com.kimojar.util.tool.common.KiUtils#numberToBytes(long, int, ByteOrder)}.
	 * 
	 * @param value 需要拆分的整数.
	 * @param byteOrder 拆分出来的字节数组的字节序.
	 * @return 拆分出来的字节数组字符串.
	 */
	public static String longToBytesString(long value, ByteOrder byteOrder) {
		Validate.notNull(byteOrder, "byteorder can not be null");
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < 8; i++) {
			int offset;
			if(byteOrder.equals(ByteOrder.LITTLE_ENDIAN))
				offset = i * 8;
			else
				offset = (8 - i - 1) * 8;
			stringBuilder.append("[" + String.valueOf((byte) (value >> offset & 0xFF)) + "]" + " ");
		}
		return stringBuilder.toString().trim();
	}

	/**
	 * 将short(字)拆分为字节数组.
	 * <li>字长度为2byte,与Java中的short类型对应,所以可取义为wordToBytes.
	 * <li>拆分规则同{@link com.kimojar.util.tool.common.KiUtils#numberToBytes(long, int, ByteOrder)}.
	 * 
	 * @param word 需要拆分的short.
	 * @param byteOrder 拆分出来的字节数组的字节序.
	 * @return 拆分出来的字节数组.
	 */
	public static byte[] shortToBytes(short word, ByteOrder byteOrder) {
		Validate.notNull(byteOrder, "byteorder can not be null");
		return numberToBytes(word, 2, byteOrder);
	}

	/**
	 * 将int(双字)拆分为字节数组.
	 * <li>双字长度为4byte,与Java中的int类型对应,所以可取义为dwordToBytes.
	 * <li>拆分规则同{@link com.kimojar.util.tool.common.KiUtils#numberToBytes(long, int, ByteOrder)}.
	 * 
	 * @param dword 需要拆分的int.
	 * @param byteOrder 拆分出来的字节数组的字节序.
	 * @return 拆分出来的字节数组.
	 */
	public static byte[] intToBytes(int dword, ByteOrder byteOrder) {
		Validate.notNull(byteOrder, "byteorder can not be null");
		return numberToBytes(dword, 4, byteOrder);
	}

	/**
	 * 将一个字节数组合并为一个整数
	 * <li>如果需要合并的字节数组的长度，超过对应整数的最大字节数，则无法合并将抛出IllegalArgumentException异常
	 * <li>如果需要合并的字节数组的长度，小于对应整数的最大字节数，则按照字节序来正常合并
	 * 
	 * @param bytes 需要合并的字节数组，最大长度为8，因为合并整数最大为long，顶多占用8个字节，超出的字节将无法处理，如果合并为int型，则长度应不超过4个字节;如果合并为short，则长度不应超过2个字节
	 * @param byteOrder 需要合并的字节数组的字节序，取值BIG_ENDIAN或LITTLE_ENDIAN
	 * @return 合并得到的整数
	 */
	public static long bytesToLong(byte[] bytes, ByteOrder byteOrder) {
		Validate.notNull(bytes, "bytes can not be null");
		Validate.notNull(byteOrder, "byteorder can not be null");
		Validate.inclusiveBetween(0, 8, bytes.length, "the length of bytes cannot be more than 8");
		long result = 0;
		if(byteOrder.equals(ByteOrder.LITTLE_ENDIAN)) {
			for(int i = bytes.length - 1; i >= 0; i--) {
				result <<= 8;// result = result << 8;
				result |= bytes[i] & 0xFF;// result = result | (bytes[i] & 0xFF);
			}
		} else {
			for(int i = 0; i < bytes.length; i++) {
				result <<= 8;
				result |= bytes[i] & 0xFF;
			}
		}
		return result;
	}

	/**
	 * 将两个字节合并为一个short(字).
	 * <li>字长度为2byte,与Java中的short类型对应,所以可取义为bytesToWord.
	 * <li>高字节左移8位,然后和低字节进行OR(或,|)操作.
	 * <li>Simatic PLC的字节序列是BIG_ENDIAN,也就是高字节在字节序的前序,低字节在字节序的后序.
	 * 
	 * @param higherByte 高字节
	 * @param lowerByte 低字节
	 * @return 合并得到的short
	 */
	public static short bytesToShort(byte higherByte, byte lowerByte) {
		return (short) (((higherByte & 0xFF) << 8) | (lowerByte & 0xFF));
	}

	/**
	 * 将字节数组合并为一个short(字).
	 * <li>字长度为2byte,与Java中的short类型对应,所以可取义为bytesToWord.
	 * <li>合并规则同{@link com.kimojar.util.tool.common.KiUtils#bytesToLong(byte[], ByteOrder)}.
	 * 
	 * @param bytes 需要合并的字节数组.
	 * @param byteOrder 需要合并的字节数组的字节序.
	 * @return 合并得到的short.
	 */
	public static short bytesToShort(byte[] bytes, ByteOrder byteOrder) {
		Validate.notNull(bytes, "bytes can not be null");
		Validate.notNull(byteOrder, "byteorder can not be null");
		Validate.inclusiveBetween(0, 2, bytes.length, "the length of bytes cannot be more than 2");
		return (short) bytesToLong(bytes, byteOrder);
	}

	/**
	 * 将四个字节合并为一个int(双字).
	 * <li>可取义为bytesToDWord.
	 * <li>高字节左移24位,次高字节左移16位,次低字节左移8位,然后和最低字节进行OR(或,|)操作.
	 * 
	 * @param highestByte 最高字节.
	 * @param higherByte 次高字节.
	 * @param lowerByte 次低字节.
	 * @param lowestByte 最低字节.
	 * @return 合并得到的int.
	 */
	public static int bytesToInt(byte highestByte, byte higherByte, byte lowerByte, byte lowestByte) {
		return ((highestByte & 0xFF) << 24) | ((higherByte & 0xFF) << 16) | ((lowerByte & 0xFF) << 8) | ((lowestByte & 0xFF));
	}

	/**
	 * 将字节数组合并为一个int(双字).
	 * <li>双字长度为4byte,与Java中的int类型对应,所以可取义为bytesToDWord.
	 * <li>合并规则同{@link com.kimojar.util.tool.common.KiUtils#bytesToLong(byte[], ByteOrder)}.
	 * 
	 * @param bytes 需要合并的字节数组.
	 * @param byteOrder 需要合并的字节数组的字节序.
	 * @return 合并得到的int.
	 */
	public static int bytesToInt(byte[] bytes, ByteOrder byteOrder) {
		Validate.notNull(bytes, "bytes can not be null");
		Validate.notNull(byteOrder, "byteorder can not be null");
		Validate.inclusiveBetween(0, 4, bytes.length, "the length of bytes cannot be more than 4");
		return (int) bytesToLong(bytes, byteOrder);
	}

	/**
	 * 将一个整数拆分为一个bit数组.
	 * <li>如果字节序为null,则返回一个空的int数组.
	 * <li>如果拆分出来的bit数组的需要长度超过对应的整数的最大可拆分长度,则取对应的整数的最大可拆分长度
	 * <li>如果拆分出来的bit数组的需要长度小于对应的整数的最大可拆分长度,则实际拆分出来的bit数组将从最高位开始忽略
	 * 
	 * @param value 需要拆分的整数,最大为long,也可以是int,short.
	 * @param bitsLength 拆分出来的bit数组的需要长度,应小于等于64,因为整数最大为long,顶多占用8个字节.如果拆分int,则应不大于32;如果拆分short,则应不大于16.
	 * @param byteOrder 拆分出来的bit数组的字节序,取值BIG_ENDIAN或LITTLE_ENDIAN.
	 * @return 拆分出来的bit数组.为了方便操作每个bit位存于int[]中.
	 */
	public static int[] longToBits(long value, int bitsLength, ByteOrder byteOrder) {
		Validate.notNull(byteOrder, "byteorder can not be null");
		bitsLength = bitsLength > 64 ? 64 : bitsLength;
		int[] bits = new int[bitsLength];
		for(int i = 0; i < bitsLength; i++) {
			if(byteOrder.equals(ByteOrder.LITTLE_ENDIAN))
				bits[i] = (int) (value >> i & 0x01);
			else
				bits[i] = (int) (value >> (bitsLength - 1 - i) & 0x01);
		}
		return bits;
	}

	/**
	 * 将一个整数拆分为一个bit数组字符串.拆分规则同{@link com.kimojar.util.tool.common.KiUtils#LongToBits(long, int, ByteOrder)}.
	 * <li>字符串形式如:10000001,之间没有空格.
	 * 
	 * @param value 需要拆分的整数.
	 * @param bitsLength 拆分出来的bit数组的需要长度.
	 * @param byteOrder 拆分出来的bit数组的字节序.
	 * @return 拆分出来的bit数组字符串.
	 */
	public static String longToBitsString(long value, int bitsLength, ByteOrder byteOrder) {
		Validate.notNull(byteOrder, "byteorder can not be null");
		bitsLength = bitsLength > 64 ? 64 : bitsLength;
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < bitsLength; i++) {
			if(byteOrder.equals(ByteOrder.LITTLE_ENDIAN))
				stringBuilder.append(String.valueOf(value >> i & 0x01));
			else
				stringBuilder.append(String.valueOf(value >> (bitsLength - 1 - i) & 0x01));
		}
		return stringBuilder.toString().trim();
	}

	// Returns the bit at Pos.Bit
	public static boolean getBitAt(byte byt, int bitPosi) {
		int Value = byt & 0x0FF;
		byte[] Mask = {
		(byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08,
		(byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x80
		};
		if(bitPosi < 0)
			bitPosi = 0;
		if(bitPosi > 7)
			bitPosi = 7;
		return (Value & Mask[bitPosi]) != 0;
	}

	// Returns the bit at Pos.Bit
	public static boolean getBitAt(byte[] byt, int bytePosi, int bitPosi) {
		int Value = byt[bytePosi] & 0x0FF;
		byte[] Mask = {
		(byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08,
		(byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x80
		};
		if(bitPosi < 0)
			bitPosi = 0;
		if(bitPosi > 7)
			bitPosi = 7;
		return (Value & Mask[bitPosi]) != 0;
	}

	public static void setBitAt(byte byt, int bitPosi, boolean value) {
		byte[] Mask = {
		(byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08,
		(byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x80
		};
		if(bitPosi < 0)
			bitPosi = 0;
		if(bitPosi > 7)
			bitPosi = 7;
		if(value)
			byt = (byte) (byt | Mask[bitPosi]);
		else
			byt = (byte) (byt & ~Mask[bitPosi]);
	}

	public static void setBitAt(byte[] bytes, int bytePosi, int bitPosi, boolean value) {
		byte[] Mask = {
		(byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08,
		(byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x80
		};
		if(bitPosi < 0)
			bitPosi = 0;
		if(bitPosi > 7)
			bitPosi = 7;
		if(value)
			bytes[bytePosi] = (byte) (bytes[bytePosi] | Mask[bitPosi]);
		else
			bytes[bytePosi] = (byte) (bytes[bytePosi] & ~Mask[bitPosi]);
	}

	/**
	 * Returns a (8xbyts.length) bit unsigned value : fom 0 to 2^(byts.lengthx8)-1
	 * 
	 * @param byts
	 * @return
	 */
	public static int getUnsignValue(byte... byts) {
		int result = 0;
		for(int i = 0; i < byts.length; i++) {
			result <<= 8;
			result |= byts[i] & 0xFF;
		}
		return result;
	}

	/**
	 * Returns a 16 bit unsigned value : from 0 to 65535 (2^16-1)
	 * 
	 * @param bytes
	 * @param pos start position
	 * @return
	 */
	public static int getWordAt(byte[] bytes, int pos) {
		int hi = bytes[pos] & 0x00FF;
		int lo = bytes[pos + 1] & 0x00FF;
		return (hi << 8) + lo;
	}

	/**
	 * Returns a 16 bit unsigned value : from 0 to 65535 (2^16-1)
	 * <li>只会取bytes数组的第一和第二个字节
	 * 
	 * @param bytes
	 * @return
	 */
	public static int getWord(byte[] bytes) {
		int hi = bytes[0] & 0x00FF;
		int lo = bytes[1] & 0x00FF;
		return (hi << 8) + lo;
	}

	/**
	 * Returns a 16 bit signed value : from -32768 to 32767
	 * 
	 * @param bytes
	 * @param pos
	 * @return
	 */
	public static int getShortAt(byte[] bytes, int pos) {
		int hi = bytes[pos];
		int lo = bytes[pos + 1] & 0x00FF;
		return (hi << 8) + lo;
	}

	// Returns a 32 bit unsigned value : from 0 to 4294967295 (2^32-1)
	public static long getDWordAt(byte[] Buffer, int Pos) {
		long Result;
		Result = (long) (Buffer[Pos] & 0x0FF);
		Result <<= 8;
		Result += (long) (Buffer[Pos + 1] & 0x0FF);
		Result <<= 8;
		Result += (long) (Buffer[Pos + 2] & 0x0FF);
		Result <<= 8;
		Result += (long) (Buffer[Pos + 3] & 0x0FF);
		return Result;
	}

	// Returns a 32 bit signed value : from 0 to 4294967295 (2^32-1)
	public static int getDIntAt(byte[] Buffer, int Pos) {
		int Result;
		Result = Buffer[Pos];
		Result <<= 8;
		Result += (Buffer[Pos + 1] & 0x0FF);
		Result <<= 8;
		Result += (Buffer[Pos + 2] & 0x0FF);
		Result <<= 8;
		Result += (Buffer[Pos + 3] & 0x0FF);
		return Result;
	}

	// Returns a 32 bit floating point
	public static float getFloatAt(byte[] Buffer, int Pos) {
		int IntFloat = getDIntAt(Buffer, Pos);
		return Float.intBitsToFloat(IntFloat);
	}

	// Returns an ASCII string
	public static String getStringAt(byte[] Buffer, int Pos, int MaxLen) {
		byte[] StrBuffer = new byte[MaxLen];
		System.arraycopy(Buffer, Pos, StrBuffer, 0, MaxLen);
		String S;
		try {
			S = new String(StrBuffer, "UTF-8"); // the charset is UTF-8
		} catch(UnsupportedEncodingException ex) {
			S = "";
		}
		return S;
	}

	public static String getPrintableStringAt(byte[] Buffer, int Pos, int MaxLen) {
		byte[] StrBuffer = new byte[MaxLen];
		System.arraycopy(Buffer, Pos, StrBuffer, 0, MaxLen);
		for(int c = 0; c < MaxLen; c++) {
			if((StrBuffer[c] < 31) || (StrBuffer[c] > 126))
				StrBuffer[c] = 46; // '.'
		}
		String S;
		try {
			S = new String(StrBuffer, "UTF-8"); // the charset is UTF-8
		} catch(UnsupportedEncodingException ex) {
			S = "";
		}
		return S;
	}

	public static Date getDateAt(byte[] Buffer, int Pos) {
		int Year, Month, Day, Hour, Min, Sec;
		Calendar S7Date = Calendar.getInstance();

		Year = BCDToByte(Buffer[Pos]);
		if(Year < 90)
			Year += 2000;
		else
			Year += 1900;

		Month = BCDToByte(Buffer[Pos + 1]) - 1;
		Day = BCDToByte(Buffer[Pos + 2]);
		Hour = BCDToByte(Buffer[Pos + 3]);
		Min = BCDToByte(Buffer[Pos + 4]);
		Sec = BCDToByte(Buffer[Pos + 5]);

		S7Date.set(Year, Month, Day, Hour, Min, Sec);

		return S7Date.getTime();
	}

	public static void setWordAt(byte[] Buffer, int Pos, int Value) {
		int Word = Value & 0x0FFFF;
		Buffer[Pos] = (byte) (Word >> 8);
		Buffer[Pos + 1] = (byte) (Word & 0x00FF);
	}

	public static void setShortAt(byte[] Buffer, int Pos, int Value) {
		Buffer[Pos] = (byte) (Value >> 8);
		Buffer[Pos + 1] = (byte) (Value & 0x00FF);
	}

	public static void setDWordAt(byte[] Buffer, int Pos, long Value) {
		long DWord = Value & 0x0FFFFFFFF;
		Buffer[Pos + 3] = (byte) (DWord & 0xFF);
		Buffer[Pos + 2] = (byte) ((DWord >> 8) & 0xFF);
		Buffer[Pos + 1] = (byte) ((DWord >> 16) & 0xFF);
		Buffer[Pos] = (byte) ((DWord >> 24) & 0xFF);
	}

	public static void setDIntAt(byte[] Buffer, int Pos, int Value) {
		Buffer[Pos + 3] = (byte) (Value & 0xFF);
		Buffer[Pos + 2] = (byte) ((Value >> 8) & 0xFF);
		Buffer[Pos + 1] = (byte) ((Value >> 16) & 0xFF);
		Buffer[Pos] = (byte) ((Value >> 24) & 0xFF);
	}

	public static void setFloatAt(byte[] Buffer, int Pos, float Value) {
		int DInt = Float.floatToIntBits(Value);
		setDIntAt(Buffer, Pos, DInt);
	}

	public static void setDateAt(byte[] Buffer, int Pos, Date DateTime) {
		int Year, Month, Day, Hour, Min, Sec, Dow;
		Calendar S7Date = Calendar.getInstance();
		S7Date.setTime(DateTime);

		Year = S7Date.get(Calendar.YEAR);
		Month = S7Date.get(Calendar.MONTH) + 1;
		Day = S7Date.get(Calendar.DAY_OF_MONTH);
		Hour = S7Date.get(Calendar.HOUR_OF_DAY);
		Min = S7Date.get(Calendar.MINUTE);
		Sec = S7Date.get(Calendar.SECOND);
		Dow = S7Date.get(Calendar.DAY_OF_WEEK);

		if(Year > 1999)
			Year -= 2000;

		Buffer[Pos] = ByteToBCD(Year);
		Buffer[Pos + 1] = ByteToBCD(Month);
		Buffer[Pos + 2] = ByteToBCD(Day);
		Buffer[Pos + 3] = ByteToBCD(Hour);
		Buffer[Pos + 4] = ByteToBCD(Min);
		Buffer[Pos + 5] = ByteToBCD(Sec);
		Buffer[Pos + 6] = 0;
		Buffer[Pos + 7] = ByteToBCD(Dow);
	}

	public static int BCDToByte(byte B) {
		return ((B >> 4) * 10) + (B & 0x0F);
	}

	public static byte ByteToBCD(int Value) {
		return (byte) (((Value / 10) << 4) | (Value % 10));
	}

	public static String stringLeftPading(String str, int length, String append) {
		int strLen = str.length();
		if(append == null || "".equals(append))
			append = " ";
		StringBuffer sb = new StringBuffer();
		if(strLen < length) {
			length = length - strLen;
			strLen = 0;
			while(strLen < length) {
				sb.append(append);
				strLen = sb.toString().length();
			}
		}
		sb.append(str);
		return sb.toString();
	}

	public static String stringRightPading(String str, int length, String append) {
		int strLen = str.length();
		if(append == null || "".equals(append))
			append = " ";
		StringBuffer sb = new StringBuffer();
		if(strLen < length) {
			length = length - strLen;
			strLen = 0;
			while(strLen < length) {
				sb.append(append);
				strLen = sb.toString().length();
			}
		}
		sb.insert(0, str);
		return sb.toString();
	}

}
