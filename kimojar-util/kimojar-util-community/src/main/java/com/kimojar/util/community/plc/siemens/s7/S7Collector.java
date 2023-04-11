/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7
 * FILE S7Collector.java
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
package com.kimojar.util.community.plc.siemens.s7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kimojar.util.community.data.ICollector;
import com.kimojar.util.community.data.ICollectorDataListener;
import com.kimojar.util.community.plc.siemens.s7.socket.netty.S7Client;
import com.kimojar.util.community.plc.siemens.s7.telegram.S7Constant.ErrorCode;

/**
 * S7协议数据采集器
 * <p>
 * 一个数据采集器可以包含多个group，一个group对应一个S7连接由一个线程负责扫描，一个group可以包含多个item。
 * 
 * @author KiMoJar
 * @date 2022-05-05
 */
public class S7Collector implements ICollector<S7Group, byte[]> {

	private static final Logger logger = LoggerFactory.getLogger(S7Collector.class);

	/**
	 * group和扫描线程的对应关系
	 */
	private GroupScannerWrapper groupScannerWrapper;

	/**
	 * group和其监听器列表的对应关系
	 */
	private ConcurrentHashMap<String, ArrayList<ICollectorDataListener<S7Group, byte[]>>> groupListenerMap = new ConcurrentHashMap<String, ArrayList<ICollectorDataListener<S7Group, byte[]>>>();

	/**
	 * 是否开始扫描S7数据
	 */
	private AtomicBoolean ifScan = new AtomicBoolean(false);

	public S7Collector(List<S7Group> groupList) {
		this.groupScannerWrapper = new GroupScannerWrapper(groupList);
	}

