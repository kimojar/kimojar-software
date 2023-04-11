/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wapp
 * PACKAGE com.kimojar.ironman.mark.wapp
 * FILE WAppScreen.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-16
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
package com.kimojar.ironman.mark.wapp;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.kimojar.util.common.gui.ComponentDict;
import com.kimojar.util.common.gui.HyperlinkLabel;

/**
 * @author KiMoJar
 * @date 2022-12-16
 */
public class WAppScreen extends JPanel {

	private static final long serialVersionUID = -4113793885769295720L;

	public WAppScreen() {
		ComponentDict.record(WAppI18N.JPanel_Screen, this);
		initComponent();
	}

	private void initComponent() {
		setLayout(new BorderLayout());
		add(layoutNorthPanel(), BorderLayout.NORTH);
		add(layoutCentralPanel(), BorderLayout.CENTER);
	}

	private JPanel layoutNorthPanel() {
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JButton saveAllButton = new JButton();
		northPanel.add(ComponentDict.record(WAppI18N.JButton_SaveAll, saveAllButton));
		saveAllButton.setText(WAppI18N.JButton_SaveAll.i18n());
		saveAllButton.addMouseListener(Listener.saveAllButtonClicked());
		JButton deleteButton = new JButton();
		northPanel.add(ComponentDict.record(WAppI18N.JButton_Delete, deleteButton));
		deleteButton.setText(WAppI18N.JButton_Delete.i18n());
		deleteButton.addMouseListener(Listener.deleteButtonClicked());
		JButton generateCmdButton = new JButton();
		northPanel.add(ComponentDict.record(WAppI18N.JButton_GenerateCommand, generateCmdButton));
		generateCmdButton.setText(WAppI18N.JButton_GenerateCommand.i18n());
		generateCmdButton.addMouseListener(Listener.generateCmdButtonClicked());
		JButton importButton = new JButton();
		northPanel.add(ComponentDict.record(WAppI18N.JButton_Import, importButton));
		importButton.setText(WAppI18N.JButton_Import.i18n());
		importButton.addMouseListener(Listener.importButtonClicked());
		JButton exportButton = new JButton();
		northPanel.add(ComponentDict.record(WAppI18N.JButton_Export, exportButton));
		exportButton.setText(WAppI18N.JButton_Export.i18n());
		exportButton.addMouseListener(Listener.exportButtonClicked());
		return northPanel;
	}

	private JPanel layoutCentralPanel() {
		JPanel centralPanel = new JPanel();

		centralPanel.setLayout(new BorderLayout());
		centralPanel.add(ComponentDict.record(WAppI18N.JScrollPane_AppTree, layoutAppListPane()), BorderLayout.WEST);
		centralPanel.add(ComponentDict.record(WAppI18N.JScrollPane_AppTree, layoutAppDetialPane()), BorderLayout.CENTER);

		/*
		 * 使用GridBagLayout会存在某些点击时，组件位置会偏移的问题，所以换为了BorderLayout才稳定些
		 * GridBagLayout gbl = new GridBagLayout();
		 * GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, 10, 1, new Insets(0, 0, 0, 0), 0, 0);
		 * centralPanel.setLayout(gbl);
		 * gbc.gridx = 0;
		 * gbc.weightx = 1;
		 * gbl.setConstraints(centralPanel.add(ComponentDict.record(WAppI18N.JScrollPane_AppTree, layoutAppListPane())), gbc);
		 * gbc.gridx = 1;
		 * gbc.weightx = 1;
		 * gbl.setConstraints(centralPanel.add(ComponentDict.record(WAppI18N.JScrollPane_AppDetial, layoutAppDetialPane())), gbc);
		 */
		return centralPanel;
	}

	private JScrollPane layoutAppListPane() {
		JScrollPane treeScrollPane = new JScrollPane();
		treeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		treeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		treeScrollPane.setBorder(null);
		JTree appTree = new JTree();
		treeScrollPane.setViewportView(ComponentDict.record(WAppI18N.JTree_AppTree, appTree));
		appTree.setEditable(false);
		appTree.setRootVisible(false);// 不显示root节点
		appTree.setShowsRootHandles(true);// 显示折叠图标，部分主题不支持
		appTree.setModel(Listener.appTreeModel());
		appTree.setComponentPopupMenu(Listener.appTreePopupMenu());
		appTree.setCellRenderer(Listener.appTreeCellRender());
		appTree.addTreeSelectionListener(Listener.appTreeSelectionListener());
		appTree.updateUI();
		return treeScrollPane;
	}

