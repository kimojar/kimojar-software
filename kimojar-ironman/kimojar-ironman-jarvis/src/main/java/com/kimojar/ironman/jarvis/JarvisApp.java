/**
 * ==============================================================================
 * PROJECT kimojar-jarvis
 * PACKAGE com.kimojar.ironman.jarvis
 * FILE JarvisApp.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-06-19
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
package com.kimojar.ironman.jarvis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;

import com.kimojar.util.classloader.JarLoader;
import com.kimojar.util.common.i18n.Logger;
import com.kimojar.util.common.i18n.LoggerFactory;

/**
 * @author KiMoJar
 * @date 2020-06-19
 */
public class JarvisApp {

	static {
		// 指定logback配置文件
		String current = System.getProperty("user.dir");
		String logbackXmlPath = current + File.separator + "resource" + File.separator + "logback.xml";
		System.setProperty("logback.configurationFile", logbackXmlPath);
		// 加载lib文件夹下的jar
		JarLoader.load();
	}

	/** 国际化Logger */
	private static Logger log = LoggerFactory.getLogger(JarvisApp.class);
	/** Jarvis持久数据 */
	public static Preferences JVSP = Preferences.userRoot().node("/com/kimojar/jarvis");
	private static final String JVSP_VISION_PROVIDERS = "vision.providers";
	/** Jarvis交互界面 */
	private static IVision vision;
	/** Mark战甲 */
	private static List<IMark> marks = new ArrayList<>();

	/**
	 * 加载Mark战甲
	 */
	private static void loadMark() {
		// 扫描mark服务
		String markPath = System.getProperty("user.dir") + File.separator + "mark";
		JarLoader.loadFolder(markPath);
		ServiceLoader<IMark> markLoader = ServiceLoader.load(IMark.class);
		for(IMark mark : markLoader) {
			String jarPath = mark.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
			marks.add(mark);
			log.info("load mark: {}", jarPath);
		}
	}

	/**
	 * 加载Jarvis交互界面
	 */
	private static void loadJarvisVision() {
		// 扫描Jarvis vision服务
		ServiceLoader<IVision> screenLoader = ServiceLoader.load(IVision.class);
		List<IVision> visionList = new ArrayList<>();
		for(IVision vision : screenLoader) {
			String visionPath = vision.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
			visionList.add(vision);
			log.info("find Jarvis vision provider: {}", visionPath);
		}
		// 获取上次设置的首选vision
		String visionProviders = JVSP.get(JVSP_VISION_PROVIDERS, "");
		String visionProvider = null;
		if(!visionProviders.isEmpty())
			for(String vp : visionProviders.split(","))
				if(!vp.isEmpty() && visionProvider == null)
					visionProvider = vp;
		JVSP.put(JVSP_VISION_PROVIDERS, (visionProviders = ""));
		// 加载vision
		if(visionList.isEmpty()) {
			log.error("can not find Jarvis vision provider, Jarvis UI will not display");
			return;
		} else {
			for(IVision vision : visionList) {
				JarvisApp.vision = (JarvisApp.vision == null) ? vision : JarvisApp.vision;
				JarvisApp.vision = (vision.getClass().getName().equals(visionProvider)) ? vision : JarvisApp.vision;
				visionProviders += vision.getClass().getName() + ",";
			}
			JVSP.put(JVSP_VISION_PROVIDERS, visionProviders);// 每次均重设首选vision
			String visionPath = JarvisApp.vision.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
			log.info("load Jarvis vision: {} at {}", JarvisApp.vision.name(), visionPath);
		}
		loadMark();
		// 加载mark
		marks.forEach(mark -> {
			if(mark.hasScreen()) {
				String markPath = mark.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
				JarvisApp.vision.addMarkScreen(mark);
				log.info("load mark screen: {} at {}", mark.name(), markPath);
			}
		});
		// 显示vision
		if(vision != null) {
			SwingUtilities.invokeLater(() -> {
				vision.display();
			});
		}
	}
	
	public static void main(String[] args) throws Exception {
		List<String> argList = new ArrayList<>();
		for(String arg : args)
			if(arg != null && !arg.isEmpty())
				argList.add(arg);
		if(argList.isEmpty()) {
			argList.add("vision");
		}
		if(argList.contains("vision")) {
			loadJarvisVision();
		}
		if(argList.contains("cmd")) {

		}
	}

}