/**
 * ==============================================================================
 * PROJECT kimojar-util-classloader
 * PACKAGE com.kimojar.util.classloader
 * FILE JarLoader.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2017-03-10
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
package com.kimojar.util.classloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * @author KiMoJar
 * @date 2017-03-10
 */
public class JarLoader {

	public static final String SYSTEM_PROPERTY_KIMOJAR_LIB = "kiMoJar.lib";

	/**
	 * 加载命令行参数指定的位置的jar包
	 * <li>如果存在系统属性<code>kiMoJar.lib</code>，那么就加载该属性指定目录下的jar包
	 * <li>否则加载<code>.\lib</code>目录下的jar包
	 */
	public static void load() {
		String libPath = System.getProperty(SYSTEM_PROPERTY_KIMOJAR_LIB);
		if(isValidPath(libPath)) {
			loadFolder(libPath);
			return;
		}
		libPath = System.getProperty("user.dir") + File.separator + "lib";
		if(isValidPath(libPath)) {
			loadFolder(libPath);
			return;
		}
	}

	/**
	 * 判断指定路径是否合法
	 * 
	 * @param libPath
	 * @return
	 */
	private static boolean isValidPath(String libPath) {
		if(libPath == null || libPath.isEmpty())
			return false;
		try {
			File libDir = new File(libPath);
			if(libDir.exists() && libDir.isDirectory())
				return true;
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 加载指定文件夹中的所有jar文件(不加载子目录)，使用线程上下文类加载器
	 * 
	 * @param path
	 */
	public static void loadFolder(String path) {
		File lib = new File(path);
		if(lib.exists() && lib.isDirectory())
			Arrays.stream(lib.listFiles()).forEach(jar -> loadJar(jar, null));
	}

	/**
	 * 加载指定文件夹中的所有jar文件(不加载子目录)
	 * 
	 * @param path
	 */
	public static void loadFolder(String path, URLClassLoader classLoader) {
		File lib = new File(path);
		if(lib.exists() && lib.isDirectory())
			Arrays.stream(lib.listFiles()).forEach(jar -> loadJar(jar, classLoader));
	}

	/**
	 * 加载指定文件夹中的所有jar文件(加载子目录)
	 * 
	 * @param path
	 * @param classLoader
	 */
	public static void loadFolderTree(String path, URLClassLoader classLoader) {
		File lib = new File(path);
		if(lib.exists() && lib.isDirectory())
			try {
				Files.walk(lib.toPath()).forEach(jarPath -> loadJar(jarPath.toFile(), classLoader));
			} catch(IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * 加载指定路径的jar包
	 * 
	 * @param jarFile
	 */
	public static void loadJar(File jarFile, URLClassLoader classLoader) {
		try {
			if(jarFile.exists() && jarFile.isFile() && jarFile.getName().endsWith(".jar")) {
				classLoader = (classLoader == null) ? (URLClassLoader) Thread.currentThread().getContextClassLoader() : classLoader;
				Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
				method.setAccessible(true);
				method.invoke(classLoader, jarFile.toURI().toURL());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
