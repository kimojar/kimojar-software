/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wapp
 * PACKAGE com.kimojar.ironman.mark.wapp
 * FILE Listener.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-19
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

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.lang3.StringUtils;

import com.kimojar.util.common.file.FileHelper;
import com.kimojar.util.common.gui.ComponentDict;
import com.kimojar.util.common.gui.IconUtil;

/**
 * @author KiMoJar
 * @date 2022-12-19
 */
public class Listener {

	private static final String pathSeparator = "#";

	public static TreeModel appTreeModel() {
		return new DefaultTreeModel(new DefaultMutableTreeNode("WinApps") {

			private static final long serialVersionUID = 1L;
			{
				Map<String, List<WAppInfo>> apps = WAppInfo.loadWAppInfo();
				Map<String, DefaultMutableTreeNode> categroyNodeMap = new HashMap<>();
				apps.keySet().forEach(categroy -> {
					if(categroy.contains(pathSeparator) && categroy.split(pathSeparator).length >= 2) {
						String[] categroys = categroy.split(pathSeparator);
						String subCategoryPath = "";
						String superCategoryPath = "";
						for(int i = 0; i < categroys.length; i++) {
							String categroyOne = categroys[i];
							superCategoryPath = subCategoryPath;
							if(subCategoryPath.isEmpty())
								subCategoryPath += categroyOne;
							else
								subCategoryPath += pathSeparator + categroyOne;
							if(!categroyNodeMap.containsKey(subCategoryPath)) {
								DefaultMutableTreeNode categroyNode = new DefaultMutableTreeNode(subCategoryPath);
								categroyNodeMap.put(subCategoryPath, categroyNode);
								if(apps.containsKey(subCategoryPath)) {
									apps.get(subCategoryPath).forEach(app -> {
										DefaultMutableTreeNode appNode = new DefaultMutableTreeNode(app);
										categroyNode.add(appNode);
									});
								}
								if(superCategoryPath.isEmpty())
									add(categroyNode);
								else
									categroyNodeMap.get(superCategoryPath).add(categroyNode);
							}
						}
					} else {
						DefaultMutableTreeNode categroyNode = new DefaultMutableTreeNode(categroy);
						categroyNodeMap.put(categroy, categroyNode);
						add(categroyNode);
						apps.get(categroy).forEach(app -> {
							DefaultMutableTreeNode appNode = new DefaultMutableTreeNode(app);
							categroyNode.add(appNode);
						});
					}
				});
			}
		});
	}

