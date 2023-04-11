/**
 * ==============================================================================
 * PROJECT kimojar-util-common
 * PACKAGE com.kimojar.util.common.gui
 * FILE IComponentDict.java
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
package com.kimojar.util.common.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.border.TitledBorder;

import com.kimojar.util.common.i18n.Internationalization;

/**
 * 组件字典
 * 
 * @author KiMoJar
 * @date 2022-12-16
 */
public class ComponentDict {

	/** id-组件字典 */
	private static final Map<Internationalization, Component> idDict = new ConcurrentHashMap<>();
	/** id-TitledBorder组件字典 */
	private static final Map<Internationalization, TitledBorder> titledBorderDict = new ConcurrentHashMap<>();
	/** 组件-id字典 */
	private static final Map<Component, Internationalization> compDict = new ConcurrentHashMap<>();
	/** name-组件字典 */
	private static final Map<String, Component> nameDict = new ConcurrentHashMap<>();
	/** 类型-组件字典 */
	private static final Map<String, List<Component>> typeDict = new ConcurrentHashMap<String, List<Component>>();

	/**
	 * 记录一个组件
	 * 
	 * @param <T> 组件类型
	 * @param componentId 组件id
	 * @param component 组件
	 * @return T 被记录的组件
	 */
	public static <T extends Component> T record(Internationalization componentId, T component) {
		idDict.put(componentId, component);
		compDict.put(component, componentId);
		nameDict.put(componentId.getClass().getName() + "." + componentId.getPropertyKey(), component);
		if(!typeDict.containsKey(component.getClass().getName()))
			typeDict.put(component.getClass().getName(), new ArrayList<Component>());
		typeDict.get(component.getClass().getName()).add(component);
		return component;
	}

	/**
	 * 记录一个TitledBorder组件
	 * 
	 * @param componentId
	 * @param titledBorder
	 * @return
	 */
	public static TitledBorder recordTitledBorder(Internationalization componentId, TitledBorder titledBorder) {
		titledBorderDict.put(componentId, titledBorder);
		return titledBorder;
	}

	/**
	 * 根据组件id查询组件
	 * 
	 * @param <T> 组件类型
	 * @param componentId 组件id
	 * @return T 组件
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Component> T lookup(Internationalization componentId) {
		return (T) idDict.get(componentId);
	}

	/**
	 * 根据组件id查询组件
	 * 
	 * @param <T> 组件类型
	 * @param componentIdString 组件id字符串
	 * @return T 组件
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Component> T lookup(String componentIdString) {
		for(Entry<Internationalization, Component> entry : idDict.entrySet())
			if((entry.getKey().getClass().getName() + "." + entry.getKey().getPropertyKey()).equals(componentIdString))
				return (T) entry.getValue();
		return null;
	}

	/**
	 * 根据组件类型查询组件
	 * 
	 * @param <T> 组件类型
	 * @param clazz 组件类类型
	 * @return List<T> 组件列表
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Component> List<T> lookup(Class<T> clazz) {
		if(typeDict.get(clazz.getName()) == null)
			return new ArrayList<>(0);
		else
			return (List<T>) typeDict.get(clazz.getName());
	}

	/**
	 * 根据组件查询组件id
	 * 
	 * @param component
	 * @return
	 */
	public static Internationalization lookup(Component component) {
		return compDict.get(component);
	}

	/**
	 * 根据组件id查询TitledBorder
	 * 
	 * @return
	 */
	public static TitledBorder lookupTitledBorder(Internationalization componentId) {
		return titledBorderDict.get(componentId);
	}

	/**
	 * 获取所有记录的TitledBorder
	 * 
	 * @return
	 */
	public static Map<Internationalization, TitledBorder> lookupTitledBorders() {
		return titledBorderDict;
	}

}
