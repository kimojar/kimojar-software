/**
 * ==============================================================================
 * PROJECT kimojar-util-common
 * PACKAGE com.kimojar.util.common.file
 * FILE FileHelper.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2020-07-01
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
package com.kimojar.util.common.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KiMoJar
 * @date 2020-07-01
 */
public class FileHelper {

	private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);

	public static final int KEYWORDTYPE_EL = 1;
	public static final int KEYWORDTYPE_TXT = 2;
	private static int KEYWORDTYPE = KEYWORDTYPE_EL;

	/**
	 * <h1>根据文件类型过滤文件,选择并返回一个文件</h1>
	 * 可定义文件选择对话框名称和文件类型统称(如果使用文件类型过滤的话)<br>
	 * <br>
	 * 
	 * @param dialogName
	 *            对话框名称
	 * @param fileTypeName
	 *            文件类型的统称
	 * @param fileType
	 *            需要过滤的文件类型
	 * @return 返回一个文件,如果未选择则返回null
	 */
	public static File chooserFile(String dialogName, String fileTypeName, String... fileType) {
		FileNameExtensionFilter fileNameFilter = null;
		JFileChooser fileChooser = null;
		try {
			fileChooser = new JFileChooser();
			if(fileType != null && fileType.length != 0) {
				fileNameFilter = new FileNameExtensionFilter(fileTypeName, fileType);
				fileChooser.setFileFilter(fileNameFilter);
			}
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setDialogTitle(dialogName);
			int retValue = fileChooser.showOpenDialog(null);
			if(retValue == JFileChooser.APPROVE_OPTION) {
				return fileChooser.getSelectedFile();
			} else {
				logger.trace("未选择文件!");
				return null;
			}
		} catch(Exception e) {
			e.printStackTrace();
			logger.trace("选择文件异常!e:{}", e.getMessage());
			return null;
		} finally {
			fileChooser = null;
			fileNameFilter = null;
		}
	}

	/**
	 * 根据文件类型过滤文件,选择并返回一个或多个文件
	 * <li>可定义文件选择对话框名称和文件类型统称(如果使用文件类型过滤的话)
	 * <li>如果选择多个文件,会按照文件名成正向排序放入List.需要注意的是java的排序法则和windows排序法则不一样,具体参见{K.Java.Java API.javax.swing.JFileChooser.getSelectedFiles()}</li>
	 * <li>如果对选择文件返回的列表有顺序要求,建议通过该方法取到列表后再做排序</li>
	 * 
	 * @param dialogName
	 *            对话框名称
	 * @param fileTypeName
	 *            文件类型的统称
	 * @param fileType
	 *            需要过滤的文件类型,只需要后缀即可,例如txt,log,iso等(不需要加.)
	 * @return 返回一个或多个文件,如果未选择或异常,则返回一个空的文件列表
	 */
	public static List<File> chooserFiles(String dialogName, String fileTypeName, String... fileType) {
		List<File> filesList = new ArrayList<File>();
		FileNameExtensionFilter fileNameFilter = null;
		JFileChooser fileChooser = null;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			fileChooser = new JFileChooser();
			if(fileType != null && fileType.length != 0) {
				fileNameFilter = new FileNameExtensionFilter(fileTypeName, fileType);
				fileChooser.setFileFilter(fileNameFilter);
			}
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.setDialogTitle(dialogName);
			int retValue = fileChooser.showOpenDialog(null);
			if(retValue == JFileChooser.APPROVE_OPTION) {
				File[] filesArray = fileChooser.getSelectedFiles();
				for(File temp : filesArray) {
					filesList.add(temp);
					logger.info("选择了文件{}", temp.getAbsolutePath());
				}
			} else {
				logger.trace("未选择文件!");
			}
		} catch(Exception e) {
			e.printStackTrace();
			logger.trace("选择文件异常!e:{}", e.getMessage());
		} finally {
			fileChooser = null;
			fileNameFilter = null;
		}
		return filesList;
	}

	/**
	 * 解决chooserFile(String, String, String...)方法无法有效排序的问题
	 * 
	 * @see com.kimojar.util.tool.file.tools.common.FileHelper#chooserFiles(String, String, String...)
	 * @param dialogName
	 * @param fileTypeName
	 * @param fileType
	 * @return
	 */
	public static List<File> chooserFilesAsc(String dialogName, String fileTypeName, String... fileType) {
		return sortFileListByLastModifiedDateAsc(chooserFiles(dialogName, fileTypeName, fileType));
	}

	/**
	 * <h1>选择并返回一个文件夹</h1><br>
	 * 
	 * @param dialogName
	 *            对话框名称
	 * @return 返回一个文件夹文件,如果未选中则返回null
	 */
	public static File chooserFolder(String dialogName) {
		JFileChooser fileChooser = null;
		try {
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());// 这样设置的目的是使用系统给的文件选择UI，但是会和FlatLaf冲突
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setDialogTitle(dialogName);
			int retValue = fileChooser.showOpenDialog(null);
			if(retValue == JFileChooser.APPROVE_OPTION) {
				return fileChooser.getSelectedFile();
			} else {
				logger.trace("未选择文件!");
				return null;
			}
		} catch(Exception e) {
			e.printStackTrace();
			logger.trace("选择文件异常!e:{}", e.getMessage());
			return null;
		} finally {
			fileChooser = null;
		}
	}

	/**
	 * 根据路径path创建文件,将信息写入该文件
	 * <li>输出编码格式默认为UTF-8</li>
	 * 
	 * @param path
	 *            文件创建路径(绝对路径,包含文件名和后缀)
	 * @param headInfos
	 *            需要写入文件的头信息列表
	 * @param logInfos
	 *            需要写入文件的日志信息列表
	 * @param tailInfos
	 *            需要写入文件的尾信息列表
	 */
	public static void writeInfoToTxtFile(String path, List<String> headInfos, List<String> logInfos, List<String> tailInfos) {
		FileOutputStream fileOutputStream = null;// 文件输出流
		OutputStreamWriter outputStreamWriter = null;// 字符输出流,字符流通向字节流的桥梁
		BufferedWriter bufferedWriter = null;// 字节输出流
		if(path == null || path.isEmpty()) {
			logger.trace("信息写入终止,因为文件创建路径不存在:{}", path);
			return;
		}
		File destinationFile = new File(path);
		if(destinationFile.exists())
			destinationFile.delete();
		else
			destinationFile.getParentFile().mkdirs();
		try {
			destinationFile.createNewFile();
			fileOutputStream = new FileOutputStream(destinationFile);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			bufferedWriter = new BufferedWriter(outputStreamWriter);

			if(headInfos != null) {
				for(String headInfo : headInfos) {
					bufferedWriter.write(headInfo);
					bufferedWriter.newLine();
				}
			}

			if(logInfos != null) {
				for(String logInfo : logInfos) {
					bufferedWriter.write(logInfo);
					bufferedWriter.newLine();
				}
			}

			if(tailInfos != null) {
				for(String tailInfo : tailInfos) {
					bufferedWriter.write(tailInfo);
					bufferedWriter.newLine();
				}
			}
		} catch(IOException e) {
			logger.error("信息写入文件异常:{}", e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close();
				outputStreamWriter.close();
				fileOutputStream.close();
			} catch(IOException e) {
				logger.error("IO输出流关闭异常:{}", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置关键词类型.
	 * <li>如果关键词类型是正则表达式类型,则匹配时使用java.util.regex.Pattern来匹配/li>
	 * <li>如果关键词类型是普通文本类型,则匹配时使用java.util.regex.Pattern来匹配/li>
	 * 
	 * @param keywordType
	 */
	public static void setKeywordType(int keywordType) {
		switch(keywordType){
			case KEYWORDTYPE_EL:
				KEYWORDTYPE = KEYWORDTYPE_EL;
				break;
			case KEYWORDTYPE_TXT:
				KEYWORDTYPE = KEYWORDTYPE_TXT;
				break;
			default:
				KEYWORDTYPE = KEYWORDTYPE_EL;
				break;
		}
	}

	public static List<String> filteInfoFromTxtFile(File srcFile, List<Object> permitKeywords, List<Object> rejectKeywords, String charsetName) {
		return filteInfoFromTxtFile(Arrays.asList(srcFile), permitKeywords, rejectKeywords, charsetName);
	}

	/**
	 * 从srcFiles源文件中,根据正向和反向限定的关键词,过滤出需要的信息.
	 * <li>只是以源文件中的一行信息为单位来做匹配,匹配通过则该行有效,且不对该行作任何更改</li>
	 * <li>如果正向限定词包含A,并且反向限定词也包含A,那么信息QAZ将被判定为无效数据,所以尽量避免同一个关键词均包含在正反向限定中</li>
	 * <li>如果过滤异常,则返回一个元素为0的列表</li>
	 * <li>参数是List<Object>实例,其中Object是String或者List<String>实例</li>
	 * <li>如果List<Object>中Object是String实例,那么只要字符串包含该List<Object>中的key值,就认为匹配成功</li>
	 * <li>如果List<Object>中Object是List<String>实例,那么当且仅当字符串包含该List<Object>中List<String>中的所有key值,才认为匹配成功</li>
	 * <li>如果List<Object>中Object是其他实例,那么则不作匹配,该关键词无效</li>
	 * <li>如果关键词列表为空或者null,则不作匹配</li>
	 * 
	 * @param srcFiles
	 *            需要过滤信息的源文件
	 * @param permitKeywords
	 *            匹配信息的正向限定关键词,只要匹配其中之一就认为允许,支持正则表达式
	 * @param rejectKeywords
	 *            匹配信息的反向限定关键词,只要匹配其中之一就认为拒绝,支持正则表达式
	 * @param charsetName
	 *            解码每一行数据的字符集,如果为null则使用UTF-8字符集,如果为空则使用GBK字符集
	 * @return List<String> resultInfos 通过匹配的信息列表(均为原始数据,未进行加工)
	 */
	public static List<String> filteInfoFromTxtFile(List<File> srcFiles, List<Object> permitKeywords, List<Object> rejectKeywords, String charsetName) {
		java.util.Date startTime = new java.util.Date();
		List<String> resultInfos = new ArrayList<String>();
		if(srcFiles == null || srcFiles.isEmpty()) {
			logger.info("filt info from files stoped,because of empty or null file list");
			return resultInfos;
		}
		if(charsetName == null)
			charsetName = "UTF-8";
		else if(charsetName.isEmpty())
			charsetName = "GBK";
		else if(!Charset.isSupported(charsetName)) {
			logger.error("filte info from files failed,because of unsupport charset:{},collected {} infos", charsetName, resultInfos.size());
			return resultInfos;
		}
		for(File srcFile : srcFiles) {
			/*
			 * 文件读取io流 Q: 使用FileReader会导致读取出来的中文字符乱码 R:
			 * 原因在于FileReader读取文件过程中,FileReader继承了InputStreamReader,
			 * 但并没有实现父类中带字符集参数的构造函数,所以FileReader只能按系统默认的字符集来解码,
			 * 在解码过程中导致乱码出现. S: 使用InputStreamReader代替FileReader即可.
			 */
			FileInputStream fileInputStream = null;
			InputStreamReader inputStreamReader = null;
			BufferedReader bufferedReader = null;
			String line = null;
			try {
				fileInputStream = new FileInputStream(srcFile);
				inputStreamReader = new InputStreamReader(fileInputStream, charsetName);
				bufferedReader = new BufferedReader(inputStreamReader);
				while(bufferedReader.ready()) {
					line = bufferedReader.readLine();
					boolean isshow = filteInfo(line, permitKeywords, rejectKeywords);
					if(isshow) {
						resultInfos.add(line);
					}
				}
			} catch(FileNotFoundException e) {
				e.printStackTrace();
				resultInfos.clear();
				return resultInfos;
			} catch(IOException e) {
				e.printStackTrace();
				resultInfos.clear();
				return resultInfos;
			} finally {
				try {
					fileInputStream.close();
					inputStreamReader.close();
					bufferedReader.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		long costedTime = (new java.util.Date()).getTime() - startTime.getTime();
		/*
		 * 子类重写后,在加入,基类只过滤出需要的原始信息,即第一次过滤 String tip_gaincost =
		 * String.
		 * format("filte info from files cost %sms,collected %s infos"
		 * , costedTime, resultInfos.size());
		 * resultInfos.add(tip_gaincost);
		 */
		logger.trace("filte info from files cost {}ms,collected {} infos", costedTime, resultInfos.size());
		return resultInfos;
	}

	/**
	 * 根据正向和反向关键词,从参数信息列表中过滤出符合通配的每一项.
	 * 
	 * @param permitKeywords
	 * @param rejectKeywords
	 */
	public static List<String> filteInfoFromList(List<String> orgInfos, List<Object> permitKeywords, List<Object> rejectKeywords) {
		List<String> resultInfo = new ArrayList<String>();
		if(orgInfos == null || orgInfos.isEmpty())
			return resultInfo;
		for(String line : orgInfos) {
			if(FileHelper.filteInfo(line, permitKeywords, rejectKeywords)) {
				resultInfo.add(line);
			}
		}
		return resultInfo;
	}

	/**
	 * 根据正向和反向限定的关键词,判断参数信息line字符串是否匹配.
	 * <li>如果正向限定词包含A,并且反向限定词也包含A,那么QAZ将被判定为无效数据,所以尽量避免同一个关键词均包含在正反向限定中</li>
	 * <li>参数是List<Object>实例,其中Object是String或者List<String>实例</li>
	 * <li>如果List<Object>中Object是String实例,那么只要字符串包含该List<Object>中的key值,就认为匹配成功</li>
	 * <li>如果List<Object>中Object是List<String>实例,那么当且仅当字符串包含该List<Object>中List<String>中的所有key值,才认为匹配成功</li>
	 * <li>如果List<Object>中Object是其他实例,那么则不作匹配,该关键词无效</li>
	 * <li>如果关键词列表均为null,则不作匹配,且默认为匹配成功</li>
	 * 
	 * @param line
	 *            需要匹配的参数字符串
	 * @param permitKeywords
	 *            匹配信息的正向限定关键词,只要匹配其中之一就认为允许,支持正则表达式
	 * @param rejectKeywords
	 *            匹配信息的反向限定关键词,只要匹配其中之一就认为拒绝,支持正则表达式
	 * @return 匹配成功返回true,失败返回false
	 */
	@SuppressWarnings({ "unchecked" })
	public static boolean filteInfo(String line, List<Object> permitKeywords, List<Object> rejectKeywords) {
		if(line == null)
			return false;
		if(permitKeywords == null && rejectKeywords == null)
			return true;
		boolean isshow = false;
		if(permitKeywords != null && !permitKeywords.isEmpty()) {
			for(Object permitKeyword : permitKeywords) {
				if(permitKeyword instanceof String)// 允许关键词列表中的元素是String,只要line匹配到就允许
				{
					if((KEYWORDTYPE == KEYWORDTYPE_EL && Pattern.compile(permitKeyword.toString()).matcher(line).find())
					|| (KEYWORDTYPE == KEYWORDTYPE_TXT && line.contains(permitKeyword.toString()))) {
						isshow = true;
						break;// 如果关键词匹配,则允许
					}
				} else if(permitKeyword instanceof List)// 允许关键词中的元素是List,要List中的所有关键词均匹配到才允许,只要List中有一个关键词未匹配到就不允许
				{
					boolean permit = false;
					for(String permitWord : (List<String>) permitKeyword) {
						if((KEYWORDTYPE == KEYWORDTYPE_EL && Pattern.compile(permitWord).matcher(line).find())
						|| (KEYWORDTYPE == KEYWORDTYPE_TXT && line.contains(permitWord)))
							permit = true;
						else {
							permit = false;
							break;// 如果list中的有关键词未匹配,则不允许
						}
					}
					if(permit) {
						isshow = true;
						break;// 如果list中的所有关键词都匹配,则允许
					}
				}
			}
		}
		if(rejectKeywords != null && !rejectKeywords.isEmpty()) {
			for(Object rejectKeyword : rejectKeywords) {
				if(rejectKeyword instanceof String)// 拒绝关键词列表中的元素是String,只要line匹配到就拒绝
				{
					if((KEYWORDTYPE == KEYWORDTYPE_EL && Pattern.compile(rejectKeyword.toString()).matcher(line).find())
					|| (KEYWORDTYPE == KEYWORDTYPE_TXT && line.contains(rejectKeyword.toString()))) {
						isshow = false;
						break;// 如果关键词匹配,则拒绝
					}
				} else if(rejectKeyword instanceof List)// 拒绝关键词中的元素是List,要List中的所有关键词均匹配到才拒绝,只要List中有一个关键词未匹配到就不拒绝
				{
					boolean reject = false;
					for(String rejectWord : (List<String>) rejectKeyword) {
						if((KEYWORDTYPE == KEYWORDTYPE_EL && Pattern.compile(rejectWord).matcher(line).find())
						|| (KEYWORDTYPE == KEYWORDTYPE_TXT && line.contains(rejectWord)))
							reject = true;
						else {
							reject = false;// 如果list中的有关键词未匹配,则不拒绝
							break;
						}
					}
					if(reject) {
						isshow = false;
						break;// 如果list中的所有关键词都匹配,则拒绝
					}
				}
			}
		}
		return isshow;
	}

	/**
	 * 选择一个JSON文件,从输入流中读取该文件的字符内容并返回.
	 * <li>返回的字符串是JSON文件中,每一行字符串的叠加,其中的空格会保留</li>
	 * <li>异常也会返回一个空字符串</li>
	 * 
	 * @return String json文件的字符串形式
	 */
	public static String getStringFromJSONFile() {
		return getStringFromFile(FileHelper.chooserFile("选择JSON文件", "json", "json"), null);
	}

	/**
	 * 读取文件中的所有行,将每一行append到StringBuilder
	 * <li>异常也会返回一个空字符串</li>
	 * 
	 * @param file
	 * @param charsetName
	 * @return
	 */
	public static String getStringFromFile(File file, String charsetName) {
		StringBuilder stringBuilder = new StringBuilder();

		if(charsetName == null || charsetName.isEmpty() || !Charset.isSupported(charsetName))
			charsetName = "UTF-8";
		if(file.isFile() && file.exists()) {
			FileInputStream fileInputStream = null;
			InputStreamReader inputStreamReader = null;
			BufferedReader bufferedReader = null;
			try {
				fileInputStream = new FileInputStream(file);
				inputStreamReader = new InputStreamReader(fileInputStream, charsetName);
				bufferedReader = new BufferedReader(inputStreamReader);
				while(bufferedReader.ready()) {
					String line = bufferedReader.readLine();
					stringBuilder.append(line);
				}
				// Pattern pattern =
				// Pattern.compile("\t|\r|\n|\f|\\s*");// 制表符,回车符,换行符,换页符
				// Matcher matcher = pattern.matcher(jsonString);
				// jsonString = matcher.replaceAll("");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
				stringBuilder.delete(0, stringBuilder.length());
			} catch(IOException e) {
				e.printStackTrace();
				stringBuilder.delete(0, stringBuilder.length());
			} finally {
				try {
					fileInputStream.close();
					inputStreamReader.close();
					bufferedReader.close();
				} catch(IOException e) {
					e.printStackTrace();
					stringBuilder.delete(0, stringBuilder.length());
				}
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * 读取文件中的所有行,将每一行添加到List
	 * <li>异常也会返回一个空字符串</li>
	 * 
	 * @param file 文件
	 * @param charsetName 字符集
	 * @return 文件每一行作为元素组成的List
	 */
	public static List<String> getListFromFile(File file, String charsetName) {
		List<String> stringList = new ArrayList<String>();

		if(charsetName == null || charsetName.isEmpty() || !Charset.isSupported(charsetName))
			charsetName = "UTF-8";
		if(file.isFile() && file.exists()) {
			FileInputStream fileInputStream = null;
			InputStreamReader inputStreamReader = null;
			BufferedReader bufferedReader = null;
			try {
				fileInputStream = new FileInputStream(file);
				inputStreamReader = new InputStreamReader(fileInputStream, charsetName);
				bufferedReader = new BufferedReader(inputStreamReader);
				while(bufferedReader.ready())
					stringList.add(bufferedReader.readLine());
			} catch(FileNotFoundException e) {
				e.printStackTrace();
				stringList.clear();
			} catch(IOException e) {
				e.printStackTrace();
				stringList.clear();
			} finally {
				try {
					fileInputStream.close();
					inputStreamReader.close();
					bufferedReader.close();
				} catch(IOException e) {
					e.printStackTrace();
					stringList.clear();
				}
			}
		}
		return stringList;
	}

	/**
	 * 将List<String>中的信息从头写入到文件,一个List元素是一行
	 * 
	 * @param destFile 要写入的文件
	 * @param infoList 要写入的信息
	 * @param charsetName 写入文件的字符集
	 */
	public static void writeListToFile(File destFile, List<String> infoList, String charsetName) {
		writeListToFile(destFile, infoList, charsetName, false);
	}

	/**
	 * 将List<String>中的信息写入到文件,一个List元素是一行
	 * 
	 * @param destFile 要写入的文件
	 * @param infoList 要写入的信息
	 * @param charsetName 写入文件的字符集
	 * @param append true=从文件尾开始写,false=从文件头开始写
	 */
	public static void writeListToFile(File destFile, List<String> infoList, String charsetName, boolean append) {
		if(infoList == null || infoList.isEmpty())
			return;
		if(charsetName == null || charsetName.isEmpty() || !Charset.isSupported(charsetName))
			charsetName = "UTF-8";
		if(destFile.isFile() && destFile.exists()) {
			FileOutputStream fileOutputStream = null;// 文件输出流
			OutputStreamWriter outputStreamWriter = null;// 字符输出流,字符流通向字节流的桥梁
			BufferedWriter bufferedWriter = null;// 字节输出流
			try {
				fileOutputStream = new FileOutputStream(destFile, append);
				outputStreamWriter = new OutputStreamWriter(fileOutputStream, charsetName);
				bufferedWriter = new BufferedWriter(outputStreamWriter);
				for(String info : infoList) {
					bufferedWriter.write(info);
					bufferedWriter.newLine();
				}
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bufferedWriter.close();
					outputStreamWriter.close();
					fileOutputStream.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将参数文件列表,根据文件的最后修改时间,按照升序的方式排序
	 * 
	 * @param fileList 需要排序的文件列表
	 * @return 排序完成的文件列表
	 */
	private static List<File> sortFileListByLastModifiedDateAsc(List<File> fileList) {
		Map<Long, File> filesMap = new HashMap<Long, File>();
		List<File> resultList = new ArrayList<File>();
		for(File logFile : fileList) {
			filesMap.put(logFile.lastModified(), logFile);
		}
		Long[] sortArray = new Long[filesMap.size()];
		filesMap.keySet().toArray(sortArray);
		Arrays.sort(sortArray);
		for(Long key : sortArray) {
			resultList.add(filesMap.get(key));
			logger.info("选择的文件排序{}", filesMap.get(key));
		}
		return resultList;
	}

	// public static void main(String args[])
	// {
	// File file = FileHelper.chooserFolder("!!");
	// System.out.println(file.getAbsolutePath());
	// FileHelper.chooserFiles("@@", "K", "log", "jar");
	// }
}
