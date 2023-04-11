/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity.node
 * FILE WarehouseType.java
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
@DatabaseTable(tableName = WarehouseType.TABLE_NAME)
public class WarehouseType {

	public static final String TABLE_NAME = "WCS_WHSTYPE";

	@DatabaseField(id = true, columnName = "ID", unique = true)
	private String id;
	@DatabaseField(columnName = "SEQNO")
	private long seqno;
	@DatabaseField(columnName = "WHSTYPENUM")
	private String whstypenum;
	@DatabaseField(columnName = "WHSTYPENAME")
	private String whstypename;
	@DatabaseField(columnName = "WHSTYPEDESC")
	private String whstypedesc;
	@DatabaseField(columnName = "ORGANIZATIONID")
	private String organizationid;
	@DatabaseField(columnName = "STATUS")
	private String status;
	@DatabaseField(columnName = "REMARK")
	private String remark;
	@DatabaseField(columnName = "VERSION")
	private long version;
}
