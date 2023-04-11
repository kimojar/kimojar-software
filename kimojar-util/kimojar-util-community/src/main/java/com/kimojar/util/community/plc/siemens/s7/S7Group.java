/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7
 * FILE S7Group.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-05-05
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
package com.kimojar.util.community.plc.siemens.s7;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author KiMoJar
 * @date 2022-05-05
 */
@Data
public class S7Group {

	private String id;
	private String name;
	private String ip;
	private int port;
	private int rack;
	private int solt;
	private int rate;
	private List<String> listeners;
	private List<S7Item> items;
	private boolean enable;
	private S7Item currentScanItem = null;

	public void addListener(String listenerClass) {
		if(listeners == null)
			listeners = new ArrayList<>();
		listeners.add(listenerClass);
	}

	public void addItem(S7Item item) {
		if(items == null)
			items = new ArrayList<>();
		items.add(item);
	}

	public boolean isValid() {
		return ip != null && listeners != null && listeners.size() > 0 && items != null && items.size() > 0;
	}
}
