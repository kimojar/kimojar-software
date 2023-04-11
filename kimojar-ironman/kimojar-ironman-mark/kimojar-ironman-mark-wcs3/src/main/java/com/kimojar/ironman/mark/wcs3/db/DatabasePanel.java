/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.db
 * FILE DatabasePanel.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-03-06
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
package com.kimojar.ironman.mark.wcs3.db;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;

import com.kimojar.ironman.mark.wcs3.WCS3I18N;
import com.kimojar.ironman.mark.wcs3.db.DBConnection.ConnectResult;
import com.kimojar.util.common.comparator.Comparators;
import com.kimojar.util.common.file.FileHelper;
import com.kimojar.util.common.gui.ComponentDict;

/**
 * @author KiMoJar
 * @date 2023-03-06
 */
public class DatabasePanel extends JPanel {

	private static final long serialVersionUID = 3192469434275713128L;

	private static final DatabasePanel INSTANCE = new DatabasePanel();

	public static DatabasePanel instance() {
		return INSTANCE;
	}

	private DatabasePanel() {
		DriverLoader.loadDefaultJDBCDriver();
		initComponent();
		layoutComponent();
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		// database connection name
		JLabel databaseConnetionNameLabel = new JLabel();
		ComponentDict.record(WCS3I18N.JLabel_DBConnectionName, databaseConnetionNameLabel);
		databaseConnetionNameLabel.setText(WCS3I18N.JLabel_DBConnectionName.i18n());
		databaseConnetionNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField databaseConnectionNameField = new JTextField();
		ComponentDict.record(WCS3I18N.JTextField_DBConnectionName, databaseConnectionNameField);

		// database type
		JLabel databaseTypeLabel = new JLabel();
		ComponentDict.record(WCS3I18N.JLabel_DBType, databaseTypeLabel);
		databaseTypeLabel.setText(WCS3I18N.JLabel_DBType.i18n());
		databaseTypeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JComboBox<DatabaseType> databaseTypeCombox = new JComboBox<>();
		ComponentDict.record(WCS3I18N.JComboBox_DBType, databaseTypeCombox);
		databaseTypeCombox.setModel(databaseTypeComboBoxModel());
		databaseTypeCombox.setRenderer(databaseTypeListCellRender());
		databaseTypeCombox.setSelectedItem(null);
		databaseTypeCombox.addItemListener(databaseTypeItemListener());

		// database driver
		JLabel databaseDriverLabel = new JLabel();
		ComponentDict.record(WCS3I18N.JLabel_DBDriver, databaseDriverLabel);
		databaseDriverLabel.setText(WCS3I18N.JLabel_DBDriver.i18n());
		databaseDriverLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JComboBox<String> databaseDriverCombox = new JComboBox<>();
		ComponentDict.record(WCS3I18N.JComboBox_DBDriver, databaseDriverCombox);
		databaseDriverCombox.setSelectedItem(null);
		databaseDriverCombox.setEnabled(false);

		// database address
		JLabel databaseAddressLabel = new JLabel();
		ComponentDict.record(WCS3I18N.JLabel_DBAddress, databaseAddressLabel);
		databaseAddressLabel.setText(WCS3I18N.JLabel_DBAddress.i18n());
		databaseAddressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		databaseAddressLabel.addMouseListener(databaseAddressLabelMouseListener());
		JTextField databaseAddressField = new JTextField();
		ComponentDict.record(WCS3I18N.JTextField_DBAddress, databaseAddressField);
		databaseAddressField.setColumns(15);

		// database port
		JLabel databasePortLabel = new JLabel();
		ComponentDict.record(WCS3I18N.JLabel_DBPort, databasePortLabel);
		databasePortLabel.setText(WCS3I18N.JLabel_DBPort.i18n());
		databasePortLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField databasePortField = new JTextField();
		ComponentDict.record(WCS3I18N.JTextField_DBPort, databasePortField);

		// database name
		JLabel databaseNameLabel = new JLabel();
		ComponentDict.record(WCS3I18N.JLabel_DBName, databaseNameLabel);
		databaseNameLabel.setText(WCS3I18N.JLabel_DBName.i18n());
		databaseNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField databaseNameField = new JTextField();
		ComponentDict.record(WCS3I18N.JTextField_DBName, databaseNameField);

		// database user
		JLabel databaseUserLabel = new JLabel();
		ComponentDict.record(WCS3I18N.JLabel_DBUser, databaseUserLabel);
		databaseUserLabel.setText(WCS3I18N.JLabel_DBUser.i18n());
		databaseUserLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField databaseUserField = new JTextField();
		ComponentDict.record(WCS3I18N.JTextField_DBUser, databaseUserField);

		// database pass
		JLabel databasePassLabel = new JLabel();
		ComponentDict.record(WCS3I18N.JLabel_DBPass, databasePassLabel);
		databasePassLabel.setText(WCS3I18N.JLabel_DBPass.i18n());
		databasePassLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField databasePassField = new JTextField();
		ComponentDict.record(WCS3I18N.JTextField_DBPass, databasePassField);

		// ping
		JButton pingButton = new JButton();
		ComponentDict.record(WCS3I18N.JButton_DBPing, pingButton);
		pingButton.setText(WCS3I18N.JButton_DBPing.i18n());

		// connect
		JButton connectButton = new JButton();
		ComponentDict.record(WCS3I18N.JButton_DBConnect, connectButton);
		connectButton.setText(WCS3I18N.JButton_DBConnect.i18n());
		connectButton.addMouseListener(databaseConnectButtonMouseListener());

		// disconnect
		JButton disconnectButton = new JButton();
		ComponentDict.record(WCS3I18N.JButton_DBDisconnect, disconnectButton);
		disconnectButton.setText(WCS3I18N.JButton_DBDisconnect.i18n());
		disconnectButton.addMouseListener(databaseDisConnectButtonMouseListener());
	}

