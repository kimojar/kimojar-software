/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity
 * FILE NodeRoute.java
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
@DatabaseTable(tableName = "WCS_ROUTE")
public class NodeRoute {

	@DatabaseField(columnName = "ID", unique = true, id = true)
	private String id;
	@DatabaseField(persisted = true, canBeNull = false, columnName = "SOURCEWHSAREAID", foreign = true, foreignAutoRefresh = true, foreignColumnName = "ID")
	private WarehousePhysicArea sourcewhsareaid;
	@DatabaseField(persisted = true, canBeNull = false, columnName = "DESCWHSAREAID", foreign = true, foreignAutoRefresh = true, foreignColumnName = "ID")
	private WarehousePhysicArea descwhsareaid;
	@DatabaseField(columnName = "WEIGHT")
	private int weight;
	@DatabaseField(columnName = "CONTROLID")
	private String controlid;
	@DatabaseField(columnName = "WRITESOURCE")
	private int writesource;
	@DatabaseField(columnName = "CLEARSOURCE")
	private int clearsource;
	@DatabaseField(columnName = "WRITEDESC")
	private int writedesc;
	@DatabaseField(columnName = "ORGANIZATIONID")
	private String organizationid;
	@DatabaseField(columnName = "STATUS")
	private String status;
	@DatabaseField(columnName = "REMARK")
	private String remark;
	@DatabaseField(columnName = "VERSION", version = true, dataType = DataType.LONG)
	private long version;
	@DatabaseField(columnName = "LOADNOTIFY")
	private int loadnotify;
	@DatabaseField(columnName = "UNLOADNOTIFY")
	private int unloadnotify;
	@DatabaseField(columnName = "AVERAGETIME")
	private long averagetime;
	@DatabaseField(columnName = "ISFINALROUTE")
	private int isfinalroute;
	@DatabaseField(columnName = "CONSTRAINTS")
	private String constraint;
}
