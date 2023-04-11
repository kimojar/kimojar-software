/**
 * ==============================================================================
 * PROJECT kimojar-ironman-jarvis
 * PACKAGE com.kimojar.ironman.jarvis.util
 * FILE JacksonUtil.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-17
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
package com.kimojar.ironman.jarvis.util;

import java.lang.reflect.Field;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author KiMoJar
 * @date 2022-12-17
 */
public class JacksonUtil {

	/** 重用ObjectMapper */
	public static final ObjectMapper jsonMapper = new ObjectMapper();
	private static boolean isTabIndent = false;
	private static boolean isNoSpaceBetweenKV = false;
	private static boolean isOneSpaceBetweenKV = false;
	private static boolean isTowSpaceBetweenKV = false;
	public static final DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

	/**
	 * 序列化时kv之间用":"来分隔
	 * 
	 * @return
	 */
	public static ObjectMapper writeWithNoSpaceBetweenKV() {
		if(isNoSpaceBetweenKV)
			return jsonMapper;
		try {
			Class<DefaultPrettyPrinter> clazz = DefaultPrettyPrinter.class;
			Field separatorWithSpace = clazz.getDeclaredField("_objectFieldValueSeparatorWithSpaces");
			separatorWithSpace.setAccessible(true);
			String value = separatorWithSpace.get(prettyPrinter).toString();
			String separator = value.trim();
			separatorWithSpace.set(prettyPrinter, separator);
		} catch(Exception e) {
			e.printStackTrace();
		}
		isNoSpaceBetweenKV = true;
		isOneSpaceBetweenKV = false;
		isTowSpaceBetweenKV = false;
		return jsonMapper;
	}

	/**
	 * 序列化时kv之间用": "来分隔
	 * 
	 * @return
	 */
	public static ObjectMapper writeWithOneSpaceBetweenKV() {
		if(isOneSpaceBetweenKV)
			return jsonMapper;
		try {
			Class<DefaultPrettyPrinter> clazz = DefaultPrettyPrinter.class;
			Field separatorWithSpace = clazz.getDeclaredField("_objectFieldValueSeparatorWithSpaces");
			separatorWithSpace.setAccessible(true);
			String value = separatorWithSpace.get(prettyPrinter).toString();
			String separator = value.trim();
			value = separator + " ";
			separatorWithSpace.set(prettyPrinter, value);
		} catch(Exception e) {
			e.printStackTrace();
		}
		isNoSpaceBetweenKV = false;
		isOneSpaceBetweenKV = true;
		isTowSpaceBetweenKV = false;
		return jsonMapper;
	}

	/**
	 * 序列化时kv之间用" : "来分隔
	 * 
	 * @return
	 */
	public static ObjectMapper writeWithTowSpaceBetweenKV() {
		if(isTowSpaceBetweenKV)
			return jsonMapper;
		try {
			Class<DefaultPrettyPrinter> clazz = DefaultPrettyPrinter.class;
			Field separatorWithSpace = clazz.getDeclaredField("_objectFieldValueSeparatorWithSpaces");
			separatorWithSpace.setAccessible(true);
			String value = separatorWithSpace.get(prettyPrinter).toString();
			String separator = value.trim();
			value = " " + separator + " ";
			separatorWithSpace.set(prettyPrinter, value);
		} catch(Exception e) {
			e.printStackTrace();
		}
		isNoSpaceBetweenKV = false;
		isOneSpaceBetweenKV = false;
		isTowSpaceBetweenKV = true;
		return jsonMapper;
	}

	/**
	 * 序列化时使用tab作为缩进
	 * 
	 * @return
	 */
	public static ObjectMapper writeWithTabIntend() {
		if(isTabIndent)
			return jsonMapper;
		prettyPrinter.indentArraysWith(new DefaultIndenter("	", DefaultIndenter.SYS_LF));
		prettyPrinter.indentObjectsWith(new DefaultIndenter("	", DefaultIndenter.SYS_LF));
		jsonMapper.setDefaultPrettyPrinter(prettyPrinter);
		jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		isTabIndent = true;
		return jsonMapper;
	}

	/**
	 * 序列化时使用空格作为缩进
	 * 
	 * @return
	 */
	public static ObjectMapper writeWithSpaceIntend() {
		if(!isTabIndent)
			return jsonMapper;
		prettyPrinter.indentArraysWith(new DefaultIndenter("  ", DefaultIndenter.SYS_LF));
		prettyPrinter.indentObjectsWith(new DefaultIndenter("  ", DefaultIndenter.SYS_LF));
		jsonMapper.setDefaultPrettyPrinter(prettyPrinter);
		jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		isTabIndent = false;
		return jsonMapper;
	}

}