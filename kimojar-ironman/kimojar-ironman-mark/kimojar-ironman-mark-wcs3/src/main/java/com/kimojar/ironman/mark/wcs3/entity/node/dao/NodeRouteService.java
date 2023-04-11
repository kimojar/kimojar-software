/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity.node.dao
 * FILE NodeRouteService.java
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
package com.kimojar.ironman.mark.wcs3.entity.node.dao;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.kimojar.ironman.mark.wcs3.entity.node.NodeRoute;

/**
 * @author KiMoJar
 * @date 2023-03-07
 */
public class NodeRouteService {

	/**
	 * 查询所有路径
	 * 
	 * @param connectionSource
	 * @return
	 * @throws SQLException
	 */
	public static List<NodeRoute> queryAll(ConnectionSource connectionSource) throws SQLException {
		Dao<NodeRoute, ?> dao = DaoManager.createDao(connectionSource, NodeRoute.class);
		return dao.queryForAll();
	}

}
