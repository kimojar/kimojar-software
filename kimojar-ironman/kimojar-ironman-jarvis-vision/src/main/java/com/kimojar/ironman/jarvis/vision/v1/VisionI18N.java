/**
 * ==============================================================================
 * PROJECT kimojar-ironman-jarvis-vision
 * PACKAGE com.kimojar.ironman.jarvis.vision.v1
 * FILE VisionI18N.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-10
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
package com.kimojar.ironman.jarvis.vision.v1;

import com.kimojar.util.common.i18n.Internationalization;

/**
 * @author KiMoJar
 * @date 2022-12-10
 */
public enum VisionI18N implements Internationalization {

	// main
	JFrame_Main("Jarvis"),
	JMenuBar("MenuBar"),
	JMenu_Help("Help"),
	JMenu_Appearance("Appearance"),
	JTabbedPane_Content("ContentTabbedPane"),
	// about
	JMenuItem_About("About"),
	JLabel_Logo("Jarvis"),
	Txt_AboutDescription("Jarvis is a set of assist tools that like Iron Man's Mark battle suit."),
	Txt_AboutAuthoriation("Copyright (c) 2020-%s KiMoJar Software."),
	Txt_AboutFailedToOpenBrowser("Failed to open %s in browser."),
	Txt_AboutFailedToOpenEmail("Failed to open %s in mail."),
	URI_OfficalWebsite("https://yuque.com/mongoosej"),
	URI_OfficalEmail("mongoosej@foxmail.com"),
	URI_OfficalGitHub("https://github.com/mongoosejar/Jarvis"),
	URI_OfficalBlog("https://www.cnblogs.com/mongoosej/"),
	// font
	JMenu_Font("Font"),
	JMenuItem_LoadFont("Load Font..."),
	JMenuItem_RestoreFont("Restore Font"),
	JMenuItem_IncreaseFontSize("Increase Font Size"),
	JMenuItem_DecreaseFontSize("Decrease Font Size"),
	Txt_FailedToInstallFont("failed to install font: {} {}"),
	ButtonGroup_FontFamily("FontFamilyButtonGroup"),
	ButtonGroup_FontSize("FontSizeButtonGroup"),
	// language
	JMenu_Language("Language"),
	JCheckBoxMenuItem_Chinese("Chinese"),
	JCheckBoxMenuItem_English("English"),
	// theme
	JMenu_Theme("Theme"),
	JMenuItem_LoadTheme("Load Theme..."),
	JMenuItem_AboutCurrentTheme("About Current Theme"),
	Text_FailedToInstallTheme("Failed to install theme: {} {}"),
	JMenu_CoreTheme("Core Themes"),
	JMenu_ExtendTheme("Extend Themes"),

	;

	public int code;// 标准编码
	public String message;// 编码对应的默认描述
	public String scode;// 编码传递时需要转化的编码，字符型
	public int icode;// 编码传递时需要转化的编码，整形

	private VisionI18N(int code, String message) {
		this(code, message, "", 0);
	}

	private VisionI18N(String message) {
		this(0, message, "", 0);
	}

	private VisionI18N(int code, String message, String scode) {
		this(code, message, scode, 0);
	}

	private VisionI18N(int code, String message, int icode) {
		this(code, message, "", icode);
	}

	private VisionI18N(int code, String message, String scode, int icode) {
		this.code = code;
		this.message = message;
		this.scode = scode;
		this.icode = icode;
	}

	public VisionI18N getByCode(int code) {
		for(VisionI18N one : VisionI18N.values())
			if(one.code == code)
				return one;
		return null;
	}

	public VisionI18N getBySCode(String scode) {
		for(VisionI18N one : VisionI18N.values())
			if(one.scode == scode)
				return one;
		return null;
	}

	public VisionI18N getByICode(int icode) {
		for(VisionI18N one : VisionI18N.values())
			if(one.icode == icode)
				return one;
		return null;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String getPropertyKey() {
		return this.name();
	}

	@Override
	public String getDefaultMessage() {
		return message;
	}
}
