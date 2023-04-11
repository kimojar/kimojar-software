/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3
 * FILE WCS3I18N.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-02-28
 * ==============================================================================
 * Copyright (C) 2023
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
package com.kimojar.ironman.mark.wcs3;

import com.kimojar.util.common.i18n.Internationalization;

/**
 * @author KiMoJar
 * @date 2023-02-28
 */
public enum WCS3I18N implements Internationalization {

	MarkName("WCS3 Assistant"),
	MarkDescription("WCS3 assistant is a set of assit tool."),
	JPanel_Screen,
	// western panel
	JScrollPane_Assistant,
	JTree_Assistant,
	// central panel
	JPanel_Assistant,
	// root assistant
	Assistant_Root_Name("WCS3"),
	// overview assistant
	Assistant_Overview_Name("Overview"),
	// route assistant
	Assistant_Route_Name("Route Assistant"),
	// synchronize route assistant
	Assistant_SynRoute_Name("Synchronize"),
	Assistant_SynRoute_TitleBorder_SynRouteData("Synchronize Route Data"),
	Assistant_SynRoute_JRadioButton_ReplaceWithSite("Replace scene route data with site. (scene file will be backup first)"),
	Assistant_SynRoute_JRadioButton_MergeWithSite("Merge scene route data with site, if exist same route then replace with site. (scene file will be backup first)"),
	Assistant_SynRoute_JRadioButton_MergeWithScene("Merge scene route data with site, if exist same route then replace with scene. (scene file will be backup first)"),
	Assistant_SynRoute_JRadioButton_ExportAsSql("Export site route data as sql statement to clipboard."),
	Assistant_SynRoute_JButton_ChooseSceneDBFile("Choose Scene DB File"),
	Assistant_SynRoute_JLabel_ChooseSceneDBFile,
	Assistant_SynRoute_JButton_CheckDBConnection("Check Core DB Connection"),
	Assistant_SynRoute_JButton_Synchronize("Synchronize"),
	Text_PleaseConnectCoreDBFirstBeforeSynchronize("Please connect Core database first before synchronize route data."),
	Text_PleaseConnectSceneDBFirstBeforeSynchronize("Please connect Scene database first before synchronize route data."),
	// public use DB Connect panel
	JLabel_DBType("DatabaseType"),
	JComboBox_DBType,
	JLabel_DBDriver("DatabaseDriver"),
	JComboBox_DBDriver,
	JPanel_DBPanel,
	JLabel_DBAddress("DatabaseAddress"),
	JLabel_DBAddress_FileAddress("DatabaseFile"),
	JTextField_DBAddress,
	JLabel_DBPort("DatabasePort"),
	JTextField_DBPort,
	JLabel_DBName("DatabaseName"),
	JTextField_DBName,
	JLabel_DBConnectionName("DataSourceName"),
	JTextField_DBConnectionName,
	JLabel_DBUser("DatabaseUser"),
	JTextField_DBUser,
	JLabel_DBPass("DatabasePass"),
	JTextField_DBPass,
	JButton_DBPing("Ping"),
	JButton_DBConnect("Connect"),
	JButton_DBDisconnect("Disconnect"),
	Text_DBConnectionInfo("Database Connection Infomation"),
	Text_DatasourceNotSet("Datasource name not set"),
	Text_DBTypeNotSelect("Database type not select"),
	Text_DBAddressNotSet("Database address not set"),
	Text_DBPortNotSet("Database port not set"),
	Text_DBNameNotSet("Database name not set"),
	Text_DBUserNotSet("Database user not set"),
	Text_DBPassNotSet("Database pass not set"),
	Text_DBConnectSuccess("Connect %s success!"),
	Text_DBConnectFailed("Connect %s failed, because: %s"),
	;

	public int code;// 标准编码
	public String message;// 编码对应的默认描述
	public String scode;// 编码传递时需要转化的编码，字符型
	public int icode;// 编码传递时需要转化的编码，整形

	private WCS3I18N() {
		this(0, "", "", 0);
	}

	private WCS3I18N(int code) {
		this(code, "", "", 0);
	}

	private WCS3I18N(String message) {
		this(0, message, "", 0);
	}

	private WCS3I18N(int code, String message) {
		this(code, message, "", 0);
	}

	private WCS3I18N(int code, String message, String scode) {
		this(code, message, scode, 0);
	}

	private WCS3I18N(int code, String message, int icode) {
		this(code, message, "", icode);
	}

	private WCS3I18N(int code, String message, String scode, int icode) {
		this.code = code;
		this.message = message;
		this.scode = scode;
		this.icode = icode;
	}

	public WCS3I18N getByCode(int code) {
		for(WCS3I18N one : WCS3I18N.values())
			if(one.code == code)
				return one;
		return null;
	}

	public WCS3I18N getBySCode(String scode) {
		for(WCS3I18N one : WCS3I18N.values())
			if(one.scode == scode)
				return one;
		return null;
	}

	public WCS3I18N getByICode(int icode) {
		for(WCS3I18N one : WCS3I18N.values())
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
