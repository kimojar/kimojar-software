/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity.node
 * FILE WarehousePhysicArea.java
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
package com.kimojar.ironman.mark.wcs3.entity.node;

import java.util.Date;

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
@DatabaseTable(tableName = WarehousePhysicArea.TABLE_NAME)
public class WarehousePhysicArea {

	public static final String INTERNALCODE_FIELD_NAME = "INTERNALCODE";
	public static final String TABLE_NAME = "WCS_WHSPHYAREA";

	@DatabaseField(columnName = "ID", unique = true, id = true)
	private String id;
	@DatabaseField(columnName = "VERSION")
	private long version;
	@DatabaseField(columnName = "WHSAREANUM")
	private String whsareanum;
	@DatabaseField(persisted = true, columnName = "WHSID", canBeNull = false, foreign = true, foreignAutoRefresh = true, foreignColumnName = "WHSID")
	public Warehouse whsid;
	@DatabaseField(columnName = "WHSAREANAME")
	private String whsareaname;
	@DatabaseField(columnName = "WHSAREADESC")
	private String whsareadesc;
	@DatabaseField(columnName = "PRNWHSAREAID")
	private String prnwhsareaid;
	@DatabaseField(columnName = "AREATYPE")
	private String areatype;
	@DatabaseField(columnName = "LAYER")
	private String layer;
	@DatabaseField(columnName = "COL")
	private String col;
	@DatabaseField(columnName = "RANK")
	private String rank;
	@DatabaseField(columnName = "XPOS")
	private String xpos;
	@DatabaseField(columnName = "YPOS")
	private String ypos;
	@DatabaseField(columnName = "ZPOS")
	private String zpos;
	@DatabaseField(columnName = "USESTATUS")
	private String usestatus;
	@DatabaseField(columnName = "ERRSTATUS")
	private String errstatus;
	@DatabaseField(columnName = "STATUSDATE")
	private Date statusdate;
	@DatabaseField(columnName = "STATUSREASON")
	private String statusreason;
	@DatabaseField(columnName = "POSFEATURE")
	private String posfeature;
	@DatabaseField(columnName = "INTERNALCODE")
	private String internalcode;
	@DatabaseField(columnName = "RELATE")
	private String relate;
	@DatabaseField(columnName = "SPACE")
	private Double space;
	@DatabaseField(columnName = "STATUS")
	private String status;
	@DatabaseField(columnName = "REMARK")
	private String remark;
	@DatabaseField(columnName = "STORESTATUS")
	private String storestatus;
	@DatabaseField(columnName = "ISROUTENODE")
	private Boolean routeNode;
}
