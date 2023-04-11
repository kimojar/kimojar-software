/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3
 * FILE WCS3Screen.java
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
package com.kimojar.ironman.mark.wcs3;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;

import com.kimojar.ironman.mark.wcs3.assist.IAssistant;
import com.kimojar.ironman.mark.wcs3.assist.RootAssistant;
import com.kimojar.ironman.mark.wcs3.db.DatabasePanel;
import com.kimojar.util.common.gui.ComponentDict;

/**
 * @author KiMoJar
 * @date 2023-02-28
 */
public class WCS3Screen extends JPanel {

	private static final long serialVersionUID = 4980937500298595995L;

	private IAssistant assistant = new RootAssistant();

	public WCS3Screen() {
		ComponentDict.record(WCS3I18N.JPanel_Screen, this);
		ComponentDict.record(WCS3I18N.JPanel_DBPanel, DatabasePanel.instance());
		initComponent();
		layoutComponent();
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		// assistant tree
		JScrollPane assistantScrollPane = new JScrollPane();
		ComponentDict.record(WCS3I18N.JScrollPane_Assistant, assistantScrollPane);
		assistantScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		assistantScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		assistantScrollPane.setBorder(null);
		JTree assistantTree = new JTree();
		assistantScrollPane.setViewportView(ComponentDict.record(WCS3I18N.JTree_Assistant, assistantTree));
		assistantTree.setEditable(false);
		assistantTree.setRootVisible(false);// 不显示root节点
		assistantTree.setShowsRootHandles(true);// 显示折叠图标，部分主题不支持
		assistantTree.setModel(Listener.assistantTreeModel(assistant));
		assistantTree.setCellRenderer(Listener.assistantTreeCellRender());
		assistantTree.addTreeSelectionListener(Listener.assistantTreeSelectionListener());
		assistantTree.updateUI();

		// assistant content
		JPanel centralPanel = new JPanel();
		ComponentDict.record(WCS3I18N.JPanel_Assistant, centralPanel);
		centralPanel.setLayout(new BorderLayout());
	}

	/**
	 * 布局组件
	 */
	private void layoutComponent() {
		setLayout(new BorderLayout());
		add(ComponentDict.lookup(WCS3I18N.JScrollPane_Assistant), BorderLayout.WEST);
		add(ComponentDict.lookup(WCS3I18N.JPanel_Assistant), BorderLayout.CENTER);
	}

}
