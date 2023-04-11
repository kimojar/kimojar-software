/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity
 * FILE NodeSystemControl.java
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
@DatabaseTable(tableName = NodeSystemControl.TABLE_NAME)
public class NodeSystemControl {

	public static final String TABLE_NAME = "WCS_L_SYSCONTROL";
	public static final String Control_TYPE = "ETYPE";
	public static final String Control_IFUSE = "ISUSE";
	public static final String Control_CONTROLNODEID = "CONTROLNODEID";
	public static final String Control_ID = "CONTROLID";
	public static final String Control_NUM = "CONTROLNUM";
	
	@DatabaseField(id = true, columnName = "CONTROLID")
	private String CONTROLID;
	@DatabaseField(columnName = "CONTROLNUM")
	private String CONTROLNUM;
	@DatabaseField(columnName = "ETYPE")
	private String ETYPE;
	@DatabaseField(columnName = "ISUSE", format = "integer")
	private boolean ISUSE;
	@DatabaseField(columnName = "CLASSTYPE")
	private String CLASSTYPE;
	@DatabaseField(columnName = "CONTROLDESC")
	private String CONTROLDESC;
	@DatabaseField(columnName = "CONTROLNODEID")
	private String CONTROLNODEID;
	@DatabaseField(columnName = "CONTROLSTATUS")
	private String CONTROLSTATUS;
}
