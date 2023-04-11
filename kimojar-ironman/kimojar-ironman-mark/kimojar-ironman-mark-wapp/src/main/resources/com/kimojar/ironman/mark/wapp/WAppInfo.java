/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wapp
 * PACKAGE com.kimojar.ironman.mark.wapp
 * FILE WAppInfo.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-16
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kimojar.ironman.jarvis.util.JacksonUtil;
import com.kimojar.util.common.comparator.Comparators;

/**
 * @author KiMoJar
 * @date 2022-12-16
 */
public class WAppInfo {

	/** 应用id */
	public String appid;
	/** 应用名称 */
	public String name;
	/** 应用描述 */
	public String description;
	/** 应用目录 */
	public String directory;
	/** 应用版本 */
	public String version;
	/** 应用类别 */
	public String category;
	/** 启动程序路径 */
	public String executePath;
	/** 卸载程序路径 */
	public String uninstallPath;
	/** 应用数据路径 */
	public String dataPath;
	/** 配置文件路径 */
	public String configPath;
	/** 官方网址 */
	public String website;
	/** 官方下载网址 */
	public String download;
	/** 官方在线使用手册 */
	public String manualOnline;
	/** 官方离线使用手册 */
	public String manualOffline;
	/** GitHub地址 */
	public String github;
	/** Gitee地址 */
	public String gitee;
	/** 指令 */
	public WCommand command;
	/** 自述文件路径 */
	public String readmePath;
	/** 图标路径 */
	public String iconPath;

	@JsonIgnore
	public static String Field_Appid = "Appid";
	@JsonIgnore
	public static String Field_Name = "Name";
	@JsonIgnore
	public static String Field_Version = "Version";
	@JsonIgnore
	public static String Field_Category = "Category";
	@JsonIgnore
	public static String Field_Directory = "Directory";
	@JsonIgnore
	public static String Field_ExecutePath = "ExecutePath";
	@JsonIgnore
	public static String Field_UninstallPath = "UninstallPath";
	@JsonIgnore
	public static String Field_DataPath = "DataPath";
	@JsonIgnore
	public static String Field_ConfigPath = "ConfigPath";
	@JsonIgnore
	public static String Field_Website = "Website";
	@JsonIgnore
	public static String Field_Download = "Download";
	@JsonIgnore
	public static String Field_ManualOnline = "ManualOnline";
	@JsonIgnore
	public static String Field_ManualOffline = "ManualOffline";
	@JsonIgnore
	public static String Field_Github = "Github";
	@JsonIgnore
	public static String Field_Gitee = "Gitee";
	@JsonIgnore
	public static String Field_Command = "Command";
	@JsonIgnore
	public static String Field_ReadmePath = "ReadmePath";
	@JsonIgnore
	public static String Field_IconPath = "IconPath";

	@JsonIgnore
	private static String charset = "UTF-8";
	@JsonIgnore
	private static Map<String, List<WAppInfo>> apps;

