/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity.node.dao
 * FILE NodeRouteEquipService.java
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
package com.kimojar.ironman.mark.wcs3.entity.node.dao;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.kimojar.ironman.mark.wcs3.entity.node.NodeRouteEquip;

/**
 * @author KiMoJar
 * @date 2023-03-08
 */
public class NodeRouteEquipService {

	/**
	 * 根据路径id查询路径所包含的设备
	 * 
	 * @param connectionSource
	 * @param routeId
	 * @return
	 * @throws SQLException
	 */
	public static List<NodeRouteEquip> getRouteEquipsByRouteId(ConnectionSource connectionSource, String routeId) throws SQLException {
		QueryBuilder<NodeRouteEquip, ?> queryBuilder = DaoManager.createDao(connectionSource, NodeRouteEquip.class).queryBuilder();
		queryBuilder.where().eq(NodeRouteEquip.TABLE_COLUMN_ID, routeId).query();
		queryBuilder.orderBy(NodeRouteEquip.TABLE_COLUMN_LINENUM, true);
		return queryBuilder.query();
	}

	/**
	 * 删除路径id所包含的设备
	 * 
	 * @throws SQLException
	 */
	public static int deleteRouteEquipsByRouteId(ConnectionSource connectionSource, String routeId) throws SQLException {
		DeleteBuilder<NodeRouteEquip, ?> deleteBuilder = DaoManager.createDao(connectionSource, NodeRouteEquip.class).deleteBuilder();
		deleteBuilder.where().eq(NodeRouteEquip.TABLE_COLUMN_ROUTEID, routeId);
		return deleteBuilder.delete();
	}
}
