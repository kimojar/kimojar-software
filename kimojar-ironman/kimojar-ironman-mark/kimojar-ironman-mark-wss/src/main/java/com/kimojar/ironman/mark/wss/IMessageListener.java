/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wss
 * PACKAGE com.kimojar.ironman.mark.wss
 * FILE IMessageListener.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-02-01
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
package com.kimojar.ironman.mark.wss;

/**
 * WebService服务端的消息监听
 * 
 * @author KiMoJar
 * @date 2023-02-01
 */
public interface IMessageListener {

	/**
	 * WebService ServerA接口被调用
	 * 
	 * @param message
	 */
	void onMessageA(String message);

	/**
	 * WebService ServerB接口被调用
	 * 
	 * @param message
	 */
	void onMessageB(String message);

	/**
	 * WebService ServerB接口被调用
	 * 
	 * @param message
	 */
	void onMessageC(String message);
}
