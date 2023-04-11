/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity
 * FILE SceneRoute.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-03-07
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
package com.kimojar.ironman.mark.wcs3.entity.scene;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author KiMoJar
 * @date 2023-03-07
 */
@NoArgsConstructor
@Getter
@Setter
@DatabaseTable(tableName = SceneRoute.TABLE_NAME)
public class SceneRoute {
	
	public static final String TABLE_NAME = "WCSCFG_ROUTE";
	public static final String TABLE_COLUMN_ID = "ID";

	@DatabaseField(id = true, columnName = "ID")
	private String id;
	@DatabaseField(columnName = "SOURCEWHSAREAID")
	private String sourceWhsAreaId;
	@DatabaseField(columnName = "DESCWHSAREAID")
	private String descWhsAreaId;
	@DatabaseField(columnName = "WEIGHT")
	private int weight;
	@DatabaseField(columnName = "CONTROLID")
	private String controlId;
	@DatabaseField(columnName = "WRITESOURCE")
	private int writeSource;
	@DatabaseField(columnName = "CLEARSOURCE")
	private int clearSource;
	@DatabaseField(columnName = "WRITEDESC")
	private int writeDesc;
	@DatabaseField(columnName = "LOADNOTIFY")
	private int loadNotify;
	@DatabaseField(columnName = "UNLIADNOTIFY")
	private int unloadNotify;
	@DatabaseField(columnName = "AVERAGETIMS")
	private long averageTims;
	@DatabaseField(columnName = "ISFINALROUTE")
	private int isFinalRoute;
	@DatabaseField(columnName = "CONSTRAINTS")
	private String constraints;
	@DatabaseField(columnName = "SOURCEWHSAREANUM")
	private String sourceWhsAreaNum;
	@DatabaseField(columnName = "DESCWHSAREANUM")
	private String descWhsAreaNum;
	@DatabaseField(columnName = "CONTROLNUM")
	private String controlNum;
	
}
