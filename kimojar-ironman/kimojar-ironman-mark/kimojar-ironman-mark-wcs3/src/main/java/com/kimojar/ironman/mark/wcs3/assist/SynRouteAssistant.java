/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.assist
 * FILE SynRouteAssistant.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-02-28
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
package com.kimojar.ironman.mark.wcs3.assist;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.kimojar.ironman.mark.wcs3.WCS3I18N;
import com.kimojar.ironman.mark.wcs3.db.DBConnection;
import com.kimojar.ironman.mark.wcs3.db.DatabasePanel;
import com.kimojar.ironman.mark.wcs3.db.IDBConnectListener;
import com.kimojar.ironman.mark.wcs3.entity.node.NodeEquip;
import com.kimojar.ironman.mark.wcs3.entity.node.NodeRoute;
import com.kimojar.ironman.mark.wcs3.entity.node.NodeRouteEquip;
import com.kimojar.ironman.mark.wcs3.entity.node.dao.NodeEquipService;
import com.kimojar.ironman.mark.wcs3.entity.node.dao.NodeRouteEquipService;
import com.kimojar.ironman.mark.wcs3.entity.node.dao.NodeRouteService;
import com.kimojar.ironman.mark.wcs3.entity.node.dao.NodeSystemControlService;
import com.kimojar.ironman.mark.wcs3.entity.scene.SceneRoute;
import com.kimojar.ironman.mark.wcs3.entity.scene.SceneRouteEquip;
import com.kimojar.ironman.mark.wcs3.entity.scene.dao.SceneRouteEquipService;
import com.kimojar.ironman.mark.wcs3.entity.scene.dao.SceneRouteService;
import com.kimojar.util.common.gui.ComponentDict;
import com.kimojar.util.common.ref.AnnotationUtil;

/**
 * @author KiMoJar
 * @date 2023-02-28
 */
public class SynRouteAssistant extends BasicAssistant {

	private static final long serialVersionUID = -2072012773576045810L;

	private static JRadioButton selectSynRadioButton;

	private static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyyMMddHHmmss");
	private static String lineSeparator = System.getProperty("line.separator");

	public SynRouteAssistant() {
		initComponent();
		layoutComponent();
	}

	@Override
	public String name() {
		return WCS3I18N.Assistant_SynRoute_Name.i18n();
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		initSynRouteDataComponent();
	}

