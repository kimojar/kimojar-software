/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7
 * FILE S7DBArea.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-11-14
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
import java.util.Arrays;
import java.util.List;

/**
 * A area of Data Block, 可简单理解为一个或多个连续的{@link S7DBElement}的集合。
 * 
 * @param <D>
 * @author KiMoJar
 * @date 2022-11-14
 */
public class S7DBArea<D> {

	/** 数据区所属DB块 */
	protected int dbNumber;
	/** 数据区下的数据元素 */
	protected List<S7DBElement<D>> elementList = new ArrayList<>();
	/** 数据区起始地址 */
	protected int startAddress = Integer.MAX_VALUE;
	/** 数据区结束地址 */
	protected int endAddress = Integer.MIN_VALUE;
	/** 数据区对应的item */
	protected S7Item item = new S7Item();

	public S7DBArea(int dbNumber) {
		this.dbNumber = dbNumber;
		this.item.setDbNumer(dbNumber);
	}

	/**
	 * 添加数据元素
	 * 
	 * @param element
	 * @return
	 */
	public boolean addElement(S7DBElement<D> element) {
		if(element == null)
			return false;
		if(dbNumber != element.getDBNumber())
			return false;
		elementList.add(element);
		for(S7DBElement<D> ent : elementList) {
			int from = ent.getStartByte();
			int to = from + ent.getPayloadLength();
			startAddress = (from < startAddress) ? from : startAddress;
			endAddress = (to > endAddress) ? to : endAddress;
		}
		item.setRwByteCount(endAddress - startAddress);
		return true;
	}

	public S7Item getItem() {
		return item;
	}

	public void update(byte[] data) {
		if(startAddress > endAddress)
			return;
		if(data.length < ((endAddress - startAddress) + 1))
			return;
		for(S7DBElement<D> element : elementList) {
			int from = element.getStartByte() - startAddress;
			int to = from + element.getPayloadLength();
			byte[] payload = Arrays.copyOfRange(data, from, to);
			element.setPayload(payload);
		}
	}
}
