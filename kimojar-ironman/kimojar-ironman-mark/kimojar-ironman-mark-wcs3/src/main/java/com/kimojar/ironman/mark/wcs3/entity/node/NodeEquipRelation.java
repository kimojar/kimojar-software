/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity.node
 * FILE NodeEquipRelation.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-03-08
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
package com.kimojar.ironman.mark.wcs3.entity.node;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author KiMoJar
 * @date 2023-03-08
 */
@NoArgsConstructor
@Getter
@Setter
@DatabaseTable(tableName = NodeEquipRelation.TABLE_NAME)
public class NodeEquipRelation {

	public static final String TABLE_NAME = "WCS_L_EQUIPRELA";
	public static final String TABLE_COLUMN_EQUIPID = "EQUIPID";
	
	@DatabaseField(id = true, columnName = "ID")
	private String id;
	@DatabaseField(columnName = "RELAEQUIPID")
	private String RELAEQUIPID;
	@DatabaseField(columnName = "EQUIPWORKSTATION")
	private String EQUIPWORKSTATION;
	@DatabaseField(columnName = "RANK")
	private String rank;
	@DatabaseField(columnName = "LEVEL")
	private String level;
	@DatabaseField(columnName = "STATIONTYPE")
	private String STATIONTYPE;
	@DatabaseField(columnName = "RELATEEQUIPCODE")
	private String relateequipcode;
	@DatabaseField(columnName = "RELACONTROLID")
	private String relacontrolid;
	@DatabaseField(columnName = "LOCNUM")
	private String locnum;
	@DatabaseField(columnName = "EQUIPCODE")
	private String equipcode;
	@DatabaseField(columnName = "ISCHECK", format = "integer")
	private boolean ifcheck;
	@DatabaseField(columnName = "RUNTIME")
	private int runtime;
	@DatabaseField(columnName = "NEXT")
	private String next;
	@DatabaseField(columnName = "PRIOR")
	private String prior;
	@DatabaseField(columnName = "PARKSPACES")
	private int packspaces;
	@DatabaseField(columnName = "EQUIPID")
	private String equipid;
	@DatabaseField(columnName = "STATIONMODE")
	private String STATIONMODE;
	@DatabaseField(columnName = "TUNNELNUM")
	private String tunnelnum;
	@DatabaseField(columnName = "RACK")
	private String rack;
	@DatabaseField(columnName = "GROUPNO")
	private String groupno;
	@DatabaseField(columnName = "SPARE1")
	private String spare1;
	@DatabaseField(columnName = "SPARE2")
	private String spare2;
	
	private boolean allow = false;
	private Date altertime = new Date();
}