	private void initSynRouteDataComponent() {
		TitledBorder synchronizeRouteDataBorder = new TitledBorder(new LineBorder(null), WCS3I18N.Assistant_SynRoute_TitleBorder_SynRouteData.i18n(), TitledBorder.LEADING, TitledBorder.TOP);
		ComponentDict.recordTitledBorder(WCS3I18N.Assistant_SynRoute_TitleBorder_SynRouteData, synchronizeRouteDataBorder);

		// choose scene db file
		JButton chooseSceneDBFileButton = new JButton();
		ComponentDict.record(WCS3I18N.Assistant_SynRoute_JButton_ChooseSceneDBFile, chooseSceneDBFileButton);
		chooseSceneDBFileButton.setText(WCS3I18N.Assistant_SynRoute_JButton_ChooseSceneDBFile.i18n());
		chooseSceneDBFileButton.addMouseListener(DatabasePanel.popupDatabaseConnectionPanelWhenMousePressed(DBConnection.WCSDB_SCENE));
		JLabel chooseSceneDBFileLabel = new JLabel();
		ComponentDict.record(WCS3I18N.Assistant_SynRoute_JLabel_ChooseSceneDBFile, chooseSceneDBFileLabel);

		// replace with site radio button
		JRadioButton replaceWithSiteRadioButton = new JRadioButton();
		ComponentDict.record(WCS3I18N.Assistant_SynRoute_JRadioButton_ReplaceWithSite, replaceWithSiteRadioButton);
		replaceWithSiteRadioButton.setText(WCS3I18N.Assistant_SynRoute_JRadioButton_ReplaceWithSite.i18n());
		replaceWithSiteRadioButton.addItemListener(sychronizeRadioButtonItemListener());

		// merge with site radio button
		JRadioButton mergeWithSiteRadioButton = new JRadioButton();
		ComponentDict.record(WCS3I18N.Assistant_SynRoute_JRadioButton_MergeWithSite, mergeWithSiteRadioButton);
		mergeWithSiteRadioButton.setText(WCS3I18N.Assistant_SynRoute_JRadioButton_MergeWithSite.i18n());
		mergeWithSiteRadioButton.addItemListener(sychronizeRadioButtonItemListener());

		// merge with scene radio button
		JRadioButton mergeWithSceneRadioButton = new JRadioButton();
		ComponentDict.record(WCS3I18N.Assistant_SynRoute_JRadioButton_MergeWithScene, mergeWithSceneRadioButton);
		mergeWithSceneRadioButton.setText(WCS3I18N.Assistant_SynRoute_JRadioButton_MergeWithScene.i18n());
		mergeWithSceneRadioButton.addItemListener(sychronizeRadioButtonItemListener());

		// export as sql radio button
		JRadioButton exportAsSqlRadioButton = new JRadioButton();
		ComponentDict.record(WCS3I18N.Assistant_SynRoute_JRadioButton_ExportAsSql, exportAsSqlRadioButton);
		exportAsSqlRadioButton.setText(WCS3I18N.Assistant_SynRoute_JRadioButton_ExportAsSql.i18n());
		exportAsSqlRadioButton.addItemListener(sychronizeRadioButtonItemListener());

		// group radio button
		ButtonGroup group = new ButtonGroup();
		group.add(replaceWithSiteRadioButton);
		group.add(mergeWithSiteRadioButton);
		group.add(mergeWithSceneRadioButton);
		group.add(exportAsSqlRadioButton);
		replaceWithSiteRadioButton.setSelected(true);// select by default
		selectSynRadioButton = replaceWithSiteRadioButton;

		// check core database connection
		JButton checkDatabaseButton = new JButton();
		ComponentDict.record(WCS3I18N.Assistant_SynRoute_JButton_CheckDBConnection, checkDatabaseButton);
		checkDatabaseButton.setText(WCS3I18N.Assistant_SynRoute_JButton_CheckDBConnection.i18n());
		checkDatabaseButton.addMouseListener(DatabasePanel.popupDatabaseConnectionPanelWhenMousePressed(DBConnection.WCSDB_CORE));

		// synchronize
		JButton synchronizeButton = new JButton();
		ComponentDict.record(WCS3I18N.Assistant_SynRoute_JButton_Synchronize, synchronizeButton);
		synchronizeButton.setText(WCS3I18N.Assistant_SynRoute_JButton_Synchronize.i18n());
		synchronizeButton.addMouseListener(synchronizeButtonMouseListener());

		DBConnection.addListener(onDBConnectChanged());
	}

