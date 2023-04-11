/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wss
 * PACKAGE com.kimojar.ironman.mark.wss
 * FILE MiddleSplitPaneUI.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-02-06
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
package com.kimojar.ironman.mark.wss;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * @author KiMoJar
 * @date 2023-02-06
 */
public class MiddleSplitPaneUI extends BasicSplitPaneUI {

	public MiddleSplitPaneUI() {
		super();
	}

	/**
	 * Creates the default divider.
	 */
	@Override
	public BasicSplitPaneDivider createDefaultDivider() {
		return new MiddleSplitPaneDivider(this);
	}

	private class MiddleSplitPaneDivider extends BasicSplitPaneDivider {

		private static final long serialVersionUID = -2028171048715992832L;
		private int oneTouchSize, oneTouchOffset;
		boolean centerOneTouchButtons;
		// center空白区域

		public MiddleSplitPaneDivider(MiddleSplitPaneUI ui) {
			super(ui);

			oneTouchSize = ONE_TOUCH_SIZE;
			oneTouchOffset = ONE_TOUCH_OFFSET;
			centerOneTouchButtons = true;

			setLayout(new DividerLayout());
			setBasicSplitPaneUI(ui);
			orientation = splitPane.getOrientation();
			setCursor((orientation == JSplitPane.HORIZONTAL_SPLIT) ? Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR) : Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
			setBackground(UIManager.getColor("SplitPane.background"));
		}

		protected class DividerLayout implements LayoutManager {

			@Override
			public void layoutContainer(Container c) {
				if(leftButton != null && rightButton != null) {
					if(splitPane.isOneTouchExpandable()) {
						Insets insets = getInsets();

						if(orientation == JSplitPane.VERTICAL_SPLIT) {
							int extraX = (insets != null) ? insets.left : 0;
							int blockSize = getHeight();

							if(insets != null) {
								blockSize -= (insets.top + insets.bottom);
								blockSize = Math.max(blockSize, 0);
							}
							blockSize = Math.min(blockSize, oneTouchSize);

							int y = (c.getSize().height - blockSize) / 2;

							if(!centerOneTouchButtons) {
								y = (insets != null) ? insets.top : 0;
								extraX = 0;
							}
							int width = (int) MiddleSplitPaneDivider.this.getSize().getWidth();
							leftButton.setBounds(extraX - oneTouchOffset + width / 2, y,
							blockSize * 2, blockSize);
							rightButton.setBounds(extraX - oneTouchOffset +
							oneTouchSize * 2 + width / 2, y,
							blockSize * 2, blockSize);
						} else {
							int extraY = (insets != null) ? insets.top : 0;
							int blockSize = getWidth();
							if(insets != null) {
								blockSize -= (insets.left + insets.right);
								blockSize = Math.max(blockSize, 0);
							}
							blockSize = Math.min(blockSize, oneTouchSize);

							int x = (c.getSize().width - blockSize) / 2;

							if(!centerOneTouchButtons) {
								x = (insets != null) ? insets.left : 0;
								extraY = 0;
							}
							int height = (int) MiddleSplitPaneDivider.this.getSize().getHeight();
							leftButton.setBounds(x, extraY - oneTouchOffset + height / 2,
							blockSize, blockSize * 2);
							rightButton.setBounds(x, extraY - oneTouchOffset +
							oneTouchSize * 2 + height / 2, blockSize,
							blockSize * 2);
						}
					} else {
						leftButton.setBounds(-5, -5, 1, 1);
						rightButton.setBounds(-5, -5, 1, 1);
					}
				}
			}

			@Override
			public Dimension minimumLayoutSize(Container c) {
				// NOTE: This isn't really used, refer to
				// BasicSplitPaneDivider.getPreferredSize for the
				// reason.
				// I leave it in hopes of having this used at some
				// point.
				if(splitPane == null) {
					return new Dimension(0, 0);
				}
				Dimension buttonMinSize = null;

				if(splitPane.isOneTouchExpandable() && leftButton != null) {
					buttonMinSize = leftButton.getMinimumSize();
				}

				Insets insets = getInsets();
				int width = getDividerSize();
				int height = width;

				if(orientation == JSplitPane.VERTICAL_SPLIT) {
					if(buttonMinSize != null) {
						int size = buttonMinSize.height;
						if(insets != null) {
							size += insets.top + insets.bottom;
						}
						height = Math.max(height, size);
					}
					width = 1;
				} else {
					if(buttonMinSize != null) {
						int size = buttonMinSize.width;
						if(insets != null) {
							size += insets.left + insets.right;
						}
						width = Math.max(width, size);
					}
					height = 1;
				}
				return new Dimension(width, height);
			}

			@Override
			public Dimension preferredLayoutSize(Container c) {
				return minimumLayoutSize(c);
			}

			@Override
			public void removeLayoutComponent(Component c) {
			}

			@Override
			public void addLayoutComponent(String string, Component c) {
			}
		}
	}
}
