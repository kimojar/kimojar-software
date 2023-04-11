/**
 * ==============================================================================
 * PROJECT kimojar-ironman-jarvis-vision
 * PACKAGE com.kimojar.ironman.jarvis.vision.v1
 * FILE JarvisVision.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-09
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatInspector;
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes.FlatIJLookAndFeelInfo;
import com.formdev.flatlaf.util.SystemInfo;
import com.kimojar.ironman.jarvis.IMark;
import com.kimojar.ironman.jarvis.IVision;
import com.kimojar.ironman.jarvis.JarvisApp;
import com.kimojar.util.common.gui.ComponentDict;
import com.kimojar.util.common.gui.HyperlinkLabel;
import com.kimojar.util.common.i18n.Internationalization;
import com.kimojar.util.common.i18n.LanguageTag;

/**
 * @author KiMoJar
 * @date 2022-12-09
 */
public class JarvisVision extends JFrame implements IVision {

	private static final long serialVersionUID = -8120749316222333506L;
	public static final String UI_KEY_FlatLafFont = "defaultFont";
	public static final String JVSP_FontFamily = "vision.v1.font.family";
	public static final String JVSP_FontSize = "vision.v1.font.size";
	public static final String JVSP_ThemeClass = "vision.v1.theme.class";
	public static final String JVSP_Language = "vision.v1.language";
	public static final String JVSP_TabIndex = "vision.v1.tab.index";

	private List<IMark> marks = new ArrayList<>();

	public JarvisVision() {
		installFlatLaf();
		loadJVSP();
		initComponent();
		initFontAndTheme();
		updateFontMenuItems();
		updateThemeMenuItems();
	}

	@Override
	public void addMarkScreen(IMark mark) {
		if(!marks.contains(mark))
			marks.add(mark);
		JTabbedPane tabbedPane = ComponentDict.lookup(VisionI18N.JTabbedPane_Content);
		tabbedPane.addTab(mark.name(), mark.screen());
		{// 这样做的目的是强制再设置以下字体，解决app列表的目录名称显示不完全（后两个字符会变为..）的问题
			Font currentFont = UIManager.getFont(UI_KEY_FlatLafFont);
			currentFont = (currentFont == null) ? UIManager.getFont("Label.font") : currentFont;// 非FlatLaf主题，没有defaultFont
			int preferFontSize = JarvisApp.JVSP.getInt(JVSP_FontSize, currentFont.getSize());
			Listener.fontSizeChanged(new ActionEvent(this, 0, String.valueOf(preferFontSize)));
		}
		FlatLaf.updateUI();
	}

	@Override
	public void display() {
		setVisible(true);
	}

	@Override
	public List<IMark> getMark() {
		return marks;
	}

	@Override
	public void languageChanged(LanguageTag tag) {
		Internationalization.setLanguage(tag);
		ComponentDict.lookup(JMenu.class).forEach(menu -> {
			Internationalization componentId = ComponentDict.lookup(menu);
			if(componentId != null)
				menu.setText(componentId.i18n());
		});
		ComponentDict.lookup(JMenuItem.class).forEach(menuItem -> {
			Internationalization componentId = ComponentDict.lookup(menuItem);
			if(componentId != null)
				menuItem.setText(componentId.i18n());
		});
		ComponentDict.lookup(JCheckBoxMenuItem.class).forEach(checkboxMenuItem -> {
			Internationalization componentId = ComponentDict.lookup(checkboxMenuItem);
			if(componentId != null)
				checkboxMenuItem.setText(componentId.i18n());
		});
		ComponentDict.lookup(JLabel.class).forEach(label -> {
			Internationalization componentId = ComponentDict.lookup(label);
			if(componentId != null)
				label.setText(componentId.i18n());
		});
		ComponentDict.lookup(HyperlinkLabel.class).forEach(label -> {
			Internationalization componentId = ComponentDict.lookup(label);
			if(componentId != null)
				label.setText(componentId.i18n());
		});
		ComponentDict.lookup(JButton.class).forEach(button -> {
			Internationalization componentId = ComponentDict.lookup(button);
			if(componentId != null)
				button.setText(componentId.i18n());
		});
		ComponentDict.lookup(JRadioButton.class).forEach(radioButton -> {
			Internationalization componentId = ComponentDict.lookup(radioButton);
			if(componentId != null)
				radioButton.setText(componentId.i18n());
		});
		ComponentDict.lookup(JCheckBox.class).forEach(checkbox -> {
			Internationalization componentId = ComponentDict.lookup(checkbox);
			if(componentId != null)
				checkbox.setText(componentId.i18n());
		});
		ComponentDict.lookupTitledBorders().forEach((id, titledBorder) -> {
			if(titledBorder != null)
				titledBorder.setTitle(id.i18n());
		});
		getMark().forEach(mark -> mark.languageChanged(tag));
		FlatLaf.updateUI();
	}