	private JPanel layoutAppDetialPane() {
		JScrollPane detialScrollPane = new JScrollPane();
		detialScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		detialScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		detialScrollPane.setBorder(null);
		JPanel detialPanel = new JPanel();
		detialScrollPane.setViewportView(detialPanel);
		BorderLayout layout = new BorderLayout();
		layout.setVgap(20);
		detialPanel.setLayout(layout);
		detialPanel.add(layoutBaseInfomationPanel(), BorderLayout.NORTH);
		detialPanel.add(layoutCommandInformationPanel(), BorderLayout.CENTER);
		return detialPanel;
	}

	private JPanel layoutBaseInfomationPanel() {
		JPanel baseInfoPanel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 0, 10, 1, new Insets(5, 5, 5, 5), 0, 0);
		baseInfoPanel.setLayout(gbl);
		TitledBorder baseInfoTitledBorder = new TitledBorder(new LineBorder(null), WAppI18N.TitledBorder_BaseInformation.i18n(), TitledBorder.LEADING, TitledBorder.TOP);
		baseInfoPanel.setBorder(ComponentDict.recordTitledBorder(WAppI18N.TitledBorder_BaseInformation, baseInfoTitledBorder));
		int rowIndex = 0;
		int columnIndex = 1;
		int labelWidth = 1;
		int textFieldWith = 2;
		int iconWidth = 2;
		int textFieldColumn = 6;
		int columnCount = (iconWidth + 2 * labelWidth + 2 * textFieldWith);

