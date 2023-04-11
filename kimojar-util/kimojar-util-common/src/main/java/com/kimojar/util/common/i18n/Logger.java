/**
 * ==============================================================================
 * PROJECT kimojar-util-common
 * PACKAGE com.kimojar.util.common.i18n
 * FILE Logger.java
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
package com.kimojar.util.common.i18n;

/**
 * 支持国际化的日志记录器：{@link org.slf4j.Logger}的封装。
 * 
 * <pre>
 * Logger log = LoggerFactory.getLogger(class);
 * log.debug(InternationalizationImp.LOAD, para, para);
 * </pre>
 * 
 * @author KiMoJar
 * @date 2022-12-10
 */
public class Logger {

	private org.slf4j.Logger logger;

	protected Logger(org.slf4j.Logger logger) {
		this.logger = logger;
	}

	public void trace(String format, Object... arguments) {
		if(logger.isTraceEnabled())
			logger.trace(format, arguments);
	}

	public void trace(Internationalization i18nCode, Object... arguments) {
		if(logger.isTraceEnabled())
			logger.trace(i18nCode.i18n(), arguments);
	}

	public void debug(String format, Object... arguments) {
		if(logger.isDebugEnabled())
			logger.debug(format, arguments);
	}

	public void debug(Internationalization i18nCode, Object... arguments) {
		if(logger.isDebugEnabled())
			logger.debug(i18nCode.i18n(), arguments);
	}

	public void info(String format, Object... arguments) {
		if(logger.isInfoEnabled())
			logger.info(format, arguments);
	}

	public void info(Internationalization i18nCode, Object... arguments) {
		if(logger.isInfoEnabled())
			logger.info(i18nCode.i18n(), arguments);
	}

	public void warn(String format, Object... arguments) {
		if(logger.isWarnEnabled())
			logger.warn(format, arguments);
	}

	public void warn(Internationalization i18nCode, Object... arguments) {
		if(logger.isWarnEnabled())
			logger.warn(i18nCode.i18n(), arguments);
	}

	public void error(String format, Object... arguments) {
		if(logger.isErrorEnabled())
			logger.error(format, arguments);
	}

	public void error(Internationalization i18nCode, Object... arguments) {
		if(logger.isErrorEnabled())
			logger.error(i18nCode.i18n(), arguments);
	}

}