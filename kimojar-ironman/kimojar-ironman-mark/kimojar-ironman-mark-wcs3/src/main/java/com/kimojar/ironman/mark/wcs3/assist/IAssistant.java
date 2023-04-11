/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.assist
 * FILE IAssistant.java
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
package com.kimojar.ironman.mark.wcs3.assist;

import java.util.List;

import javax.swing.JPanel;

/**
 * @author KiMoJar
 * @date 2023-02-28
 */
public interface IAssistant {

	/**
	 * 获取助手名称
	 * 
	 * @return
	 */
	String name();

	/**
	 * 获取助手的交互界面
	 * 
	 * @return
	 */
	JPanel getPanel();

	/**
	 * 助手是否有自己的助手
	 * 
	 * @return
	 */
	boolean hasSubAssistant();

	/**
	 * 获取下级助手
	 * 
	 * @return
	 */
	List<IAssistant> getSubAssistants();

	/**
	 * 添加下级助手
	 * 
	 * @param sub
	 */
	void addSubAssistant(IAssistant sub);
}