	private static void installFlatLaf() {
		// macOS (see https://www.formdev.com/flatlaf/macos/)
		if(SystemInfo.isMacOS) {
			// enable screen menu bar
			// (moves menu bar from JFrame window to top of screen)
			System.setProperty("apple.laf.useScreenMenuBar", "true");

			// application name used in screen menu bar
			// (in first menu after the "apple" menu)
			System.setProperty("apple.awt.application.name", "Jarvis");

			// appearance of window title bars
			// possible values:
			// - "system": use current macOS appearance (light or dark)
			// - "NSAppearanceNameAqua": use light appearance
			// - "NSAppearanceNameDarkAqua": use dark appearance
			// (needs to be set on main thread; setting it on AWT thread does not work)
			System.setProperty("apple.awt.application.appearance", "system");
		}
		// Linux
		else if(SystemInfo.isLinux) {
			// enable custom window decorations
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
		}
		// application specific UI defaults
		FlatLaf.registerCustomDefaultsSource(JarvisVision.class.getPackage().getName());
		// install inspectors
		FlatInspector.install("ctrl shift alt X");
		FlatUIDefaultsInspector.install("ctrl shift alt Y");
	}

	private void loadJVSP() {
		// load perfer theme
		try {
			String preferThemeClass = JarvisApp.JVSP.get(JVSP_ThemeClass, FlatDarkLaf.class.getName());
			UIManager.setLookAndFeel(preferThemeClass);
			if(JarvisApp.JVSP.get(JVSP_ThemeClass, "").isEmpty())
				JarvisApp.JVSP.put(JVSP_ThemeClass, UIManager.getLookAndFeel().getClass().getName());
		} catch(Exception e) {
			e.printStackTrace();
		}
		// load prefer font，只有当前主题是FlatLaf系列时，字体设置才有效。默认主题设置组件字体需要为每一类组件单独设置，太麻烦了
		if(UIManager.getLookAndFeel() instanceof FlatLaf) {
			try {
				Font currentFont = UIManager.getFont(UI_KEY_FlatLafFont);
				String preferFontFamily = JarvisApp.JVSP.get(JVSP_FontFamily, currentFont.getFamily());
				int preferFontSize = JarvisApp.JVSP.getInt(JVSP_FontSize, currentFont.getSize());
				Font newFont = FontUtil.getCompositeFont(preferFontFamily, currentFont.getStyle(), preferFontSize);
				UIManager.put(UI_KEY_FlatLafFont, newFont);
				if(JarvisApp.JVSP.get(JVSP_FontFamily, "").isEmpty())
					JarvisApp.JVSP.put(JVSP_FontFamily, preferFontFamily);
				if(JarvisApp.JVSP.getInt(JVSP_FontSize, 0) == 0)
					JarvisApp.JVSP.putInt(JVSP_FontSize, preferFontSize);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		// load prefer language
		try {
			String preferLanguage = JarvisApp.JVSP.get(JVSP_Language, LanguageTag.English.tag);
			Internationalization.setLanguage(LanguageTag.getByString(preferLanguage));
			if(JarvisApp.JVSP.get(JVSP_Language, "").isEmpty())
				JarvisApp.JVSP.put(JVSP_Language, preferLanguage);
		} catch(Exception e) {
			e.printStackTrace();
		}

		FlatLaf.updateUI();
	}

	private void initComponent() {
		// menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(ComponentDict.record(VisionI18N.JMenuBar, menuBar));

		// appearance menu
		JMenu appearanceMenu = new JMenu();
		menuBar.add(ComponentDict.record(VisionI18N.JMenu_Appearance, appearanceMenu));
		appearanceMenu.setText(VisionI18N.JMenu_Appearance.i18n());
		appearanceMenu.setMnemonic('A');

		// font menu
		JMenu fontMenu = new JMenu();
		appearanceMenu.add(ComponentDict.record(VisionI18N.JMenu_Font, fontMenu));
		fontMenu.setText(VisionI18N.JMenu_Font.i18n());
		fontMenu.setMnemonic('F');
		JMenuItem loadFontMenuItem = new JMenuItem();
		fontMenu.add(ComponentDict.record(VisionI18N.JMenuItem_RestoreFont, loadFontMenuItem));
		loadFontMenuItem.setText(VisionI18N.JMenuItem_LoadFont.i18n());
		loadFontMenuItem.addActionListener(e -> Listener.loadFontMenuItemClicked(e));
		JMenuItem restoreFontMenuItem = new JMenuItem();
		fontMenu.add(ComponentDict.record(VisionI18N.JMenuItem_RestoreFont, restoreFontMenuItem));
		restoreFontMenuItem.setText(VisionI18N.JMenuItem_RestoreFont.i18n());
		restoreFontMenuItem.addActionListener(e -> Listener.restoreFontMenuItemClicked(e, this));
		JMenuItem incrFontMenuItem = new JMenuItem();
		fontMenu.add(ComponentDict.record(VisionI18N.JMenuItem_IncreaseFontSize, incrFontMenuItem));
		incrFontMenuItem.setText(VisionI18N.JMenuItem_IncreaseFontSize.i18n());
		incrFontMenuItem.addActionListener(e -> Listener.incrFontMenuItemClicked(e, this));
		JMenuItem decrFontMenuItem = new JMenuItem();
		fontMenu.add(ComponentDict.record(VisionI18N.JMenuItem_DecreaseFontSize, decrFontMenuItem));
		decrFontMenuItem.setText(VisionI18N.JMenuItem_DecreaseFontSize.i18n());
		decrFontMenuItem.addActionListener(e -> Listener.decrFontMenuItemClicked(e, this));

		// theme menu
		JMenu themeMenu = new JMenu();
		appearanceMenu.add(ComponentDict.record(VisionI18N.JMenu_Theme, themeMenu));
		themeMenu.setText(VisionI18N.JMenu_Theme.i18n());
		themeMenu.setMnemonic('T');
		themeMenu.addActionListener(e -> Listener.aboutMenuItemClicked(e));
		JMenuItem loadThemeMenuItem = new JMenuItem();
		themeMenu.add(ComponentDict.record(VisionI18N.JMenuItem_LoadTheme, loadThemeMenuItem));
		loadThemeMenuItem.setText(VisionI18N.JMenuItem_LoadTheme.i18n());
		loadThemeMenuItem.addActionListener(e -> Listener.loadThemeMenuItemClicked(e));
		JMenuItem aboutThemeMenuItem = new JMenuItem();
		themeMenu.add(ComponentDict.record(VisionI18N.JMenuItem_AboutCurrentTheme, aboutThemeMenuItem));
		aboutThemeMenuItem.setText(VisionI18N.JMenuItem_AboutCurrentTheme.i18n());
		aboutThemeMenuItem.addActionListener(e -> Listener.aboutCurrentThemeMenuItemClicked(e));
		JMenu coreThemeMenu = new JMenu();
		themeMenu.add(ComponentDict.record(VisionI18N.JMenu_CoreTheme, coreThemeMenu));
		coreThemeMenu.setText(VisionI18N.JMenu_CoreTheme.i18n());
		JMenu extendThemeMenu = new JMenu();
		themeMenu.add(ComponentDict.record(VisionI18N.JMenu_ExtendTheme, extendThemeMenu));
		extendThemeMenu.setText(VisionI18N.JMenu_ExtendTheme.i18n());

		// language menu
		JMenu languageMenu = new JMenu();
		appearanceMenu.add(ComponentDict.record(VisionI18N.JMenu_Language, languageMenu));
		languageMenu.setText(VisionI18N.JMenu_Language.i18n());
		languageMenu.setMnemonic('L');
		JCheckBoxMenuItem englishMenuItem = new JCheckBoxMenuItem();
		languageMenu.add(ComponentDict.record(VisionI18N.JCheckBoxMenuItem_English, englishMenuItem));
		englishMenuItem.setText(VisionI18N.JCheckBoxMenuItem_English.i18n());
		englishMenuItem.addActionListener(e -> Listener.englishMenuItemClicked(e, this));
		englishMenuItem.setSelected(JarvisApp.JVSP.get(JVSP_Language, LanguageTag.English.tag).equals(LanguageTag.English.tag));
		ButtonGroup languageGroup = new ButtonGroup();
		JCheckBoxMenuItem chineseMenuItem = new JCheckBoxMenuItem();
		languageMenu.add(ComponentDict.record(VisionI18N.JCheckBoxMenuItem_Chinese, chineseMenuItem));
		chineseMenuItem.setText(VisionI18N.JCheckBoxMenuItem_Chinese.i18n());
		chineseMenuItem.addActionListener(e -> Listener.chineseMenuItemClicked(e, this));
		chineseMenuItem.setSelected(JarvisApp.JVSP.get(JVSP_Language, LanguageTag.English.tag).equals(LanguageTag.Chinese.tag));
		languageGroup.add(englishMenuItem);
		languageGroup.add(chineseMenuItem);

		// help menu
		JMenu helpMenu = new JMenu();
		menuBar.add(ComponentDict.record(VisionI18N.JMenu_Help, helpMenu));
		helpMenu.setText(VisionI18N.JMenu_Help.i18n());
		helpMenu.setMnemonic('H');
		JMenuItem aboutMenuItem = new JMenuItem();
		helpMenu.add(ComponentDict.record(VisionI18N.JMenuItem_About, aboutMenuItem));
		aboutMenuItem.setText(VisionI18N.JMenuItem_About.i18n());
		aboutMenuItem.setMnemonic('A');
		aboutMenuItem.addActionListener(e -> Listener.aboutMenuItemClicked(e));

		// content
		setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		add(ComponentDict.record(VisionI18N.JTabbedPane_Content, tabbedPane), BorderLayout.CENTER);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.addChangeListener(e -> Listener.selectedTabChanged(e));

		// 设置窗体
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension framSize = new Dimension(screenSize.width / 3 * 2, screenSize.height / 3 * 2);
		ImageIcon icon = new ImageIcon(JarvisVision.class.getResource("resource/ironman-red-48.png"));
		setIconImage(icon.getImage());
		setTitle("Jarvis");
		setSize(framSize);// 设置尺寸,如果在设置窗口位置后再次设置尺寸,窗口位置可能不再居中了
		setPreferredSize(framSize);// 设置窗口合适尺寸,用于最大化最小化窗口时使用
		setResizable(true);// 设置是否可调整大小
		setLocationRelativeTo(null);// 设置荧幕居中
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		ComponentDict.record(VisionI18N.JFrame_Main, this);
		// SwingUtilities.updateComponentTreeUI(this);
		FlatLaf.updateUI();// this will update all ui created by this application
		this.addWindowListener(new WindowAdapter() {

			/**
			 * 点击窗口关闭按钮时调用
			 * 
			 * @param e
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				// TODO invoke plugin stop
				System.exit(0);
			}
		});
	}

	private String[] availableFontFamilyNames;
	private int initialFontMenuItemCount = -1;

	private void initFontAndTheme() {
		availableFontFamilyNames = FontUtil.getAvailableFontFamilyNames().clone();
		Arrays.sort(availableFontFamilyNames);
		availableCoreThemeInfos.add(new FlatIJLookAndFeelInfo(FlatDarkLaf.NAME, FlatDarkLaf.class.getName(), true));
		availableCoreThemeInfos.add(new FlatIJLookAndFeelInfo(FlatLightLaf.NAME, FlatLightLaf.class.getName(), false));
		availableCoreThemeInfos.add(new FlatIJLookAndFeelInfo(FlatDarculaLaf.NAME, FlatDarculaLaf.class.getName(), true));
		availableCoreThemeInfos.add(new FlatIJLookAndFeelInfo(FlatIntelliJLaf.NAME, FlatIntelliJLaf.class.getName(), false));
		availableCoreThemeInfos.add(new FlatIJLookAndFeelInfo("Metal", MetalLookAndFeel.class.getName(), false));
		availableCoreThemeInfos.add(new FlatIJLookAndFeelInfo("Synth look and feel", SynthLookAndFeel.class.getName(), false));
		availableCoreThemeInfos.add(new FlatIJLookAndFeelInfo("Nimbus", NimbusLookAndFeel.class.getName(), false));
		availableExtendThemeInfos.addAll(Arrays.asList(FlatAllIJThemes.INFOS));
	}

	public void updateFontMenuItems() {
		JMenu fontMenu = ComponentDict.lookup(VisionI18N.JMenu_Font);
		if(initialFontMenuItemCount < 0)
			initialFontMenuItemCount = fontMenu.getItemCount();
		else
			for(int i = fontMenu.getItemCount() - 1; i >= initialFontMenuItemCount; i--)
				fontMenu.remove(i);// remove old font items
		// get current font
		Font currentFont = UIManager.getFont("Label.font");
		String currentFamily = currentFont.getFamily();
		String currentSize = Integer.toString(currentFont.getSize());
		// add font families
		fontMenu.addSeparator();
		ArrayList<String> families = new ArrayList<>();
		families.add("Arial");
		families.add("Calibri");
		families.add("Comic Sans MS");
		families.add("Microsoft YaHei UI");
		families.add("Dialog");
		families.add("Open Sans");
		families.add("Tahoma");
		families.add("Verdana");
		if(!families.contains(currentFamily))
			families.add(currentFamily);
		families.sort(String.CASE_INSENSITIVE_ORDER);
		ButtonGroup fontFamiliesGroup = new ButtonGroup();
		for(String family : families) {
			if(Arrays.binarySearch(availableFontFamilyNames, family) < 0)
				continue; // not available
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(family);
			item.setSelected(family.equals(currentFamily));
			item.addActionListener(e -> Listener.fontFamilyChanged(e));
			fontMenu.add(item);
			fontFamiliesGroup.add(item);
		}
		// add font sizes
		fontMenu.addSeparator();
		ArrayList<String> sizes = new ArrayList<>(Arrays.asList(
		"10", "11", "12", "14", "16", "18", "20", "24", "28"));
		if(!sizes.contains(currentSize))
			sizes.add(currentSize);
		sizes.sort(String.CASE_INSENSITIVE_ORDER);
		ButtonGroup fontSizesGroup = new ButtonGroup();
		for(String size : sizes) {
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(size);
			item.setSelected(size.equals(currentSize));
			item.addActionListener(e -> Listener.fontSizeChanged(e));
			fontMenu.add(item);
			fontSizesGroup.add(item);
		}
		updateFontState();
	}

	private List<FlatIJLookAndFeelInfo> availableCoreThemeInfos = new ArrayList<>();
	private List<FlatIJLookAndFeelInfo> availableExtendThemeInfos = new ArrayList<>();

	private void updateThemeMenuItems() {
		LookAndFeel currentTheme = UIManager.getLookAndFeel();
		JMenu coreThemeMenu = ComponentDict.lookup(VisionI18N.JMenu_CoreTheme);
		coreThemeMenu.removeAll();// remove old font items
		ButtonGroup themesGroup = new ButtonGroup();
		for(FlatIJLookAndFeelInfo coreThemeInfo : availableCoreThemeInfos) {
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(coreThemeInfo.getName());
			item.setSelected(coreThemeInfo.getName().equals(currentTheme.getName()));
			item.addActionListener(e -> Listener.themeChanged(e, coreThemeInfo));
			coreThemeMenu.add(item);
			themesGroup.add(item);
		}
		JMenu extendThemeMenu = ComponentDict.lookup(VisionI18N.JMenu_ExtendTheme);
		extendThemeMenu.removeAll();// remove old font items
		for(FlatIJLookAndFeelInfo extendThemeInfo : availableExtendThemeInfos) {
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(extendThemeInfo.getName());
			item.setSelected(extendThemeInfo.getName().equals(currentTheme.getName()));
			item.addActionListener(e -> Listener.themeChanged(e, extendThemeInfo));
			extendThemeMenu.add(item);
			themesGroup.add(item);
		}
	}

	/**
	 * 跟新字体菜单的可用状态<br>
	 * 只有在FlatLaf主题下才启用字体设置，因为只有当前主题是FlatLaf系列时，字体设置才有效。默认主题设置组件字体需要为每一类组件单独设置，太麻烦了
	 */
	protected void updateFontState() {
		JMenu fontMenu = ComponentDict.lookup(VisionI18N.JMenu_Font);
		Arrays.asList(fontMenu.getMenuComponents()).forEach(item -> item.setEnabled(UIManager.getLookAndFeel() instanceof FlatLaf));
	}

}