/**
 * ==============================================================================
 * PROJECT kimojar-util-common
 * PACKAGE com.kimojar.util.common.comparator
 * FILE Comparators.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-03-06
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
package com.kimojar.util.common.comparator;

/**
 * @author KiMoJar
 * @date 2023-03-06
 */
public class Comparators {

	public static int charSequenceCompare(String str1, String str2) {
		int n1 = str1.length();
		int n2 = str2.length();
		int min = Math.min(n1, n2);
		for(int i = 0; i < min; i++) {
			char c1 = str1.charAt(i);
			char c2 = str2.charAt(i);
			if(c1 != c2) {
				c1 = Character.toUpperCase(c1);
				c2 = Character.toUpperCase(c2);
				if(c1 != c2) {
					c1 = Character.toLowerCase(c1);
					c2 = Character.toLowerCase(c2);
					if(c1 != c2) {
						// No overflow because of numeric promotion
						return c1 - c2;
					}
				}
			}
		}
		return n1 - n2;
	}
}