	public static Map<String, List<WAppInfo>> loadWAppInfo(File wappJsonFile) {
		Map<String, List<WAppInfo>> apps = new HashMap<>();
		if(wappJsonFile.exists() && wappJsonFile.isFile()) {
			InputStreamReader reader;
			try {
				reader = new InputStreamReader(new FileInputStream(wappJsonFile), charset);
				apps = JacksonUtil.jsonMapper.readValue(reader, new TypeReference<Map<String, List<WAppInfo>>>() {});
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return apps;
	}

	public static Map<String, List<WAppInfo>> loadWAppInfo() {
		if(apps == null) {
			String jsonPath = System.getProperty("user.dir") + File.separator + "resource" + File.separator + "wapp.json";
			File jsonFile = new File(jsonPath);
			apps = loadWAppInfo(jsonFile);
		}
		return apps;
	}

	public static void setLoadCharset(String charset) {
		if(charset == null || charset.isEmpty())
			return;
		try {
			WAppInfo.charset = Charset.forName(charset).toString();
		} catch(Exception e) {
		}
	}

	public static boolean saveWAppInfo() throws StreamWriteException, DatabindException, IOException {
		return writeWAppInfo(apps);
	}

	public static boolean writeWAppInfo(Map<String, List<WAppInfo>> appInfoGroup) throws StreamWriteException, DatabindException, IOException {
		if(appInfoGroup == null)
			return false;
		String jsonPath = System.getProperty("user.dir") + File.separator + "resource" + File.separator;
		File jsonFile = new File(jsonPath + "wapp.json");
		if(jsonFile.exists() && jsonFile.isFile()) {
			File jsonBakFile = new File(jsonPath + "wapp.bak.json");
			if(jsonBakFile.exists() && jsonBakFile.isFile())
				jsonBakFile.delete();
			jsonFile.renameTo(new File(jsonPath + "wapp.bak.json"));
		}
		// sort list
		appInfoGroup.values().forEach(apps -> {
			apps.sort((app1, app2) -> Comparators.charSequenceCompare(app1.name, app2.name));
			apps.forEach(app -> {
				if(app.command != null)
					if(app.command.childCmds != null)
						app.command.childCmds.sort((cmd1, cmd2) -> Comparators.charSequenceCompare(cmd1.cmd, cmd2.cmd));
			});
		});
		// write to file
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(jsonFile), charset);
		JacksonUtil.writeWithOneSpaceBetweenKV();
		JacksonUtil.writeWithTabIntend()
		// .setSerializationInclusion(JsonInclude.Include.NON_NULL)
		.writer()
		.with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
		.writeValue(writer, appInfoGroup);
		return true;
	}

	public static void addWAppInfo(WAppInfo app) {
		loadWAppInfo().get(app.category).add(app);
	}

	public static void deleteWAppInfo(WAppInfo app) {
		loadWAppInfo().get(app.category).remove(app);
	}

	public static void addWAppCategory(String category) {
		if(loadWAppInfo().containsKey(category))
			return;
		else
			loadWAppInfo().put(category, new ArrayList<>());
	}

	public static boolean renameWAppCategory(String oldName, String newName) {
		if(loadWAppInfo().containsKey(oldName) && !loadWAppInfo().containsKey(newName)) {
			loadWAppInfo().put(newName, loadWAppInfo().get(oldName));
			loadWAppInfo().remove(oldName);
			loadWAppInfo().get(newName).forEach(app -> {
				app.category = newName;
			});
			return true;
		}
		return false;
	}

	public static void deleteWAppCategory(String category) {
		if(loadWAppInfo().containsKey(category))
			loadWAppInfo().remove(category);
	}

	public static List<String> loadVariable() {
		List<String> variables = new ArrayList<>();
		apps.values().forEach(appList -> {
			appList.forEach(app -> {
				if(!app.directory.isEmpty())
					variables.add("%" + app.name + Field_Directory + "% = " + app.directory);
				if(!app.executePath.isEmpty())
					variables.add("%" + app.name + Field_ExecutePath + "% = " + app.executePath);
				if(!app.uninstallPath.isEmpty())
					variables.add("%" + app.name + Field_UninstallPath + "% = " + app.uninstallPath);
				if(!app.dataPath.isEmpty())
					variables.add("%" + app.name + Field_DataPath + "% = " + app.dataPath);
				if(!app.configPath.isEmpty())
					variables.add("%" + app.name + Field_ConfigPath + "% = " + app.configPath);
				if(!app.website.isEmpty())
					variables.add("%" + app.name + Field_Website + "% = " + app.website);
				if(!app.download.isEmpty())
					variables.add("%" + app.name + Field_Download + "% = " + app.download);
				if(!app.manualOnline.isEmpty())
					variables.add("%" + app.name + Field_ManualOnline + "% = " + app.manualOnline);
				if(!app.manualOffline.isEmpty())
					variables.add("%" + app.name + Field_ManualOffline + "% = " + app.manualOffline);
				if(!app.github.isEmpty())
					variables.add("%" + app.name + Field_Github + "% = " + app.github);
				if(!app.gitee.isEmpty())
					variables.add("%" + app.name + Field_Gitee + "% = " + app.gitee);
			});
		});
		return variables;
	}

}