	/**
	 * 布局组件
	 */
	private void layoutComponent() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, 10, 1, new Insets(5, 5, 5, 5), 0, 0);
		this.setLayout(gbl);
		int rowIndex = 0;
		int columnIndex = 0;
		int labelWidth = 1;
		int valueWidth = 3;
		int weightxA = 2;
		int weightxB = 1;

		// data source name
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JLabel_DBConnectionName)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxB;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JTextField_DBConnectionName)), gbc);

		// database type
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JLabel_DBType)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxA;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JComboBox_DBType)), gbc);

		// database version
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JLabel_DBDriver)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxB;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JComboBox_DBDriver)), gbc);

		// database address
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JLabel_DBAddress)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxA;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JTextField_DBAddress)), gbc);

		// database port
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JLabel_DBPort)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxB;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JTextField_DBPort)), gbc);

		// database name
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JLabel_DBName)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxA;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JTextField_DBName)), gbc);

		// database user
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JLabel_DBUser)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxA;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JTextField_DBUser)), gbc);

		// database pass
		gbc.gridx = (columnIndex = 0);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JLabel_DBPass)), gbc);
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = valueWidth;
		gbc.weightx = weightxB;
		gbl.setConstraints(this.add(ComponentDict.lookup(WCS3I18N.JTextField_DBPass)), gbc);

		// connect disconnect
		JPanel operatePanel = new JPanel();
		// operatePanel.add(ComponentDict.lookup(WCS3I18N.JButton_DBPing));
		operatePanel.add(ComponentDict.lookup(WCS3I18N.JButton_DBConnect));
		operatePanel.add(ComponentDict.lookup(WCS3I18N.JButton_DBDisconnect));
		gbc.gridx = (columnIndex = (labelWidth + valueWidth) * 1);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth + valueWidth;
		gbc.weightx = 0;
		gbl.setConstraints(this.add(operatePanel), gbc);
	}

	/** 数据库类型ComboBoxModel */
	private static ComboBoxModel<DatabaseType> databaseTypeComboBoxModel() {
		return new DefaultComboBoxModel<DatabaseType>() {

			private static final long serialVersionUID = 6307867344044431921L;
			{
				List<DatabaseType> types = Collections.list(DriverLoader.dbDrivers.keys());
				types.sort((o1, o2) -> Comparators.charSequenceCompare(o1.name, o2.name));
				types.forEach(type -> addElement(type));
			}
		};
	}

	/** 数据库类型ListCellRenderer */
	private static DefaultListCellRenderer databaseTypeListCellRender() {
		return new DefaultListCellRenderer() {

			private static final long serialVersionUID = 4352538661076698783L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value != null) {
					DatabaseType type = (DatabaseType) value;
					setText(type.name);
				}
				return this;
			}
		};
	}

	/** 数据库地址标签鼠标监听：文件型数据库打开选择窗口 */
	private static MouseListener databaseAddressLabelMouseListener() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if(!((JLabel) e.getSource()).isEnabled())
					return;
				JComboBox<DatabaseType> databaseTypeCombox = ComponentDict.lookup(WCS3I18N.JComboBox_DBType);
				DatabaseType selectDBType = (DatabaseType) databaseTypeCombox.getSelectedItem();
				if(selectDBType == DatabaseType.H2_Embed) {
					File dbFile = FileHelper.chooserFile(null, "*.h2.db, *.h2.mv", "db", "mv");
					if(dbFile != null) {
						String dbFilePath = dbFile.getPath();
						int suffixIndex = Math.max(dbFilePath.lastIndexOf(".h2.db"), dbFilePath.lastIndexOf(".h2.mv"));
						String dbURL = dbFilePath.substring(0, suffixIndex);
						JTextField databaseAddressField = ComponentDict.lookup(WCS3I18N.JTextField_DBAddress);
						databaseAddressField.setText(dbURL);
					}
				}
			}
		};
	}

	/**
	 * @return
	 */
	private static ItemListener databaseTypeItemListener() {
		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					DatabaseType databaseType = (DatabaseType) e.getItem();
					JComboBox<String> databaseDriverCombox = ComponentDict.lookup(WCS3I18N.JComboBox_DBDriver);
					DefaultComboBoxModel<String> databaseDriverModel = new DefaultComboBoxModel<String>() {

						private static final long serialVersionUID = 1L;
						{
							DriverLoader.dbDrivers.get(databaseType).keySet().forEach(driverName -> addElement(driverName));
						}
					};
					databaseDriverCombox.setModel(databaseDriverModel);
					databaseDriverCombox.setEnabled(true);
					JLabel databaseAddressLabel = ComponentDict.lookup(WCS3I18N.JLabel_DBAddress);
					JTextField databaseAddressField = ComponentDict.lookup(WCS3I18N.JTextField_DBAddress);
					JTextField databasePortField = ComponentDict.lookup(WCS3I18N.JTextField_DBPort);
					JTextField databaseNameField = ComponentDict.lookup(WCS3I18N.JTextField_DBName);
					switch(databaseType){
						case MySQL:
						case SQLServer:
						case Oracle:
						case DB2:
						case H2_Server:
							databaseAddressLabel.setText(WCS3I18N.JLabel_DBAddress.i18n());
							databaseAddressLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							try {
								databaseAddressField.setText(InetAddress.getLocalHost().getHostAddress());
							} catch(UnknownHostException e1) {
								databaseAddressField.setText("127.0.0.1");
							}
							databasePortField.setText(String.valueOf(databaseType.port));
							databasePortField.setEnabled(true);
							databaseNameField.setEnabled(true);
							break;
						case H2_Embed:
							databaseAddressLabel.setText("<html><a href=\"#\">" + WCS3I18N.JLabel_DBAddress_FileAddress.i18n() + "</a></html>");
							databaseAddressLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
							databaseAddressField.setText(null);
							databasePortField.setText("");
							databasePortField.setEnabled(false);
							databaseNameField.setEnabled(false);
							break;
						default:
							break;
					}
				}
			}
		};
	}

	private static MouseListener databaseConnectButtonMouseListener() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if(!((JButton) e.getSource()).isEnabled())
					return;
				JTextField datasourceNameField = ComponentDict.lookup(WCS3I18N.JTextField_DBConnectionName);
				JComboBox<DatabaseType> databaseTypeCombox = ComponentDict.lookup(WCS3I18N.JComboBox_DBType);
				JComboBox<String> databaseVersionCombox = ComponentDict.lookup(WCS3I18N.JComboBox_DBDriver);
				JTextField databaseAddressField = ComponentDict.lookup(WCS3I18N.JTextField_DBAddress);
				JTextField databasePortField = ComponentDict.lookup(WCS3I18N.JTextField_DBPort);
				JTextField databaseNameField = ComponentDict.lookup(WCS3I18N.JTextField_DBName);
				JTextField databaseUserField = ComponentDict.lookup(WCS3I18N.JTextField_DBUser);
				JTextField databasePassField = ComponentDict.lookup(WCS3I18N.JTextField_DBPass);
				// value
				String datasourceName = datasourceNameField.getText();
				if(StringUtils.isEmpty(datasourceName))
					JOptionPane.showMessageDialog(null, WCS3I18N.Text_DatasourceNotSet.i18n(), null, JOptionPane.ERROR_MESSAGE);
				DatabaseType dbType = (DatabaseType) databaseTypeCombox.getSelectedItem();
				if(dbType == null) {
					JOptionPane.showMessageDialog(null, WCS3I18N.Text_DBTypeNotSelect.i18n(), null, JOptionPane.ERROR_MESSAGE);
					return;
				}
				String dbDriver = (String) databaseVersionCombox.getSelectedItem();
				String dbAddress = databaseAddressField.getText();
				String dbPort = databasePortField.getText();
				String dbName = databaseNameField.getText();
				String dbUser = databaseUserField.getText();
				String dbPass = databasePassField.getText();
				boolean isFileDBConnection = false;
				if(dbType == DatabaseType.H2_Embed)
					isFileDBConnection = true;
				if(StringUtils.isEmpty(dbAddress))
					JOptionPane.showMessageDialog(null, WCS3I18N.Text_DBAddressNotSet.i18n(), null, JOptionPane.ERROR_MESSAGE);
				else if(StringUtils.isEmpty(dbPort) && !isFileDBConnection)
					JOptionPane.showMessageDialog(null, WCS3I18N.Text_DBPortNotSet.i18n(), null, JOptionPane.ERROR_MESSAGE);
				else if(StringUtils.isEmpty(dbName) && !isFileDBConnection)
					JOptionPane.showMessageDialog(null, WCS3I18N.Text_DBNameNotSet.i18n(), null, JOptionPane.ERROR_MESSAGE);
				else if(StringUtils.isEmpty(dbUser) && !isFileDBConnection)
					JOptionPane.showMessageDialog(null, WCS3I18N.Text_DBUserNotSet.i18n(), null, JOptionPane.ERROR_MESSAGE);
				else if(StringUtils.isEmpty(dbPass) && !isFileDBConnection)
					JOptionPane.showMessageDialog(null, WCS3I18N.Text_DBPassNotSet.i18n(), null, JOptionPane.ERROR_MESSAGE);
				else {
					DBConnection dbConnection = null;
					if(DBConnection.dbConnectionCache.containsKey(datasourceName))
						dbConnection = DBConnection.dbConnectionCache.get(datasourceName);
					else
						dbConnection = new DBConnection();
					dbConnection.setDataSourceName(datasourceName);
					dbConnection.setDatabaseType(dbType);
					dbConnection.setDatabaseDriver(dbDriver);
					dbConnection.setDatabaseAddress(dbAddress);
					dbConnection.setDatabasePort(dbPort);
					dbConnection.setDatabaseName(dbName);
					dbConnection.setDatabaseUser(dbUser);
					dbConnection.setDatabasePass(dbPass);
					dbConnection.setDriver(DriverLoader.dbDrivers.get(dbType).get(dbDriver));
					ConnectResult result = dbConnection.connect();
					disableOrEnableRelateComponent(dbConnection);
					if(result.success)
						JOptionPane.showMessageDialog(null, String.format(WCS3I18N.Text_DBConnectSuccess.i18n(), datasourceName), null, JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(null, String.format(WCS3I18N.Text_DBConnectFailed.i18n(), datasourceName, result.description), null, JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	private static MouseListener databaseDisConnectButtonMouseListener() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if(!((JButton) e.getSource()).isEnabled())
					return;
				JTextField datasourceNameField = ComponentDict.lookup(WCS3I18N.JTextField_DBConnectionName);
				String datasourceName = datasourceNameField.getText();
				if(StringUtils.isEmpty(datasourceName))
					JOptionPane.showMessageDialog(null, WCS3I18N.Text_DatasourceNotSet.i18n(), null, JOptionPane.ERROR_MESSAGE);
				if(DBConnection.dbConnectionCache.containsKey(datasourceName)) {
					DBConnection dbConnection = DBConnection.dbConnectionCache.get(datasourceName);
					dbConnection.disConnect();
				}
				disableOrEnableRelateComponent(null);
			}
		};
	}

	/** 弹出数据库连接窗口 */
	public static MouseListener popupDatabaseConnectionPanelWhenMousePressed(String dataSourceName) {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if(!((JButton) e.getSource()).isEnabled())
					return;
				DatabasePanel.showDatabaseConnectionPanel(dataSourceName);
			}
		};
	}

	/** 根据数据库源显示数据库连接窗口 */
	public static void showDatabaseConnectionPanel(String dataSourceName) {
		if(dataSourceName == null)
			return;
		JTextField datasourceNameField = ComponentDict.lookup(WCS3I18N.JTextField_DBConnectionName);
		JComboBox<DatabaseType> databaseTypeCombox = ComponentDict.lookup(WCS3I18N.JComboBox_DBType);
		JComboBox<String> databaseDriverCombox = ComponentDict.lookup(WCS3I18N.JComboBox_DBDriver);
		JTextField databaseAddressField = ComponentDict.lookup(WCS3I18N.JTextField_DBAddress);
		JTextField databasePortField = ComponentDict.lookup(WCS3I18N.JTextField_DBPort);
		JTextField databaseNameField = ComponentDict.lookup(WCS3I18N.JTextField_DBName);
		JTextField databaseUserField = ComponentDict.lookup(WCS3I18N.JTextField_DBUser);
		JTextField databasePassField = ComponentDict.lookup(WCS3I18N.JTextField_DBPass);
		JButton databaseConnectButton = ComponentDict.lookup(WCS3I18N.JButton_DBConnect);
		JButton databaseDisconnectButton = ComponentDict.lookup(WCS3I18N.JButton_DBDisconnect);
		if(DBConnection.dbConnectionCache.containsKey(dataSourceName)) {
			DBConnection dbConnection = DBConnection.dbConnectionCache.get(dataSourceName);
			datasourceNameField.setText(dbConnection.getDataSourceName());
			databaseTypeCombox.setSelectedItem(dbConnection.getDatabaseType());
			databaseDriverCombox.setSelectedItem(dbConnection.getDatabaseDriver());
			databaseAddressField.setText(dbConnection.getDatabaseAddress());
			databasePortField.setText(dbConnection.getDatabasePort());
			databaseNameField.setText(dbConnection.getDatabaseName());
			databaseUserField.setText(dbConnection.getDatabaseUser());
			databasePassField.setText(dbConnection.getDatabasePass());
			disableOrEnableRelateComponent(dbConnection);
		} else {
			JLabel databaseAddressLabel = ComponentDict.lookup(WCS3I18N.JLabel_DBAddress);
			databaseAddressLabel.setText(WCS3I18N.JLabel_DBAddress.i18n());
			datasourceNameField.setText(dataSourceName);
			databaseTypeCombox.setSelectedItem(null);
			databaseDriverCombox.setSelectedItem(null);
			databaseAddressField.setText(null);
			databasePortField.setText(null);
			databaseNameField.setText(null);
			databaseUserField.setText(null);
			databasePassField.setText(null);
			databaseConnectButton.setEnabled(true);
			databaseDisconnectButton.setEnabled(false);
			disableOrEnableRelateComponent(null);
		}
		Object[] messages = new Object[] {
		ComponentDict.lookup(WCS3I18N.JPanel_DBPanel),
		};
		Object[] options = new Object[] {
		// ComponentDict.lookup(WCS3I18N.JButton_DBPing),
		ComponentDict.lookup(WCS3I18N.JButton_DBConnect),
		ComponentDict.lookup(WCS3I18N.JButton_DBDisconnect),
		};
		JOptionPane.showOptionDialog(null, messages, WCS3I18N.Text_DBConnectionInfo.i18n(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
	}

	private static void disableOrEnableRelateComponent(DBConnection dbConnection) {
		JTextField datasourceNameField = ComponentDict.lookup(WCS3I18N.JTextField_DBConnectionName);
		JComboBox<DatabaseType> databaseTypeCombox = ComponentDict.lookup(WCS3I18N.JComboBox_DBType);
		JComboBox<String> databaseDriverCombox = ComponentDict.lookup(WCS3I18N.JComboBox_DBDriver);
		JTextField databaseAddressField = ComponentDict.lookup(WCS3I18N.JTextField_DBAddress);
		JTextField databasePortField = ComponentDict.lookup(WCS3I18N.JTextField_DBPort);
		JTextField databaseNameField = ComponentDict.lookup(WCS3I18N.JTextField_DBName);
		JTextField databaseUserField = ComponentDict.lookup(WCS3I18N.JTextField_DBUser);
		JTextField databasePassField = ComponentDict.lookup(WCS3I18N.JTextField_DBPass);
		JButton databaseConnectButton = ComponentDict.lookup(WCS3I18N.JButton_DBConnect);
		JButton databaseDisconnectButton = ComponentDict.lookup(WCS3I18N.JButton_DBDisconnect);
		JLabel databaseAddressLabel = ComponentDict.lookup(WCS3I18N.JLabel_DBAddress);
		if(dbConnection == null || !dbConnection.isConnect()) {
			datasourceNameField.setEnabled(true);
			databaseTypeCombox.setEnabled(true);
			databaseDriverCombox.setEnabled(true);
			databaseAddressField.setEnabled(true);
			databasePortField.setEnabled(true);
			databaseNameField.setEnabled(true);
			databaseUserField.setEnabled(true);
			databasePassField.setEnabled(true);
			databaseConnectButton.setEnabled(true);
			databaseDisconnectButton.setEnabled(false);
			databaseAddressLabel.setEnabled(true);
		} else {
			datasourceNameField.setEnabled(false);
			databaseTypeCombox.setEnabled(false);
			databaseDriverCombox.setEnabled(false);
			databaseAddressField.setEnabled(false);
			databasePortField.setEnabled(false);
			databaseNameField.setEnabled(false);
			databaseUserField.setEnabled(false);
			databasePassField.setEnabled(false);
			databaseConnectButton.setEnabled(false);
			databaseDisconnectButton.setEnabled(true);
			databaseAddressLabel.setEnabled(false);
		}
	}

}
