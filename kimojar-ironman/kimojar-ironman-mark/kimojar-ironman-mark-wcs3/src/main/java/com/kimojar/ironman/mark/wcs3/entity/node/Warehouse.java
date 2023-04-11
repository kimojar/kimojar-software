/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity.node
 * FILE Warehouse.java
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
@DatabaseTable(tableName = Warehouse.TABLE_NAME)
public class Warehouse {

	public static final String WHSID_FIELD_NAME = "WHSID";
	public static final String WHSNUM_FIELD_NAME = "WHSNUM";
	public static final String TABLE_NAME = "WCS_WHS";
	
	@DatabaseField(columnName = "WHSID", id = true)
	private String whsid;
	@DatabaseField(columnName = "VERSION")
	private long version;
	@DatabaseField(persisted = true, canBeNull = false, columnName = "WHSTYPEID", foreign = true, foreignAutoRefresh = true, foreignColumnName = "ID")
	private WarehouseType whsType;
	@DatabaseField(columnName = "WHSCLASSIFID")
	private String whsClassIf;
	@DatabaseField(columnName = "PRNWHSID")
	private String prnwhsid;
	@DatabaseField(columnName = "TREECODE")
	private String treecode;
	@DatabaseField(columnName = "SEQNO")
	private Long seqno;
	@DatabaseField(columnName = "WHSNUM")
	private String whsnum;
	@DatabaseField(columnName = "WHSNAME")
	private String whsname;
	@DatabaseField(columnName = "WHSDESC")
	private String whsdesc;
	@DatabaseField(columnName = "ORGID")
	private String orgid;
	@DatabaseField(columnName = "ADDR")
	private String addr;
	@DatabaseField(columnName = "MANAGERID")
	private String managerid;
	@DatabaseField(columnName = "TEL")
	private String tel;
	@DatabaseField(columnName = "ISFREEZE")
	private Boolean isfreeze;
	@DatabaseField(columnName = "FREEZEDATE")
	private Date freezedate;
	@DatabaseField(columnName = "ISCYCLECOUNT")
	private Boolean iscyclecount;
	@DatabaseField(columnName = "COUNTCODE")
	private String countcode;
	@DatabaseField(columnName = "LASTCOUNTDATE")
	private Date lastcountdate;
	@DatabaseField(columnName = "COUNTALERT")
	private Double countalert;
	@DatabaseField(columnName = "ORGANIZATIONID")
	private String organizationid;
	@DatabaseField(columnName = "STATUS")
	private String status;
	@DatabaseField(columnName = "REMARK")
	private String remark;
}
