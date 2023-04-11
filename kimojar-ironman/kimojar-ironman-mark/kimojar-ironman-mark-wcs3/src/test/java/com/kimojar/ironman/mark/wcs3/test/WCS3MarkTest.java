/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.test
 * FILE WCS3MarkTest.java
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
package com.kimojar.ironman.mark.wcs3.test;

import java.util.ServiceLoader;

import javax.swing.SwingUtilities;

import com.kimojar.ironman.jarvis.IMark;
import com.kimojar.ironman.jarvis.IVision;

/**
 * @author KiMoJar
 * @date 2023-02-28
 */
public class WCS3MarkTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServiceLoader<IVision> screenLoader = ServiceLoader.load(IVision.class);
		for(IVision vision : screenLoader) {
			ServiceLoader<IMark> markLoader = ServiceLoader.load(IMark.class);
			for(IMark mark : markLoader)
				vision.addMarkScreen(mark);
			SwingUtilities.invokeLater(() -> {
				vision.display();
			});
		}
	}

}
