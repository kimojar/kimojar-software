/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.db
 * FILE DriverLoader.java
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载不通版本的数据库驱动，不同版本间隔离
 * 
 * @author KiMoJar
 * @date 2023-03-05
 */
public class DriverLoader {

	public static final ConcurrentHashMap<DatabaseType, Map<String, Driver>> dbDrivers = new ConcurrentHashMap<DatabaseType, Map<String, Driver>>();

	public static void loadDefaultJDBCDriver() {
		dbDrivers.clear();
		File jdbcDriverPath = new File(System.getProperty("user.dir") + File.separator + "jdbc");
		if(jdbcDriverPath.exists() && jdbcDriverPath.isDirectory()) {
			for(File subDir : jdbcDriverPath.listFiles()) {
				if(subDir.isDirectory()) {
					DatabaseType type = DatabaseType.getByName(subDir.getName());
					if(type != null) {
						Map<String, Driver> driverMap = new HashMap<>();
						dbDrivers.put(type, driverMap);
						for(File subFile : subDir.listFiles()) {
							if(subFile.isFile() && subFile.getName().endsWith(".jar")) {
								try {
									URLClassLoader loader = new URLClassLoader(new URL[] { subFile.toURI().toURL() });// use specific classloader to load jar for same name class sperating
									ServiceLoader<Driver> spi = ServiceLoader.load(Driver.class, loader);
									List<Driver> drivers = new ArrayList<>();
									spi.forEach(driver -> {
										if(type.isSupportDriver(driver.getClass().getName()))
											drivers.add(driver);
									});
									if(!drivers.isEmpty())
										driverMap.put(subFile.getName().substring(0, subFile.getName().length() - 4), drivers.get(0));
								} catch(MalformedURLException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

}
