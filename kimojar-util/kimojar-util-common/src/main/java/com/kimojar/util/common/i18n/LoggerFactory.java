/**
 * ==============================================================================
 * PROJECT kimojar-util-common
 * PACKAGE com.kimojar.util.common.i18n
 * FILE LoggerFactory.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-14
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
package com.kimojar.util.common.i18n;

/**
 * Keep the same style with slf4j, in case of change from {@link com.kimojar.util.common.i18n.Logger} to {@link org.slf4j.Logger}
 * 
 * @author KiMoJar
 * @date 2022-12-14
 */
public class LoggerFactory {

	public static Logger getLogger(Class<?> clazz) {
		return new Logger(org.slf4j.LoggerFactory.getLogger(clazz));
	}
}
