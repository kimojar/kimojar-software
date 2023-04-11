/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity.node
 * FILE NodeEquipLine.java
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
@DatabaseTable(tableName = NodeEquipLine.TABLE_NAME)
public class NodeEquipLine {

	public static final String TABLE_NAME = "WCS_L_EQUIPLINE";
	public static final String TABLE_COLUMN_EQUIPID = "EQUIPID";
	public static final String TABLE_COLUMN_EQUIPWORKSTATION = "EQUIPWORKSTATION";
	public static final String TABLE_COLUMN_GROUPNO = "GROUPNO";

	@DatabaseField(id = true, columnName = "ID")
	private String ID;
	@DatabaseField(columnName = "INFOITEM")
	private String INFOITEM;
	@DatabaseField(columnName = "INFODESC")
	private String INFODESC;
	@DatabaseField(columnName = "INFOVALUE")
	private String INFOVALUE;
	@DatabaseField(columnName = "LINENUM")
	private String LINENUM;
	@DatabaseField(columnName = "EQUIPWORKSTATION")
	private String equipworkstation;
	@DatabaseField(columnName = "GROUPNO")
	private String groupno;
	@DatabaseField(columnName = "EQUIPID")
	private String equipid;
}
