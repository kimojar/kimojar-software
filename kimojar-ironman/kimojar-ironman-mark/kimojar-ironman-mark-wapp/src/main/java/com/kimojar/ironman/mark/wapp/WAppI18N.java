/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wapp
 * PACKAGE com.kimojar.ironman.mark.wapp
 * FILE WAppI18N.java
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

import com.kimojar.util.common.i18n.Internationalization;

/**
 * @author KiMoJar
 * @date 2022-12-16
 */
public enum WAppI18N implements Internationalization {

	MarkName("WinApp"),
	MarkDescription("Windows application assistor that can help to manage cmd quick instructions & application infomation."),
	JPanel_Screen(""),
	// layoutNorthPanel
	JButton_SaveAll("Save"),
	JButton_Delete("Delete"),
	JButton_GenerateCommand("GenCMD"),
	JButton_Import("Import"),
	JButton_Export("Export"),
	// layoutAppListPane
	JScrollPane_AppTree("AppTreeScrollPane"),
	JTree_AppTree("AppTree"),
	// layoutBaseInfomationPanel
	JScrollPane_AppDetial("AppDetialScrollPane"),
	TitledBorder_BaseInformation("BaseInformation"),
	JLabel_Icon(""),
	JLabel_Name("Name"),
	JTextField_Name(""),
	JLabel_Version("Version"),
	JTextField_Version(""),
	JLabel_Category("Category"),
	JComboBox_Category(""),
	JLabel_Directory("Directory"),
	JTextField_Directory(""),
	JLabel_ExecutePath("ExecutePath"),
	JTextField_ExecutePath(""),
	JLabel_UninstallPath("UninstallPath"),
	JTextField_UninstallPath(""),
	JLabel_DataPath("DataPath"),
	JTextField_DataPath(""),
	JLabel_ConfigPath("ConfigPath"),
	JTextField_ConfigPath(""),
	JLabel_Website("Website"),
	JTextField_Website(""),
	JLabel_Download("Download"),
	JTextField_Download(""),
	JLabel_ManualOnline("ManualOnline"),
	JTextField_ManualOnline(""),
	JLabel_ManualOffline("ManualOffline"),
	JTextField_ManualOffline(""),
	JLabel_GitHub("GitHub"),
	JTextField_GitHub(""),
	JLabel_Gitee("Gitee"),
	JTextField_Gitee(""),
	JLabel_Description("Description"),
	JTextArea_Description(""),
	// app tree popup menu
	JMenuItem_AddCategory("Add Category"),
	JMenuItem_RenameCategory("Rename Category"),
	JMenuItem_AddCmd("Add Command"),
	JMenuItem_DeleteCmd("Delete Command"),
	JMenuItem_RenameCmd("Rename Command"),
	// layoutCommandInformationPanel
	TitledBorder_CommandInformation("CommandInformation"),
	JTree_CmdTree(""),
	JLabel_CmdAction("Cmd Action"),
	JButton_CmdAction("Execute"),
	Text_OnlyOneLineCmdCanExecuteHere("only one line command can be execute here"),
	JTextArea_CmdAction(""),
	JLabel_CmdVariable("Avaiable Var"),
	JList_CmdVariable(""),
	// listener
	Text_DirectoryNotExist("%s not exist"),
	Text_InvalidURL("\"%s\" is invalid"),
	Text_AreYouSureToDelete("Are you sure to delete?"),
	Text_AppNameCannotBeEmpty("app name can not be empty"),
	Text_AppCategoryMustBeSelect("app category must be select"),
	Text_ChooseAppIcon("choose App icon"),
	Text_PleaseInputCategoryName("please input category name:"),
	Text_PleaseInputCategoryNewName("please input category new name:"),
	Text_PleaseInputCommandName("please input command name:"),
	Text_PleaseInputCommandNewName("please input command new name:"),
	Text_ChooseExportDirectory("please choose export directory"),
	Text_ExportDirectoryNotSelectedOrExportDirectoryNotExist("export failed: directory not selected or not exist"),
	Text_ExportSuccess("export success"),
	Text_ExportError("export exception: "),
	Text_ChooseImportFile("please choose import file"),
	Text_ImportFileNotSelectOrNotExist("import file not select or not exist"),
	Text_ImportSuccess("import success"),
	Text_SaveSuccess("save success"),
	Text_PleaseInputJarvisWakeupCmdName("please input Jarvis wakeup command name:"),
	Text_JarvisWakeupCmdNameInvalid("Jarvis wakeup command name was set as 'jvs' by default, because name can not contian character like '\\ / : * ? \\\" < > |' or empty."),
	Text_GenerateSuccess("generate windows command success"),
	Text_GenerateSuccessUsage("usage: win + R -> %s [personal cmd para]"),
	Text_IconSetError("export exception: "),
	Key_IconLabelIconPath("IconLabelIconPath"),
	ReserveKey_Jarvis("Jarvis"),
	Text_CategoryAlreadyExist("category already exist"),
	;

	public int code;// 标准编码
	public String message;// 编码对应的默认描述
	public String scode;// 编码传递时需要转化的编码，字符型
	public int icode;// 编码传递时需要转化的编码，整形

	private WAppI18N(int code) {
		this(code, "", "", 0);
	}

	private WAppI18N(String message) {
		this(0, message, "", 0);
	}
	
	private WAppI18N(int code, String message) {
		this(code, message, "", 0);
	}

	private WAppI18N(int code, String message, String scode) {
		this(code, message, scode, 0);
	}

	private WAppI18N(int code, String message, int icode) {
		this(code, message, "", icode);
	}

	private WAppI18N(int code, String message, String scode, int icode) {
		this.code = code;
		this.message = message;
		this.scode = scode;
		this.icode = icode;
	}

	public WAppI18N getByCode(int code) {
		for(WAppI18N one : WAppI18N.values())
			if(one.code == code)
				return one;
		return null;
	}

	public WAppI18N getBySCode(String scode) {
		for(WAppI18N one : WAppI18N.values())
			if(one.scode == scode)
				return one;
		return null;
	}

	public WAppI18N getByICode(int icode) {
		for(WAppI18N one : WAppI18N.values())
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
