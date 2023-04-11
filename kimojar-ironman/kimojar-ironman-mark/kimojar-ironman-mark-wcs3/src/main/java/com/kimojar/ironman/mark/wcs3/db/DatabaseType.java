/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.db
 * FILE DatabaseType.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-03-05
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
package com.kimojar.ironman.mark.wcs3.db;

/**
 * @author KiMoJar
 * @date 2023-03-05
 */
public enum DatabaseType {

	MySQL("MySQL", "mysql", "jdbc:mysql://{address}:{port}/{name}", "com.mysql.jdbc.Driver", "com.mysql.cj.jdbc.Driver", 3306),
	SQLServer("SQL Server", "sqlserver", "jdbc:sqlserver://{address}:{port};databasename={name}", "com.microsoft.sqlserver.jdbc.SQLServerDriver", 1433),
	Oracle("Oracle", "oracle", "jdbc:oracle:thin:@{address}:{port}:{name}", "oracle.jdbc.OracleDriver", "oracle.jdbc.driver.OracleDriver", 1521),
	DB2("DB2", "db2", "jdbc:db2://{address}:{port}/{name}", "com.ibm.db2.jcc.DB2Driver", "COM.ibm.db2.jdbc.app.DB2Driver", 5000),
	H2_Embed("H2", "h2", "jdbc:h2:file:{address};AUTO_RECONNECT=TRUE", "org.h2.Driver", 0),
	H2_Server("H2 Server", "h2", "jdbc:h2:tcp:{address}:{port}/{name}", "org.h2.Driver", 9092),
	;

	public String name;
	public String urlPortion;
	public String url;
	public String driverName;
	public String url2;
	public String driverName2;
	public int port;

	private DatabaseType(String name, String urlPortion, String url, String driverName, int port) {
		this(name, urlPortion, url, driverName, null, null, port);
	}

	private DatabaseType(String name, String urlPortion, String url, String driverName, String driverName2, int port) {
		this(name, urlPortion, url, driverName, url, driverName2, port);
	}

	private DatabaseType(String name, String urlPortion, String url, String driverName, String url2, String driverName2, int port) {
		this.name = name;
		this.urlPortion = urlPortion;
		this.url = url;
		this.driverName = driverName;
		this.url2 = url2;
		this.driverName2 = driverName2;
		this.port = port;
	}

	public boolean isSupportDriver(String driverName) {
		if(driverName == null || driverName.isEmpty())
			return false;
		else if(this.driverName.equals(driverName) || this.driverName2.equals(driverName))
			return true;
		else
			return false;
	}

	public static DatabaseType getByName(String databaseName) {
		for(DatabaseType type : DatabaseType.values())
			if(type.name.toLowerCase().equals(databaseName))
				return type;
		return null;
	}

}
