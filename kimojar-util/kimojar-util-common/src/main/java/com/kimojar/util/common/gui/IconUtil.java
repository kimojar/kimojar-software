/**
 * ==============================================================================
 * PROJECT kimojar-util-common
 * PACKAGE com.kimojar.util.common.gui
 * FILE IconUtil.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-26
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
package com.kimojar.util.common.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

/**
 * @author KiMoJar
 * @date 2022-12-26
 */
public class IconUtil {

	/**
	 * 创建一个指定尺寸的图标
	 * 
	 * @param image 图标原始图片
	 * @param perferSize 期望尺寸
	 * @param constrained 是否保持纵横比
	 * @return
	 */
	public static ImageIcon createAssignSizeIcon(Image image, Dimension perferSize, boolean constrained) {
		ImageIcon icon = new ImageIcon(image) {

			private static final long serialVersionUID = 1L;

			@Override
			public synchronized void paintIcon(java.awt.Component cmp, Graphics g, int x, int y) {
				// 初始化参数
				Point startPoint = new Point(0, 0);// 默认绘制起点
				Dimension cmpSize = perferSize;// 获取组件大小
				Dimension imgSize = new Dimension(getIconWidth(), getIconHeight());// 获取图像大小

				// 计算绘制起点和区域
				if(constrained) {// 等比例缩放
					// 计算图像宽高比例
					double ratio = 1.0 * imgSize.width / imgSize.height;
					// 计算等比例缩放后的区域大小
					imgSize.width = (int) Math.min(cmpSize.width, ratio * cmpSize.height);
					imgSize.height = (int) (imgSize.width / ratio);
					// 计算绘制起点
					startPoint.x = (int) (cmp.getAlignmentX() * (cmpSize.width - imgSize.width));
					startPoint.y = (int) (cmp.getAlignmentY() * (cmpSize.height - imgSize.height));
				} else {// 完全填充
					imgSize = cmpSize;
				}

				// 根据起点和区域大小进行绘制
				if(getImageObserver() == null) {
					g.drawImage(getImage(), startPoint.x, startPoint.y,
					imgSize.width, imgSize.height, cmp);
				} else {
					g.drawImage(getImage(), startPoint.x, startPoint.y,
					imgSize.width, imgSize.height, getImageObserver());
				}
			};
		};
		return icon;
	}

	/**
	 * 创建一个根据组件尺寸自适应大小的图标
	 * 
	 * @param image 原始图标图片
	 * @param constrained 是否保持纵横比
	 * @return
	 */
	public static ImageIcon createAutoAdjustIcon(Image image, boolean constrained) {
		ImageIcon icon = new ImageIcon(image) {

			private static final long serialVersionUID = 1L;

			@Override
			public synchronized void paintIcon(java.awt.Component cmp, Graphics g, int x, int y) {
				// 初始化参数
				Point startPoint = new Point(0, 0);// 默认绘制起点
				Dimension cmpSize = cmp.getSize();// 获取组件大小
				Dimension imgSize = new Dimension(getIconWidth(), getIconHeight());// 获取图像大小

				// 计算绘制起点和区域
				if(constrained) {// 等比例缩放
					// 计算图像宽高比例
					double ratio = 1.0 * imgSize.width / imgSize.height;
					// 计算等比例缩放后的区域大小
					imgSize.width = (int) Math.min(cmpSize.width, ratio * cmpSize.height);
					imgSize.height = (int) (imgSize.width / ratio);
					// 计算绘制起点
					startPoint.x = (int) (cmp.getAlignmentX() * (cmpSize.width - imgSize.width));
					startPoint.y = (int) (cmp.getAlignmentY() * (cmpSize.height - imgSize.height));
				} else {// 完全填充
					imgSize = cmpSize;
				}

				// 根据起点和区域大小进行绘制
				if(getImageObserver() == null) {
					g.drawImage(getImage(), startPoint.x, startPoint.y,
					imgSize.width, imgSize.height, cmp);
				} else {
					g.drawImage(getImage(), startPoint.x, startPoint.y,
					imgSize.width, imgSize.height, getImageObserver());
				}
			};
		};
		return icon;
	}
}
