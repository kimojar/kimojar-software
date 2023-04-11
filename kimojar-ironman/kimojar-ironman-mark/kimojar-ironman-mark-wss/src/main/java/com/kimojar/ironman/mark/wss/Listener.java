/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wss
 * PACKAGE com.kimojar.ironman.mark.wss
 * FILE Listener.java
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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.kimojar.ironman.mark.wss.WebServiceServer.SoapProtocol;
import com.kimojar.util.common.gui.ComponentDict;

/**
 * @author KiMoJar
 * @date 2023-02-01
 */
public class Listener {

	private static WebServiceServer webserverServer = new WebServiceServer();

	/**
	 * publish按钮监听：点击时发布webservice服务，发布成功将响应操作项disable/enable
	 * 
	 * @return
	 */
	public static MouseListener publishButtonClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				JButton publishButton = ComponentDict.lookup(WSSI18N.JButton_Publish);
				if(!publishButton.isEnabled())
					return;// disabled button still can traggle click event
				if(!webserverServer.published) {
					SwingUtilities.invokeLater(() -> {
						// 改变ws发布协议
						JComboBox<SoapProtocol> protocolCombox = ComponentDict.lookup(WSSI18N.JComboBox_Protocol);
						WebServiceServer.changeSoapProtocol((SoapProtocol) protocolCombox.getSelectedItem());
						// 改变ws的serverA
						JCheckBox enableServerACheckBox = ComponentDict.lookup(WSSI18N.JCheckBox_ServerAEnable);
						JTextField serverAMethodNameTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerAMethodName);
						WebServiceServer.changeServerAName(serverAMethodNameTextField.getText().trim());
						if(enableServerACheckBox.isSelected())
							WebServiceServer.enableServerA(false);
						else
							WebServiceServer.enableServerA(true);
						// 改变ws的serverB
						JCheckBox enableServerBCheckBox = ComponentDict.lookup(WSSI18N.JCheckBox_ServerBEnable);
						JTextField serverBMethodNameTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerBMethodName);
						WebServiceServer.changeServerBName(serverBMethodNameTextField.getText().trim());
						if(enableServerBCheckBox.isSelected())
							WebServiceServer.enableServerB(false);
						else
							WebServiceServer.enableServerB(true);
						// 改变ws的serverC
						JCheckBox enableServerCCheckBox = ComponentDict.lookup(WSSI18N.JCheckBox_ServerCEnable);
						JTextField serverCMethodNameTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerCMethodName);
						WebServiceServer.changeServerCName(serverCMethodNameTextField.getText().trim());
						if(enableServerCCheckBox.isSelected())
							WebServiceServer.enableServerC(false);
						else
							WebServiceServer.enableServerC(true);
						// 改变组件状态
						JTextField addressTextField = ComponentDict.lookup(WSSI18N.JTextField_Address);
						if(webserverServer.publish(addressTextField.getText().trim())) {
							// 服务发布成功，对应的接口高亮
							if(enableServerACheckBox.isSelected()) {
								JPanel serverAPanel = ComponentDict.lookup(WSSI18N.JPanel_ServerA);
								TitledBorder border = (TitledBorder) serverAPanel.getBorder();
								border.setTitleColor(Color.GREEN);
							}
							if(enableServerBCheckBox.isSelected()) {
								JPanel serverAPanel = ComponentDict.lookup(WSSI18N.JPanel_ServerB);
								TitledBorder border = (TitledBorder) serverAPanel.getBorder();
								border.setTitleColor(Color.GREEN);
							}
							if(enableServerCCheckBox.isSelected()) {
								JPanel serverAPanel = ComponentDict.lookup(WSSI18N.JPanel_ServerC);
								TitledBorder border = (TitledBorder) serverAPanel.getBorder();
								border.setTitleColor(Color.GREEN);
							}
							// 服务发布成功，对应的操作项目disable/enable
							ComponentDict.lookup(WSSI18N.JButton_Stop).setEnabled(true);
							ComponentDict.lookup(WSSI18N.JButton_Browse).setEnabled(true);
							ComponentDict.lookup(WSSI18N.JLabel_Address).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JTextField_Address).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JLabel_Protocol).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JComboBox_Protocol).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JButton_Publish).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JCheckBox_ServerAEnable).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JCheckBox_ServerBEnable).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JCheckBox_ServerCEnable).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JLabel_ServerAMethodName).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JTextField_ServerAMethodName).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JLabel_ServerBMethodName).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JTextField_ServerBMethodName).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JLabel_ServerCMethodName).setEnabled(false);
							ComponentDict.lookup(WSSI18N.JTextField_ServerCMethodName).setEnabled(false);
							// add ws listener
							webserverServer.addMessageListener(Listener.WebServiceServerMessageListener());
						}
					});
				}
			}
		};
	}

	/**
	 * stop按钮监听：点击时停止webservice服务，并将响应操作项disable/enable
	 * 
	 * @return
	 */
	public static MouseListener stopButtonClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				JButton stopButton = ComponentDict.lookup(WSSI18N.JButton_Stop);
				if(!stopButton.isEnabled())
					return;// disabled button still can traggle click event
				if(webserverServer.published) {
					SwingUtilities.invokeLater(() -> {
						webserverServer.stop();
						// 服务发布成功，对应的接口取消高亮
						JCheckBox enableServerACheckBox = ComponentDict.lookup(WSSI18N.JCheckBox_ServerAEnable);
						if(enableServerACheckBox.isSelected()) {
							JPanel serverAPanel = ComponentDict.lookup(WSSI18N.JPanel_ServerA);
							TitledBorder border = (TitledBorder) serverAPanel.getBorder();
							border.setTitleColor(null);
						}
						JCheckBox enableServerBCheckBox = ComponentDict.lookup(WSSI18N.JCheckBox_ServerBEnable);
						if(enableServerBCheckBox.isSelected()) {
							JPanel serverAPanel = ComponentDict.lookup(WSSI18N.JPanel_ServerB);
							TitledBorder border = (TitledBorder) serverAPanel.getBorder();
							border.setTitleColor(null);
						}
						JCheckBox enableServerCCheckBox = ComponentDict.lookup(WSSI18N.JCheckBox_ServerCEnable);
						if(enableServerCCheckBox.isSelected()) {
							JPanel serverAPanel = ComponentDict.lookup(WSSI18N.JPanel_ServerC);
							TitledBorder border = (TitledBorder) serverAPanel.getBorder();
							border.setTitleColor(null);
						}
						// 服务发布成功，对应的操作项目disable/enable
						ComponentDict.lookup(WSSI18N.JButton_Stop).setEnabled(false);
						ComponentDict.lookup(WSSI18N.JButton_Browse).setEnabled(false);
						ComponentDict.lookup(WSSI18N.JLabel_Address).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JTextField_Address).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JLabel_Protocol).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JComboBox_Protocol).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JButton_Publish).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JCheckBox_ServerAEnable).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JCheckBox_ServerBEnable).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JCheckBox_ServerCEnable).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JLabel_ServerAMethodName).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JTextField_ServerAMethodName).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JLabel_ServerBMethodName).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JTextField_ServerBMethodName).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JLabel_ServerCMethodName).setEnabled(true);
						ComponentDict.lookup(WSSI18N.JTextField_ServerCMethodName).setEnabled(true);
					});
				}
			}
		};
	}

	/**
	 * browse按钮监听：点击时打开webservice服务地址
	 * 
	 * @return
	 */
	public static MouseListener browseButtonClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				JButton browseButton = ComponentDict.lookup(WSSI18N.JButton_Browse);
				if(!browseButton.isEnabled())
					return;// disabled button still can traggle click event
				if(webserverServer.published) {
					SwingUtilities.invokeLater(() -> {
						JTextField addressTextField = ComponentDict.lookup(WSSI18N.JTextField_Address);
						try {
							String address = addressTextField.getText().trim();
							address = (address.endsWith("?wsdl") || address.endsWith("?WSDL")) ? address : address + "?wsdl";
							Desktop.getDesktop().browse(new URL(address).toURI());
						} catch(IOException | URISyntaxException e1) {
							e1.printStackTrace();
						}
					});
				}
			}
		};
	}

	/**
	 * 接口服务是否启用按钮的状态监听：选中或者取消选中时，将接口服务内容enable或者disable
	 * 
	 * @param e
	 */
	public static void enableCheckBoxItemStateChanged(ItemEvent e) {
		JTextField methodNameTextField = null;
		JTextField returnDelayTextField = null;
		JTextArea returnDataTextArea = null;
		JTextArea receivedDataTextArea = null;
		if(e.getSource() == ComponentDict.lookup(WSSI18N.JCheckBox_ServerAEnable)) {
			methodNameTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerAMethodName);
			returnDelayTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerAReturnDelay);
			returnDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerAReturnData);
			receivedDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerAReceivedData);
		} else if(e.getSource() == ComponentDict.lookup(WSSI18N.JCheckBox_ServerBEnable)) {
			methodNameTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerBMethodName);
			returnDelayTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerBReturnDelay);
			returnDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerBReturnData);
			receivedDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerBReceivedData);
		} else {
			methodNameTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerCMethodName);
			returnDelayTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerCReturnDelay);
			returnDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerCReturnData);
			receivedDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerCReceivedData);
		}
		// disable/enable
		if(e.getStateChange() == ItemEvent.DESELECTED) {
			methodNameTextField.setEnabled(false);
			returnDelayTextField.setEnabled(false);
			returnDataTextArea.setEnabled(false);
			receivedDataTextArea.setEnabled(false);
		} else {
			methodNameTextField.setEnabled(true);
			returnDelayTextField.setEnabled(true);
			returnDataTextArea.setEnabled(true);
			receivedDataTextArea.setEnabled(true);
		}
	}

	private static IMessageListener WebServiceServerMessageListener() {
		return new IMessageListener() {

			private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
			private String lineSeparator = System.getProperty("line.separator");
			private int maxShowRows = 6000;

			@Override
			public void onMessageA(String message) {
				JTextArea receivedDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerAReceivedData);
				if(receivedDataTextArea.getText().length() > maxShowRows)
					receivedDataTextArea.setText("");// 收到的数据显示超过一定行数，清空防止内存溢出
				if(!receivedDataTextArea.getText().isEmpty())
					receivedDataTextArea.append(lineSeparator);
				receivedDataTextArea.append(sdf.format(new Date()));
				receivedDataTextArea.append(lineSeparator);
				receivedDataTextArea.append(message);
				receivedDataTextArea.append(lineSeparator);
				receivedDataTextArea.setCaretPosition(receivedDataTextArea.getText().length());// 定位到最后
				JTextArea returnDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerAReturnData);
				webserverServer.setServerAReturnData(returnDataTextArea.getText());
				JTextField returnDelayTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerAReturnDelay);
				int returnDelay = 0;
				try {
					returnDelay = Integer.parseInt(returnDelayTextField.getText().trim());
				}catch(Exception e) {
					returnDelay = 0;
				}
				webserverServer.setServerAReturnDelay(returnDelay);
			}

			@Override
			public void onMessageB(String message) {
				JTextArea receivedDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerBReceivedData);
				if(receivedDataTextArea.getText().length() > maxShowRows)
					receivedDataTextArea.setText("");// 收到的数据显示超过一定行数，清空防止内存溢出
				if(!receivedDataTextArea.getText().isEmpty())
					receivedDataTextArea.append(lineSeparator);
				receivedDataTextArea.append(sdf.format(new Date()));
				receivedDataTextArea.append(lineSeparator);
				receivedDataTextArea.append(message);
				receivedDataTextArea.append(lineSeparator);
				receivedDataTextArea.setCaretPosition(receivedDataTextArea.getText().length());// 定位到最后
				JTextArea returnDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerBReturnData);
				webserverServer.setServerBReturnData(returnDataTextArea.getText());
				JTextField returnDelayTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerBReturnDelay);
				int returnDelay = 0;
				try {
					returnDelay = Integer.parseInt(returnDelayTextField.getText().trim());
				}catch(Exception e) {
					returnDelay = 0;
				}
				webserverServer.setServerBReturnDelay(returnDelay);
			}

			@Override
			public void onMessageC(String message) {
				JTextArea receivedDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerCReceivedData);
				if(receivedDataTextArea.getText().length() > maxShowRows)
					receivedDataTextArea.setText("");// 收到的数据显示超过一定行数，清空防止内存溢出
				if(!receivedDataTextArea.getText().isEmpty())
					receivedDataTextArea.append(lineSeparator);
				receivedDataTextArea.append(sdf.format(new Date()));
				receivedDataTextArea.append(lineSeparator);
				receivedDataTextArea.append(message);
				receivedDataTextArea.append(lineSeparator);
				receivedDataTextArea.setCaretPosition(receivedDataTextArea.getText().length());// 定位到最后
				JTextArea returnDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerCReturnData);
				webserverServer.setServerCReturnData(returnDataTextArea.getText());
				JTextField returnDelayTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerCReturnDelay);
				int returnDelay = 0;
				try {
					returnDelay = Integer.parseInt(returnDelayTextField.getText().trim());
				}catch(Exception e) {
					returnDelay = 0;
				}
				webserverServer.setServerCReturnDelay(returnDelay);
			}
		};
	}
}
