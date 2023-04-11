/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wcs3
 * PACKAGE com.kimojar.ironman.mark.wcs3.entity.scene.dao
 * FILE SceneRouteService.java
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
package com.kimojar.ironman.mark.wcs3.entity.scene.dao;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.kimojar.ironman.mark.wcs3.entity.scene.SceneRoute;

/**
 * @author KiMoJar
 * @date 2023-03-07
 */
public class SceneRouteService {

	/**
	 * 清空场景数据库的路径表和路径设备表
	 * 
	 * @param connectionSource
	 * @return
	 * @throws SQLException
	 */
	public static int emptyTable(ConnectionSource connectionSource) throws SQLException {
		Dao<SceneRoute, ?> dao = DaoManager.createDao(connectionSource, SceneRoute.class);
		SceneRouteEquipService.emptyTable(connectionSource);
		return dao.executeRawNoArgs("delete from " + SceneRoute.TABLE_NAME);
	}

	/**
	 * 保存参数路径，crate or update
	 * 
	 * @param connectionSource
	 * @param sceneRoutes
	 * @throws SQLException
	 */
	public static void save(ConnectionSource connectionSource, List<SceneRoute> sceneRoutes) throws SQLException {
		Dao<SceneRoute, ?> dao = DaoManager.createDao(connectionSource, SceneRoute.class);
		for(SceneRoute sceneRoute : sceneRoutes)
			dao.createOrUpdate(sceneRoute);
	}

	/**
	 * 查询所有路径
	 * 
	 * @param connectionSource
	 * @return
	 * @throws SQLException
	 */
	public static List<SceneRoute> queryAll(ConnectionSource connectionSource) throws SQLException {
		Dao<SceneRoute, ?> dao = DaoManager.createDao(connectionSource, SceneRoute.class);
		return dao.queryForAll();
	}

	/**
	 * 根据id查询路径
	 * 
	 * @param connectionSource
	 * @param routeId
	 * @return
	 * @throws SQLException
	 */
	public static SceneRoute queryById(ConnectionSource connectionSource, String routeId) throws SQLException {
		Dao<SceneRoute, ?> dao = DaoManager.createDao(connectionSource, SceneRoute.class);
		return dao.queryBuilder().where().eq(SceneRoute.TABLE_COLUMN_ID, routeId).queryForFirst();
	}
}
