/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wapp
 * PACKAGE com.kimojar.ironman.mark.wapp
 * FILE CommandGenerator.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-27
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
package com.kimojar.ironman.mark.wapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author KiMoJar
 * @date 2022-12-27
 */
public class CommandGenerator {

	public static final String TAB = String.valueOf((char) 9);
	public static final String LF = String.valueOf((char) 10);// Line Feed
	public static final String CR = String.valueOf((char) 13);// Carryout

	public static final String NEWLINE = System.getProperty("line.separator");
	public static final String LongSeparator = "----------------------------------------------------------------------------------------------------";
	public static final String ShortSeparator = "------------------------------------------------------------";

	/**
	 * 根据本地语言环境导出jarvis指令。zh-CN以GB2312编码导出，其他以UTF-8编码导出
	 * 
	 * @param apps
	 */
	public static void generateCommand(Map<String, List<WAppInfo>> apps) {
		String languageTag = Locale.getDefault().toLanguageTag();
		switch(languageTag){
			case "zh-CN":
				generateCommand(apps, "GB2312");
				break;
			default:
				generateCommand(apps, "UTF-8");
				break;
		}
	}

	/**
	 * 以指定编码字符集导出jarvis指令。
	 * 
	 * @param apps
	 * @param charset
	 */
	public static void generateCommand(Map<String, List<WAppInfo>> apps, String charset) {
		StringBuffer content = new StringBuffer();
		content.append(setHeader());
		content.append(NEWLINE);
		content.append(setCall());
		content.append(NEWLINE);
		content.append(setChoose(apps));
		content.append(NEWLINE);
		content.append(setBegin());
		content.append(NEWLINE);
		content.append(setNotify());
		content.append(NEWLINE);
		content.append(setWelcome(apps));
		content.append(NEWLINE + NEWLINE + NEWLINE + NEWLINE);
		content.append(setParameter(apps));
		content.append(NEWLINE);
		content.append(setAppHandler(apps));
		content.append(NEWLINE);
		content.append(setJarvisHandler());
		content.append(NEWLINE);
		content.append(setCmdHandler());
		content.append(NEWLINE);
		content.append(setDebugHandler());
		content.append(NEWLINE);
		content.append(setAdminHandler());
		content.append(NEWLINE);
		content.append(setClearHandler());
		content.append(NEWLINE);
		content.append(setExitHandler());
		content.append(NEWLINE);
		BufferedWriter bufferedWriter = null;
		try {
			String jvsDir = System.getProperty("user.home");
			String jvsPath = jvsDir + File.separator + "jvs.bat";
			File jvsBat = new File(jvsPath);
			if(jvsBat.exists() && jvsBat.isFile()) {
				File jvsBakFile = new File(jvsDir + File.separator + "jvs.bat.bak");
				if(jvsBakFile.exists() && jvsBakFile.isFile())
					jvsBakFile.delete();
				jvsBat.renameTo(new File(jvsDir + File.separator + "jvs.bat.bak"));
			}
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(jvsBat), charset);
			bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write(content.toString());
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch(Exception e) {

		} finally {
			try {
				if(bufferedWriter != null)
					bufferedWriter.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String setHeader() {
		StringBuilder result = new StringBuilder();
		result.append("@echo off");
		result.append(NEWLINE);
		result.append("title Jarvis code by KiMoJar");
		result.append(NEWLINE);
		// 2023-03-09：根据本地语言环境选择导出的编码字符集
		// result.append("chcp 65001");// 950=繁体中文，65001=UTF-8，936=简体中文默认GBK，437=MS-DOS 美国英语
		// result.append(NEWLINE);
		result.append("mode con cols=80 lines=20");
		result.append(NEWLINE);
		result.append("goto SetParameter");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setCall() {
		StringBuilder result = new StringBuilder();
		result.append(":Call");
		result.append(NEWLINE);
		result.append("set DEBUG=off");
		result.append(NEWLINE);
		result.append("set CMD=%1");
		result.append(NEWLINE);
		result.append("set PARA1=%2");
		result.append(NEWLINE);
		result.append("set PARA2=%3");
		result.append(NEWLINE);
		result.append("set PARA3=%4");
		result.append(NEWLINE);
		result.append("if \"%CMD%\"==\"\" goto Welcome");
		result.append(NEWLINE);
		result.append("echo Hi! Sir");
		result.append(NEWLINE);
		result.append("echo I'm Jarvis");
		result.append(NEWLINE);
		result.append("echo I received your command:%CMD% %PARA1% %PARA2% %PARA3%");
		result.append(NEWLINE);
		result.append("goto Choose");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setChoose(Map<String, List<WAppInfo>> apps) {
		StringBuilder result = new StringBuilder();
		result.append(":Choose");
		result.append(NEWLINE);
		result.append("rem CMD should be 10 character length for a better format");
		result.append(NEWLINE);
		result.append("rem " + LongSeparator + "App Handler");
		result.append(NEWLINE);
		apps.entrySet().forEach(entry -> {
			result.append("rem " + ShortSeparator + entry.getKey());
			result.append(NEWLINE);
			entry.getValue().forEach(app -> {
				result.append("if %CMD%==" + StringUtils.leftPad(app.command.cmd, 10, " ") + TAB + "goto" + TAB + "APP-Handler-" + app.name);
				result.append(NEWLINE);
			});
		});

		result.append("rem " + LongSeparator + "Jarvis Handler");
		result.append(NEWLINE);
		result.append("if %CMD%==" + StringUtils.leftPad("jvs", 10, " ") + TAB + "goto" + TAB + "Jarvis-Handler-Jarvis");
		result.append(NEWLINE);
		result.append("if %CMD%==" + StringUtils.leftPad("cmd", 10, " ") + TAB + "goto" + TAB + "Jarvis-Handler-Cmd");
		result.append(NEWLINE);
		result.append("if %CMD%==" + StringUtils.leftPad("debug", 10, " ") + TAB + "goto" + TAB + "Jarvis-Handler-Debug");
		result.append(NEWLINE);
		result.append("if %CMD%==" + StringUtils.leftPad("admin", 10, " ") + TAB + "goto" + TAB + "Jarvis-Handler-Admin");
		result.append(NEWLINE);
		result.append("if %CMD%==" + StringUtils.leftPad("cls", 10, " ") + TAB + "goto" + TAB + "Jarvis-Handler-Clear");
		result.append(NEWLINE);
		result.append("if %CMD%==" + StringUtils.leftPad("exit", 10, " ") + TAB + "goto" + TAB + "Jarvis-Handler-Exit");
		result.append(NEWLINE);
		result.append("rem " + LongSeparator);
		result.append(NEWLINE);
		result.append("goto Notify");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setBegin() {
		StringBuilder result = new StringBuilder();
		result.append(":Begin");
		result.append(NEWLINE);
		result.append("set CMD=");
		result.append(NEWLINE);
		result.append("set /p CMD=Choose command code you wanna to execute:");
		result.append(NEWLINE);
		result.append("if \"%DEBUG%\"==\"off\" (mode con cols=80 lines=20)");
		result.append(NEWLINE);
		result.append("if \"%DEBUG%\"==\"on\" (mode con cols=80 lines=300)");
		result.append(NEWLINE);
		result.append("if \"%CMD%\"==\"\" (");
		result.append(NEWLINE);
		result.append("echo empty command code");
		result.append(NEWLINE);
		result.append("echo.");
		result.append(NEWLINE);
		result.append("goto Begin");
		result.append(NEWLINE);
		result.append(") ^");
		result.append(NEWLINE);
		result.append("else (");
		result.append(NEWLINE);
		result.append("for /f \"tokens=1,2,3,4,* delims= \" %%a in (\"%CMD%\") do (");
		result.append(NEWLINE);
		result.append("set CMD=%%a");
		result.append(NEWLINE);
		result.append("set PARA1=%%b");
		result.append(NEWLINE);
		result.append("set PARA2=%%c");
		result.append(NEWLINE);
		result.append("set PARA3=%%d");
		result.append(NEWLINE);
		result.append("))");
		result.append(NEWLINE);
		result.append("goto Choose");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setNotify() {
		StringBuilder result = new StringBuilder();
		result.append(":Notify");
		result.append(NEWLINE);
		result.append("echo wrong command code");
		result.append(NEWLINE);
		result.append("echo.");
		result.append(NEWLINE);
		result.append("goto Begin");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setWelcome(Map<String, List<WAppInfo>> apps) {
		StringBuilder result = new StringBuilder();
		result.append(":Welcome");
		result.append(NEWLINE);
		result.append("mode con cols=80 lines=200");
		result.append(NEWLINE);
		result.append("echo Hi");
		result.append(NEWLINE);
		result.append("echo I'm Jarvis");
		result.append(NEWLINE);
		result.append("echo Please give me command");
		result.append(NEWLINE);
		result.append("echo Command Code List:");
		result.append(NEWLINE);

		result.append("echo " + LongSeparator + "App Handler");
		result.append(NEWLINE);
		apps.entrySet().forEach(entry -> {
			result.append("echo " + ShortSeparator + entry.getKey());
			result.append(NEWLINE);
			entry.getValue().forEach(app -> {
				result.append("echo " + StringUtils.leftPad(app.command.cmd, 10, " ") + " APP-Handler-" + app.name);
				result.append(NEWLINE);
			});
		});

		result.append("echo " + LongSeparator + "Jarvis Handler");
		result.append(NEWLINE);
		result.append("echo " + StringUtils.leftPad("jvs", 10, " ") + " Jarvis-Handler-Jarvis");
		result.append(NEWLINE);
		result.append("echo " + StringUtils.leftPad("cmd", 10, " ") + " Jarvis-Handler-Cmd");
		result.append(NEWLINE);
		result.append("echo " + StringUtils.leftPad("debug", 10, " ") + " Jarvis-Handler-Debug");
		result.append(NEWLINE);
		result.append("echo " + StringUtils.leftPad("admin", 10, " ") + " Jarvis-Handler-Admin");
		result.append(NEWLINE);
		result.append("echo " + StringUtils.leftPad("cls", 10, " ") + " Jarvis-Handler-Clear");
		result.append(NEWLINE);
		result.append("echo " + StringUtils.leftPad("exit", 10, " ") + " Jarvis-Handler-Exit");
		result.append(NEWLINE);
		result.append("echo " + LongSeparator);
		result.append(NEWLINE);
		result.append("echo.");
		result.append(NEWLINE);
		result.append("echo.");
		result.append(NEWLINE);
		result.append("goto Begin");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setParameter(Map<String, List<WAppInfo>> apps) {
		StringBuilder result = new StringBuilder();
		result.append(":SetParameter");
		result.append(NEWLINE);
		getParameterMap(apps).entrySet().forEach(entry -> {
			result.append("rem " + ShortSeparator + entry.getKey());
			result.append(NEWLINE);
			entry.getValue().forEach((para, value) -> {
				result.append("set " + para + "=" + disposePathSpace(value));
				result.append(NEWLINE);
			});
		});
		result.append("goto Call");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setAppHandler(Map<String, List<WAppInfo>> apps) {
		StringBuilder result = new StringBuilder();
		apps.entrySet().forEach(entry -> {
			entry.getValue().forEach(app -> {
				result.append(":APP-Handler-" + app.name);
				result.append(NEWLINE);
				result.append(getCommandString(app.command));
				result.append(getChildCommandString(app.command));
				result.append("if \"%CMD%\"==\"" + app.command.cmd + "\" (if \"%DEBUG%\"==\"off\" exit)");
				result.append(NEWLINE);
				result.append("echo.");
				result.append(NEWLINE);
				result.append("goto Begin");
				result.append(NEWLINE);
				result.append(NEWLINE);
			});
		});
		return result.toString();
	}

	private static String setJarvisHandler() {
		StringBuilder result = new StringBuilder();
		result.append(":Jarvis-Handler-Jarvis");
		result.append(NEWLINE);
		result.append("if \"%PARA1%\"==\"\" (");
		result.append(NEWLINE);
		result.append("cls");
		result.append(NEWLINE);
		result.append("goto Welcome");
		result.append(NEWLINE);
		result.append(")");
		result.append(NEWLINE);
		result.append("if \"%PARA1%\"==\"bat\" start /max notepad %JarvisExecutePath%");
		result.append(NEWLINE);
		result.append("if \"%PARA1%\"==\"dir\" start /max explorer %JarvisPath%");
		result.append(NEWLINE);
		result.append("if \"%CMD%\"==\"jvs\" (if \"%DEBUG%\"==\"off\" exit)");
		result.append(NEWLINE);
		result.append("echo.");
		result.append(NEWLINE);
		result.append("goto Begin");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setCmdHandler() {
		StringBuilder result = new StringBuilder();
		result.append(":Jarvis-Handler-Cmd");
		result.append(NEWLINE);
		result.append("if \"%PARA1%\"==\"\" (");
		result.append(NEWLINE);
		result.append("cls");
		result.append(NEWLINE);
		result.append("start");
		result.append(NEWLINE);
		result.append(")");
		result.append(NEWLINE);
		result.append("if \"%CMD%\"==\"cmd\" (if \"%DEBUG%\"==\"off\" exit)");
		result.append(NEWLINE);
		result.append("echo.");
		result.append(NEWLINE);
		result.append("goto Begin");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setDebugHandler() {
		StringBuilder result = new StringBuilder();
		result.append(":Jarvis-Handler-Debug");
		result.append(NEWLINE);
		result.append("if \"%PARA1%\"==\"\" (");
		result.append(NEWLINE);
		result.append("set DEBUG=on");
		result.append(NEWLINE);
		result.append("echo on");
		result.append(NEWLINE);
		result.append(")");
		result.append(NEWLINE);
		result.append("if \"%PARA1%\"==\"on\" (");
		result.append(NEWLINE);
		result.append("set DEBUG=on");
		result.append(NEWLINE);
		result.append("echo on");
		result.append(NEWLINE);
		result.append(")");
		result.append(NEWLINE);
		result.append("if \"%PARA1%\"==\"off\" (");
		result.append(NEWLINE);
		result.append("set DEBUG=off");
		result.append(NEWLINE);
		result.append("echo off");
		result.append(NEWLINE);
		result.append(")");
		result.append(NEWLINE);
		result.append("if \"%DEBUG%\"==\"on\" cls");
		result.append(NEWLINE);
		result.append("echo debug %DEBUG%");
		result.append(NEWLINE);
		result.append("goto Begin");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setAdminHandler() {
		StringBuilder result = new StringBuilder();
		result.append(":Jarvis-Handler-Admin");
		result.append(NEWLINE);
		result.append("mshta vbscript:CreateObject(\"Shell.Application\").ShellExecute(\"cmd.exe\",\"/c %~s0 ::\",\"\",\"runas\",1)(window.close)&&exit");
		result.append(NEWLINE);
		result.append("cd /d \"%~dp0\"");
		result.append(NEWLINE);
		result.append("goto Begin");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setClearHandler() {
		StringBuilder result = new StringBuilder();
		result.append(":Jarvis-Handler-Clear");
		result.append(NEWLINE);
		result.append("cls");
		result.append(NEWLINE);
		result.append("goto Begin");
		result.append(NEWLINE);
		return result.toString();
	}

	private static String setExitHandler() {
		StringBuilder result = new StringBuilder();
		result.append(":Jarvis-Handler-Exit");
		result.append(NEWLINE);
		result.append("exit");
		result.append(NEWLINE);
		return result.toString();
	}

	/**
	 * 从指令对象中解析出顶层指令
	 * 
	 * @param cmd
	 * @return
	 */
	private static String getCommandString(WCommand cmd) {
		StringBuilder result = new StringBuilder();
		if(StringUtils.isNotEmpty(cmd.action)) {
			result.append(cmd.action);
			result.append(NEWLINE);
		}
		return result.toString();
	}

	/**
	 * 从指令对象中解析出子指令
	 * 
	 * @param cmd
	 * @return
	 */
	private static String getChildCommandString(WCommand cmd) {
		StringBuilder result = new StringBuilder();
		if(cmd.childCmds != null) {
			cmd.childCmds.forEach(child -> {
				if(StringUtils.isNotEmpty(child.action)) {
					// 由于jackson序列化时，如果value值中包含了换行符，CRLF会被转换为LF即\n，导致反序列化时，生成的value值由原来的CRLF变成了只有LF。
					// 那么在cmd指令中CRLF和LF就是不同的含义了，进而会导致指令执行达不到预期的效果。
					// 此处的目的就是判断value中是否包含了可能的换行符，如果包含了，则转为CRLF。
					// 不清楚jackson是否还有其他办法保证序列化时value值中的CRLF不会转为LF。网上有另外一个办法就是序列化前，将换行符强制替换为\\r\\n，在反序列化时再将\\r\\n替换为换行符。
					String action = child.action;
					if(!action.contains(NEWLINE)) {
						if(action.contains(LF))
							action = action.replaceAll(LF, NEWLINE);
						else if(action.contains(CR))
							action = action.replaceAll(CR, NEWLINE);
					}
					if(child.cmd.equals("-")) {// 无参数指令
						if(action.contains(NEWLINE)) {
							result.append("if \"%PARA1%\"==\"" + "\" (");
							result.append(NEWLINE);
							result.append(action);
							result.append(NEWLINE);
							result.append(")");
							result.append(NEWLINE);
						} else {
							result.append("if \"%PARA1%\"==\"" + "\" " + action);
							result.append(NEWLINE);
						}
					} else {// 有参数指令
						if(action.contains(NEWLINE)) {
							result.append("if \"%PARA1%\"==\"" + child.cmd + "\" (");
							result.append(NEWLINE);
							result.append(action);
							result.append(NEWLINE);
							result.append(")");
							result.append(NEWLINE);
						} else {
							result.append("if \"%PARA1%\"==\"" + child.cmd + "\" " + action);
							result.append(NEWLINE);
						}
					}
				}
			});
		}
		return result.toString();
	}

	public static List<String> getParameterVariable(Map<String, List<WAppInfo>> apps) {
		List<String> vars = new ArrayList<>();
		vars.add("DEBUG");
		vars.add("CMD");
		vars.add("PARA1");
		vars.add("PARA2");
		vars.add("PARA3");
		getParameterMap(apps).values().forEach(map -> map.keySet().forEach(para -> vars.add(para)));
		return vars;
	}

	public static Map<String, Map<String, String>> getParameterMap(Map<String, List<WAppInfo>> apps) {
		Map<String, Map<String, String>> categoryMap = new LinkedHashMap<>();
		{
			Map<String, String> paraMap = new LinkedHashMap<>();
			paraMap.put("JarvisPath", "%userprofile%");
			paraMap.put("JarvisExecutePath", "%JarvisPath%\\jvs.bat");
			paraMap.put("WinSystem32Path", "C:\\Windows\\System32");
			paraMap.put("WinStartMenuPath", "%USERPROFILE%\\AppData\\Roaming\\Microsoft\\Windows\\\"Start Menu\"\\Programs");
			paraMap.put("WinStartupPath", "%USERPROFILE%\\AppData\\Roaming\\Microsoft\\Windows\\\"Start Menu\"\\Programs\\Startup");
			paraMap.put("WinPicturePath", "%USERPROFILE%\\AppData\\Local\\Packages\\Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy\\LocalState\\Assets");
			categoryMap.put("Jarvis", paraMap);
		}
		{
			apps.entrySet().forEach(categroyApps -> {
				Map<String, String> paraMap = new LinkedHashMap<>();
				categoryMap.put(categroyApps.getKey(), paraMap);
				categroyApps.getValue().forEach(app -> {
					if(StringUtils.isNotEmpty(app.version))
						paraMap.put(app.name + WAppInfo.Field_Version, app.version);
					if(StringUtils.isNotEmpty(app.directory))
						paraMap.put(app.name + WAppInfo.Field_Directory, app.directory);
					if(StringUtils.isNotEmpty(app.executePath))
						paraMap.put(app.name + WAppInfo.Field_ExecutePath, app.executePath);
					if(StringUtils.isNotEmpty(app.uninstallPath))
						paraMap.put(app.name + WAppInfo.Field_UninstallPath, app.uninstallPath);
					if(StringUtils.isNotEmpty(app.dataPath))
						paraMap.put(app.name + WAppInfo.Field_DataPath, app.dataPath);
					if(StringUtils.isNotEmpty(app.configPath))
						paraMap.put(app.name + WAppInfo.Field_ConfigPath, app.configPath);
					if(StringUtils.isNotEmpty(app.website))
						paraMap.put(app.name + WAppInfo.Field_Website, app.website);
					if(StringUtils.isNotEmpty(app.download))
						paraMap.put(app.name + WAppInfo.Field_Download, app.download);
					if(StringUtils.isNotEmpty(app.manualOnline))
						paraMap.put(app.name + WAppInfo.Field_ManualOnline, app.manualOnline);
					if(StringUtils.isNotEmpty(app.manualOffline))
						paraMap.put(app.name + WAppInfo.Field_ManualOffline, app.manualOffline);
					if(StringUtils.isNotEmpty(app.github))
						paraMap.put(app.name + WAppInfo.Field_Github, app.github);
					if(StringUtils.isNotEmpty(app.gitee))
						paraMap.put(app.name + WAppInfo.Field_Gitee, app.gitee);
				});
			});
		}
		return categoryMap;
	}

	private static String disposePathSpace(String path) {
		// if(path.matches("^[A-z]:\\\\(.+?\\\\)*")) {}
		Matcher mattcher = Pattern.compile(":.*\\s.*").matcher(path);
		if(mattcher.find()) {
			// String spacePath = mattcher.group();
			// path = path.replace(spacePath, "\"" + spacePath + "\"");
			String[] parts = path.split(":");
			path = parts[0] + ":\"" + parts[1] + "\"";
		}
		return path;
	}
}
