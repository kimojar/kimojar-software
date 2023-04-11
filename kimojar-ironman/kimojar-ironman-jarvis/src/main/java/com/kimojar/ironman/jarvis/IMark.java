/**
 * ==============================================================================
 * PROJECT kimojar-jarvis
 * PACKAGE com.kimojar.ironman.jarvis
 * FILE IMark.java
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

import javax.swing.JPanel;

import com.kimojar.util.common.i18n.LanguageTag;

/**
 * 钢铁战甲接口
 * 
 * @author KiMoJar
 * @date 2022-12-09
 */
public interface IMark {

	/**
	 * 战甲名称
	 * 
	 * @return
	 */
	String name();

	/**
	 * 战甲描述
	 * 
	 * @return
	 */
	String description();

	/**
	 * 战甲版本
	 * 
	 * @return
	 */
	String version();

	/**
	 * 战甲是否提供交互界面
	 * 
	 * @return
	 */
	boolean hasScreen();

	/**
	 * 获取战甲的交互界面
	 * 
	 * @return 返回一个{@link javax.swing.JPanel JPanel}实例作为战甲的交互界面
	 */
	JPanel screen();

	/**
	 * 战甲是否提供指令
	 * 
	 * @return
	 */
	boolean hasCommand();

	/**
	 * 语言改变
	 * 
	 * @param tag 语言类型
	 */
	default void languageChanged(LanguageTag tag) {

	}

}
