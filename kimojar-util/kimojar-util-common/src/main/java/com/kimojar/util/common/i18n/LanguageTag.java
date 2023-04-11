/**
 * ==============================================================================
 * PROJECT kimojar-util-common
 * PACKAGE com.kimojar.util.common.i18n
 * FILE LanguageTag.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-02-13
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
package com.kimojar.util.common.i18n;

/**
 * @author KiMoJar
 * @date 2023-02-13
 */
public enum LanguageTag {

	Chinese("zh-CN"),
	English("en-US"),
	;

	public String tag;

	private LanguageTag(String tag) {
		this.tag = tag;
	}
	
	public static LanguageTag getByString(String tag) {
		for(LanguageTag ltag : LanguageTag.values())
			if(ltag.tag.equals(tag))
				return ltag;
		return null;
	}
}
