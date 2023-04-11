/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wsc
 * PACKAGE com.kimojar.ironman.mark.wsc
 * FILE WebServiceClient.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-02-03
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
package com.kimojar.ironman.mark.wsc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * WebService客户端
 * 
 * @author KiMoJar
 * @date 2023-02-03
 */
public class WebServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(WebServiceClient.class);

	private String address = "";
	private String namespace = "http://com.kimojar.ironman.mark.wss/";
	private SoapProtocol protocol = SoapProtocol.Protocol1_2;
	private String functionName = "serverA";
	private String functionPara = "para";
	private String requestData = "Hello web service server, I'm client.";
	private String functionResponse = "serverAResponse";
	private int invokeTimeout = 10000;// 调用超时时间默认10s

	public InvokeResult invokeBySoapConnection() {
		InvokeResult invokeResult = new InvokeResult();
		try {
			SOAPConnection soapConnection = SOAPConnectionFactory.newInstance().createConnection();
			SOAPMessage requestMessage = createSoapMessage();
			// 记录请求消息日志
			StringOutputStream requestString = new StringOutputStream();
			requestMessage.writeTo(requestString);
			invokeResult.requestMessage = requestString.getString();
			if(logger.isTraceEnabled())
				logger.trace("request soap message: {}", requestString.getString());
			/*
			 * URL endPoint = new URL(new URL(invokeURLString), invokeURLString, new URLStreamHandler()
			 * {
			 * @Override
			 * protected URLConnection openConnection(URL url) throws IOException
			 * {
			 * URL target = new URL(url.toString());
			 * HttpURLConnection connection = (HttpURLConnection) target.openConnection();
			 * // 通过此处设置请求的Content-Type无效,
			 * // 因为使用SOAPConnection发送请求,使用的是SOAPMessage协议的Content-Type
			 * connection.setRequestProperty("Content-Type", "application/soap+xml");
			 * connection.setRequestMethod("POST");
			 * return connection;
			 * }
			 * });
			 */
			// URL endPoint = new URL(address);
			URL endPoint = new URL(null, address, new URLStreamHandler() {

				@Override
				protected URLConnection openConnection(URL u) throws IOException {
					URL url = new URL(u.toString());
					URLConnection connection = url.openConnection();
					// connection.setConnectTimeout(10000);// 通常就是2s,经过测试,设置长了也没用
					connection.setReadTimeout(invokeTimeout);
					return connection;
				}
			});

			long beginTime = System.currentTimeMillis();
			SOAPMessage responseMessage = soapConnection.call(requestMessage, endPoint);
			long endTime = System.currentTimeMillis();
			invokeResult.invokeCost = endTime - beginTime;
			// 记录响应消息日志
			StringOutputStream responseString = new StringOutputStream();
			responseMessage.writeTo(responseString);
			invokeResult.responseMessage = responseString.getString();
			if(logger.isTraceEnabled())
				logger.trace("response soap message: {}", responseString.getString());
			analysisResponse(responseMessage, invokeResult);
		} catch(Exception e) {
			logger.debug("invoke webservice error. exception: {}, address: {}", e, address);
			e.printStackTrace();
			invokeResult.isInvokeSuccess = false;
			invokeResult.failedReason = e.toString();
		}
		return invokeResult;
	}

	public InvokeResult invokeByHttpConnection() {
		InvokeResult invokeResult = new InvokeResult();
		try {
			HttpURLConnection httpConnection = (HttpURLConnection) new URL(address).openConnection();
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setRequestProperty("Content-Type", "application/soap+xml");
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setReadTimeout(invokeTimeout);

			OutputStream output = httpConnection.getOutputStream();
			SOAPMessage requestMessage = createSoapMessage();
			long beginTime = System.currentTimeMillis();
			requestMessage.writeTo(output);
			output.flush();
			output.close();
			// 记录请求消息日志
			StringOutputStream requestString = new StringOutputStream();
			requestMessage.writeTo(requestString);
			invokeResult.requestMessage = requestString.getString();
			if(logger.isTraceEnabled())
				logger.trace("request soap message: {}", requestString.getString());

			InputStream input = httpConnection.getInputStream();
			long endTime = System.currentTimeMillis();
			invokeResult.invokeCost = endTime - beginTime;
			MessageFactory messageFactory = MessageFactory.newInstance(protocol.value);
			SOAPMessage responseMessage = messageFactory.createMessage(null, input);
			// 记录响应消息日志
			StringOutputStream responseString = new StringOutputStream();
			responseMessage.writeTo(responseString);
			invokeResult.responseMessage = responseString.getString();
			if(logger.isTraceEnabled())
				logger.trace("response soap message: {}", responseString.getString());
			analysisResponse(responseMessage, invokeResult);
		} catch(Exception e) {
			logger.debug("invoke webservice error. exception: {}, address: {}", e, address);
			e.printStackTrace();
			invokeResult.isInvokeSuccess = false;
			invokeResult.failedReason = e.toString();
		}
		return invokeResult;
	}

	private SOAPMessage createSoapMessage() throws SOAPException {
		MessageFactory messageFactory = MessageFactory.newInstance(protocol.value);
		SOAPMessage soapMessage = messageFactory.createMessage();
		// 在soap消息的MimeHeaders中指定Content-Type是无效的,指定soapaction也是多余的
		// MimeHeaders mimeHeader = soapMessage.getMimeHeaders();
		// mimeHeader.setHeader("SOAPAction", interfaceProperties.getProperty("InvokeWebService.Action"));
		SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();// soap消息的外壳
		SOAPBody body = envelope.getBody();// soap消息的body
		/*
		 * 创建一个name对象,实际上就是创建一个元素,指定其名称,前缀,以及命名空间
		 * 前缀可要可无,但是最好不要,要了好像有问题
		 * 但是命名空间是必须要的,而且与WebService wsdl中方法的命名空间一致,否则调用会返回找不到方法
		 */
		Name name = envelope.createName(functionName, "", namespace);
		SOAPBodyElement funcationElement = body.addBodyElement(name);
		SOAPElement paraElement = funcationElement.addChildElement(functionPara);
		paraElement.setValue(requestData);
		soapMessage.saveChanges();
		return soapMessage;
	}

	/**
	 * 解析调用结果
	 * 
	 * @param responseMessage
	 * @return
	 * @throws SOAPException
	 */
	private void analysisResponse(SOAPMessage responseMessage, InvokeResult invokeResult) throws SOAPException {
		/*
		 * 如果返回消息有DMPProtocolResponse有命名空间,则需要用如下方法
		 * 顺丰WMS接口就有这个用来命名空间的问题
		 * 使用命名空间来获取,即使不使用命名空间,也可以,所以推荐改该方法
		 * NodeList nodes1 = responseMessage.getSOAPPart().getEnvelope().getBody().getElementsByTagNameNS(wsNamespace, wsResponse);
		 * System.out.println("Return code:" + nodes1.item(0).getTextContent());
		 */
		SOAPBody body = responseMessage.getSOAPPart().getEnvelope().getBody();
		// Node responseNode = body.getElementsByTagName(functionResponse).item(0);// responseNode中不包含命名空间时没问题，如果responseNode包含了命名空间获取responseNode时会为null
		// Node responseNode = body.getElementsByTagNameNS(namespace, functionResponse).item(0);// responseNode中包含或者不包含命名空间，均可用该方法解决"命名空间获取responseNode会为null"的问题
		Node responseNode = body.extractContentAsDocument().getChildNodes().item(0);// 也可用该方法规避responseNode是否带命名空间的问题，推荐，因为该方法可以不使用命名空间
		if(isInvokeSuccess(responseNode)) {
			invokeResult.isInvokeSuccess = true;
			invokeResult.successReturn = getInvokeSuccessReturn(responseNode);
		} else {
			invokeResult.isInvokeSuccess = false;
			invokeResult.failedCode = getInvokeFailedCode(responseNode);
			invokeResult.failedReason = getInvokeFailedReason(responseNode);
		}
	}

	/**
	 * 根据返回的soap消息判断调用是否成功
	 * 
	 * @param responseNode soap消息中body的子元素
	 * @return
	 */
	private boolean isInvokeSuccess(Node responseNode) {
		String nameWithNS = responseNode.getNodeName();// body的子元素带命名空间前缀名称，例如："ns2:serverAResponse"，调用成功的话是{function}Response，调用失败的话是Fault
		String name = responseNode.getLocalName();// body的子元素不带命名空间前缀名称，例如："serverAResponse"
		if(name.equalsIgnoreCase("Fault") && nameWithNS.contains(":")) {
			String namespacePrefix = nameWithNS.split(":")[0];// 命名空间前缀，例如："ns2"
			String namespace = responseNode.lookupNamespaceURI(namespacePrefix);
			if(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE.equals(namespace)
			|| SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE.equals(namespace)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 调用成功时，获取调用得到的返回值
	 * <li>如果调用接口有返回值的话，则返回调用接口得到的返回值
	 * <li>如果调用接口没有返回值的话，则返回null
	 * 
	 * @param responseNode
	 * @return
	 */
	private String getInvokeSuccessReturn(Node responseNode) {
		Node returnNode = responseNode.getChildNodes().item(0);// 返回值元素
		if(returnNode == null)
			return null;// 没有返回值的方法调用，调用结果也没有返回值，即returnNode会为null
		else
			return returnNode.getTextContent();
	}

	/**
	 * 调用失败时，获取对应的错误码，只有{@link WebServiceClient#isInvokeSuccess(Node)}为false时调用有效
	 * 
	 * @param responseNode
	 * @return
	 */
	private String getInvokeFailedCode(Node responseNode) {
		NodeList children = responseNode.getChildNodes();// Code和Reason元素
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);// Code或Reason元素
			if(child.getLocalName().equalsIgnoreCase("Code"))
				return child.getTextContent();
		}
		return null;
	}

	/**
	 * 调用失败时，获取对应的错误原因，只有{@link WebServiceClient#isInvokeSuccess(Node)}为false时调用有效
	 * 
	 * @param responseNode
	 * @return
	 */
	private String getInvokeFailedReason(Node responseNode) {
		NodeList children = responseNode.getChildNodes();// Code和Reason元素
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);// Code或Reason元素
			if(child.getLocalName().equalsIgnoreCase("Reason"))
				return child.getTextContent();
		}
		return null;
	}

	public void setInvokeAddress(String address) {
		this.address = StringUtils.isEmpty(address) ? this.address : address;
	}

	public void setInvokeNamespace(String namespace) {
		this.namespace = StringUtils.isEmpty(namespace) ? this.namespace : namespace;
	}

	public void setInvokeSoapProtocol(SoapProtocol protocol) {
		this.protocol = protocol == null ? this.protocol : protocol;
	}

	public void setInvokeFunctionName(String function) {
		this.functionName = StringUtils.isEmpty(function) ? this.functionName : function;
	}

	public void setInvokeFunctionPara(String paraName) {
		this.functionPara = StringUtils.isEmpty(paraName) ? this.functionPara : paraName;
	}

	public void setInvokeRequestData(String requestData) {
		this.requestData = requestData;
	}

	public void setInvokeFunctionResponse(String responseName) {
		this.functionResponse = StringUtils.isEmpty(responseName) ? this.functionResponse : responseName;
	}

	public void setInvokeTimeout(int invokeTimeout) {
		this.invokeTimeout = invokeTimeout < 500 ? this.invokeTimeout : invokeTimeout;
	}

	public enum SoapProtocol {

		Protocol1_1(SOAPConstants.SOAP_1_1_PROTOCOL),
		Protocol1_2(SOAPConstants.SOAP_1_2_PROTOCOL),
		;

		public String value;

		SoapProtocol(String value) {
			this.value = value;
		}
	}

	private class StringOutputStream extends OutputStream {

		StringBuilder builder = new StringBuilder();

		@Override
		public void write(int b) throws IOException {
			builder.append((char) b);
		}

		public String getString() {
			return builder.toString();
		}
	}

	public class InvokeResult {

		/**
		 * webservice是否调用成功
		 */
		boolean isInvokeSuccess;
		/**
		 * webservice调用成功时的返回值，如果接口方法无返回值则为null
		 */
		String successReturn = null;
		/**
		 * webservice调用失败时的失败代码
		 */
		String failedCode = null;
		/**
		 * webservice调用失败时的失败原因
		 */
		String failedReason = null;

		/**
		 * 调用webservice的请求报文
		 */
		String requestMessage;

		/**
		 * 调用webservice得到的响应报文
		 */
		String responseMessage;

		/**
		 * 调用webservice的消耗时间
		 */
		long invokeCost;
	}

}
