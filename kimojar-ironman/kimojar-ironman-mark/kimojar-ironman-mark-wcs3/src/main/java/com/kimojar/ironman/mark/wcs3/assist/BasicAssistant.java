/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.assist
 * FILE BasicAssistant.java
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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * 基本抽象助手
 * 
 * @author KiMoJar
 * @date 2023-02-28
 */
public abstract class BasicAssistant extends JPanel implements IAssistant {

	private static final long serialVersionUID = 5029030093429243513L;

	protected List<IAssistant> childAssistants = new ArrayList<>();

	@Override
	public abstract String name();

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public boolean hasSubAssistant() {
		return !childAssistants.isEmpty();
	}

	@Override
	public List<IAssistant> getSubAssistants() {
		return childAssistants;
	}
	
	@Override
	public void addSubAssistant(IAssistant sub) {
		if(sub != null)
			childAssistants.add(sub);
	}

}