		// app name
		JLabel nameLabel = new JLabel();
		gbc.gridx = (columnIndex = iconWidth);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_Name, nameLabel)), gbc);
		nameLabel.setText(WAppI18N.JLabel_Name.i18n());
		nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField nameTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_Name, nameTextField)), gbc);
		nameTextField.setText(WAppI18N.JTextField_Name.i18n());
		nameTextField.setColumns(textFieldColumn);
		nameTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());
		// app version
		JLabel versionLabel = new JLabel();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_Version, versionLabel)), gbc);
		versionLabel.setText(WAppI18N.JLabel_Version.i18n());
		versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField versionTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_Version, versionTextField)), gbc);
		versionTextField.setText(WAppI18N.JTextField_Version.i18n());
		versionTextField.setColumns(textFieldColumn);
		versionTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());

		// app category
		JLabel categoryLabel = new JLabel();
		gbc.gridx = (columnIndex = iconWidth);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_Category, categoryLabel)), gbc);
		categoryLabel.setText(WAppI18N.JLabel_Category.i18n());
		categoryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JComboBox<String> categoryComboBox = new JComboBox<>();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JComboBox_Category, categoryComboBox)), gbc);
		categoryComboBox.setModel(Listener.categoryComboBoxModel());
		categoryComboBox.setSelectedItem(null);
		categoryComboBox.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());
		// app directory
		JLabel directoryLabel = new HyperlinkLabel();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_Directory, directoryLabel)), gbc);
		directoryLabel.setText("<html><a href=\"#\">" + WAppI18N.JLabel_Directory.i18n() + "</a></html>");
		directoryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		directoryLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		directoryLabel.addMouseListener(Listener.directoryLabelClicked(WAppI18N.JLabel_Directory, WAppI18N.JTextField_Directory));
		JTextField directoryTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_Directory, directoryTextField)), gbc);
		directoryTextField.setText(WAppI18N.JLabel_Directory.i18n());
		directoryTextField.setColumns(textFieldColumn);
		directoryTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());

		// app executePath
		JLabel executePathLabel = new HyperlinkLabel();
		gbc.gridx = (columnIndex = iconWidth);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_ExecutePath, executePathLabel)), gbc);
		executePathLabel.setText("<html><a href=\"#\">" + WAppI18N.JLabel_ExecutePath.i18n() + "</a></html>");
		executePathLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		executePathLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		executePathLabel.addMouseListener(Listener.directoryLabelClicked(WAppI18N.JLabel_ExecutePath, WAppI18N.JTextField_ExecutePath));
		JTextField executePathTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_ExecutePath, executePathTextField)), gbc);
		executePathTextField.setText(WAppI18N.JTextField_ExecutePath.i18n());
		executePathTextField.setColumns(textFieldColumn);
		executePathTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());
		// app uninstallPath
		JLabel uninstallLabel = new HyperlinkLabel();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_UninstallPath, uninstallLabel)), gbc);
		uninstallLabel.setText("<html><a href=\"#\">" + WAppI18N.JLabel_UninstallPath.i18n() + "</a></html>");
		uninstallLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		uninstallLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		uninstallLabel.addMouseListener(Listener.directoryLabelClicked(WAppI18N.JLabel_UninstallPath, WAppI18N.JTextField_UninstallPath));
		JTextField uninstallTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_UninstallPath, uninstallTextField)), gbc);
		directoryTextField.setText(WAppI18N.JTextField_UninstallPath.i18n());
		uninstallTextField.setColumns(textFieldColumn);
		uninstallTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());

		// app dataPath
		JLabel dataPathLabel = new HyperlinkLabel();
		gbc.gridx = (columnIndex = iconWidth);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_DataPath, dataPathLabel)), gbc);
		dataPathLabel.setText("<html><a href=\"#\">" + WAppI18N.JLabel_DataPath.i18n() + "</a></html>");
		dataPathLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		dataPathLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		dataPathLabel.addMouseListener(Listener.directoryLabelClicked(WAppI18N.JLabel_DataPath, WAppI18N.JTextField_DataPath));
		JTextField dataPathTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_DataPath, dataPathTextField)), gbc);
		dataPathTextField.setText(WAppI18N.JTextField_DataPath.i18n());
		dataPathTextField.setColumns(textFieldColumn);
		dataPathTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());
		// app configPath
		JLabel configLabel = new HyperlinkLabel();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_ConfigPath, configLabel)), gbc);
		configLabel.setText("<html><a href=\"#\">" + WAppI18N.JLabel_ConfigPath.i18n() + "</a></html>");
		configLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		configLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		configLabel.addMouseListener(Listener.directoryLabelClicked(WAppI18N.JLabel_ConfigPath, WAppI18N.JTextField_ConfigPath));
		JTextField configTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_ConfigPath, configTextField)), gbc);
		directoryTextField.setText(WAppI18N.JTextField_ConfigPath.i18n());
		configTextField.setColumns(textFieldColumn);
		configTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());

		// app website
		JLabel websiteLabel = new HyperlinkLabel();
		gbc.gridx = (columnIndex = iconWidth);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_Website, websiteLabel)), gbc);
		websiteLabel.setText("<html><a href=\"#\">" + WAppI18N.JLabel_Website.i18n() + "</a></html>");
		websiteLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		websiteLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		websiteLabel.addMouseListener(Listener.websiteLabelClicked(WAppI18N.JLabel_Website, WAppI18N.JTextField_Website));
		JTextField websiteTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_Website, websiteTextField)), gbc);
		websiteTextField.setText(WAppI18N.JTextField_Website.i18n());
		websiteTextField.setColumns(textFieldColumn);
		websiteTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());
		// app download
		JLabel downloadLabel = new HyperlinkLabel();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_Download, downloadLabel)), gbc);
		downloadLabel.setText("<html><a href=\"#\">" + WAppI18N.JLabel_Download.i18n() + "</a></html>");
		downloadLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		downloadLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		downloadLabel.addMouseListener(Listener.websiteLabelClicked(WAppI18N.JLabel_Download, WAppI18N.JTextField_Download));
		JTextField downloadTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_Download, downloadTextField)), gbc);
		directoryTextField.setText(WAppI18N.JTextField_Download.i18n());
		downloadTextField.setColumns(textFieldColumn);
		downloadTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());

		// app manual online
		JLabel manualOnlineLabel = new HyperlinkLabel();
		gbc.gridx = (columnIndex = iconWidth);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_ManualOnline, manualOnlineLabel)), gbc);
		manualOnlineLabel.setText("<html><a href=\"#\">" + WAppI18N.JLabel_ManualOnline.i18n() + "</a></html>");
		manualOnlineLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		manualOnlineLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		manualOnlineLabel.addMouseListener(Listener.websiteLabelClicked(WAppI18N.JLabel_ManualOnline, WAppI18N.JTextField_ManualOnline));
		JTextField manualOnlineTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_ManualOnline, manualOnlineTextField)), gbc);
		manualOnlineTextField.setText(WAppI18N.JTextField_ManualOnline.i18n());
		manualOnlineTextField.setColumns(textFieldColumn);
		manualOnlineTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());
		// app manual offline
		JLabel manualOfflineLabel = new HyperlinkLabel();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_ManualOffline, manualOfflineLabel)), gbc);
		manualOfflineLabel.setText("<html><a href=\"#\">" + WAppI18N.JLabel_ManualOffline.i18n() + "</a></html>");
		manualOfflineLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		manualOfflineLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		manualOfflineLabel.addMouseListener(Listener.websiteLabelClicked(WAppI18N.JLabel_ManualOffline, WAppI18N.JTextField_ManualOffline));
		JTextField manualOfflineTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_ManualOffline, manualOfflineTextField)), gbc);
		directoryTextField.setText(WAppI18N.JTextField_ManualOffline.i18n());
		manualOfflineTextField.setColumns(textFieldColumn);
		manualOfflineTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());

		// app github
		JLabel githubLabel = new HyperlinkLabel();
		gbc.gridx = (columnIndex = iconWidth);
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_GitHub, githubLabel)), gbc);
		githubLabel.setText("<html><a href=\"#\">" + WAppI18N.JLabel_GitHub.i18n() + "</a></html>");
		githubLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		githubLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		githubLabel.addMouseListener(Listener.websiteLabelClicked(WAppI18N.JLabel_GitHub, WAppI18N.JTextField_GitHub));
		JTextField githubTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_GitHub, githubTextField)), gbc);
		githubTextField.setText(WAppI18N.JTextField_GitHub.i18n());
		githubTextField.setColumns(textFieldColumn);
		githubTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());
		// app gitee
		JLabel giteeLabel = new HyperlinkLabel();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = labelWidth;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_Gitee, giteeLabel)), gbc);
		giteeLabel.setText("<html><a href=\"#\">" + WAppI18N.JLabel_Gitee.i18n() + "</a></html>");
		giteeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		giteeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		giteeLabel.addMouseListener(Listener.websiteLabelClicked(WAppI18N.JLabel_Gitee, WAppI18N.JTextField_Gitee));
		JTextField giteeTextField = new JTextField();
		gbc.gridx = (columnIndex += gbc.gridwidth);
		gbc.gridwidth = textFieldWith;
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JTextField_Gitee, giteeTextField)), gbc);
		directoryTextField.setText(WAppI18N.JTextField_Gitee.i18n());
		giteeTextField.setColumns(textFieldColumn);
		giteeTextField.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());

		// app icon
		JLabel iconLabel = new JLabel();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = iconWidth;
		gbc.gridheight = rowIndex;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_Icon, iconLabel)), gbc);
		iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		iconLabel.setIcon(new ImageIcon(WAppScreen.class.getResource("resource/Alphabet-K-128x128.png")));// set default icon
		iconLabel.setText(WAppI18N.JLabel_Icon.i18n());
		iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		iconLabel.addMouseListener(Listener.iconLabelMousePressedListener());
		iconLabel.setMaximumSize(new Dimension(128, 128));
		iconLabel.setMinimumSize(new Dimension(128, 128));
		iconLabel.setPreferredSize(new Dimension(128, 128));

		// app description
		JLabel descriptionLabel = new JLabel();
		gbc.gridx = 0;
		gbc.gridy = rowIndex++;
		gbc.gridwidth = labelWidth;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbl.setConstraints(baseInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_Description, descriptionLabel)), gbc);
		descriptionLabel.setText(WAppI18N.JLabel_Description.i18n());
		descriptionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JScrollPane descriptionScrollPane = new JScrollPane();
		gbc.gridx = 1;
		gbc.gridwidth = (columnCount - labelWidth);
		gbc.weightx = 1;
		gbl.setConstraints(baseInfoPanel.add(descriptionScrollPane), gbc);
		descriptionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		descriptionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		JTextArea descriptionTextArea = new JTextArea();
		descriptionScrollPane.setViewportView(ComponentDict.record(WAppI18N.JTextArea_Description, descriptionTextArea));
		descriptionTextArea.setText(WAppI18N.JTextArea_Description.i18n());
		descriptionTextArea.setRows(3);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.addMouseListener(Listener.appDetialTextFieldMouseExitedListener());

		return baseInfoPanel;
	}

	private JPanel layoutCommandInformationPanel() {
		JPanel cmdInfoPanel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, 10, 1, new Insets(5, 5, 5, 5), 0, 0);
		cmdInfoPanel.setLayout(gbl);
		TitledBorder commandInfoBorder = new TitledBorder(new LineBorder(null), WAppI18N.TitledBorder_CommandInformation.i18n(), TitledBorder.LEADING, TitledBorder.TOP);
		cmdInfoPanel.setBorder(ComponentDict.recordTitledBorder(WAppI18N.TitledBorder_CommandInformation, commandInfoBorder));

		JScrollPane cmdTreeScrollPane = new JScrollPane();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 3;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbl.setConstraints(cmdInfoPanel.add(cmdTreeScrollPane), gbc);
		cmdTreeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		cmdTreeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		cmdTreeScrollPane.setBorder(null);
		JTree cmdTree = new JTree();
		cmdTreeScrollPane.setViewportView(cmdInfoPanel.add(ComponentDict.record(WAppI18N.JTree_CmdTree, cmdTree)));
		cmdTree.setEditable(false);
		cmdTree.setRootVisible(true);// 显示root节点
		cmdTree.setShowsRootHandles(true);// 显示折叠图标，部分主题不支持
		cmdTree.setComponentPopupMenu(Listener.cmdTreePopupMenu());
		cmdTree.setCellRenderer(Listener.cmdTreeCellRender());
		cmdTree.addTreeSelectionListener(Listener.cmdTreeSelectionListener());
		cmdTree.setModel(null);

		JLabel cmdActionLabel = new JLabel();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbl.setConstraints(cmdInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_CmdAction, cmdActionLabel)), gbc);
		cmdActionLabel.setText(WAppI18N.JLabel_CmdAction.i18n());

		JScrollPane cmdActionScrollPane = new JScrollPane();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		gbc.gridheight = 2;
		gbc.weightx = 3;
		gbc.weighty = 1;
		gbl.setConstraints(cmdInfoPanel.add(cmdActionScrollPane), gbc);
		cmdActionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		cmdActionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		cmdActionScrollPane.setBorder(null);
		JTextArea cmdActionTextArea = new JTextArea();
		cmdActionScrollPane.setViewportView(cmdInfoPanel.add(ComponentDict.record(WAppI18N.JTextArea_CmdAction, cmdActionTextArea)));
		cmdActionTextArea.setRows(5);
		cmdActionTextArea.setEnabled(false);
		cmdActionTextArea.addMouseListener(Listener.cmdActionTextFieldMouseExitedListener());

		JLabel variableLabel = new JLabel();
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbl.setConstraints(cmdInfoPanel.add(ComponentDict.record(WAppI18N.JLabel_CmdVariable, variableLabel)), gbc);
		variableLabel.setText(WAppI18N.JLabel_CmdVariable.i18n());

		JScrollPane cmdVariableScrollPane = new JScrollPane();
		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbl.setConstraints(cmdInfoPanel.add(cmdVariableScrollPane), gbc);
		cmdVariableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		cmdVariableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		cmdVariableScrollPane.setBorder(null);
		JList<String> cmdVariableList = new JList<>();
		cmdVariableScrollPane.setViewportView(cmdInfoPanel.add(ComponentDict.record(WAppI18N.JList_CmdVariable, cmdVariableList)));
		cmdVariableList.setEnabled(false);
		cmdVariableList.addMouseListener(Listener.variableListClicked());

		return cmdInfoPanel;
	}

}