	public static TreeCellRenderer appTreeCellRender() {
		return new DefaultTreeCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				String text = "";
				if(node.getUserObject() instanceof WAppInfo)
					text = ((WAppInfo) node.getUserObject()).name;
				else if(node.getUserObject() instanceof String) {
					text = node.getUserObject().toString();
					if(text.contains(pathSeparator))
						text = text.substring(text.lastIndexOf(pathSeparator) + 1, text.length());
				}
				super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				setIcon(null);
				setText(text);
				// if(!leaf || node.getUserObject() instanceof String)
				// setText("<html><h2>" + getText() + "</h2></html>");
				// else
				// setText("<html><h3>" + getText() + "</h3></html>");
				// if(hasFocus && leaf)
				// setFont(getFont().deriveFont(Font.ITALIC));
				// else
				// setFont(getFont().deriveFont(Font.BOLD));
				return this;
			}
		};
	}

	public static TreeSelectionListener appTreeSelectionListener() {
		return new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				JTree cmdTree = ComponentDict.lookup(WAppI18N.JTree_CmdTree);
				if(!newNode.isLeaf() || (newNode.getUserObject() instanceof String)) {
					setAppDetialComponent(null);// selected category, clear component value
					setCmdDetialComponent(null);
				} else {
					// selected app, set app detial to component value
					setAppDetialComponent((WAppInfo) newNode.getUserObject());
					setCmdDetialComponent(null);
					// set command tree
					cmdTree.setModel(Listener.cmdTreeModel((WAppInfo) newNode.getUserObject()));
				}
			}
		};
	}

	public static JPopupMenu appTreePopupMenu() {
		return new JPopupMenu("category") {

			private static final long serialVersionUID = -7411507788850341924L;
			{
				JMenuItem addCategoryMenuItem = new JMenuItem();
				add(ComponentDict.record(WAppI18N.JMenuItem_AddCategory, addCategoryMenuItem));
				addCategoryMenuItem.setText(WAppI18N.JMenuItem_AddCategory.i18n());
				addCategoryMenuItem.addMouseListener(Listener.addCategoryMenuItemClicked());
				JMenuItem renameCategoryMenuItem = new JMenuItem();
				add(ComponentDict.record(WAppI18N.JMenuItem_RenameCategory, renameCategoryMenuItem));
				renameCategoryMenuItem.setText(WAppI18N.JMenuItem_RenameCategory.i18n());
				renameCategoryMenuItem.addMouseListener(Listener.renameCategoryMenuItemClicked());
			}
		};
	}

	public static TreeModel cmdTreeModel(WAppInfo app) {
		WCommand cmd = app.command;
		if(cmd == null) {
			cmd = new WCommand(app.name, "");
			app.command = cmd;
			cmd.addCommand(new WCommand("-", "start " + disposePathSpace(app.executePath)));
			cmd.addCommand(new WCommand("?", "goto Begin"));
			if(StringUtils.isEmpty(app.version))
				cmd.addCommand(new WCommand("vn", "echo..."));
			else
				cmd.addCommand(new WCommand("vn", "start mshta vbscript:msgbox(\"%" + app.name + WAppInfo.Field_Version + "%\",64,\"" + app.name + " Version\")(window.close)"));
			if(StringUtils.isEmpty(app.directory))
				cmd.addCommand(new WCommand("dir", "echo..."));
			else
				cmd.addCommand(new WCommand("dir", "start explorer %" + app.name + WAppInfo.Field_Directory + "%"));
			if(StringUtils.isEmpty(app.executePath))
				cmd.addCommand(new WCommand("exe", "echo..."));
			else
				cmd.addCommand(new WCommand("exe", "start %" + app.name + WAppInfo.Field_ExecutePath + "%"));
			if(StringUtils.isEmpty(app.uninstallPath))
				cmd.addCommand(new WCommand("uins", "echo..."));
			else
				cmd.addCommand(new WCommand("uins", "start %" + app.name + WAppInfo.Field_UninstallPath + "%"));
			if(StringUtils.isEmpty(app.dataPath))
				cmd.addCommand(new WCommand("data", "echo..."));
			else
				cmd.addCommand(new WCommand("data", "start explorer %" + app.name + WAppInfo.Field_DataPath + "%"));
			if(StringUtils.isEmpty(app.configPath))
				cmd.addCommand(new WCommand("cfg", "echo..."));
			else
				cmd.addCommand(new WCommand("cfg", "start explorer %" + app.name + WAppInfo.Field_ConfigPath + "%"));
			if(StringUtils.isEmpty(app.website))
				cmd.addCommand(new WCommand("ws", "echo..."));
			else
				cmd.addCommand(new WCommand("ws", "start %" + app.name + WAppInfo.Field_Website + "%"));
			if(StringUtils.isEmpty(app.download))
				cmd.addCommand(new WCommand("dl", "echo..."));
			else
				cmd.addCommand(new WCommand("dl", "start %" + app.name + WAppInfo.Field_Download + "%"));
			if(StringUtils.isEmpty(app.manualOnline))
				cmd.addCommand(new WCommand("mn", "echo..."));
			else
				cmd.addCommand(new WCommand("mn", "start %" + app.name + WAppInfo.Field_ManualOnline + "%"));
			if(StringUtils.isEmpty(app.manualOffline))
				cmd.addCommand(new WCommand("mno", "echo..."));
			else
				cmd.addCommand(new WCommand("mno", "start %" + app.name + WAppInfo.Field_ManualOffline + "%"));
			if(StringUtils.isEmpty(app.github))
				cmd.addCommand(new WCommand("gh", "echo..."));
			else
				cmd.addCommand(new WCommand("gh", "start %" + app.name + WAppInfo.Field_Github + "%"));
			if(StringUtils.isEmpty(app.gitee))
				cmd.addCommand(new WCommand("gt", "echo..."));
			else
				cmd.addCommand(new WCommand("gt", "start %" + app.name + WAppInfo.Field_Gitee + "%"));
		}
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(cmd);
		cmd.childCmds.forEach(childCmd -> {
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childCmd);
			rootNode.add(childNode);
		});
		return new DefaultTreeModel(rootNode);
	}

	public static TreeCellRenderer cmdTreeCellRender() {
		return new DefaultTreeCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				if(node.getUserObject() instanceof WCommand)
					value = ((WCommand) node.getUserObject()).cmd;
				super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				setIcon(null);
				// if(hasFocus)
				// setFont(getFont().deriveFont(Font.ITALIC));
				// else
				// setFont(getFont().deriveFont(Font.BOLD));
				return this;
			}
		};
	}

	public static TreeSelectionListener cmdTreeSelectionListener() {
		return new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				setCmdDetialComponent((WCommand) newNode.getUserObject());
			}
		};
	}

	public static JPopupMenu cmdTreePopupMenu() {
		return new JPopupMenu("command") {

			private static final long serialVersionUID = -4753951676330697508L;
			{
				JMenuItem addCmdMenuItem = new JMenuItem();
				add(ComponentDict.record(WAppI18N.JMenuItem_AddCmd, addCmdMenuItem));
				addCmdMenuItem.setText(WAppI18N.JMenuItem_AddCmd.i18n());
				addCmdMenuItem.addMouseListener(Listener.addCmdMenuItemClicked());
				JMenuItem deleteCmdMenuItem = new JMenuItem();
				add(ComponentDict.record(WAppI18N.JMenuItem_DeleteCmd, deleteCmdMenuItem));
				deleteCmdMenuItem.setText(WAppI18N.JMenuItem_DeleteCmd.i18n());
				deleteCmdMenuItem.addMouseListener(Listener.deleteCmdMenuItemClicked());
				JMenuItem renameCmdMenuItem = new JMenuItem();
				add(ComponentDict.record(WAppI18N.JMenuItem_RenameCmd, renameCmdMenuItem));
				renameCmdMenuItem.setText(WAppI18N.JMenuItem_RenameCmd.i18n());
				renameCmdMenuItem.addMouseListener(Listener.renameCmdMenuItemClicked());
			}
		};
	}

	public static ComboBoxModel<String> categoryComboBoxModel() {
		Object[] keyArray = WAppInfo.loadWAppInfo().keySet().toArray();
		String[] items = new String[keyArray.length];
		for(int i = 0; i < items.length; i++)
			items[i] = keyArray[i].toString();
		return new DefaultComboBoxModel<String>(items);
	}

	/**
	 * @return
	 */
	public static ListCellRenderer<? super String> categoryComboxBoxRender() {
		return new DefaultListCellRenderer() {

			private static final long serialVersionUID = 3369465702235226362L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value != null)
					if(value.toString().contains(pathSeparator))
						this.setText(value.toString().replaceAll(pathSeparator, "\\" + File.separator));
				return this;
			}
		};
	}

	public static ItemListener categoryComboBoxItemListener() {
		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String newCategory = (String) e.getItem();
					JTree appTree = ComponentDict.lookup(WAppI18N.JTree_AppTree);
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) appTree.getLastSelectedPathComponent();
					if(node.isLeaf() && node.getUserObject() instanceof WAppInfo) {
						WAppInfo app = (WAppInfo) node.getUserObject();
						String oldCategory = app.category;
						if(!oldCategory.equals(newCategory)) {
							WAppInfo.deleteWAppInfo(app);
							app.category = newCategory;
							WAppInfo.addWAppInfo(app);
							node.removeFromParent();
							DefaultMutableTreeNode categoryNode = (DefaultMutableTreeNode) getTreeNode(appTree, newCategory);
							categoryNode.insert(node, categoryNode.getChildCount());
							node.setParent(categoryNode);
							appTree.setSelectionPath(new TreePath(node.getPath()));
							appTree.scrollPathToVisible(new TreePath(node.getPath()));
							SwingUtilities.invokeLater(() -> SwingUtilities.updateComponentTreeUI(appTree));
						}
					}
				}
			}
		};
	}

	public static MouseListener appDetialTextFieldMouseExitedListener() {
		return new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				JTree appTree = ComponentDict.lookup(WAppI18N.JTree_AppTree);
				if(appTree.getLastSelectedPathComponent() == null)
					return;
				if(e.getComponent() instanceof JTextField) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) appTree.getLastSelectedPathComponent();
					if(node.isLeaf() && node.getUserObject() instanceof WAppInfo) {
						WAppInfo app = (WAppInfo) node.getUserObject();
						JTextField textField = (JTextField) e.getComponent();
						if(textField == ComponentDict.lookup(WAppI18N.JTextField_Name))
							app.name = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_Version))
							app.version = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_Directory))
							app.directory = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_ExecutePath))
							app.executePath = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_UninstallPath))
							app.uninstallPath = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_DataPath))
							app.dataPath = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_ConfigPath))
							app.configPath = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_Website))
							app.website = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_Download))
							app.download = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_ManualOnline))
							app.manualOnline = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_ManualOffline))
							app.manualOffline = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_GitHub))
							app.github = textField.getText().trim();
						else if(textField == ComponentDict.lookup(WAppI18N.JTextField_Gitee))
							app.gitee = textField.getText().trim();
					}
				} else if(e.getComponent() instanceof JTextArea) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) appTree.getLastSelectedPathComponent();
					if(node.isLeaf() && node.getUserObject() instanceof WAppInfo) {
						WAppInfo app = (WAppInfo) node.getUserObject();
						JTextArea textArea = (JTextArea) e.getComponent();
						if(textArea == ComponentDict.lookup(WAppI18N.JTextArea_Description))
							app.description = textArea.getText();
					}
				}
			}
		};
	}

	public static MouseListener iconLabelMousePressedListener() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				JTree appTree = ComponentDict.lookup(WAppI18N.JTree_AppTree);
				File iconFile = FileHelper.chooserFile(WAppI18N.Text_ChooseAppIcon.i18n(), "png", "png");
				if(iconFile == null || !iconFile.exists())
					return;
				JLabel iconLabel = ComponentDict.lookup(WAppI18N.JLabel_Icon);
				ImageIcon icon = null;
				try {
					icon = new ImageIcon(iconFile.toURI().toURL());
					iconLabel.putClientProperty(WAppI18N.Key_IconLabelIconPath, iconFile.getPath());
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) appTree.getLastSelectedPathComponent();
					if(node != null && node.isLeaf()) {
						WAppInfo app = (WAppInfo) node.getUserObject();
						File localIconDir = new File(System.getProperty("user.dir") + File.separator + "resource" + File.separator + "icon");
						if(!localIconDir.exists())
							localIconDir.mkdirs();
						File copyIconFile = new File(localIconDir.getPath() + File.separator + app.name + ".png");
						Files.copy(iconFile.toPath(), copyIconFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
						app.iconPath = copyIconFile.getPath();
					}
				} catch(Exception e1) {
					e1.printStackTrace();
					icon = null;
					JOptionPane.showMessageDialog(null, WAppI18N.Text_IconSetError.i18n() + e1, ((JButton) e.getComponent()).getText(), JOptionPane.ERROR_MESSAGE);
				}
				if(icon == null)
					icon = new ImageIcon(Listener.class.getResource(DefaultIconPath));
				iconLabel.setIcon(IconUtil.createAutoAdjustIcon(icon.getImage(), true));
			}
		};
	}

	public static MouseListener directoryLabelClicked(WAppI18N directoryLabelId, WAppI18N directoryTextFieldId) {
		return new MouseAdapter() {

			/**
			 * @param e
			 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				JTextField directoryTextField = ComponentDict.lookup(directoryTextFieldId);
				if(directoryTextField == null)
					return;
				try {
					File appDirectory = new File(directoryTextField.getText().trim());
					Desktop.getDesktop().open(appDirectory);
				} catch(IOException | IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(null, String.format(WAppI18N.Text_DirectoryNotExist.i18n(), directoryTextField.getText().trim()), directoryLabelId.i18n(),
					JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	public static MouseListener websiteLabelClicked(WAppI18N websiteLabelId, WAppI18N websiteTextFieldId) {
		return new MouseAdapter() {

			/**
			 * @param e
			 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				JTextField websiteTextField = ComponentDict.lookup(websiteTextFieldId);
				if(websiteTextField == null)
					return;
				try {
					URI uri = new URI(websiteTextField.getText().trim());
					Desktop.getDesktop().browse(uri);
				} catch(IOException | IllegalArgumentException | URISyntaxException ex) {
					JOptionPane.showMessageDialog(null, String.format(WAppI18N.Text_InvalidURL.i18n(), websiteTextField.getText().trim()), websiteLabelId.i18n(),
					JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	public static MouseListener cmdActionTextFieldMouseExitedListener() {
		return new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {// 鼠标右键点击保存
				JTree cmdTree = ComponentDict.lookup(WAppI18N.JTree_CmdTree);
				if(cmdTree.getLastSelectedPathComponent() == null)
					return;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) cmdTree.getLastSelectedPathComponent();
				if(e.getComponent() instanceof JTextArea) {
					JTextArea textArea = (JTextArea) e.getComponent();
					WCommand cmd = (WCommand) node.getUserObject();
					if(textArea == ComponentDict.lookup(WAppI18N.JTextArea_CmdAction))
						cmd.action = textArea.getText().trim();
				}
			}
		};
	}

	public static MouseListener cmdActionButtonClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				JTree appTree = ComponentDict.lookup(WAppI18N.JTree_AppTree);
				JTree cmdTree = ComponentDict.lookup(WAppI18N.JTree_CmdTree);
				DefaultMutableTreeNode appNode = (DefaultMutableTreeNode) appTree.getLastSelectedPathComponent();
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) cmdTree.getLastSelectedPathComponent();
				if(appNode != null && appNode.getUserObject() instanceof WAppInfo) {
					if(treeNode != null && treeNode.getUserObject() instanceof WCommand) {
						JTextArea actionTextArea = ComponentDict.lookup(WAppI18N.JTextArea_CmdAction);
						String command = actionTextArea.getText().trim();
						if(command.isEmpty())
							return;
						try {
							Runtime.getRuntime().exec("cmd /k " + command);
						} catch(IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		};
	}

	public static MouseListener variableListClicked() {
		return new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					JTree appTree = ComponentDict.lookup(WAppI18N.JTree_AppTree);
					JTree cmdTree = ComponentDict.lookup(WAppI18N.JTree_CmdTree);
					JList<String> varList = ComponentDict.lookup(WAppI18N.JList_CmdVariable);
					DefaultMutableTreeNode appNode = (DefaultMutableTreeNode) appTree.getLastSelectedPathComponent();
					DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) cmdTree.getLastSelectedPathComponent();
					if(appNode != null && appNode.getUserObject() instanceof WAppInfo) {
						if(treeNode != null && treeNode.getUserObject() instanceof WCommand) {
							if(!varList.isSelectionEmpty()) {
								String value = varList.getSelectedValue();
								JTextArea actionTextArea = ComponentDict.lookup(WAppI18N.JTextArea_CmdAction);
								// String action = actionTextArea.getText().trim();
								// actionTextArea.setText(action + " %" + value + "%");
								actionTextArea.insert("%" + value + "%", actionTextArea.getCaretPosition());
							}
						}
					}
				}
			}
		};
	}

	public static MouseListener saveAllButtonClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				SwingUtilities.invokeLater(() -> {
					JTree appTree = ComponentDict.lookup(WAppI18N.JTree_AppTree);
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) appTree.getLastSelectedPathComponent();
					if(node == null || node.getUserObject() instanceof String) {
						JTextField nameTextField = ComponentDict.lookup(WAppI18N.JTextField_Name);
						JComboBox<String> categoryCombox = ComponentDict.lookup(WAppI18N.JComboBox_Category);
						if(StringUtils.isEmpty(nameTextField.getText().trim())) {
							JOptionPane.showMessageDialog(null, WAppI18N.Text_AppNameCannotBeEmpty.i18n(), ((JButton) e.getComponent()).getText(), JOptionPane.ERROR_MESSAGE);
						} else if(categoryCombox.getSelectedItem() == null) {
							JOptionPane.showMessageDialog(null, WAppI18N.Text_AppCategoryMustBeSelect.i18n(), ((JButton) e.getComponent()).getText(), JOptionPane.ERROR_MESSAGE);
						} else {
							WAppInfo newApp = new WAppInfo();
							newApp.name = nameTextField.getText().trim();
							newApp.category = categoryCombox.getSelectedItem().toString().trim();
							JTextField versionTextField = ComponentDict.lookup(WAppI18N.JTextField_Version);
							newApp.version = versionTextField.getText().trim();
							JTextField directoryTextField = ComponentDict.lookup(WAppI18N.JTextField_Directory);
							newApp.directory = directoryTextField.getText().trim();
							JTextField executePathTextField = ComponentDict.lookup(WAppI18N.JTextField_ExecutePath);
							newApp.executePath = executePathTextField.getText().trim();
							JTextField uninstallPathTextField = ComponentDict.lookup(WAppI18N.JTextField_UninstallPath);
							newApp.uninstallPath = uninstallPathTextField.getText().trim();
							JTextField dataPathTextField = ComponentDict.lookup(WAppI18N.JTextField_DataPath);
							newApp.dataPath = dataPathTextField.getText().trim();
							JTextField configPathTextField = ComponentDict.lookup(WAppI18N.JTextField_ConfigPath);
							newApp.configPath = configPathTextField.getText().trim();
							JTextField websiteTextField = ComponentDict.lookup(WAppI18N.JTextField_Website);
							newApp.website = websiteTextField.getText().trim();
							JTextField downloadTextField = ComponentDict.lookup(WAppI18N.JTextField_Download);
							newApp.download = downloadTextField.getText().trim();
							JTextField manualOnlineTextField = ComponentDict.lookup(WAppI18N.JTextField_ManualOnline);
							newApp.manualOnline = manualOnlineTextField.getText().trim();
							JTextField manualOfflineTextField = ComponentDict.lookup(WAppI18N.JTextField_ManualOffline);
							newApp.manualOffline = manualOfflineTextField.getText().trim();
							JTextField githubTextField = ComponentDict.lookup(WAppI18N.JTextField_GitHub);
							newApp.github = githubTextField.getText().trim();
							JTextField giteeTextField = ComponentDict.lookup(WAppI18N.JTextField_Gitee);
							newApp.gitee = giteeTextField.getText().trim();
							JTextArea descriptionTextArea = ComponentDict.lookup(WAppI18N.JTextArea_Description);
							newApp.description = descriptionTextArea.getText();

							DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) searchNode((TreeNode) appTree.getModel().getRoot(), newApp.category);
							DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newApp);
							parentNode.insert(newNode, parentNode.getChildCount());
							newNode.setParent(parentNode);
							appTree.setSelectionPath(new TreePath(newNode.getPath()));
							appTree.scrollPathToVisible(new TreePath(newNode.getPath()));
							WAppInfo.addWAppInfo(newApp);
							SwingUtilities.invokeLater(() -> SwingUtilities.updateComponentTreeUI(appTree));
						}
					}
					try {
						// wait focus event has been disposed for app info setting, because mousePressed before focusLost
						Thread.sleep(200);
						WAppInfo.saveWAppInfo();
						JOptionPane.showMessageDialog(null, WAppI18N.Text_SaveSuccess.i18n(), ((JButton) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
					} catch(Exception e1) {
						e1.printStackTrace();
					}
				});
			}
		};
	}

	public static MouseListener deleteButtonClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if(!e.getComponent().isEnabled())
					return;
				JTree appTree = ComponentDict.lookup(WAppI18N.JTree_AppTree);
				DefaultMutableTreeNode appNode = (DefaultMutableTreeNode) appTree.getLastSelectedPathComponent();
				if(appNode == null || appNode.getParent() == null)// appNode.getParent is used to determine whether this node has been delete
					return;
				int result = JOptionPane.showConfirmDialog(null, WAppI18N.Text_AreYouSureToDelete.i18n(), ((JButton) e.getComponent()).getText(), JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);
				// 0=Yes, 1=No, 2=Cancel, -1=closed
				if(result == 0) {
					SwingUtilities.invokeLater(() -> {
						try {
							if(!appNode.isRoot() && appNode.getUserObject() instanceof String) {
								String categroyName = (String) appNode.getUserObject();
								WAppInfo.deleteWAppCategory(categroyName);
								appNode.removeFromParent();
								JComboBox<String> categroyCombox = ComponentDict.lookup(WAppI18N.JComboBox_Category);
								categroyCombox.setModel(Listener.categoryComboBoxModel());
								SwingUtilities.updateComponentTreeUI(appTree);
							} else {
								WAppInfo app = (WAppInfo) appNode.getUserObject();
								appNode.removeFromParent();
								WAppInfo.deleteWAppInfo(app);
								// WAppInfo.saveWAppInfo();
								setAppDetialComponent(null);
								setCmdDetialComponent(null);
								SwingUtilities.updateComponentTreeUI(appTree);
							}
						} catch(Exception e1) {
							e1.printStackTrace();
						}
					});
				}
			}
		};
	}

	public static MouseListener generateCmdButtonClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				String wakeupCmdName = JOptionPane.showInputDialog(null, WAppI18N.Text_PleaseInputJarvisWakeupCmdName.i18n(), ((JButton) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
				if(wakeupCmdName == null || StringUtils.isBlank(wakeupCmdName) || wakeupCmdName.contains("\\") || wakeupCmdName.contains("/") || wakeupCmdName.contains(":") || wakeupCmdName.contains("*")
				|| wakeupCmdName.contains("?") || wakeupCmdName.contains("\"") || wakeupCmdName.contains("<") || wakeupCmdName.contains(">") || wakeupCmdName.contains("|")) {
					JOptionPane.showMessageDialog(null, WAppI18N.Text_JarvisWakeupCmdNameInvalid.i18n(), ((JButton) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
					wakeupCmdName = "jvs";
				}
				CommandGenerator.generateCommand(WAppInfo.loadWAppInfo(), wakeupCmdName);
				Object[] message = new Object[] {
				WAppI18N.Text_GenerateSuccess.i18n(),
				String.format(WAppI18N.Text_GenerateSuccessUsage.i18n(), wakeupCmdName),
				};
				JOptionPane.showMessageDialog(null, message, ((JButton) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
			}
		};
	}

	public static MouseListener importButtonClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				File importFile = FileHelper.chooserFile(WAppI18N.Text_ChooseImportFile.i18n(), "json", "json");
				if(importFile == null || !importFile.exists()) {
					JOptionPane.showMessageDialog(null, WAppI18N.Text_ImportFileNotSelectOrNotExist.i18n(), ((JButton) e.getComponent()).getText(), JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					Map<String, List<WAppInfo>> currentApps = WAppInfo.loadWAppInfo();
					Map<String, List<WAppInfo>> importApps = WAppInfo.loadWAppInfo(importFile);
					for(Entry<String, List<WAppInfo>> entry : importApps.entrySet()) {
						if(!currentApps.containsKey(entry.getKey())) {
							currentApps.put(entry.getKey(), entry.getValue());// 目录不存在，则整个目录的app都导入
						} else {
							for(WAppInfo iapp : entry.getValue()) {
								boolean exist = false;
								for(WAppInfo capp : currentApps.get(entry.getKey())) {
									if(capp.name.equals(iapp.name))
										exist = true;
								}
								if(!exist)
									WAppInfo.addWAppInfo(iapp);// 目录存在，则将当前不存在的app导入
							}
						}
					}
					try {
						WAppInfo.saveWAppInfo();
						JTree appTree = ComponentDict.lookup(WAppI18N.JTree_AppTree);
						appTree.setModel(Listener.appTreeModel());
						JComboBox<String> categroyCombox = ComponentDict.lookup(WAppI18N.JComboBox_Category);
						categroyCombox.setModel(Listener.categoryComboBoxModel());
						SwingUtilities.invokeLater(() -> SwingUtilities.updateComponentTreeUI(appTree));
					} catch(IOException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, WAppI18N.Text_ImportSuccess.i18n(), ((JButton) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
	}

	public static MouseListener exportButtonClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				File dir = FileHelper.chooserFolder(WAppI18N.Text_ChooseExportDirectory.i18n());
				if(dir == null || !dir.exists()) {
					JOptionPane.showMessageDialog(null, WAppI18N.Text_ExportDirectoryNotSelectedOrExportDirectoryNotExist.i18n(), ((JButton) e.getComponent()).getText(), JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					File wappFile = new File(System.getProperty("user.dir") + File.separator + "resource" + File.separator + "wapp.json");
					File exportFile = new File(dir.getPath() + File.separator + "Jarvis.WindowsApplication.json");
					try {
						Files.copy(wappFile.toPath(), exportFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					} catch(IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, WAppI18N.Text_ExportError.i18n() + e1, ((JButton) e.getComponent()).getText(), JOptionPane.ERROR_MESSAGE);
						return;
					}
					JOptionPane.showMessageDialog(null, WAppI18N.Text_ExportSuccess.i18n(), ((JButton) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
	}

	public static MouseListener addCategoryMenuItemClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				String categoryName = JOptionPane.showInputDialog(null, WAppI18N.Text_PleaseInputCategoryName.i18n(), ((JMenuItem) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
				if(!StringUtils.isEmpty(categoryName)) {
					categoryName = categoryName.replaceAll(pathSeparator, "");// 去掉预留的特殊字符
					JTree appTree = ComponentDict.lookup(WAppI18N.JTree_AppTree);
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) appTree.getLastSelectedPathComponent();
					DefaultMutableTreeNode categoryNode = null;
					if(node == null || node.isRoot()) {
						categoryNode = new DefaultMutableTreeNode(categoryName);
						DefaultMutableTreeNode root = (DefaultMutableTreeNode) appTree.getModel().getRoot();
						root.insert(categoryNode, root.getChildCount());
						categoryNode.setParent(root);
					} else if(node.getUserObject() instanceof String) {
						categoryNode = new DefaultMutableTreeNode(categoryName = node.getUserObject().toString() + pathSeparator + categoryName);
						node.insert(categoryNode, node.getChildCount());
						categoryNode.setParent(node);
					} else {
						DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
						categoryNode = new DefaultMutableTreeNode(categoryName = parentNode.getUserObject().toString() + pathSeparator + categoryName);
						parentNode.insert(categoryNode, node.getChildCount());
						categoryNode.setParent(parentNode);
					}
					WAppInfo.addWAppCategory(categoryName);
					appTree.setSelectionPath(new TreePath(categoryNode.getPath()));
					appTree.scrollPathToVisible(new TreePath(categoryNode.getPath()));
					JComboBox<String> categroyCombox = ComponentDict.lookup(WAppI18N.JComboBox_Category);
					categroyCombox.setModel(Listener.categoryComboBoxModel());
					SwingUtilities.invokeLater(() -> SwingUtilities.updateComponentTreeUI(appTree));
				}
			}
		};
	}

	public static MouseListener renameCategoryMenuItemClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				JTree appTree = ComponentDict.lookup(WAppI18N.JTree_AppTree);
				DefaultMutableTreeNode categoryNode = (DefaultMutableTreeNode) appTree.getLastSelectedPathComponent();
				if(categoryNode == null)
					return;
				if(!categoryNode.isRoot() && categoryNode.getUserObject() instanceof String) {
					String newName = JOptionPane.showInputDialog(null, WAppI18N.Text_PleaseInputCategoryNewName.i18n(), ((JMenuItem) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
					if(!StringUtils.isEmpty(newName)) {
						String oldName = (String) categoryNode.getUserObject();
						newName = oldName.substring(0, oldName.lastIndexOf(pathSeparator)+1) + newName;
						if(WAppInfo.renameWAppCategory(oldName, newName)) {
							categoryNode.setUserObject(newName);
							JComboBox<String> categroyCombox = ComponentDict.lookup(WAppI18N.JComboBox_Category);
							categroyCombox.setModel(Listener.categoryComboBoxModel());
							SwingUtilities.invokeLater(() -> SwingUtilities.updateComponentTreeUI(appTree));
						}
					}
				}
			}
		};
	}

	public static MouseListener addCmdMenuItemClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				JTree cmdTree = ComponentDict.lookup(WAppI18N.JTree_CmdTree);
				if(cmdTree.getModel() == null)
					return;
				String cmdName = JOptionPane.showInputDialog(null, WAppI18N.Text_PleaseInputCategoryName.i18n(), ((JMenuItem) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
				if(!StringUtils.isEmpty(cmdName)) {
					DefaultMutableTreeNode root = (DefaultMutableTreeNode) cmdTree.getModel().getRoot();
					WCommand addedCmd = new WCommand(cmdName, null);
					DefaultMutableTreeNode addedNode = new DefaultMutableTreeNode(addedCmd);
					WCommand cmd = (WCommand) root.getUserObject();
					cmd.addCommand(addedCmd);
					root.insert(addedNode, root.getChildCount());
					addedNode.setParent(root);
					cmdTree.setSelectionPath(new TreePath(addedNode.getPath()));
					cmdTree.scrollPathToVisible(new TreePath(addedNode.getPath()));
					SwingUtilities.invokeLater(() -> SwingUtilities.updateComponentTreeUI(cmdTree));
				}
			}
		};
	}

	public static MouseListener deleteCmdMenuItemClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				JTree cmdTree = ComponentDict.lookup(WAppI18N.JTree_CmdTree);
				DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) cmdTree.getModel().getRoot();
				for(TreePath selectedPath : cmdTree.getSelectionPaths()) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
					if(selectedNode == null)
						continue;
					if(selectedNode != rootNode) {
						WCommand cmd = (WCommand) selectedNode.getUserObject();
						WCommand parentCmd = (WCommand) rootNode.getUserObject();
						parentCmd.removeCommand(cmd);
						selectedNode.removeFromParent();
					}
				}
				SwingUtilities.invokeLater(() -> SwingUtilities.updateComponentTreeUI(cmdTree));
			}
		};
	}

	public static MouseListener renameCmdMenuItemClicked() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				JTree cmdTree = ComponentDict.lookup(WAppI18N.JTree_CmdTree);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) cmdTree.getLastSelectedPathComponent();
				if(node == null)
					return;
				String newName = JOptionPane.showInputDialog(null, WAppI18N.Text_PleaseInputCommandName.i18n(), ((JMenuItem) e.getComponent()).getText(), JOptionPane.INFORMATION_MESSAGE);
				if(!StringUtils.isEmpty(newName)) {
					WCommand cmd = (WCommand) node.getUserObject();
					cmd.cmd = newName;
					SwingUtilities.invokeLater(() -> SwingUtilities.updateComponentTreeUI(cmdTree));
				}
			}
		};
	}

	private static void setAppDetialComponent(WAppInfo app) {
		if(app == null)
			app = new WAppInfo();
		JLabel iconLabel = ComponentDict.lookup(WAppI18N.JLabel_Icon);
		iconLabel.setIcon(IconUtil.createAutoAdjustIcon(getIcon(app).getImage(), true));
		JTextField nameTextField = ComponentDict.lookup(WAppI18N.JTextField_Name);
		nameTextField.setText(app.name);
		JTextField versionTextField = ComponentDict.lookup(WAppI18N.JTextField_Version);
		versionTextField.setText(app.version);
		JComboBox<String> categoryComboBox = ComponentDict.lookup(WAppI18N.JComboBox_Category);
		categoryComboBox.setSelectedItem(app.category);
		JTextField directoryTextField = ComponentDict.lookup(WAppI18N.JTextField_Directory);
		directoryTextField.setText(app.directory);
		JTextField executePathTextField = ComponentDict.lookup(WAppI18N.JTextField_ExecutePath);
		executePathTextField.setText(app.executePath);
		JTextField uninstallPathTextField = ComponentDict.lookup(WAppI18N.JTextField_UninstallPath);
		uninstallPathTextField.setText(app.uninstallPath);
		JTextField dataPathTextField = ComponentDict.lookup(WAppI18N.JTextField_DataPath);
		dataPathTextField.setText(app.dataPath);
		JTextField configPathTextField = ComponentDict.lookup(WAppI18N.JTextField_ConfigPath);
		configPathTextField.setText(app.configPath);
		JTextField websiteTextField = ComponentDict.lookup(WAppI18N.JTextField_Website);
		websiteTextField.setText(app.website);
		JTextField downloadTextField = ComponentDict.lookup(WAppI18N.JTextField_Download);
		downloadTextField.setText(app.download);
		JTextField manualOnlineTextField = ComponentDict.lookup(WAppI18N.JTextField_ManualOnline);
		manualOnlineTextField.setText(app.manualOnline);
		JTextField manualOfflineTextField = ComponentDict.lookup(WAppI18N.JTextField_ManualOffline);
		manualOfflineTextField.setText(app.manualOffline);
		JTextField gitHubTextField = ComponentDict.lookup(WAppI18N.JTextField_GitHub);
		gitHubTextField.setText(app.github);
		JTextField giteeTextField = ComponentDict.lookup(WAppI18N.JTextField_Gitee);
		giteeTextField.setText(app.gitee);
		JTextArea descriptionTextArea = ComponentDict.lookup(WAppI18N.JTextArea_Description);
		descriptionTextArea.setText(app.description);
	}

	private static void setCmdDetialComponent(WCommand cmd) {
		JTextArea cmdActionTextArea = ComponentDict.lookup(WAppI18N.JTextArea_CmdAction);
		JList<String> cmdVariableList = ComponentDict.lookup(WAppI18N.JList_CmdVariable);
		if(cmd == null) {
			JTree cmdTree = ComponentDict.lookup(WAppI18N.JTree_CmdTree);
			cmdTree.setModel(null);
			cmdActionTextArea.setText("");
			cmdVariableList.setModel(new DefaultListModel<String>());
			cmdActionTextArea.setEnabled(false);
			cmdVariableList.setEnabled(false);
		} else {
			cmdActionTextArea.setEnabled(true);
			cmdVariableList.setEnabled(true);
			cmdActionTextArea.setText(cmd.action);
			DefaultListModel<String> model = new DefaultListModel<>();
			CommandGenerator.getParameterVariable(WAppInfo.loadWAppInfo()).forEach(var -> model.addElement(var));
			cmdVariableList.setModel(model);
		}
	}

	private static TreeNode getTreeNode(JTree tree, String nodeName) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		return searchNode(root, nodeName);
	}

	private static TreeNode searchNode(TreeNode current, String targetName) {
		if(current.toString().equals(targetName))
			return current;
		Enumeration<?> children = current.children();
		while(children.hasMoreElements()) {
			TreeNode result = searchNode((TreeNode) children.nextElement(), targetName);
			if(result == null)
				continue;
			else
				return result;
		}
		return null;
	}

	private static ImageIcon getIcon(WAppInfo app) {
		ImageIcon icon = null;
		if(app.iconPath != null && !app.iconPath.isEmpty()) {
			try {
				File iconFile = new File(app.iconPath);
				if(iconFile.exists() && iconFile.isFile())
					icon = new ImageIcon(iconFile.toURI().toURL());
			} catch(MalformedURLException e) {
				icon = null;
			}
		}
		if(icon == null) {
			String name = (app.name == null) ? "" : app.name;
			URL iconURL = Listener.class.getResource("resource/appicon/" + name + ".png");
			iconURL = (iconURL == null) ? Listener.class.getResource(DefaultIconPath) : iconURL;
			icon = new ImageIcon(iconURL);
		}
		return icon;
	}

	private static String disposePathSpace(String path) {
		Matcher mattcher = Pattern.compile(":.*\\s.*").matcher(path);
		if(mattcher.find()) {
			String spacePath = mattcher.group();
			path = path.replace(spacePath, "\"" + spacePath + "\"");
		}
		return path;
	}

	protected static void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if(node.getChildCount() >= 0) {
			for(Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}
		// Expansion or collapse must be done bottom-up
		if(expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

	private static final String DefaultIconPath = "resource/Alphabet-K-128x128.png";
}