/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity.node
 * FILE NodeEquip.java
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
@DatabaseTable(tableName = NodeEquip.TABLE_NAME)
public class NodeEquip {

	public static final String TABLE_NAME = "WCS_L_EQUIP";
	public static final String TABLE_COLUMN_EQUIPID = "EQUIPID";
	public static final String TABLE_COLUMN_CONTROLID = "CONTROLID";
	public static final String TABLE_COLUMN_EQUIPCODE = "EQUIPCODE";
	public static final String TABLE_COLUMN_ISUSE = "ISUSE";
	public static final String TABLE_COLUMN_PREEQUIP = "PREEQUIP";
	public static final String TABLE_COLUMN_ETYPE = "ETYPE";

	@DatabaseField(id = true, columnName = "EQUIPID")
	public String equipid;
	@DatabaseField(columnName = "CONTROLID")
	public String controlid;
	@DatabaseField(columnName = "EQUIPDESC")
	public String equipdesc;
	@DatabaseField(columnName = "ETYPE")
	public String etype;
	@DatabaseField(columnName = "PREEQUIP")
	public String preequip;
	@DatabaseField(columnName = "EQUIPNUM")
	public String equipnum;
	@DatabaseField(columnName = "EQUIPSTATUS1")
	public String equipstatus1;
	@DatabaseField(columnName = "STATUSDATE")
	public Date statusdate;
	@DatabaseField(columnName = "EQUIPERRCODE")
	public String equiperrcode;
	@DatabaseField(columnName = "EQUIPPOSI")
	public String equipposi;
	@DatabaseField(columnName = "DESCPOSI")
	public String descposi;
	@DatabaseField(columnName = "CURRCONNID")
	public String currconnid;
	@DatabaseField(columnName = "EQUIPCODE")
	public String equipcode;
	@DatabaseField(columnName = "ACCELERATION")
	private double acceleration;
	@DatabaseField(columnName = "VELOCITY")
	private double velocity;
	@DatabaseField(columnName = "POSIWIDTH")
	private double posiwidth;
	@DatabaseField(columnName = "CLASSTYPE")
	public String classtype;
	@DatabaseField(columnName = "EQUIPTUN")
	public String equiptun;
	@DatabaseField(columnName = "EQUIPSTATUS2")
	public String equipstatus2;
	@DatabaseField(columnName = "EQUIPSTATUSCODE")
	public String equipstatuscode;
	@DatabaseField(columnName = "ISUSE", format = "integer")
	public int isuse;
	@DatabaseField(columnName = "EQUIPWCSERRCODE")
	public String equipwcserrcode;
	@DatabaseField(columnName = "ALERMMESSAGE")
	public String alermmessage;
	@DatabaseField(columnName = "GROUPNUM")
	public String groupnum;
	@DatabaseField(columnName = "ISSAVE", format = "integer")
	public boolean issave;
	@DatabaseField(columnName = "ISACTIVE", format = "integer")
	private boolean isactivate;
	@DatabaseField(columnName = "WORKMODE")
	private String workmode;
	@DatabaseField(columnName = "POWER")
	private int power;
	@DatabaseField(columnName = "LOADWEIGHT")
	private double loadweight;
	@DatabaseField(columnName = "WORKPARTNER")
	private String workpartner;
	@DatabaseField(columnName = "WORKAREA")
	private String workarea;
	@DatabaseField(columnName = "ISTRANSPORT", format = "integer")
	private boolean istransport;
	@DatabaseField(columnName = "ISSEQUENCE", format = "integer")
	private boolean issequence;
	@DatabaseField(columnName = "LOCKEDTAG")
	private String lockedtag;
	@DatabaseField(columnName = "WXAXIS")
	private double wxaxis;
	@DatabaseField(columnName = "WYAXIS")
	private double wyaxis;
	@DatabaseField(columnName = "WZAXIS")
	private double wzaxis;
	@DatabaseField(columnName = "RESERVE01")
	private String reserve01;
	@DatabaseField(columnName = "RESERVE02")
	private String reserve02;
	@DatabaseField(columnName = "RESERVE03")
	private String reserve03;
	@DatabaseField(columnName = "RESERVE04")
	private String reserve04;
	@DatabaseField(columnName = "RESERVE05")
	private String reserve05;
	@DatabaseField(columnName = "ISLOCKED", format = "integer")
	private boolean isLocked = false;
	@DatabaseField(columnName = "TASKNUM")
	private String tasknum = null;
	@DatabaseField(columnName = "ORIPOSI")
	public String oriposi = null;

	private int posierror = 0;
	private int x = 0;
	private int y = 0;
	private int z = 0;
	private int s = 0;
	private int d = 0;
	private String partqty = null;
	private String planqty = null;
	private String fullbinqty = null;
	private String itemoverqty = null;
	private int fullloadqty = 1;
	private Date lockTime = new Date();

}
