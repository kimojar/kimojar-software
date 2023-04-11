/**
 * ==============================================================================
 * PROJECT kimojar-util-common
 * PACKAGE com.kimojar.util.common.i18n
 * FILE Internationalization.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-05-25
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
package com.kimojar.util.common.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * 国际化编码抽象接口.
 * <li>国际化属性文件可存在于类路径下或者外部路径下，如果同时存在，优先以外部路径的为准</li>
 * <li>国际化属性文件的类路径为：实现类的类路径\实现类简单名称_zh-CN.properties，例如：com\kimojar\ironman\jarvis\JarvisI18N_zh-CN.properties</li>
 * <li>国际化属性文件的外部路径为：user.dir\language\zh-CN\实现类全限定名.properties，例如：D:\KSP\Iromman\language\zh-CN\com.kimojar.ironman.jarvis.JarvisI18N.properties</li>
 * <li>国际化属性文件放到：类路径或者外部路径/language/zh-CN,en-US...对应的语言目录下，如果两个路径均存在国际化属性文件，则优先加载外部路径的国际化属性文件</li>
 * <li>通过配置系统属性{@link Internationalization#LANGUAGE_TAG}来指定语言，语言代码参照下文的“常用语言地区代码规范”</li>
 * <li>通过配置系统属性{@link Internationalization#LANGUAGE_CHARSET}来指定国际化属性文件的编码</li>
 * <br>
 * 常用语言地区代码规范：
 * <li>简体中文：zh-CN</li>
 * <li>繁体中文：zh-TW</li>
 * <li>美国英文：en-US</li>
 * <li>韩国朝鲜文：ko-KR</li>
 * <li>日本日文：ja-JP</li>
 * <li>泰国泰文：th-TH</li>
 * <li>阿拉伯联合酋长国阿拉伯文：ar-AE</li>
 * 代码示例：
 * 
 * <pre>
 * 	public enum JarvisI18N implements {@link Internationalization} {

		NoError(1001, "无错误");// 无错误
	
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
		
		&#64;Override
		public String getPropertyKey() {
			return this.name();
		}
		
		&#64;@Override
		public String getDefaultMessage() {
			return message;
		}
	}
 * </pre>
 * 
 * 获取国际化消息的方法：
 * 
 * <pre>
 * JarvisI18N.NoError.i18n();
 * 
 * JarvisI18N.NoError.i18n("无故障");
 * </pre>
 * 
 * @author KiMoJar
 * @date 2022-05-25
 */
public interface Internationalization {

	/**
	 * 类路径下多语言属性文件表<br>
	 * key=多语言枚举类简单类名加语言环境，例如：ParameterCode_zh-CN<br>
	 * value=对应的多语言属性文件对象，例如：ParameterCode.properties文件对象
	 */
	Map<String, Properties> classPathlanguagePropertiesMap = new HashMap<String, Properties>();

	/**
	 * 外部路径下多语言属性文件表<br>
	 * key=多语言枚举类简单类名加语言环境，例如：ParameterCode_zh-CN<br>
	 * value=对应的多语言属性文件对象，例如：ParameterCode.properties文件对象
	 */
	Map<String, Properties> externalPathlanguagePropertiesMap = new HashMap<String, Properties>();

	static final String LANGUAGE_TAG = "kimojar.i18n.languageTag";
	static final String LANGUAGE_CHINESE = "zh-CN";
	static final String LANGUAGE_ENGLISH = "en-US";
	static final String LANGUAGE_CHARSET = "kimojar.i18n.languageCharset";

	/**
	 * 获取枚举对应的国际化信息
	 * 
	 * @return 根据枚举的code到对应国际化属性文件获取信息，如果找不到对应国际化属性文件或者对应国际化属性文件不存在对应code，则返回枚举的默认消息。
	 */
	public default String i18n() {
		Properties classPathLanguageProperties = getClassPathLanguageProperties(this.getClass());
		Properties externalPathLanguageProperties = getExternalPathLanguageProperties(this.getClass());
		String languageMessage = externalPathLanguageProperties.getProperty(getPropertyKey());
		languageMessage = (languageMessage == null) ? classPathLanguageProperties.getProperty(getPropertyKey()) : languageMessage;
		languageMessage = (languageMessage == null) ? getDefaultMessage() : languageMessage;
		return languageMessage;
	}

	/**
	 * 获取枚举对应的国际化信息，可单独指定默认信息。
	 * <br>
	 * 如果配置文件不存在国际化信息则使用参数信息，如果参数信息为空则使用枚举信息。
	 * 
	 * @param defaultMessage
	 * @return
	 */
	public default String i18n(String defaultMessage) {
		Properties classPathLanguageProperties = getClassPathLanguageProperties(this.getClass());
		Properties externalPathLanguageProperties = getExternalPathLanguageProperties(this.getClass());
		String languageMessage = externalPathLanguageProperties.getProperty(getPropertyKey());
		languageMessage = (languageMessage == null) ? classPathLanguageProperties.getProperty(getPropertyKey()) : languageMessage;
		languageMessage = (languageMessage == null) ? defaultMessage : languageMessage;
		languageMessage = (languageMessage == null) ? getDefaultMessage() : languageMessage;
		return languageMessage;
	}

