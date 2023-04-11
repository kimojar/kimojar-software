/**
 * ==============================================================================
 * PROJECT wcs-util-community
 * PACKAGE com.kimojar.util.community.data
 * FILE ICollectorDataListener.java
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
package com.kimojar.util.community.data;

/**
 * 数据采集器监听接口
 * <li>S:数据源类型</li>
 * <li>D:数据采集器采集的数据的类型</li>
 * 
 * @author KiMoJar
 * @date 2022-05-05
 */
public interface ICollectorDataListener<S, D> {

	/**
	 * 监听到数据
	 * 
	 * @param source 数据源
	 * @param data 数据
	 */
	void onData(S source, D data);

}
