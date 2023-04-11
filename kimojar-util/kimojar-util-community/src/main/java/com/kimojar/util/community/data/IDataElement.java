/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.data
 * FILE IDataElement.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-06-20
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
package com.kimojar.util.community.data;

/**
 * 数据元素。它是一个或多个bit的组合（通常为一个或多个字节的组合），代表特定的应用层含义。
 * 
 * @param <P> payload:表达数据元素的原始数据的类型
 * @param <D> data:数据元素所表达的数据类型
 * @author KiMoJar
 * @date 2022-06-20
 */
public interface IDataElement<P, D> {

	/**
	 * 获取数据元素的唯一标识
	 * 
	 * @return
	 */
	String id();

	/**
	 * 获取数据元素的应用名称
	 * 
	 * @return
	 */
	String name();

	/**
	 * 获取数据元素的应用描述
	 * 
	 * @return
	 */
	String desc();

	/**
	 * 获取表示数据元素的原始数据
	 * 
	 * @return
	 */
	P payload();

	/**
	 * 获取数据元素所表达的数据
	 * 
	 * @return
	 */
	D data();

}
