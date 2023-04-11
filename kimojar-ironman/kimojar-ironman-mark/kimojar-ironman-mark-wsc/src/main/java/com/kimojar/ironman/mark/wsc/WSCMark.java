/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wsc
 * PACKAGE com.kimojar.ironman.mark.wsc
 * FILE WSCMark.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-02-03
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
package com.kimojar.ironman.mark.wsc;

import javax.swing.JPanel;

import com.kimojar.ironman.jarvis.IMark;

/**
 * @author KiMoJar
 * @date 2023-02-03
 */
public class WSCMark implements IMark {
	
	@Override
	public String name() {
		return WSCI18N.MarkName.message;
	}

	@Override
	public String description() {
		return WSCI18N.MarkDescription.i18n();
	}

	@Override
	public String version() {
		return "2023-02-03";
	}

	@Override
	public boolean hasScreen() {
		return true;
	}

	@Override
	public JPanel screen() {
		return new WSCScreen();
	}

	@Override
	public boolean hasCommand() {
		return false;
	}

}
