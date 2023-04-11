/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity
 * FILE SceneRouteEquip.java
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
@DatabaseTable(tableName = "WCSCFG_ROUTEEQUIP")
public class SceneRouteEquip {

	public static final String TABLE_NAME = "WCSCFG_ROUTEEQUIP";

	@DatabaseField(id = true, columnName = "ID")
	private String id;
	@DatabaseField(columnName = "ROUTEID")
	private String routeId;
	@DatabaseField(columnName = "EQUIPID")
	private String equipId;
	@DatabaseField(columnName = "SEQ")
	private int seq;
}
