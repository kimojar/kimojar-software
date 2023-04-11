/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3
 * FILE Listener.java
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
import java.awt.Component;
import java.awt.Window;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import com.kimojar.ironman.mark.wcs3.assist.IAssistant;
import com.kimojar.util.common.gui.ComponentDict;

/**
 * @author KiMoJar
 * @date 2023-02-28
 */
public class Listener {

	/** assistant树模型 */
	public static TreeModel assistantTreeModel(IAssistant rootAssistant) {
		return new DefaultTreeModel(new DefaultMutableTreeNode(rootAssistant) {

			private static final long serialVersionUID = 5870626090554525273L;
			{
				rootAssistant.getSubAssistants().forEach(ass -> {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(ass);
					add(node);
					if(ass.hasSubAssistant())
						ass.getSubAssistants().forEach(child -> node.add(new DefaultMutableTreeNode(child)));
				});
			}
		});
	}

	/** assistant树渲染器 */
	public static TreeCellRenderer assistantTreeCellRender() {
		return new DefaultTreeCellRenderer() {

			private static final long serialVersionUID = 8798362507939788051L;

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				IAssistant assistant = (IAssistant) node.getUserObject();
				setIcon(null);
				setText(assistant.name());
				return this;
			}
		};
	}

	/** assistant树监听器 */
	public static TreeSelectionListener assistantTreeSelectionListener() {
		return new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				JPanel centralPanel = ComponentDict.lookup(WCS3I18N.JPanel_Assistant);
				centralPanel.removeAll();
				IAssistant assistant = (IAssistant) node.getUserObject();
				centralPanel.add(assistant.getPanel(), BorderLayout.CENTER);
				updateUILater();
			}
		};
	}

	public static void updateUI() {
		for(Window w : Window.getWindows())
			SwingUtilities.updateComponentTreeUI(w);
	}

	public static void updateUILater() {
		SwingUtilities.invokeLater(() -> {
			for(Window w : Window.getWindows())
				SwingUtilities.updateComponentTreeUI(w);
		});
	}

}
