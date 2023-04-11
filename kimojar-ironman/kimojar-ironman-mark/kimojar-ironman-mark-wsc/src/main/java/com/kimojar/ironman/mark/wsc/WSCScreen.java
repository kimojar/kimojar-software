/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wsc
 * PACKAGE com.kimojar.ironman.mark.wsc
 * FILE WSCScreen.java
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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.kimojar.ironman.mark.wsc.WebServiceClient.SoapProtocol;
import com.kimojar.util.common.gui.ComponentDict;

/**
 * @author KiMoJar
 * @date 2023-02-03
 */
public class WSCScreen extends JPanel {

	private static final long serialVersionUID = -4284777944572081733L;

	public WSCScreen() {
		ComponentDict.record(WSCI18N.JPanel_Screen, this);
		initComponent();// 初始化组件，记录到组件字典，前后端分离
		layoutComponent();// 布局组件
	}

	private void initComponent() {
		// address
		JLabel addressLabel = new JLabel();
		ComponentDict.record(WSCI18N.JLabel_Address, addressLabel);
		addressLabel.setText(WSCI18N.JLabel_Address.i18n());
		addressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField addressTextField = new JTextField();
		ComponentDict.record(WSCI18N.JTextField_Address, addressTextField);
		addressTextField.setText(WSCI18N.JTextField_Address.i18n());
		addressTextField.setColumns(1);

		// protocol
		JLabel protocolLabel = new JLabel();
		ComponentDict.record(WSCI18N.JLabel_Protocol, protocolLabel);
		protocolLabel.setText(WSCI18N.JLabel_Protocol.i18n());
		protocolLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JComboBox<SoapProtocol> protocolComboBox = new JComboBox<>();
		ComponentDict.record(WSCI18N.JComboBox_Protocol, protocolComboBox);
		protocolComboBox.setModel(new DefaultComboBoxModel<SoapProtocol>(SoapProtocol.values()));
		protocolComboBox.setSelectedItem(SoapProtocol.Protocol1_2);

		// funcation name
		JLabel funcationNameLabel = new JLabel();
		ComponentDict.record(WSCI18N.JLabel_FunctionName, funcationNameLabel);
		funcationNameLabel.setText(WSCI18N.JLabel_FunctionName.i18n());
		funcationNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField funcationNameTextField = new JTextField();
		ComponentDict.record(WSCI18N.JTextField_FunctionName, funcationNameTextField);
		funcationNameTextField.setText(WSCI18N.JTextField_FunctionName.i18n());
		funcationNameTextField.setColumns(1);

		// function para
		JLabel funtionParaLabel = new JLabel();
		ComponentDict.record(WSCI18N.JLabel_FunctionPara, funtionParaLabel);
		funtionParaLabel.setText(WSCI18N.JLabel_FunctionPara.i18n());
		funtionParaLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField functionParaTextField = new JTextField();
		ComponentDict.record(WSCI18N.JTextField_FunctionPara, functionParaTextField);
		functionParaTextField.setText(WSCI18N.JTextField_FunctionPara.i18n());
		functionParaTextField.setColumns(1);

		// namespace
		JLabel namespaceLabel = new JLabel();
		ComponentDict.record(WSCI18N.JLabel_Namespace, namespaceLabel);
		namespaceLabel.setText(WSCI18N.JLabel_Namespace.i18n());
		namespaceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField namespaceTextField = new JTextField();
		ComponentDict.record(WSCI18N.JTextField_Namespace, namespaceTextField);
		namespaceTextField.setText(WSCI18N.JTextField_Namespace.i18n());
		namespaceTextField.setColumns(1);

		// function reponse
		JLabel funcationResponseLabel = new JLabel();
		ComponentDict.record(WSCI18N.JLabel_FunctionResponse, funcationResponseLabel);
		funcationResponseLabel.setText(WSCI18N.JLabel_FunctionResponse.i18n());
		funcationResponseLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField funcationResponseTextField = new JTextField();
		ComponentDict.record(WSCI18N.JTextField_FunctionResponse, funcationResponseTextField);
		funcationResponseTextField.setText(WSCI18N.JTextField_FunctionResponse.i18n());
		funcationResponseTextField.setColumns(1);

		// invokeTimeout
		JLabel invokeTimeoutNameLabel = new JLabel();
		ComponentDict.record(WSCI18N.JLabel_InvokeTimeout, invokeTimeoutNameLabel);
		invokeTimeoutNameLabel.setText(WSCI18N.JLabel_InvokeTimeout.i18n());
		invokeTimeoutNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField invokeTimeoutTextField = new JTextField();
		ComponentDict.record(WSCI18N.JTextField_InvokeTimeout, invokeTimeoutTextField);
		invokeTimeoutTextField.setText(WSCI18N.JTextField_InvokeTimeout.i18n());
		invokeTimeoutTextField.setColumns(1);

		// soap connection
		JLabel connectionTypeLabel = new JLabel();
		ComponentDict.record(WSCI18N.JLabel_ConnectionType, connectionTypeLabel);
		connectionTypeLabel.setText(WSCI18N.JLabel_ConnectionType.i18n());
		connectionTypeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JRadioButton soapConnectionRadioButton = new JRadioButton();
		ComponentDict.record(WSCI18N.JRadioButton_SoapConnection, soapConnectionRadioButton);
		soapConnectionRadioButton.setText(WSCI18N.JRadioButton_SoapConnection.i18n());
		soapConnectionRadioButton.setSelected(true);

		// http connection
		JRadioButton httpConnectionRadioButton = new JRadioButton();
		ComponentDict.record(WSCI18N.JRadioButton_HttpConnection, httpConnectionRadioButton);
		httpConnectionRadioButton.setText(WSCI18N.JRadioButton_HttpConnection.i18n());
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(soapConnectionRadioButton);
		buttonGroup.add(httpConnectionRadioButton);

		// invoke
		JButton invokeButton = new JButton();
		ComponentDict.record(WSCI18N.JButton_Invoke, invokeButton);
		invokeButton.setText(WSCI18N.JButton_Invoke.i18n());
		invokeButton.addMouseListener(Listener.invokeButtonMouseListener());

		// invoke more
		JButton invokeMoreButton = new JButton();
		ComponentDict.record(WSCI18N.JButton_InvokeMore, invokeMoreButton);
		invokeMoreButton.setText(WSCI18N.JButton_InvokeMore.i18n());
		invokeMoreButton.addMouseListener(Listener.invokeMoreButtonMouseListener());

		// requestData
		JLabel requestDataLabel = new JLabel();
		ComponentDict.record(WSCI18N.JLabel_RequestData, requestDataLabel);
		requestDataLabel.setText(WSCI18N.JLabel_RequestData.i18n());
		requestDataLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextArea requestDataTextArea = new JTextArea();
		ComponentDict.record(WSCI18N.JTextArea_RequestData, requestDataTextArea);
		requestDataTextArea.setText(WSCI18N.JTextArea_RequestData.i18n());
		requestDataTextArea.setColumns(1);
		requestDataTextArea.setRows(1);

		// responseData
		JLabel responseDataLabel = new JLabel();
		ComponentDict.record(WSCI18N.JLabel_ResponseData, responseDataLabel);
		responseDataLabel.setText(WSCI18N.JLabel_ResponseData.i18n());
		responseDataLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JTextArea responseDataTextArea = new JTextArea();
		ComponentDict.record(WSCI18N.JTextArea_ResponseData, responseDataTextArea);
		responseDataTextArea.setText(WSCI18N.JTextArea_ResponseData.i18n());
		responseDataTextArea.setColumns(1);
		responseDataTextArea.setRows(1);
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
		int weightxB = 1;

		// address
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JLabel_Address)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxA;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JTextField_Address)), gbc);

		// protocol
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JLabel_Protocol)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxB;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JComboBox_Protocol)), gbc);

		// function names
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JLabel_FunctionName)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxA;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JTextField_FunctionName)), gbc);

		// function para
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JLabel_FunctionPara)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxB;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JTextField_FunctionPara)), gbc);

		// namespace
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JLabel_Namespace)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxA;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JTextField_Namespace)), gbc);

		// funcation response
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JLabel_FunctionResponse)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxB;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JTextField_FunctionResponse)), gbc);

		// invoke timeout
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JLabel_InvokeTimeout)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxA;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JTextField_InvokeTimeout)), gbc);

		// soap/http connection
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(northPanel.add(ComponentDict.lookup(WSCI18N.JLabel_ConnectionType)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxB;
		JPanel radioButtonPanel = new JPanel();
		radioButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		radioButtonPanel.add(ComponentDict.lookup(WSCI18N.JRadioButton_SoapConnection));
		radioButtonPanel.add(ComponentDict.lookup(WSCI18N.JRadioButton_HttpConnection));
		gbl.setConstraints(northPanel.add(radioButtonPanel), gbc);

		// invoke
		gbc.gridx = (columnIndex = labelWidth + valueWidth + labelWidth);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxB;
		JPanel operatePanel = new JPanel();
		operatePanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		operatePanel.add(ComponentDict.lookup(WSCI18N.JButton_Invoke));
		operatePanel.add(ComponentDict.lookup(WSCI18N.JButton_InvokeMore));
		gbl.setConstraints(northPanel.add(operatePanel), gbc);

		return northPanel;
	}

	private JPanel layoutCentralPanel() {
		JPanel centralPanel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, 10, 1, new Insets(5, 5, 5, 5), 0, 0);
		centralPanel.setLayout(gbl);
		int rowIndex = 0;
		int columnIndex = 0;
		int labelWidth = 1;
		int valueWidth = 3;
		int weightxA = 1;
		int weightyA = 1;
		int weightyB = 3;

		// requestData
		gbc.gridx = columnIndex;
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(centralPanel.add(ComponentDict.lookup(WSCI18N.JLabel_RequestData)), gbc);
		gbc.gridx = columnIndex;
		gbc.gridy = rowIndex++;
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxA;
		gbc.weighty = weightyA;
		JScrollPane requestScrollPane = new JScrollPane();
		requestScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		requestScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		requestScrollPane.setViewportView(ComponentDict.lookup(WSCI18N.JTextArea_RequestData));
		gbl.setConstraints(centralPanel.add(requestScrollPane), gbc);

		// responseData
		gbc.gridx = columnIndex;
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbl.setConstraints(centralPanel.add(ComponentDict.lookup(WSCI18N.JLabel_ResponseData)), gbc);
		gbc.gridx = columnIndex;
		gbc.gridy = rowIndex++;
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxA;
		gbc.weighty = weightyB;
		JScrollPane responseScrollPane = new JScrollPane();
		responseScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		responseScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		responseScrollPane.setViewportView(ComponentDict.lookup(WSCI18N.JTextArea_ResponseData));
		gbl.setConstraints(centralPanel.add(responseScrollPane), gbc);

		return centralPanel;
	}
}
