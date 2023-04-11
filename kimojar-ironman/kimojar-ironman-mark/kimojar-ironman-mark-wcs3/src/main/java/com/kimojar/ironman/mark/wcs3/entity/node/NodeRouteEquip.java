/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3-route
 * PACKAGE com.kimojar.ironman.mark.wcs3.route.entity
 * FILE NodeRouteEquip.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-02-27
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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author KiMoJar
 * @date 2023-02-27
 */
@NoArgsConstructor
@Getter
@Setter
@DatabaseTable(tableName = "WCS_ROUTEEQUIP")
public class NodeRouteEquip {
	
	public static final String TABLE_COLUMN_ID = "ID";
	public static final String TABLE_COLUMN_LINENUM = "LINENUM";
	public static final String TABLE_COLUMN_ROUTEID = "ROUTEID";

	@DatabaseField(columnName = "ID", unique = true, id = true)
	private String id;
	@DatabaseField(columnName = "ROUTEID")
	private String routeid;
	@DatabaseField(columnName = "EQUIPID")
	private String equipid;
	@DatabaseField(columnName = "LINENUM")
	private int linenum;
	@DatabaseField(columnName = "ORGANIZATIONID")
	private String organizationid;
	@DatabaseField(columnName = "STATUS", unique = false)
	private String status;
	@DatabaseField(columnName = "REMARK")
	private String remark;
	@DatabaseField(columnName = "VERSION", version = true, dataType = DataType.LONG)
	private long version;
}
