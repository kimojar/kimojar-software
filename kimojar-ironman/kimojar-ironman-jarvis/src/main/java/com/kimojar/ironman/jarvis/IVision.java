/**
 * ==============================================================================
 * PROJECT kimojar-jarvis
 * PACKAGE com.kimojar.ironman.jarvis
 * FILE IVision.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-09
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
package com.kimojar.ironman.jarvis;

import java.util.List;

import com.kimojar.util.common.i18n.LanguageTag;

/**
 * Jarvis的交互接口
 * 
 * @author KiMoJar
 * @date 2022-12-09
 */
public interface IVision {

	/**
	 * 获取Jarvis交互接口名称
	 * 
	 * @return
	 */
	default String name() {
		return this.getClass().getName();
	}

	/**
	 * 为Jarvis的交互界面添加交互模块
	 * 
	 * @param mark 要添加的交互模块面板
	 */
	void addMarkScreen(IMark mark);

	/**
	 * 显示Jarvis交互界面
	 */
	void display();

	/**
	 * 获取所有mark
	 * 
	 * @return
	 */
	List<IMark> getMark();

	/**
	 * Jarvis交互语言改变
	 * 
	 * @param tag
	 */
	void languageChanged(LanguageTag tag);
}
