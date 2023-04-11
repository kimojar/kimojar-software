/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wss
 * PACKAGE com.kimojar.ironman.mark.wss
 * FILE WebServiceServer.java
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
package com.kimojar.ironman.mark.wss;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.WebParam.Mode;
import javax.xml.ws.BindingType;
import javax.xml.ws.Endpoint;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kimojar.util.common.ref.AnnotationUtil;

/**
 * WebService服务端
 * <li>提供三个接口方法供调用，默认三个接口方法开启
 * <li>可使用提供的静态方法来启停接口方法，以及修改soap协议版本和接口方法名
 * <li>可添加监听器来监听收到的消息
 * <li>可使用提供方法来设置回复的消息和回复的时机
 * 
 * @author KiMoJar
 * @date 2023-01-31
 */
@WebService(targetNamespace = "http://com.kimojar.ironman.mark.wss/", serviceName = "KiMoJarWebService")
//@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class WebServiceServer {
	
	private static final Logger logger = LoggerFactory.getLogger(WebServiceServer.class);

	private Endpoint endpoint;
	private ConcurrentLinkedQueue<IMessageListener> listeners = new ConcurrentLinkedQueue<>();
	private String serverAReturnData = "invoke Server A success!";
	private String serverBReturnData = "invoke Server B success!";
	private String serverCReturnData = "invoke Server C success!";
	private int serverAReturnDelay = 0;
	private int serverBReturnDelay = 0;
	private int serverCReturnDelay = 0;
	public boolean published = false;

	@WebMethod(operationName = "serverA", action = "http://com.kimojar.ironman.mark.wss/serverA", exclude = false)
	@WebResult(name = "return")
	public String serverA(@WebParam(targetNamespace = "http://com.kimojar.ironman.mark.wss/", mode = Mode.IN, name = "para") String para) {
		logger.trace("KiMoJar WebService function ServerA was invoked! para: {}", para);
		for(IMessageListener listener : listeners)
			listener.onMessageA(para);
		if(serverAReturnDelay > 0)
			try {
				Thread.sleep(serverAReturnDelay);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		return serverAReturnData;
	}

	@WebMethod(operationName = "serverB", action = "http://com.kimojar.ironman.mark.wss/serverB", exclude = false)
	@WebResult(name = "return")
	public String serverB(@WebParam(targetNamespace = "http://com.kimojar.ironman.mark.wss/", mode = Mode.IN, name = "para") String para) {
		logger.trace("KiMoJar WebService function ServerB was invoked! para: {}", para);
		for(IMessageListener listener : listeners)
			listener.onMessageB(para);
		if(serverBReturnDelay > 0)
			try {
				Thread.sleep(serverBReturnDelay);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		return serverBReturnData;
	}

	@WebMethod(operationName = "serverC", action = "http://com.kimojar.ironman.mark.wss/serverC", exclude = false)
	@WebResult(name = "return")
	public String serverC(@WebParam(targetNamespace = "http://com.kimojar.ironman.mark.wss/", mode = Mode.IN, name = "para") String para) {
		logger.trace("KiMoJar WebService function ServerC was invoked! para: {}", para);
		for(IMessageListener listener : listeners)
			listener.onMessageC(para);
		if(serverCReturnDelay > 0)
			try {
				Thread.sleep(serverCReturnDelay);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		return serverCReturnData;
	}

	/**
	 * 更改WebService的soap协议版本
	 * 
	 * @param protocol
	 */
	@WebMethod(exclude = true)
	public static void changeSoapProtocol(SoapProtocol protocol) {
		try {
			protocol = (protocol == null) ? SoapProtocol.Protocol1_2 : protocol;
			AnnotationUtil.changeClassAnnotationProperty(WebServiceServer.class, BindingType.class, "value", protocol.value);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启用或者停用服务serverA
	 * 
	 * @param enable
	 */
	@WebMethod(exclude = true)
	public static void enableServerA(boolean enable) {
		try {
			AnnotationUtil.changeMethodAnnotationProperty(WebServiceServer.class, "serverA", WebMethod.class, "exclude", enable);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启用或者停用服务serverB
	 * 
	 * @param enable
	 */
	@WebMethod(exclude = true)
	public static void enableServerB(boolean enable) {
		try {
			AnnotationUtil.changeMethodAnnotationProperty(WebServiceServer.class, "serverB", WebMethod.class, "exclude", enable);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启用或者停用服务serverC
	 * 
	 * @param enable
	 */
	@WebMethod(exclude = true)
	public static void enableServerC(boolean enable) {
		try {
			AnnotationUtil.changeMethodAnnotationProperty(WebServiceServer.class, "serverC", WebMethod.class, "exclude", enable);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更改服务serverA的服务名
	 * 
	 * @param newName
	 */
	@WebMethod(exclude = true)
	public static void changeServerAName(String newName) {
		try {
			newName = StringUtils.isEmpty(newName) ? "serverA" : newName;
			AnnotationUtil.changeMethodAnnotationProperty(WebServiceServer.class, "serverA", WebMethod.class, "operationName", newName);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更改服务serverB的服务名
	 * 
	 * @param newName
	 */
	@WebMethod(exclude = true)
	public static void changeServerBName(String newName) {
		try {
			newName = StringUtils.isEmpty(newName) ? "serverB" : newName;
			AnnotationUtil.changeMethodAnnotationProperty(WebServiceServer.class, "serverB", WebMethod.class, "operationName", newName);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更改服务serverC的服务名
	 * 
	 * @param newName
	 */
	@WebMethod(exclude = true)
	public static void changeServerCName(String newName) {
		try {
			newName = StringUtils.isEmpty(newName) ? "serverC" : newName;
			AnnotationUtil.changeMethodAnnotationProperty(WebServiceServer.class, "serverC", WebMethod.class, "operationName", newName);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@WebMethod(exclude = true)
	public boolean publish(String address) {
		try {
			endpoint = Endpoint.publish(address, this);
		} catch(Exception e) {
			logger.debug("KiMoJar WebService publish failed: {}", address);
			return published = false;
		}
		logger.debug("KiMoJar WebService publish successed: {}", address);
		return published = true;
	}

	@WebMethod(exclude = true)
	public void stop() {
		endpoint.stop();
		published = false;
		logger.debug("KiMoJar WebService stoped");
	}

	@WebMethod(exclude = true)
	public void addMessageListener(IMessageListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}

	@WebMethod(exclude = true)
	public void setServerAReturnData(String returnData) {
		serverAReturnData = returnData;
	}

	@WebMethod(exclude = true)
	public void setServerBReturnData(String returnData) {
		serverBReturnData = returnData;
	}

	@WebMethod(exclude = true)
	public void setServerCReturnData(String returnData) {
		serverCReturnData = returnData;
	}

	@WebMethod(exclude = true)
	public void setServerAReturnDelay(int returnDelay) {
		serverAReturnDelay = returnDelay;
	}

	@WebMethod(exclude = true)
	public void setServerBReturnDelay(int returnDelay) {
		serverBReturnDelay = returnDelay;
	}

	@WebMethod(exclude = true)
	public void setServerCReturnDelay(int returnDelay) {
		serverCReturnDelay = returnDelay;
	}

	public enum SoapProtocol {

		Protocol1_1(javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING),
		Protocol1_2(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING),
		Protocol1_2_SUN("http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/"),
		;

		public String value;

		SoapProtocol(String value) {
			this.value = value;
		}
	}

}