	/**
	 * 获取用于从国际化属性文件中查找消息的key值
	 * 
	 * @return
	 */
	public String getPropertyKey();

	/**
	 * 从国际化属性文件中查找消息失败时，获取默认使用的消息
	 * 
	 * @return
	 */
	public String getDefaultMessage();

	/**
	 * 获取类路径下的国际化属性文件
	 * 
	 * @param clazz 例如：com.kimojar.ironman.jarvis.vision.v1.VisionI18N
	 * @return
	 */
	public static Properties getClassPathLanguageProperties(Class<? extends Internationalization> clazz) {
		String languageTag = System.getProperty(LANGUAGE_TAG);// zh-CN
		String languageCharset = System.getProperty(LANGUAGE_CHARSET);// UTF-8
		if(languageTag == null) {
			languageTag = Locale.getDefault().toLanguageTag();
			setLanguage(LanguageTag.getByString(languageTag));
		}
		if(languageCharset == null) {
			languageCharset = "UTF-8";
			setLanguageCharset(languageCharset);
		}
		String key = clazz.getName() + "_" + languageTag;// com.kimojar.ironman.jarvis.vision.v1.VisionI18N_zh-CN
		if(classPathlanguagePropertiesMap.containsKey(key))
			return classPathlanguagePropertiesMap.get(key);

		Properties languageProperties = new Properties();
		InputStream inputStream = null;
		// com/kimojar/ironman/jarvis/JarvisI18N_zh-CN.properties
		String languagePropertiesFilePath = "/" + key.replaceAll("\\.", "/") + ".properties";
		inputStream = clazz.getResourceAsStream(languagePropertiesFilePath);// inputStream = clazz.getResourceAsStream(clazz.getSimpleName() + "_" + languageTag + ".properties");
		if(inputStream != null) {
			try {
				InputStreamReader reader = new InputStreamReader(inputStream, languageCharset);
				languageProperties.load(reader);
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				if(inputStream != null) {
					try {
						inputStream.close();
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		classPathlanguagePropertiesMap.put(key, languageProperties);
		return languageProperties;
	}

	/**
	 * 获取类路径下的国际化属性文件
	 * 
	 * @param clazz 例如：com.kimojar.ironman.jarvis.vision.v1.VisionI18N
	 * @return
	 */
	public static Properties getExternalPathLanguageProperties(Class<? extends Internationalization> clazz) {
		String languageTag = System.getProperty(LANGUAGE_TAG);// zh-CN
		String languageCharset = System.getProperty(LANGUAGE_CHARSET);// UTF-8
		if(languageTag == null) {
			languageTag = Locale.getDefault().toLanguageTag();
			setLanguage(LanguageTag.getByString(languageTag));
		}
		if(languageCharset == null) {
			languageCharset = "UTF-8";
			setLanguageCharset(languageCharset);
		}
		String key = clazz.getName() + "_" + languageTag;// com.kimojar.ironman.jarvis.vision.v1.VisionI18N_zh-CN
		if(externalPathlanguagePropertiesMap.containsKey(key))
			return externalPathlanguagePropertiesMap.get(key);

		Properties languageProperties = new Properties();
		InputStream inputStream = null;
		try {
			// D:\KSP\com.kimojar.ironman.jarvis\language\zh-CN\com.kimojar.ironman.jarvis.JarvisI18N.properties
			String languagePropertiesFilePath = System.getProperty("user.dir") + File.separator + "language" + File.separator + languageTag + File.separator + clazz.getName() + ".properties";
			inputStream = new FileInputStream(new File(languagePropertiesFilePath));
		} catch(FileNotFoundException e) {
			inputStream = null;
		}
		if(inputStream != null) {
			try {
				InputStreamReader reader = new InputStreamReader(inputStream, languageCharset);
				languageProperties.load(reader);
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				if(inputStream != null) {
					try {
						inputStream.close();
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		externalPathlanguagePropertiesMap.put(key, languageProperties);
		return languageProperties;
	}

	/**
	 * 设置国际化语言
	 * 
	 * @param languageTag
	 */
	public static void setLanguage(LanguageTag languageTag) {
		if(languageTag != null)
			System.setProperty(LANGUAGE_TAG, languageTag.tag);
	}

	/**
	 * 设置国际化语言编码
	 * 
	 * @param languageCharset
	 */
	public static void setLanguageCharset(String languageCharset) {
		if(languageCharset != null && !languageCharset.isEmpty())
			System.setProperty(LANGUAGE_CHARSET, languageCharset);
	}

}
