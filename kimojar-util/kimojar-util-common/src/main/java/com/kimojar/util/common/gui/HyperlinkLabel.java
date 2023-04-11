/**
 * ==============================================================================
 * PROJECT kimojar-util-common
 * PACKAGE com.kimojar.util.common.gui
 * FILE HyperlinkLabel.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-03-07
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
package com.kimojar.util.common.gui;

import javax.swing.JLabel;

/**
 * 超链接样式的label
 * 
 * @author KiMoJar
 * @date 2023-03-07
 */
public class HyperlinkLabel extends JLabel {

	private static final long serialVersionUID = 740289934736550208L;

	@Override
	public void setText(String text) {
		super.setText("<html><a href=\"#\">" + text + "</a></html>");
	}

}
