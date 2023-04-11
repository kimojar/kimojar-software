/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wapp
 * PACKAGE com.kimojar.ironman.mark.wapp.test
 * FILE MarkWappTest.java
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
package com.kimojar.ironman.mark.wapp.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.kimojar.ironman.jarvis.IMark;
import com.kimojar.ironman.jarvis.IVision;
import com.kimojar.ironman.jarvis.vision.v1.JarvisVision;
import com.kimojar.ironman.mark.wapp.WAppInfo;
import com.kimojar.ironman.mark.wapp.WAppMark;
import com.kimojar.ironman.mark.wapp.WAppScreen;
import com.kimojar.util.common.comparator.Comparators;

/**
 * @author KiMoJar
 * @date 2022-12-17
 */
public class MarkWappTest {

	public static void test005() {
		List<String> categorys = new ArrayList<>();
		categorys.add("software");
		categorys.add("software\\applicationsoftware");
		categorys.add("software\\applicationsoftware\\text");
		categorys.add("software\\applicationsoftware\\brower");
		categorys.add("software\\applicationsoftware\\ide");
		categorys.add("software\\systemsoftware");
		categorys.sort((c1,c2) -> Comparators.charSequenceCompare(c1, c2));
		categorys.forEach(c -> System.out.println(c));
	}

	public static void test004() {
		ServiceLoader<IVision> screenLoader = ServiceLoader.load(IVision.class);
		for(IVision vision : screenLoader) {
			ServiceLoader<IMark> markLoader = ServiceLoader.load(IMark.class);
			for(IMark mark : markLoader) {
				vision.addMarkScreen(mark);
			}
			SwingUtilities.invokeLater(() -> {
				vision.display();
			});
		}
		try {
			Thread.sleep(10000000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void test003() {
		JarvisVision jv = new JarvisVision();
		jv.addMarkScreen(new WAppMark());
		jv.display();
		try {
			Thread.sleep(10000000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void test002() {
		com.formdev.flatlaf.FlatDarkLaf.setup();
		JFrame frame = new JFrame();
		// 设置窗体
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension framSize = new Dimension(screenSize.width / 3 * 2, screenSize.height / 3 * 2);
		frame.setTitle("WAppTest");
		frame.setSize(framSize);// 设置尺寸,如果在设置窗口位置后再次设置尺寸,窗口位置可能不再居中了
		frame.setPreferredSize(framSize);// 设置窗口合适尺寸,用于最大化最小化窗口时使用
		frame.setResizable(true);// 设置是否可调整大小
		frame.setLocationRelativeTo(null);// 设置荧幕居中
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(new WAppScreen(), BorderLayout.CENTER);
		frame.setVisible(true);
		try {
			Thread.sleep(10000000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void test001() throws StreamWriteException, DatabindException, IOException {
		// com.kimojar.ironman.jarvis.util.JacksonUtil.ignoreNullOutput();

		Map<String, List<WAppInfo>> res = new HashMap<>();

		WAppInfo ksec1 = new WAppInfo();
		ksec1.name = "ksec-1";
		WAppInfo ksec2 = new WAppInfo();
		ksec2.name = "ksec-2";
		res.put("ksec", Arrays.asList(ksec1, ksec2));

		WAppInfo browser1 = new WAppInfo();
		browser1.name = "Chrome";
		WAppInfo browser2 = new WAppInfo();
		browser2.name = "browser-2";
		res.put("browser", Arrays.asList(browser1, browser2));

		WAppInfo.writeWAppInfo(res);
		System.out.println(WAppInfo.loadWAppInfo().keySet());
	}
	
	public static void main(String[] args) {
		test004();
	}
}
