/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.db
 * FILE DBConnection.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-03-04
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

import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.DatabaseTypeUtils;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcDatabaseConnection;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author KiMoJar
 * @date 2023-03-04
 */
@NoArgsConstructor
@Getter
@Setter
public class DBConnection {

	private String dataSourceName;
	private DatabaseType databaseType;
	private String databaseDriver;
	private String databaseAddress;
	private String databasePort;
	private String databaseName;
	private String databaseUser;
	private String databasePass;
	private ConnectionSource connectionSource;
	private Driver driver;

	/** 数据库连接缓存 */
	public static ConcurrentHashMap<String, DBConnection> dbConnectionCache = new ConcurrentHashMap<>();

	private static List<IDBConnectListener> listeners = new ArrayList<>();

	public static final String WCSDB_SCENE = "Scene";
	public static final String WCSDB_CORE = "Core";

	/**
	 * 连接数据库
	 * 
	 * @return
	 */
	public ConnectResult connect() {
		ConnectResult result = new ConnectResult();
		if(isConnect()) {
			result.success = true;
			result.description = "success";
			return result;
		}
		String url = null;
		switch(databaseType){
			case MySQL:
			case SQLServer:
			case Oracle:
			case DB2:
			case H2_Server:
				url = databaseType.url.replace("{address}", databaseAddress).replace("{port}", databasePort).replace("{name}", databaseName);
				break;
			case H2_Embed:
				url = databaseType.url.replace("{address}", databaseAddress);
				break;
			default:
				break;
		}
		try {
			connectionSource = new AssignDriverConnectionSource(url, databaseUser, databasePass);
			connectionSource.getReadOnlyConnection().isTableExists("ping");
		} catch(SQLException e) {
			result.success = false;
			result.description = e.getMessage();
			return result;
		}
		dbConnectionCache.put(dataSourceName, this);
		result.success = true;
		result.description = "success";
		listeners.forEach(listener -> listener.onDBConnected(this));
		return result;

	}

	/**
	 * 判断数据库是否已连接
	 * 
	 * @return
	 */
	public boolean isConnect() {
		return connectionSource != null && connectionSource.isOpen();
	}

	/**
	 * 断开数据库连接
	 */
	public void disConnect() {
		try {
			if(isConnect())
				connectionSource.close();
			listeners.forEach(listener -> listener.onDBDisconnected(this));
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取Dao
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public <T> Dao<T, ?> getDao(Class<T> clazz) {
		if(isConnect()) {
			try {
				return DaoManager.createDao(connectionSource, clazz);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void addListener(IDBConnectListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}

	public class ConnectResult {

		public boolean success;
		public String description;
	}

	private class AssignDriverConnectionSource extends JdbcConnectionSource {

		public AssignDriverConnectionSource(String url, String username, String password) throws SQLException {
			super(url, username, password, null);
		}

		/**
		 * 重写父类的初始化方法，jdbc驱动不从{@link java.sql.DriverManager 驱动注册中心}查找，因为驱动注册中心的查找规则无法满足多版本驱动的需求
		 */
		@Override
		public void initialize() throws SQLException {
			if(initialized) {
				return;
			}
			if(databaseType == null) {
				databaseType = DatabaseTypeUtils.createDatabaseType(getUrl());
			}
			databaseType.setDriver(driver);
			initialized = true;
		}

		/**
		 * 重写父类的获取连接方法，jdbc驱动不从{@link java.sql.DriverManager 驱动注册中心}查找，因为驱动注册中心的查找规则无法满足多版本驱动的需求
		 */
		@Override
		protected DatabaseConnection makeConnection(Logger logger) throws SQLException {

			Properties properties = new Properties();
			if(databaseUser != null) {
				properties.setProperty("user", databaseUser);
			}
			if(databasePass != null) {
				properties.setProperty("password", databasePass);
			}
			DatabaseConnection connection = new JdbcDatabaseConnection(driver.connect(getUrl(), properties));
			// by default auto-commit is set to true
			connection.setAutoCommit(true);
			logger.debug("opened connection to {} got #{}", getUrl(), connection.hashCode());
			return connection;
		}
	}

}
