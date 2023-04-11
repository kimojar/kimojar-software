/**
 * ==============================================================================
 * PROJECT kimojar-util-common
 * PACKAGE com.kimojar.util.common.net
 * FILE AddressUtil.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-03-04
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
package com.kimojar.util.common.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author KiMoJar
 * @date 2023-03-04
 */
public class AddressUtil {

	/**
	 * 获取本机所有网卡的ipv4地址
	 * 
	 * @return
	 */
	public static Inet4Address[] getLocalInet4Address() {
		List<Inet4Address> addressList = new ArrayList<Inet4Address>();
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while(networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while(addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					if(address instanceof Inet4Address)
						addressList.add((Inet4Address) address);
				}
			}
		} catch(SocketException e) {
			e.printStackTrace();
		}
		Inet4Address[] addresses = new Inet4Address[addressList.size()];
		for(int i = 0; i < addressList.size(); i++) {
			addresses[i] = addressList.get(i);
		}
		return addresses;
	}

	/**
	 * 获取本机所有网卡的ipv4地址
	 * 
	 * @return
	 */
	public static String[] getLocalInet4AddressString() {
		Inet4Address[] addresses = getLocalInet4Address();
		String[] addressesString = new String[addresses.length];
		for(int i = 0; i < addresses.length; i++)
			addressesString[i] = addresses[i].getHostAddress();
		return addressesString;
	}
}
