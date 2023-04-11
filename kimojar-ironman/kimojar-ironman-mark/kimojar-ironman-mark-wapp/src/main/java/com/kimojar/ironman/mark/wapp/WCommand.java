/**
 * ==============================================================================
 * PROJECT kimojar-ironman-mark-wapp
 * PACKAGE com.kimojar.ironman.mark.wapp
 * FILE WCommand.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-12-19
 * ==============================================================================
 * Copyright (C) 2022
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
package com.kimojar.ironman.mark.wapp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KiMoJar
 * @date 2022-12-19
 */
public class WCommand {

	/** jvs指令 */
	public String cmd;
	/** jvs指令对应的Windows动作 */
	public String action;
	/** jvs指令是否是跨行指令 */
	public List<WCommand> childCmds = new ArrayList<>();

	public WCommand() {

	}

	public WCommand(String cmd, String action) {
		this.cmd = cmd;
		this.action = action;
	}

	public WCommand(String cmd, String action, List<WCommand> childCmds) {
		this.cmd = cmd;
		this.action = action;
		this.childCmds = childCmds;
	}

	public WCommand addCommand(WCommand cmd) {
		childCmds.add(cmd);
		return cmd;
	}

	public WCommand removeCommand(WCommand cmd) {
		childCmds.remove(cmd);
		return cmd;
	}

}
