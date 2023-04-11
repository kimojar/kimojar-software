/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wss
 * PACKAGE com.kimojar.ironman.mark.wss
 * FILE WSSScreen.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-01-30
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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.kimojar.ironman.mark.wss.WebServiceServer.SoapProtocol;
import com.kimojar.util.common.gui.ComponentDict;

/**
 * @author KiMoJar
 * @date 2023-01-30
 */
public class WSSScreen extends JPanel {

	private static final long serialVersionUID = 6870009878806967017L;

	public WSSScreen() {
		ComponentDict.record(WSSI18N.JPanel_Screen, this);
		initComponent();// 初始化组件，记录到组件字典，前后端分离
		layoutComponent();// 布局组件
	}

	private void initComponent() {
		// Address
		JLabel addressLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_Address, addressLabel);
		addressLabel.setText(WSSI18N.JLabel_Address.i18n());
		addressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField addressTextField = new JTextField();
		ComponentDict.record(WSSI18N.JTextField_Address, addressTextField);
		addressTextField.setText(WSSI18N.JTextField_Address.i18n());
		addressTextField.setColumns(1);

		// Protocol
		JLabel protocolLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_Protocol, protocolLabel);
		protocolLabel.setText(WSSI18N.JLabel_Protocol.i18n());
		protocolLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JComboBox<SoapProtocol> protocolComboBox = new JComboBox<>();
		ComponentDict.record(WSSI18N.JComboBox_Protocol, protocolComboBox);
		protocolComboBox.setModel(new DefaultComboBoxModel<SoapProtocol>(SoapProtocol.values()));
		protocolComboBox.setSelectedItem(SoapProtocol.Protocol1_2);

		// Namespace
		JLabel namespaceLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_Namespace, namespaceLabel);
		namespaceLabel.setEnabled(false);
		namespaceLabel.setText(WSSI18N.JLabel_Namespace.i18n());
		namespaceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField namespaceTextField = new JTextField();
		ComponentDict.record(WSSI18N.JTextField_Namespace, namespaceTextField);
		namespaceTextField.setEnabled(false);
		namespaceTextField.setText(WSSI18N.JTextField_Namespace.i18n());
		namespaceTextField.setColumns(1);

		// ReturnName
		JLabel returnNameLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ReturnName, returnNameLabel);
		returnNameLabel.setEnabled(false);
		returnNameLabel.setText(WSSI18N.JLabel_ReturnName.i18n());
		returnNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField returnNameTextField = new JTextField();
		ComponentDict.record(WSSI18N.JTextField_ReturnName, returnNameTextField);
		returnNameTextField.setEnabled(false);
		returnNameTextField.setText(WSSI18N.JTextField_ReturnName.i18n());
		returnNameTextField.setColumns(1);

		// Publish
		JButton publishButton = new JButton();
		ComponentDict.record(WSSI18N.JButton_Publish, publishButton);
		publishButton.setText(WSSI18N.JButton_Publish.i18n());
		publishButton.addMouseListener(Listener.publishButtonClicked());

		// Stop
		JButton stopButton = new JButton();
		ComponentDict.record(WSSI18N.JButton_Stop, stopButton);
		stopButton.setText(WSSI18N.JButton_Stop.i18n());
		stopButton.setEnabled(false);
		stopButton.addMouseListener(Listener.stopButtonClicked());

		// Browse
		JButton browseButton = new JButton();
		ComponentDict.record(WSSI18N.JButton_Browse, browseButton);
		browseButton.setText(WSSI18N.JButton_Browse.i18n());
		browseButton.setEnabled(false);
		browseButton.addMouseListener(Listener.browseButtonClicked());

		// ServerA Enable
		JCheckBox serverAEnableCheckBox = new JCheckBox();
		ComponentDict.record(WSSI18N.JCheckBox_ServerAEnable, serverAEnableCheckBox);
		serverAEnableCheckBox.setSelected(true);
		serverAEnableCheckBox.setText(WSSI18N.JCheckBox_ServerAEnable.i18n());
		serverAEnableCheckBox.addItemListener(e -> Listener.enableCheckBoxItemStateChanged(e));

		// ServerA MethodName
		JLabel serverAMethodNameLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerAMethodName, serverAMethodNameLabel);
		serverAMethodNameLabel.setText(WSSI18N.JLabel_ServerAMethodName.i18n());
		serverAMethodNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextField serverAMethodNameTextField = new JTextField();
		ComponentDict.record(WSSI18N.JTextField_ServerAMethodName, serverAMethodNameTextField);
		serverAMethodNameTextField.setText(WSSI18N.JTextField_ServerAMethodName.i18n());
		serverAMethodNameTextField.setColumns(1);

		// ServerA ReturnDelay
		JLabel serverAReturnDelayLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerAReturnDelay, serverAReturnDelayLabel);
		serverAReturnDelayLabel.setText(WSSI18N.JLabel_ServerAReturnDelay.i18n());
		serverAReturnDelayLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextField serverAReturnDelayTextField = new JTextField();
		ComponentDict.record(WSSI18N.JTextField_ServerAReturnDelay, serverAReturnDelayTextField);
		serverAReturnDelayTextField.setText(WSSI18N.JTextField_ServerAReturnDelay.i18n());
		serverAReturnDelayTextField.setColumns(1);

		// ServerA ReturnData
		JLabel serverAReturnDataLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerAReturnData, serverAReturnDataLabel);
		serverAReturnDataLabel.setText(WSSI18N.JLabel_ServerAReturnData.i18n());
		serverAReturnDataLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextArea serverAReturnDataTextField = new JTextArea();
		ComponentDict.record(WSSI18N.JTextArea_ServerAReturnData, serverAReturnDataTextField);
		serverAReturnDataTextField.setText(WSSI18N.JTextArea_ServerAReturnData.i18n());
		serverAReturnDataTextField.setColumns(1);
		serverAReturnDataTextField.setRows(5);

		// ServerA ReceivedData
		JLabel serverAReceivedDataLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerAReceivedData, serverAReceivedDataLabel);
		serverAReceivedDataLabel.setText(WSSI18N.JLabel_ServerAReceivedData.i18n());
		serverAReceivedDataLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextArea serverAReceivedDataTextField = new JTextArea();
		ComponentDict.record(WSSI18N.JTextArea_ServerAReceivedData, serverAReceivedDataTextField);
		serverAReceivedDataTextField.setText(WSSI18N.JTextArea_ServerAReceivedData.i18n());
		serverAReceivedDataTextField.setColumns(1);
		serverAReceivedDataTextField.setRows(5);

		// ServerB Enable
		JCheckBox serverBEnableCheckBox = new JCheckBox();
		ComponentDict.record(WSSI18N.JCheckBox_ServerBEnable, serverBEnableCheckBox);
		serverBEnableCheckBox.setSelected(true);
		serverBEnableCheckBox.setText(WSSI18N.JCheckBox_ServerBEnable.i18n());
		serverBEnableCheckBox.addItemListener(e -> Listener.enableCheckBoxItemStateChanged(e));

		// ServerB MethodName
		JLabel serverBMethodNameLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerBMethodName, serverBMethodNameLabel);
		serverBMethodNameLabel.setText(WSSI18N.JLabel_ServerBMethodName.i18n());
		serverBMethodNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextField serverBMethodNameTextField = new JTextField();
		ComponentDict.record(WSSI18N.JTextField_ServerBMethodName, serverBMethodNameTextField);
		serverBMethodNameTextField.setText(WSSI18N.JTextField_ServerBMethodName.i18n());
		serverBMethodNameTextField.setColumns(1);

		// ServerB ReturnDelay
		JLabel serverBReturnDelayLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerBReturnDelay, serverBReturnDelayLabel);
		serverBReturnDelayLabel.setText(WSSI18N.JLabel_ServerBReturnDelay.i18n());
		serverBReturnDelayLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextField serverBReturnDelayTextField = new JTextField();
		ComponentDict.record(WSSI18N.JTextField_ServerBReturnDelay, serverBReturnDelayTextField);
		serverBReturnDelayTextField.setText(WSSI18N.JTextField_ServerBReturnDelay.i18n());
		serverBReturnDelayTextField.setColumns(1);

		// ServerB ReturnData
		JLabel serverBReturnDataLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerBReturnData, serverBReturnDataLabel);
		serverBReturnDataLabel.setText(WSSI18N.JLabel_ServerBReturnData.i18n());
		serverBReturnDataLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextArea serverBReturnDataTextField = new JTextArea();
		ComponentDict.record(WSSI18N.JTextArea_ServerBReturnData, serverBReturnDataTextField);
		serverBReturnDataTextField.setText(WSSI18N.JTextArea_ServerBReturnData.i18n());
		serverBReturnDataTextField.setColumns(1);
		serverBReturnDataTextField.setRows(5);

		// ServerB ReceivedData
		JLabel serverBReceivedDataLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerBReceivedData, serverBReceivedDataLabel);
		serverBReceivedDataLabel.setText(WSSI18N.JLabel_ServerBReceivedData.i18n());
		serverBReceivedDataLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextArea serverBReceivedDataTextField = new JTextArea();
		ComponentDict.record(WSSI18N.JTextArea_ServerBReceivedData, serverBReceivedDataTextField);
		serverBReceivedDataTextField.setText(WSSI18N.JTextArea_ServerBReceivedData.i18n());
		serverBReceivedDataTextField.setColumns(1);
		serverBReceivedDataTextField.setRows(5);

		// ServerC Enable
		JCheckBox serverCEnableCheckBox = new JCheckBox();
		ComponentDict.record(WSSI18N.JCheckBox_ServerCEnable, serverCEnableCheckBox);
		serverCEnableCheckBox.setSelected(true);
		serverCEnableCheckBox.setText(WSSI18N.JCheckBox_ServerCEnable.i18n());
		serverCEnableCheckBox.addItemListener(e -> Listener.enableCheckBoxItemStateChanged(e));

		// ServerC MethodName
		JLabel serverCMethodNameLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerCMethodName, serverCMethodNameLabel);
		serverCMethodNameLabel.setText(WSSI18N.JLabel_ServerCMethodName.i18n());
		serverCMethodNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextField serverCMethodNameTextField = new JTextField();
		ComponentDict.record(WSSI18N.JTextField_ServerCMethodName, serverCMethodNameTextField);
		serverCMethodNameTextField.setText(WSSI18N.JTextField_ServerCMethodName.i18n());
		serverCMethodNameTextField.setColumns(1);

		// ServerC ReturnDelay
		JLabel serverCReturnDelayLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerCReturnDelay, serverCReturnDelayLabel);
		serverCReturnDelayLabel.setText(WSSI18N.JLabel_ServerCReturnDelay.i18n());
		serverCReturnDelayLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextField serverCReturnDelayTextField = new JTextField();
		ComponentDict.record(WSSI18N.JTextField_ServerCReturnDelay, serverCReturnDelayTextField);
		serverCReturnDelayTextField.setText(WSSI18N.JTextField_ServerCReturnDelay.i18n());
		serverCReturnDelayTextField.setColumns(1);

		// ServerC ReturnData
		JLabel serverCReturnDataLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerCReturnData, serverCReturnDataLabel);
		serverCReturnDataLabel.setText(WSSI18N.JLabel_ServerCReturnData.i18n());
		serverCReturnDataLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextArea serverCReturnDataTextField = new JTextArea();
		ComponentDict.record(WSSI18N.JTextArea_ServerCReturnData, serverCReturnDataTextField);
		serverCReturnDataTextField.setText(WSSI18N.JTextArea_ServerCReturnData.i18n());
		serverCReturnDataTextField.setColumns(1);
		serverCReturnDataTextField.setRows(5);

		// ServerC ReceivedData
		JLabel serverCReceivedDataLabel = new JLabel();
		ComponentDict.record(WSSI18N.JLabel_ServerCReceivedData, serverCReceivedDataLabel);
		serverCReceivedDataLabel.setText(WSSI18N.JLabel_ServerCReceivedData.i18n());
		serverCReceivedDataLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextArea serverCReceivedDataTextField = new JTextArea();
		ComponentDict.record(WSSI18N.JTextArea_ServerCReceivedData, serverCReceivedDataTextField);
		serverCReceivedDataTextField.setText(WSSI18N.JTextArea_ServerCReceivedData.i18n());
		serverCReceivedDataTextField.setColumns(1);
		serverCReceivedDataTextField.setRows(5);

	}

	private void layoutComponent() {
		setLayout(new BorderLayout());
		add(layoutNorthPanel(), BorderLayout.NORTH);
		add(layoutCentralPanel(), BorderLayout.CENTER);
	}

	private JPanel layoutNorthPanel() {
		JPanel northPanel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, 10, 1, new Insets(5, 5, 5, 5), 0, 0);
		northPanel.setLayout(gbl);
		int rowIndex = 0;
		int columnIndex = 0;
		int labelWidth = 1;
		int valueWidth = 3;
		int weightxA = 5;
		int weightxB = 0;

		// Address
		gbc.gridx = columnIndex;
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSSI18N.JLabel_Address)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxA;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSSI18N.JTextField_Address)), gbc);

		// Protocol
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSSI18N.JLabel_Protocol)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxB;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSSI18N.JComboBox_Protocol)), gbc);

		// // Namespace
		// gbc.gridx = (columnIndex = 0);
		// gbc.gridy = rowIndex++;
		// gbc.gridwidth = labelWidth;
		// gbc.weightx = 0;
		// gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSSI18N.JLabel_Namespace)), gbc);
		// gbc.gridx = (columnIndex += gbc.gridwidth);
		// gbc.gridwidth = valueWidth;
		// gbc.weightx = weightxA;
		// gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSSI18N.JTextField_Namespace)), gbc);
		//
		// // ReturnName
		// gbc.gridx = (columnIndex += gbc.gridwidth);
		// gbc.gridwidth = labelWidth;
		// gbc.weightx = 0;
		// gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSSI18N.JLabel_ReturnName)), gbc);
		// gbc.gridx = (columnIndex += gbc.gridwidth);
		// gbc.gridwidth = valueWidth;
		// gbc.weightx = weightxB;
		// gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSSI18N.JTextField_ReturnName)), gbc);

		// Publish Stop Browse
		JPanel operatePanel = new JPanel();
		gbc.gridx = (columnIndex = labelWidth + valueWidth + labelWidth);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxB;
		gbl.setConstraints(northPanel.add(operatePanel), gbc);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.TRAILING);
		operatePanel.setLayout(flowLayout);
		// Publish
		operatePanel.add(ComponentDict.lookup(WSSI18N.JButton_Publish));
		// Stop
		operatePanel.add(ComponentDict.lookup(WSSI18N.JButton_Stop));
		// Browse
		operatePanel.add(ComponentDict.lookup(WSSI18N.JButton_Browse));

		return northPanel;
	}

	private JPanel layoutCentralPanel() {
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		JSplitPane splitPane = new JSplitPane();
		// splitPane.setDividerLocation(0.6666d);
		// setDividerLocation(double)这个函数会用到getWidth()或者getHeight()这样的函数，而java桌面程序在没有主窗体setVisible之前，如果使用布局，尚未validate()和paint()每个组件的宽和高默认都是0。也就是说一定要在主窗体setVisible(true)之后再使用setDividerLocation(double)才会有效。
		centralPanel.add(splitPane, BorderLayout.CENTER);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		// splitPane.setUI(new MiddleSplitPaneUI());// MiddleSplitPaneUI不使用FlatLaf时是有效的，但使用FlatLaf时可能会被该主题更改掉

		JSplitPane splitPane2 = new JSplitPane();
		splitPane.setRightComponent(splitPane2);
		splitPane2.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane2.setUI(new MiddleSplitPaneUI());
		splitPane2.setOneTouchExpandable(true);

		splitPane.setLeftComponent(layoutServerPanel("A"));
		splitPane2.setLeftComponent(layoutServerPanel("B"));
		splitPane2.setRightComponent(layoutServerPanel("C"));

		return centralPanel;
	}

	private JPanel layoutServerPanel(String abc) {
		JPanel serverPanel = new JPanel();
		JCheckBox enableCheckBox = null;
		JLabel methodNameLabel = null;
		JTextField methodNameTextField = null;
		JLabel returnDelayLabel = null;
		JTextField returnDelayTextField = null;
		JLabel returnDataLabel = null;
		JTextArea returnDataTextArea = null;
		JLabel receivedDataLabel = null;
		JTextArea receivedDataTextArea = null;
		TitledBorder titledBorder = null;
		switch(abc){
			case "A":
				ComponentDict.record(WSSI18N.JPanel_ServerA, serverPanel);
				enableCheckBox = ComponentDict.lookup(WSSI18N.JCheckBox_ServerAEnable);
				methodNameLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerAMethodName);
				methodNameTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerAMethodName);
				returnDelayLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerAReturnDelay);
				returnDelayTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerAReturnDelay);
				returnDataLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerAReturnData);
				returnDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerAReturnData);
				receivedDataLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerAReceivedData);
				receivedDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerAReceivedData);
				TitledBorder serverABorder = new TitledBorder(new LineBorder(null), WSSI18N.TitledBorder_ServerA.i18n(), TitledBorder.LEADING, TitledBorder.TOP);
				ComponentDict.recordTitledBorder(WSSI18N.TitledBorder_ServerA, serverABorder);
				titledBorder = serverABorder;
				break;
			case "B":
				ComponentDict.record(WSSI18N.JPanel_ServerB, serverPanel);
				enableCheckBox = ComponentDict.lookup(WSSI18N.JCheckBox_ServerBEnable);
				methodNameLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerBMethodName);
				methodNameTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerBMethodName);
				returnDelayLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerBReturnDelay);
				returnDelayTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerBReturnDelay);
				returnDataLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerBReturnData);
				returnDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerBReturnData);
				receivedDataLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerBReceivedData);
				receivedDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerBReceivedData);
				TitledBorder serverBBorder = new TitledBorder(new LineBorder(null), WSSI18N.TitledBorder_ServerB.i18n(), TitledBorder.LEADING, TitledBorder.TOP);
				ComponentDict.recordTitledBorder(WSSI18N.TitledBorder_ServerB, serverBBorder);
				titledBorder = serverBBorder;
				break;
			case "C":
				ComponentDict.record(WSSI18N.JPanel_ServerC, serverPanel);
				enableCheckBox = ComponentDict.lookup(WSSI18N.JCheckBox_ServerCEnable);
				methodNameLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerCMethodName);
				methodNameTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerCMethodName);
				returnDelayLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerCReturnDelay);
				returnDelayTextField = ComponentDict.lookup(WSSI18N.JTextField_ServerCReturnDelay);
				returnDataLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerCReturnData);
				returnDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerCReturnData);
				receivedDataLabel = ComponentDict.lookup(WSSI18N.JLabel_ServerCReceivedData);
				receivedDataTextArea = ComponentDict.lookup(WSSI18N.JTextArea_ServerCReceivedData);
				TitledBorder serverCBorder = new TitledBorder(new LineBorder(null), WSSI18N.TitledBorder_ServerC.i18n(), TitledBorder.LEADING, TitledBorder.TOP);
				ComponentDict.recordTitledBorder(WSSI18N.TitledBorder_ServerC, serverCBorder);
				titledBorder = serverCBorder;
				break;
		}

		serverPanel.setBorder(titledBorder);
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, 10, 1, new Insets(5, 5, 5, 5), 0, 0);
		serverPanel.setLayout(gbl);
		int rowIndex = 0;
		int columnIndex = 0;
		int labelWidth = 1;
		int valueWidth = 3;

		// Enable
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(serverPanel.add(enableCheckBox), gbc);

		// MethodName
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(serverPanel.add(methodNameLabel), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = 1;
		gbl.setConstraints(serverPanel.add(methodNameTextField), gbc);

		// ReturnData
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(serverPanel.add(returnDataLabel), gbc);
		// ReturnDelay
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(serverPanel.add(returnDelayLabel), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = 0;
		gbl.setConstraints(serverPanel.add(returnDelayTextField), gbc);
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = (labelWidth + valueWidth) * 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		JScrollPane returnDataScrollPane = new JScrollPane();
		returnDataScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		returnDataScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		returnDataScrollPane.setViewportView(returnDataTextArea);
		gbl.setConstraints(serverPanel.add(returnDataScrollPane), gbc);

		// ReceivedData
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbl.setConstraints(serverPanel.add(receivedDataLabel), gbc);
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = (labelWidth + valueWidth) * 2;
		gbc.weightx = 1;
		gbc.weighty = 3;
		JScrollPane receivedDataScrollPane = new JScrollPane();
		receivedDataScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		receivedDataScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		receivedDataScrollPane.setViewportView(receivedDataTextArea);
		gbl.setConstraints(serverPanel.add(receivedDataScrollPane), gbc);

		return serverPanel;
	}

}