	/**
	 * 布局组件
	 */
	private void layoutComponent() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 0, 10, 1, new Insets(5, 5, 5, 5), 0, 0);
		this.setLayout(gbl);
		int rowIndex = 0;

		gbc.gridy = rowIndex++;
		gbl.setConstraints(add(layoutSynRouteDataPanel()), gbc);

	}

	private JPanel layoutSynRouteDataPanel() {
		JPanel synRoutePanel = new JPanel();
		synRoutePanel.setBorder(ComponentDict.lookupTitledBorder(WCS3I18N.Assistant_SynRoute_TitleBorder_SynRouteData));

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 0, 10, 1, new Insets(5, 5, 5, 5), 0, 0);
		synRoutePanel.setLayout(gbl);
		int rowIndex = 0;

		gbc.gridx = 0;
		gbc.gridy = rowIndex++;
		gbl.setConstraints(synRoutePanel.add(ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JRadioButton_ReplaceWithSite)), gbc);

		gbc.gridx = 0;
		gbc.gridy = rowIndex++;
		gbl.setConstraints(synRoutePanel.add(ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JRadioButton_MergeWithSite)), gbc);

		gbc.gridx = 0;
		gbc.gridy = rowIndex++;
		gbl.setConstraints(synRoutePanel.add(ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JRadioButton_MergeWithScene)), gbc);

		gbc.gridx = 0;
		gbc.gridy = rowIndex++;
		gbl.setConstraints(synRoutePanel.add(ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JRadioButton_ExportAsSql)), gbc);

		JPanel chooseScenePanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING, 10, 10);
		chooseScenePanel.setLayout(flowLayout);
		chooseScenePanel.add(ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JButton_ChooseSceneDBFile));
		chooseScenePanel.add(ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JLabel_ChooseSceneDBFile));
		gbc.gridx = 0;
		gbc.gridy = rowIndex++;
		gbl.setConstraints(synRoutePanel.add(chooseScenePanel), gbc);

		JPanel operatePanel = new JPanel();
		operatePanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		operatePanel.add(ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JButton_CheckDBConnection));
		operatePanel.add(ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JButton_Synchronize));
		gbc.gridx = 0;
		gbc.gridy = rowIndex++;
		gbl.setConstraints(synRoutePanel.add(operatePanel), gbc);

		return synRoutePanel;
	}

	/** 同步数据按钮选择变化 */
	private static ItemListener sychronizeRadioButtonItemListener() {
		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					selectSynRadioButton = (JRadioButton) e.getItem();
					JRadioButton exportAsSqlRadioButton = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JRadioButton_ExportAsSql);
					JButton chooseSceneDBFileButton = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JButton_ChooseSceneDBFile);
					if(selectSynRadioButton == exportAsSqlRadioButton)
						chooseSceneDBFileButton.setEnabled(false);
					else
						chooseSceneDBFileButton.setEnabled(true);
				}
			}
		};
	}

	/** 同步数据按钮监听：执行同步操作 */
	private static MouseListener synchronizeButtonMouseListener() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				DBConnection coreConnection = DBConnection.dbConnectionCache.get(DBConnection.WCSDB_CORE);
				DBConnection sceneConnection = DBConnection.dbConnectionCache.get(DBConnection.WCSDB_SCENE);
				if(coreConnection == null || !coreConnection.isConnect()) {
					JOptionPane.showMessageDialog(null, WCS3I18N.Text_PleaseConnectCoreDBFirstBeforeSynchronize.i18n(), null, JOptionPane.ERROR_MESSAGE);
					DatabasePanel.showDatabaseConnectionPanel(DBConnection.WCSDB_CORE);
				} else {
					JRadioButton replaceWithSiteRadioButton = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JRadioButton_ReplaceWithSite);
					JRadioButton mergeWithSiteRadioButton = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JRadioButton_MergeWithSite);
					JRadioButton mergeWithSceneRadioButton = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JRadioButton_MergeWithScene);
					JRadioButton exportAsSqlRadioButton = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JRadioButton_ExportAsSql);
					if(selectSynRadioButton == exportAsSqlRadioButton) {
						try {
							exportAsSql(coreConnection);
						} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | SQLException e1) {
							e1.printStackTrace();
						}
					} else {
						if(sceneConnection == null || !sceneConnection.isConnect()) {
							JOptionPane.showMessageDialog(null, WCS3I18N.Text_PleaseConnectSceneDBFirstBeforeSynchronize.i18n(), null, JOptionPane.ERROR_MESSAGE);
							DatabasePanel.showDatabaseConnectionPanel(DBConnection.WCSDB_SCENE);
						} else {
							try {
								// backup scene file first
								File sceneFile = new File(sceneConnection.getDatabaseAddress() + ".h2.db");
								File sceneBackupFile = new File(sceneConnection.getDatabaseAddress() + ymdhms.format(new Date()) + ".h2.db");
								if(sceneFile.exists() && sceneFile.isFile()) {
									try {
										Files.copy(sceneFile.toPath(), sceneBackupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
									} catch(IOException e1) {
										e1.printStackTrace();
									}
								}
								if(selectSynRadioButton == replaceWithSiteRadioButton) {
									replaceWithSite(coreConnection, sceneConnection);
								} else if(selectSynRadioButton == mergeWithSiteRadioButton) {
									mergeWithSite(coreConnection, sceneConnection);
								} else if(selectSynRadioButton == mergeWithSceneRadioButton) {
									mergeWithScene(coreConnection, sceneConnection);
								}
							} catch(SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		};
	}

	/**
	 * 场景文件连接变化时，设置标签显示信息为场景文件的路径
	 * 
	 * @return
	 */
	private IDBConnectListener onDBConnectChanged() {
		return new IDBConnectListener() {

			@Override
			public void onDBConnected(DBConnection dbConnection) {
				if(dbConnection.getDataSourceName().equals(DBConnection.WCSDB_SCENE)) {
					String sceneDBFilePath = dbConnection.getDatabaseAddress() + ".h2.db";
					if(sceneDBFilePath != null) {
						JLabel chooseSceneDBFileLabel = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JLabel_ChooseSceneDBFile);
						chooseSceneDBFileLabel.setFont(chooseSceneDBFileLabel.getFont().deriveFont(Font.ITALIC));
						chooseSceneDBFileLabel.setText(sceneDBFilePath);
						JButton chooseSceneDBFileButton = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JButton_ChooseSceneDBFile);
						chooseSceneDBFileButton.setForeground(Color.GREEN);
					}
				} else if(dbConnection.getDataSourceName().equals(DBConnection.WCSDB_CORE)) {
					JButton checkDBConnectionButton = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JButton_CheckDBConnection);
					checkDBConnectionButton.setForeground(Color.GREEN);
				}
			}

			@Override
			public void onDBDisconnected(DBConnection dbConnection) {
				if(dbConnection.getDataSourceName().equals(DBConnection.WCSDB_SCENE)) {
					JLabel chooseSceneDBFileLabel = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JLabel_ChooseSceneDBFile);
					chooseSceneDBFileLabel.setFont(chooseSceneDBFileLabel.getFont().deriveFont(Font.PLAIN));
					JButton chooseSceneDBFileButton = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JButton_ChooseSceneDBFile);
					chooseSceneDBFileButton.setForeground(null);
				} else if(dbConnection.getDataSourceName().equals(DBConnection.WCSDB_CORE)) {
					JButton checkDBConnectionButton = ComponentDict.lookup(WCS3I18N.Assistant_SynRoute_JButton_CheckDBConnection);
					checkDBConnectionButton.setForeground(null);
				}
			}
		};
	}

	/**
	 * 将现场路径和路径设备数据导出为sql，并复制到粘贴板
	 * 
	 * @param coreConnection
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static void exportAsSql(DBConnection coreConnection) throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		List<NodeRoute> nodeRoutes = NodeRouteService.queryAll(coreConnection.getConnectionSource());
		StringBuffer sqlBuffer = new StringBuffer();
		for(NodeRoute nodeRoute : nodeRoutes) {
			// scene route insert sql
			String routeSQL = generateInsertSQL(createSceneRouteFromNodeRoute(coreConnection, nodeRoute));
			sqlBuffer.append(routeSQL);
			sqlBuffer.append(lineSeparator);
			// scene route equip insert sql
			for(SceneRouteEquip sceneRouteEquip : createSceneRouteEquipFromNodeRoute(coreConnection, nodeRoute)) {
				String routeEquipSQL = generateInsertSQL(sceneRouteEquip);
				sqlBuffer.append(routeEquipSQL);
				sqlBuffer.append(lineSeparator);
			}
		}
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selection = new StringSelection(sqlBuffer.toString());// 构建String数据类型
		clipboard.setContents(selection, null);
	}

	/**
	 * 以现场路径数据替换场景路径数据的操作：清空场景路径和场景路径设备，然后根据现场路径和路径设备生成导入场景
	 * 
	 * @param coreConnection
	 * @param sceneConnection
	 * @throws SQLException
	 */
	public static void replaceWithSite(DBConnection coreConnection, DBConnection sceneConnection) throws SQLException {
		SceneRouteService.emptyTable(sceneConnection.getConnectionSource());
		SceneRouteEquipService.emptyTable(sceneConnection.getConnectionSource());
		List<SceneRoute> sceneRoutes = new ArrayList<>();// 待保存的场景路径
		List<SceneRouteEquip> sceneRouteEquips = new ArrayList<>();// 待保存的场景路径设备
		List<NodeRoute> nodeRoutes = NodeRouteService.queryAll(coreConnection.getConnectionSource());
		for(NodeRoute nodeRoute : nodeRoutes) {
			sceneRoutes.add(createSceneRouteFromNodeRoute(coreConnection, nodeRoute));
			SceneRouteEquipService.deleteRouteEquipsByRouteId(sceneConnection.getConnectionSource(), nodeRoute.getId());
			sceneRouteEquips.addAll(createSceneRouteEquipFromNodeRoute(coreConnection, nodeRoute));
		}
		SceneRouteService.save(sceneConnection.getConnectionSource(), sceneRoutes);
		SceneRouteEquipService.save(sceneConnection.getConnectionSource(), sceneRouteEquips);
	}

	/**
	 * 现场路径数据和场景路径数据合并，存在相同的路径则以现场为准。
	 * <li>相同路径，以现场路径属性覆盖场景路径属性，删除场景路径设备，然后导入现场路径设备</li>
	 * 
	 * @param coreConnection
	 * @param sceneConnection
	 * @throws SQLException
	 */
	public static void mergeWithSite(DBConnection coreConnection, DBConnection sceneConnection) throws SQLException {
		List<NodeRoute> nodeRoutes = NodeRouteService.queryAll(coreConnection.getConnectionSource());
		List<SceneRoute> sceneRoutes = new ArrayList<>();// 待保存的场景路径
		List<SceneRouteEquip> sceneRouteEquips = new ArrayList<>();// 待保存的场景路径设备
		for(NodeRoute nodeRoute : nodeRoutes) {
			SceneRoute sceneRoute = SceneRouteService.queryById(sceneConnection.getConnectionSource(), nodeRoute.getId());
			if(sceneRoute == null) {
				sceneRoutes.add(createSceneRouteFromNodeRoute(coreConnection, nodeRoute));
				SceneRouteEquipService.deleteRouteEquipsByRouteId(sceneConnection.getConnectionSource(), nodeRoute.getId());
				sceneRouteEquips.addAll(createSceneRouteEquipFromNodeRoute(coreConnection, nodeRoute));
			} else {
				sceneRoutes.add(updateSceneRouteFromNodeRoute(sceneRoute, nodeRoute));
				SceneRouteEquipService.deleteRouteEquipsByRouteId(sceneConnection.getConnectionSource(), nodeRoute.getId());
				sceneRouteEquips.addAll(createSceneRouteEquipFromNodeRoute(coreConnection, nodeRoute));
			}
		}
		SceneRouteService.save(sceneConnection.getConnectionSource(), sceneRoutes);
		SceneRouteEquipService.save(sceneConnection.getConnectionSource(), sceneRouteEquips);
	}

	/**
	 * 现场路径数据和场景路径数据合并，存在相同的路径则以场景为准。
	 * <li>相同路径，场景不处理，只导入场景中不存在的现场路径数据和对应路径设备</li>
	 * 
	 * @param coreConnection
	 * @param sceneConnection
	 * @throws SQLException
	 */
	public static void mergeWithScene(DBConnection coreConnection, DBConnection sceneConnection) throws SQLException {
		List<NodeRoute> nodeRoutes = NodeRouteService.queryAll(coreConnection.getConnectionSource());
		List<SceneRoute> sceneRoutes = new ArrayList<>();// 待保存的场景路径
		List<SceneRouteEquip> sceneRouteEquips = new ArrayList<>();// 待保存的场景路径设备
		for(NodeRoute nodeRoute : nodeRoutes) {
			SceneRoute sceneRoute = SceneRouteService.queryById(sceneConnection.getConnectionSource(), nodeRoute.getId());
			if(sceneRoute == null) {
				sceneRoutes.add(createSceneRouteFromNodeRoute(coreConnection, nodeRoute));
				SceneRouteEquipService.deleteRouteEquipsByRouteId(sceneConnection.getConnectionSource(), nodeRoute.getId());
				sceneRouteEquips.addAll(createSceneRouteEquipFromNodeRoute(coreConnection, nodeRoute));
			}
		}
		SceneRouteService.save(sceneConnection.getConnectionSource(), sceneRoutes);
		SceneRouteEquipService.save(sceneConnection.getConnectionSource(), sceneRouteEquips);
	}

	/**
	 * 根据现场路径生成场景的路径
	 * 
	 * @param coreConnection
	 * @param nodeRoute
	 * @return
	 * @throws SQLException
	 */
	private static SceneRoute createSceneRouteFromNodeRoute(DBConnection coreConnection, NodeRoute nodeRoute) throws SQLException {
		SceneRoute sceneRoute = new SceneRoute();
		sceneRoute.setId(nodeRoute.getId());
		sceneRoute.setSourceWhsAreaId(nodeRoute.getSourcewhsareaid().getId());
		sceneRoute.setDescWhsAreaId(nodeRoute.getDescwhsareaid().getId());
		sceneRoute.setWeight(nodeRoute.getWeight());
		sceneRoute.setControlId(nodeRoute.getControlid());
		sceneRoute.setWriteSource(nodeRoute.getWritesource());
		sceneRoute.setClearSource(nodeRoute.getClearsource());
		sceneRoute.setWriteDesc(nodeRoute.getWritedesc());
		sceneRoute.setLoadNotify(nodeRoute.getLoadnotify());
		sceneRoute.setUnloadNotify(nodeRoute.getUnloadnotify());
		sceneRoute.setAverageTims(nodeRoute.getAveragetime());
		sceneRoute.setIsFinalRoute(nodeRoute.getIsfinalroute());
		sceneRoute.setConstraints(nodeRoute.getConstraint());
		sceneRoute.setControlNum(NodeSystemControlService.getControlNumByControlId(coreConnection.getConnectionSource(), nodeRoute.getControlid()));
		sceneRoute.setSourceWhsAreaNum(nodeRoute.getSourcewhsareaid().getWhsareanum());
		sceneRoute.setDescWhsAreaNum(nodeRoute.getDescwhsareaid().getWhsareanum());
		return sceneRoute;
	}

	/**
	 * 根据现场路径生成场景的路径设备
	 * 
	 * @param coreConnection
	 * @param nodeRoute
	 * @return
	 * @throws SQLException
	 */
	private static List<SceneRouteEquip> createSceneRouteEquipFromNodeRoute(DBConnection coreConnection, NodeRoute nodeRoute) throws SQLException {
		List<SceneRouteEquip> sceneRouteEquips = new ArrayList<>();
		// 路径的设备数量小于2，则重新生成；路径的设备数量大于2，则导出
		List<NodeRouteEquip> nodeRouteEquips = NodeRouteEquipService.getRouteEquipsByRouteId(coreConnection.getConnectionSource(), nodeRoute.getId());
		if(nodeRouteEquips.size() < 2) {
			NodeEquip source = NodeEquipService.getByEquipCode(coreConnection.getConnectionSource(), nodeRoute.getSourcewhsareaid().getInternalcode());
			NodeEquip dest = NodeEquipService.getByEquipCode(coreConnection.getConnectionSource(), nodeRoute.getDescwhsareaid().getInternalcode());
			if(source != null) {
				SceneRouteEquip sceneRouteEquip = new SceneRouteEquip();
				sceneRouteEquip.setId(UUID.randomUUID().toString().toLowerCase().replaceAll("-", ""));
				sceneRouteEquip.setRouteId(nodeRoute.getId());
				sceneRouteEquip.setEquipId(source.getEquipid());
				sceneRouteEquip.setSeq(1);
				sceneRouteEquips.add(sceneRouteEquip);
			}
			if(dest != null) {
				SceneRouteEquip sceneRouteEquip = new SceneRouteEquip();
				sceneRouteEquip.setId(UUID.randomUUID().toString().toLowerCase().replaceAll("-", ""));
				sceneRouteEquip.setRouteId(nodeRoute.getId());
				sceneRouteEquip.setEquipId(dest.getEquipid());
				sceneRouteEquip.setSeq(2);
				sceneRouteEquips.add(sceneRouteEquip);
			}
		} else {
			for(NodeRouteEquip nodeRouteEquip : nodeRouteEquips) {
				SceneRouteEquip sceneRouteEquip = new SceneRouteEquip();
				sceneRouteEquip.setId(nodeRouteEquip.getId());
				sceneRouteEquip.setRouteId(nodeRouteEquip.getRouteid());
				sceneRouteEquip.setEquipId(nodeRouteEquip.getEquipid());
				sceneRouteEquip.setSeq(nodeRouteEquip.getLinenum());
				sceneRouteEquips.add(sceneRouteEquip);
			}
		}
		return sceneRouteEquips;
	}

	/**
	 * 根据现场路径来更新场景路径的属性
	 * 
	 * @param sceneRoute
	 * @param nodeRoute
	 * @return
	 */
	private static SceneRoute updateSceneRouteFromNodeRoute(SceneRoute sceneRoute, NodeRoute nodeRoute) {
		sceneRoute.setSourceWhsAreaId(nodeRoute.getSourcewhsareaid().getId());
		sceneRoute.setDescWhsAreaId(nodeRoute.getDescwhsareaid().getId());
		sceneRoute.setWeight(nodeRoute.getWeight());
		sceneRoute.setControlId(nodeRoute.getControlid());
		sceneRoute.setWriteSource(nodeRoute.getWritesource());
		sceneRoute.setClearSource(nodeRoute.getClearsource());
		sceneRoute.setWriteDesc(nodeRoute.getWritedesc());
		sceneRoute.setLoadNotify(nodeRoute.getLoadnotify());
		sceneRoute.setUnloadNotify(nodeRoute.getUnloadnotify());
		sceneRoute.setAverageTims(nodeRoute.getAveragetime());
		sceneRoute.setIsFinalRoute(nodeRoute.getIsfinalroute());
		sceneRoute.setConstraints(nodeRoute.getConstraint());
		sceneRoute.setSourceWhsAreaNum(nodeRoute.getSourcewhsareaid().getWhsareanum());
		sceneRoute.setDescWhsAreaNum(nodeRoute.getDescwhsareaid().getWhsareanum());
		return sceneRoute;
	}

	/**
	 * 根据实体生成对应的insert语句
	 * 
	 * @param <T>
	 * @param entity
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> String generateInsertSQL(T entity) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = entity.getClass();
		String tableName = null;
		for(Annotation annotation : clazz.getAnnotations()) {
			if(DatabaseTable.class.isAssignableFrom(annotation.annotationType())) {
				tableName = AnnotationUtil.getAnnotationValue(annotation, "tableName").toString();
				break;
			}
		}
		if(tableName != null) {
			List<String> columnNameList = new ArrayList<>();
			List<Object> columnValueList = new ArrayList<>();
			for(Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				for(Annotation annotation : field.getAnnotations()) {
					if(DatabaseField.class.isAssignableFrom(annotation.annotationType())) {
						String columnName = AnnotationUtil.getAnnotationValue(annotation, "columnName").toString();
						Object columnValue = field.get(entity);
						String foreignColumnName = (String) AnnotationUtil.getAnnotationValue(annotation, "foreignColumnName");
						// 外键属性要从外键属性对应中获取值
						if(foreignColumnName != null && !foreignColumnName.isEmpty()) {
							for(Field foreignField : columnValue.getClass().getDeclaredFields()) {
								foreignField.setAccessible(true);
								boolean found = false;
								for(Annotation foreignAnnotation : foreignField.getAnnotations()) {
									if(DatabaseField.class.isAssignableFrom(foreignAnnotation.annotationType())) {
										String columnName2 = AnnotationUtil.getAnnotationValue(foreignAnnotation, "columnName").toString();
										if(foreignColumnName.equals(columnName2)) {
											columnValue = foreignField.get(columnValue);
											found = true;
											break;
										}
									}
								}
								if(found)
									break;
							}
						}
						columnNameList.add(columnName);
						columnValueList.add(columnValue);
					}
				}
			}
			if(!columnNameList.isEmpty()) {
				StringBuilder sqlBuilder = new StringBuilder();
				sqlBuilder.append("INSERT INTO " + tableName + " (");
				for(int i = 0; i < columnNameList.size(); i++) {
					String colunmName = columnNameList.get(i);
					if(i < columnNameList.size() - 1)
						sqlBuilder.append(colunmName + ", ");
					else
						sqlBuilder.append(colunmName + ") VALUES (");
				}
				for(int i = 0; i < columnValueList.size(); i++) {
					Object columnValue = columnValueList.get(i);
					String columnSQLValue = null;
					if(columnValue == null)
						columnSQLValue = "null";
					else if(columnValue instanceof String)
						columnSQLValue = "'" + columnValue.toString() + "'";
					else
						columnSQLValue = columnValue.toString();
					if(i < columnValueList.size() - 1)
						sqlBuilder.append(columnSQLValue + ", ");
					else
						sqlBuilder.append(columnSQLValue + ");");
				}
				return sqlBuilder.toString();
			}
		}
		return null;
	}

}