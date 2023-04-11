/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7
 * FILE GroupScannerWrapper.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-05-07
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

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kimojar.util.community.plc.siemens.s7.socket.netty.S7Client;

/**
 * S7Group和扫描线程对应封装，一个Group可以由一个Client来采集数据
 * 
 * @author KiMoJar
 * @date 2022-05-08
 */
public class GroupScannerWrapper {

	private List<S7Group> groupList;

	/** group和扫描线程的对应关系 */
	private ConcurrentHashMap<String, ExecutorService> groupScannerMap = new ConcurrentHashMap<String, ExecutorService>();
	
	/** group和S7连接客户端的对应关系 */
	private ConcurrentHashMap<String, S7Client> groupClientMap = new ConcurrentHashMap<String, S7Client>();

	public GroupScannerWrapper(List<S7Group> groupList) {
		this.groupList = groupList;
		for(S7Group group : groupList)
			groupScannerMap.put(group.getId(), Executors.newSingleThreadExecutor());
		for(S7Group group : groupList)
			groupClientMap.put(group.getId(), new S7Client(group.getIp(), group.getPort(), group.getRack(), group.getSolt()));
	}

	public List<S7Group> getGroupList() {
		return groupList;
	}

	public ExecutorService getS7Scanner(S7Group group) {
		return groupScannerMap.get(group.getId());
	}

	public ExecutorService getS7Scanner(String groupId) {
		for(Entry<String, ExecutorService> entry : groupScannerMap.entrySet())
			if(groupId.equals(entry.getKey()))
				return entry.getValue();
		return null;
	}

	public S7Client getS7Client(S7Group group) {
		return groupClientMap.get(group.getId());
	}

	public S7Client getS7Client(String groupId) {
		for(Entry<String, S7Client> entry : groupClientMap.entrySet())
			if(groupId.equals(entry.getKey()))
				return entry.getValue();
		return null;
	}

	public void stopAllConnection() {
		for(Entry<String, S7Client> entry : groupClientMap.entrySet())
			entry.getValue().disconnect();
	}

}
