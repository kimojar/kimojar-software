/**
 * ==============================================================================
 * PROJECT kimojar-ironman-jarvis-vision
 * PACKAGE com.kimojar.ironman.jarvis.vision.v1
 * FILE Listener.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-12
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
package com.kimojar.ironman.jarvis.vision.v1;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Year;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes.FlatIJLookAndFeelInfo;
import com.kimojar.ironman.jarvis.JarvisApp;
import com.kimojar.util.common.gui.ComponentDict;
import com.kimojar.util.common.i18n.LanguageTag;
import com.kimojar.util.common.i18n.Logger;
import com.kimojar.util.common.i18n.LoggerFactory;

/**
 * @author KiMoJar
 * @date 2022-12-12
 */
public class Listener {

	private static Logger log = LoggerFactory.getLogger(Listener.class);

	public static void aboutMenuItemClicked(ActionEvent event) {
		// logo Label
		JLabel logoLabel = new JLabel();
		logoLabel.setIcon(new ImageIcon(JarvisVision.class.getResource("resource/ironman-red-64.png")));
		logoLabel.setText(VisionI18N.JLabel_Logo.i18n());
		logoLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");
		// offical website
		String officalWebsite = VisionI18N.URI_OfficalWebsite.i18n();
		JLabel officalWebsiteLabel = new JLabel();
		officalWebsiteLabel.setText("<html><a href=\"#\">" + officalWebsite + "</a></html>");
		officalWebsiteLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		officalWebsiteLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(officalWebsite));
				} catch(IOException | URISyntaxException ex) {
					JOptionPane.showMessageDialog(officalWebsiteLabel, String.format(VisionI18N.Txt_AboutFailedToOpenBrowser.i18n(), officalWebsite), VisionI18N.JMenuItem_About.i18n(),
					JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		// offical GitHub
		String officalGitHub = VisionI18N.URI_OfficalGitHub.i18n();
		JLabel officalGitHubLabel = new JLabel();
		officalGitHubLabel.setText("<html><a href=\"#\">" + officalGitHub + "</a></html>");
		officalGitHubLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		officalGitHubLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(officalGitHub));
				} catch(IOException | URISyntaxException ex) {
					JOptionPane.showMessageDialog(officalGitHubLabel, String.format(VisionI18N.Txt_AboutFailedToOpenBrowser.i18n(), officalGitHub), VisionI18N.JMenuItem_About.i18n(),
					JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		// offical Blog
		String officalBlogHub = VisionI18N.URI_OfficalBlog.i18n();
		JLabel officalBlogLabel = new JLabel();
		officalBlogLabel.setText("<html><a href=\"#\">" + officalBlogHub + "</a></html>");
		officalBlogLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		officalBlogLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(officalBlogHub));
				} catch(IOException | URISyntaxException ex) {
					JOptionPane.showMessageDialog(officalBlogLabel, String.format(VisionI18N.Txt_AboutFailedToOpenBrowser.i18n(), officalBlogHub), VisionI18N.JMenuItem_About.i18n(),
					JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		// offical email
		String officalEmail = VisionI18N.URI_OfficalEmail.i18n();
		JLabel officalEmailLabel = new JLabel();
		officalEmailLabel.setText("<html><a href=\"#\">" + officalEmail + "</a></html>");
		officalEmailLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标在组件上时显示的鼠标图标
		officalEmailLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().mail(new URI("mailto://" + officalEmail));
				} catch(IOException | URISyntaxException ex) {
					JOptionPane.showMessageDialog(
					officalEmailLabel,
					String.format(VisionI18N.Txt_AboutFailedToOpenEmail.i18n(),
					officalBlogHub),
					VisionI18N.JMenuItem_About.i18n(),
					JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		Object[] message = new Object[] {
		logoLabel,
		VisionI18N.Txt_AboutDescription.i18n(),
		" ",
		String.format(VisionI18N.Txt_AboutAuthoriation.i18n(), Year.now()),
		officalWebsiteLabel,
		officalGitHubLabel,
		officalBlogLabel,
		officalEmailLabel };
		JOptionPane.showMessageDialog(ComponentDict.lookup(VisionI18N.JFrame_Main), message, VisionI18N.JMenuItem_About.i18n(), JOptionPane.PLAIN_MESSAGE);
	}

	public static void loadFontMenuItemClicked(ActionEvent event) {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(null, "To be continue...", event.getActionCommand(), JOptionPane.PLAIN_MESSAGE);
		});
	}

	public static void loadThemeMenuItemClicked(ActionEvent event) {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(null, "To be continue...", event.getActionCommand(), JOptionPane.PLAIN_MESSAGE);
		});
	}

	public static void aboutCurrentThemeMenuItemClicked(ActionEvent event) {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(null, "To be continue...", event.getActionCommand(), JOptionPane.PLAIN_MESSAGE);
		});
	}

	public static void restoreFontMenuItemClicked(ActionEvent event, JarvisVision vision) {
		UIManager.put(JarvisVision.UI_KEY_FlatLafFont, null);
		vision.updateFontMenuItems();
		FlatLaf.updateUI();
	}

	public static void incrFontMenuItemClicked(ActionEvent event, JarvisVision vision) {
		Font font = UIManager.getFont(JarvisVision.UI_KEY_FlatLafFont);
		Font newFont = font.deriveFont((float) Math.min(font.getSize() + 1, 28));
		UIManager.put(JarvisVision.UI_KEY_FlatLafFont, newFont);
		vision.updateFontMenuItems();
		FlatLaf.updateUI();
	}

	public static void decrFontMenuItemClicked(ActionEvent event, JarvisVision vision) {
		Font font = UIManager.getFont(JarvisVision.UI_KEY_FlatLafFont);
		Font newFont = font.deriveFont((float) Math.max(font.getSize() - 1, 10));
		UIManager.put(JarvisVision.UI_KEY_FlatLafFont, newFont);
		vision.updateFontMenuItems();
		FlatLaf.updateUI();
	}

	/**
	 * 字体改变
	 * 
	 * @param dict
	 * @param event
	 */
	public static void fontFamilyChanged(ActionEvent event) {
		String fontFamily = event.getActionCommand();
		FlatAnimatedLafChange.showSnapshot();
		Font font = UIManager.getFont("defaultFont");
		font = (font == null) ? UIManager.getFont("Label.font") : font;// 非FlatLaf主题，没有defaultFont
		Font newFont = FontUtil.getCompositeFont(fontFamily, font.getStyle(), font.getSize());
		UIManager.put("defaultFont", newFont);
		// UIManager.put("Label.font", newFont);// 这里设置之后，部分lable字体设置可能会失效，未知原因
		FlatLaf.updateUI();
		FlatAnimatedLafChange.hideSnapshotWithAnimation();
		JarvisApp.JVSP.put(JarvisVision.JVSP_FontFamily, fontFamily);
	}

	/**
	 * 字体大小改变
	 * 
	 * @param dict
	 * @param event
	 */
	public static void fontSizeChanged(ActionEvent event) {
		String fontSizeStr = event.getActionCommand();
		Font font = UIManager.getFont("defaultFont");
		font = (font == null) ? UIManager.getFont("Label.font") : font;// 非FlatLaf主题，没有defaultFont
		Font newFont = font.deriveFont((float) Integer.parseInt(fontSizeStr));
		UIManager.put("defaultFont", newFont);
		// UIManager.put("Label.font", newFont);// 这里设置之后，部分lable字体设置可能会失效，未知原因
		FlatLaf.updateUI();
		JarvisApp.JVSP.put(JarvisVision.JVSP_FontSize, fontSizeStr);
	}

	/**
	 * 主题改变
	 * 
	 * @param dict
	 * @param event
	 */
	public static void themeChanged(ActionEvent event, FlatIJLookAndFeelInfo themeInfo) {
		if(themeInfo == null || themeInfo.getName() == null || themeInfo.getClassName() == null)
			return;
		if(!(event.getSource() instanceof JCheckBoxMenuItem))
			return;
		JCheckBoxMenuItem source = (JCheckBoxMenuItem) event.getSource();
		if(!source.isSelected())
			return;// 主题没有被选择
		LookAndFeel currentTheme = UIManager.getLookAndFeel();
		if(themeInfo.getName().equals(currentTheme.getName()))
			return;// 选择的主题已经是当前主题
		FlatAnimatedLafChange.showSnapshot();
		try {
			UIManager.setLookAndFeel(themeInfo.getClassName());
		} catch(Exception e) {
			log.info(VisionI18N.Text_FailedToInstallTheme, themeInfo, e);
			e.printStackTrace();
			try {
				UIManager.setLookAndFeel(currentTheme);
			} catch(Exception e1) {
			}
			JOptionPane.showMessageDialog(source, VisionI18N.Text_FailedToInstallTheme.i18n(), event.getActionCommand(), JOptionPane.WARNING_MESSAGE);
		}
		// update all components
		JarvisVision main = ComponentDict.lookup(VisionI18N.JFrame_Main);
		main.updateFontState();
		FlatLaf.updateUI();
		FlatAnimatedLafChange.hideSnapshotWithAnimation();
		JarvisApp.JVSP.put(JarvisVision.JVSP_ThemeClass, UIManager.getLookAndFeel().getClass().getName());
	}

	public static void englishMenuItemClicked(ActionEvent event, JarvisVision vision) {
		SwingUtilities.invokeLater(() -> {
			JarvisApp.JVSP.put(JarvisVision.JVSP_Language, LanguageTag.English.tag);
			vision.languageChanged(LanguageTag.English);
		});
	}

	public static void chineseMenuItemClicked(ActionEvent event, JarvisVision vision) {
		SwingUtilities.invokeLater(() -> {
			JarvisApp.JVSP.put(JarvisVision.JVSP_Language, LanguageTag.Chinese.tag);
			vision.languageChanged(LanguageTag.Chinese);
		});
	}

	/**
	 * 选择Mark改变
	 * 
	 * @param dict
	 * @param event
	 */
	public static void selectedTabChanged(ChangeEvent event) {
		JTabbedPane tabbedPane = ComponentDict.lookup(VisionI18N.JTabbedPane_Content);
		JarvisApp.JVSP.putInt(JarvisVision.JVSP_TabIndex, tabbedPane.getSelectedIndex());
	}
}
