/**
 * ==============================================================================
 * PROJECT wcs-util-community
 * PACKAGE com.kimojar.util.community.data
 * FILE AbstractDataElement.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-07-08
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

import java.util.UUID;
import java.util.function.Function;

import org.apache.commons.lang3.Validate;

/**
 * 抽象数据元素。实现类基本的数据元素概念。
 * 
 * @author KiMoJar
 * @param <P> payload:表达数据元素的原始数据的类型
 * @param <D> data:数据元素所表达的数据类型
 * @date 2022-07-08
 */
public abstract class AbstractDataElement<P, D> implements IDataElement<P, D> {

	/** 数据元素的唯一标识 */
	protected String id;
	/** 数据元素的名称 */
	protected String name;
	/** 数据元素的描述 */
	protected String desc;
	/** 表示数据元素的原始数据 */
	protected P payload;
	/** 数据元素所表示的数据 */
	protected D data;
	/** 原始数据解码器：将原始数据解码为该数据元素表示的数据 */
	protected Function<P, D> payloadDecoder;
	/** 数据编码器：将该数据元素表示的数据编码为原始数据 */
	protected Function<D, P> dataCoder;

	/**
	 * @param name 数据元素名称，数据元素的唯一标识，不能为null或空
	 * @param desc 数据元素描述
	 */
	public AbstractDataElement(String name, String desc) {
		this.id = UUID.randomUUID().toString();
		this.name = Validate.notEmpty(name, "DataElement name cannot be null or empty");
		this.desc = desc;
	}

	@Override
	public String id() {
		return null;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String desc() {
		return desc;
	}

	@Override
	public P payload() {
		return payload;
	}

	@Override
	public D data() {
		return data;
	}

	/**
	 * 设置原始数据的解码器
	 * 
	 * @param payloadDecoder
	 */
	public void setPayloadDecoder(Function<P, D> payloadDecoder) {
		if(payloadDecoder == null)
			return;
		this.payloadDecoder = payloadDecoder;
	}

	/**
	 * 设置数据的编码器
	 * 
	 * @param dataCoder
	 */
	public void setDataCoder(Function<D, P> dataCoder) {
		if(dataCoder == null)
			return;
		this.dataCoder = dataCoder;
	}

	/**
	 * 设置数据元素的原始数据，并尝试对设置的原始数据进行解码
	 * 
	 * @param payload
	 */
	public void setPayload(P payload) {
		if(payload == null)
			return;
		this.payload = payload;
		if(payloadDecoder != null)
			data = payloadDecoder.apply(payload);
	}

	/**
	 * 设置数据元素的数据，并尝试对设置的数据进行编码
	 * 
	 * @param data
	 */
	public void setData(D data) {
		if(data == null)
			return;
		this.data = data;
		if(dataCoder != null)
			payload = dataCoder.apply(data);
	}

}