	/**
	 * 从<code>/resource/collector.xml</code>中加载<code>S7Collector</code>
	 * 
	 * @return
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public static S7Collector loadS7Collector() {
		String configPath = System.getProperty("user.dir") + File.separator + "resource" + File.separator + "collector.xml";
		File config = new File(configPath);
		return loadS7Collector(config);
	}

	/**
	 * 从指定配置文件中加载<code>S7Collector</code>
	 * 
	 * @param collectorXML
	 * @return
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	public static S7Collector loadS7Collector(File collectorXML) {
		if(!collectorXML.exists() || !collectorXML.isFile()) {
			logger.error("collector config file {} is not exist at {}", collectorXML.getName(), collectorXML.getAbsolutePath());
			return null;
		}
		List<S7Group> s7Groups = new ArrayList<>();
		try {
			Document document = SAXReader.createDefault().read(collectorXML);
			Element root = document.getRootElement();
			for(Iterator<Element> rootIt = root.elementIterator(); rootIt.hasNext();) {
				Element s7 = rootIt.next();
				if(!s7.getName().equals(DICT_S7))
					continue;
				for(Iterator<Element> s7It = s7.elementIterator(); s7It.hasNext();) {
					Element group = s7It.next();
					if(!group.getName().equals(DICT_GROUP))
						continue;
					S7Group s7Group = new S7Group();
					s7Group.setEnable(Boolean.parseBoolean(group.attribute(DICT_GROUP_ENABLE).getText()));
					s7Group.setId(group.attribute(DICT_GROUP_ID).getText());
					s7Group.setName(group.attribute(DICT_GROUP_NAME).getText());
					s7Group.setIp(group.attribute(DICT_GROUP_IP).getText());
					s7Group.setPort(Integer.parseInt(group.attribute(DICT_GROUP_PORT).getText()));
					s7Group.setRack(Integer.parseInt(group.attribute(DICT_GROUP_RACK).getText()));
					s7Group.setSolt(Integer.parseInt(group.attribute(DICT_GROUP_SOLT).getText()));
					s7Group.setRate(Integer.parseInt(group.attribute(DICT_GROUP_RATE).getText()));
					for(Iterator<Element> loiIt = group.elementIterator(); loiIt.hasNext();) {
						Element loi = loiIt.next();
						if(loi.getName().equals(DICT_LISTENER) && Boolean.parseBoolean(loi.attribute(DICT_LISTENER_ENABLE).getText())) {
							String listener = loi.attribute(DICT_LISTENER_CLASS).getText();
							s7Group.addListener(listener);
						}
						if(loi.getName().equals(DICT_ITEM)) {
							S7Item item = new S7Item();
							item.setEnable(Boolean.parseBoolean(loi.attribute(DICT_ITEM_ENABLE).getText()));
							item.setId(loi.attribute(DICT_ITEM_ID).getText());
							item.setName(loi.attribute(DICT_ITEM_NAME).getText());
							item.setDbNumer(Integer.parseInt(loi.attribute(DICT_ITEM_DBNUMBER).getText()));
							item.setStartByte(Integer.parseInt(loi.attribute(DICT_ITEM_STARTBYTE).getText()));
							item.setRwByteCount(Integer.parseInt(loi.attribute(DICT_ITEM_RWBYTECOUNT).getText()));
							item.setStartCode(Integer.parseInt(loi.attribute(DICT_ITEM_STARTCODE).getText()));
							item.setStep(Integer.parseInt(loi.attribute(DICT_ITEM_STEP).getText()));
							for(Iterator<Element> propertyIt = loi.elementIterator(); propertyIt.hasNext();) {
								Element property = propertyIt.next();
								String key = property.attribute(DICT_ITEM_PROPERTY_KEY).getText();
								String value = property.attribute(DICT_ITEM_PROPERTY_VALUE).getText();
								item.addProperty(key, value);
							}
							s7Group.addItem(item);
						}
					}
					s7Groups.add(s7Group);
				}
			}
			S7Collector collector = new S7Collector(s7Groups);
			return collector;
		} catch(Exception e) {
			logger.error("load s7collector form config file {} occured error: {}", collectorXML.getName(), e);
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public int init() {
		for(S7Group group : groupScannerWrapper.getGroupList()) {
			try {
				for(String clazz : group.getListeners()) {
					try {
						Class<?> type = Class.forName(clazz);
						ICollectorDataListener<S7Group, byte[]> listener = (ICollectorDataListener<S7Group, byte[]>) type.newInstance();
						if(listener != null)
							addCollectorDataListener(group, listener);
					} catch(ClassCastException e) {
						e.printStackTrace();
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public void addCollectorDataListener(S7Group source, ICollectorDataListener<S7Group, byte[]> listener) {
		if(source != null) {
			if(groupListenerMap.containsKey(source.getId()))
				groupListenerMap.get(source.getId()).add(listener);
			else {
				ArrayList<ICollectorDataListener<S7Group, byte[]>> listeners = new ArrayList<ICollectorDataListener<S7Group, byte[]>>();
				listeners.add(listener);
				groupListenerMap.put(source.getId(), listeners);
			}
		}

	}

	@Override
	public int start() {
		if(!ifScan.getAndSet(true)) {
			for(final S7Group group : groupScannerWrapper.getGroupList()) {
				if(group.isEnable()) {
					groupScannerWrapper.getS7Scanner(group).execute(new Runnable() {

						public void run() {
							while(ifScan.get()) {
								try {
									String threadName = group.getId() == null ? S7Collector.class.getSimpleName() + "-" + Thread.currentThread().getName() : group.getId();
									Thread.currentThread().setName(threadName);
									if(group.isValid()) {
										S7Client s7Client = groupScannerWrapper.getS7Client(group);
										if(!s7Client.isConnect()) {
											s7Client.disconnect();
											s7Client.connect();
										}
										if(s7Client.isConnect()) {
											for(S7Item item : group.getItems()) {
												if(item.isEnable()) {
													group.setCurrentScanItem(item);
													S7Event result = s7Client.readDB(item.getDbNumer(), item.getStartByte(), item.getRwByteCount(), 0);
													if(result.getErrorcode() == ErrorCode.NoError)
														for(ICollectorDataListener<S7Group, byte[]> listener : groupListenerMap.get(group.getId()))
															listener.onData(group, result.getData());
												}
											}
										}
									}
									Thread.sleep(group.getRate());
								} catch(Exception e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
			}
		}
		return 0;
	}

	@Override
	public int stop() {
		ifScan.set(false);
		groupScannerWrapper.stopAllConnection();
		return 0;
	}

	public static final String DICT_COLLECTOR = "collector";
	public static final String DICT_S7 = "s7";
	public static final String DICT_S7_ENABLE = "enable";
	public static final String DICT_GROUP = "group";
	public static final String DICT_GROUP_ENABLE = "enable";
	public static final String DICT_GROUP_ID = "id";
	public static final String DICT_GROUP_NAME = "name";
	public static final String DICT_GROUP_IP = "ip";
	public static final String DICT_GROUP_PORT = "port";
	public static final String DICT_GROUP_RACK = "rack";
	public static final String DICT_GROUP_SOLT = "solt";
	public static final String DICT_GROUP_RATE = "rate";
	public static final String DICT_LISTENER = "listener";
	public static final String DICT_LISTENER_ENABLE = "enable";
	public static final String DICT_LISTENER_CLASS = "class";
	public static final String DICT_ITEM = "item";
	public static final String DICT_ITEM_ENABLE = "enable";
	public static final String DICT_ITEM_ID = "id";
	public static final String DICT_ITEM_NAME = "name";
	public static final String DICT_ITEM_DBNUMBER = "dbNumer";
	public static final String DICT_ITEM_STARTBYTE = "startByte";
	public static final String DICT_ITEM_RWBYTECOUNT = "rwByteCount";
	public static final String DICT_ITEM_STARTCODE = "startCode";
	public static final String DICT_ITEM_STEP = "step";
	public static final String DICT_ITEM_PROPERTY_KEY = "key";
	public static final String DICT_ITEM_PROPERTY_VALUE = "value";
}