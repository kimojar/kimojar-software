/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wss
 * PACKAGE com.kimojar.ironman.mark.wss.test
 * FILE WSSTest.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-01-31
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
package com.kimojar.ironman.mark.wss.test;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.Endpoint;

import com.kimojar.util.common.ref.AnnotationUtil;

/**
 * @author KiMoJar
 * @date 2023-01-31
 */
@WebService(targetNamespace = "http://com.ksec.wcs1/", serviceName = "WcsWebService")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class WSSTest {

	@WebMethod(operationName = "Test", action = "http://com.ksec.wcs2/WcsWebService/yest")
	// 有返回值的方法需要注解@WebResult并声明命名空间
	// @WebResult(targetNamespace = "http://com.ksec.wcs/", name = "ret")
	@WebResult(name = "ret")
	// @WebParam注解中使用name属性指定方法名,否则发布的方法的参数名默认会是arg0
	// @WebParam注解中使用targetNamespace属性指定方法的命名空间,否则调用方法收到的参数可能是null
	// public int DMPProtocol(@WebParam(targetNamespace = "http://com.ksec.wcs/", mode = Mode.IN, name="xmlData") String xmlData)
	public String test(@WebParam(targetNamespace = "http://com.kimojar.ironman.mark.wss", mode = Mode.IN, name = "para") String para) {
		return para;
	}

	/**
	 * @param args
	 */
	@WebMethod(exclude = true)
	public static void main(String[] args) throws Exception {
		WSSTest ws = new WSSTest();
		{
			Map<String, Object> webServiceProperties = new HashMap<>();
			webServiceProperties.put("serviceName", "WebService.serviceName");
			webServiceProperties.put("targetNamespace", "http://com.kimojar.ironman.mark.wss");
			AnnotationUtil.changeClassAnnotationProperties(WSSTest.class, WebService.class, webServiceProperties);
			AnnotationUtil.changeClassAnnotationProperty(WSSTest.class, BindingType.class, "value", javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING);
			
			Map<String, Object> WebMethodProperties = new HashMap<>();
			WebMethodProperties.put("operationName", "WebMethod.operationName");
			WebMethodProperties.put("action", "http://123.123.123.123:9999/WebMethod.action");
			AnnotationUtil.changeMethodAnnotationProperties(WSSTest.class, "test", WebMethod.class, WebMethodProperties);
			
			Map<String, Object> WebMethodProperties1 = new HashMap<>();
			WebMethodProperties1.put("name", "WebResult.name");
			AnnotationUtil.changeMethodAnnotationProperties(WSSTest.class, "test", WebResult.class, WebMethodProperties1);
			
//			Map<String, Object> WebParamProperties = new HashMap<>();
//			WebParamProperties.put("targetNamespace", "http://123.123.123.123:9999/WebService.targetNamespace");
//			AnnotationUtil.changeParameterAnnotationProperties(WSSTest.class, "test", 0, WebParam.class, WebParamProperties);
		}

		String address = "http://123.123.123.123:9999/ProvideWebService";
		Endpoint.publish(address, ws);
		System.out.println("WCS WebService publish success: " + address);
	}

}
