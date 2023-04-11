/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wsc
 * PACKAGE com.kimojar.ironman.mark.wsc
 * FILE Listener.java
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.kimojar.ironman.mark.wsc.WebServiceClient.InvokeResult;
import com.kimojar.ironman.mark.wsc.WebServiceClient.SoapProtocol;
import com.kimojar.util.common.gui.ComponentDict;

/**
 * @author KiMoJar
 * @date 2023-02-03
 */
public class Listener {

	public static MouseAdapter invokeButtonMouseListener() {
		return new MouseAdapter() {

			WebServiceClient webserviceClient = new WebServiceClient();
			private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
			private String lineSeparator = System.getProperty("line.separator");
			private int maxShowRows = 6000;

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				JTextField addressTextField = ComponentDict.lookup(WSCI18N.JTextField_Address);
				JComboBox<SoapProtocol> protocolCombox = ComponentDict.lookup(WSCI18N.JComboBox_Protocol);
				JTextField functionNameTextField = ComponentDict.lookup(WSCI18N.JTextField_FunctionName);
				JTextField functionParaTextField = ComponentDict.lookup(WSCI18N.JTextField_FunctionPara);
				JTextField namespaceTextField = ComponentDict.lookup(WSCI18N.JTextField_Namespace);
				JTextField functionResponseTextField = ComponentDict.lookup(WSCI18N.JTextField_FunctionResponse);
				JTextField invokeTimeoutTextField = ComponentDict.lookup(WSCI18N.JTextField_InvokeTimeout);
				JTextArea requestDataTextArea = ComponentDict.lookup(WSCI18N.JTextArea_RequestData);
				JTextArea responseDataTextArea = ComponentDict.lookup(WSCI18N.JTextArea_ResponseData);
				JRadioButton soapConnectionRadioButton = ComponentDict.lookup(WSCI18N.JRadioButton_SoapConnection);
				JRadioButton httpConnectionRadioButton = ComponentDict.lookup(WSCI18N.JRadioButton_HttpConnection);
				try {
					Integer.parseInt(invokeTimeoutTextField.getText().trim());
				} catch(NumberFormatException nfe) {
					invokeTimeoutTextField.setText("5000");
				}
				webserviceClient.setInvokeAddress(addressTextField.getText().trim());
				webserviceClient.setInvokeSoapProtocol((SoapProtocol) protocolCombox.getSelectedItem());
				webserviceClient.setInvokeFunctionName(functionNameTextField.getText().trim());
				webserviceClient.setInvokeFunctionPara(functionParaTextField.getText().trim());
				webserviceClient.setInvokeNamespace(namespaceTextField.getText().trim());
				webserviceClient.setInvokeFunctionResponse(functionResponseTextField.getText().trim());
				webserviceClient.setInvokeTimeout(Integer.parseInt(invokeTimeoutTextField.getText().trim()));
				webserviceClient.setInvokeRequestData(requestDataTextArea.getText().trim());
				InvokeResult result = null;
				if(soapConnectionRadioButton.isSelected())
					result = webserviceClient.invokeBySoapConnection();
				else if(httpConnectionRadioButton.isSelected())
					result = webserviceClient.invokeByHttpConnection();
				if(result.isInvokeSuccess) {
					if(responseDataTextArea.getText().length() > maxShowRows)
						responseDataTextArea.setText("");// 收到的数据显示超过一定行数，清空防止内存溢出
					if(!responseDataTextArea.getText().isEmpty())
						responseDataTextArea.append(lineSeparator);
					responseDataTextArea.append(sdf.format(new Date()));
					responseDataTextArea.append(lineSeparator);
					responseDataTextArea.append(result.successReturn);
					responseDataTextArea.append(lineSeparator);
					responseDataTextArea.setCaretPosition(responseDataTextArea.getText().length());// 定位到最后
					Object[] messages = new Object[] {
					WSCI18N.Text_InvokeWebServiceServerSuccess.i18n(),
					WSCI18N.Text_InvokeCost.i18n() + result.invokeCost + "ms",
					};
					JOptionPane.showMessageDialog(null, messages, ((JButton) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
				} else {
					Object[] messages = new Object[] {
					WSCI18N.Text_InvokeWebServiceServerFailed.i18n(),
					WSCI18N.Text_InvokeWebServiceServerFailedCode.i18n() + result.failedCode,
					WSCI18N.Text_InvokeWebServiceServerFailedReason.i18n() + result.failedReason,
					};
					JOptionPane.showMessageDialog(null, messages, ((JButton) e.getComponent()).getText(), JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	public static MouseAdapter invokeMoreButtonMouseListener() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				JOptionPane.showMessageDialog(null, WSCI18N.Text_ToBeContinue.i18n(), ((JButton) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
			}
		};
	}

}
