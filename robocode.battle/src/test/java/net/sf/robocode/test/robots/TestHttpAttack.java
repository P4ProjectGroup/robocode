/**
 * Copyright (c) 2001-2018 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package net.sf.robocode.test.robots;


import net.sf.robocode.test.helpers.RobocodeTestBed;
import org.junit.Assert;
import robocode.control.events.TurnEndedEvent;


/**
 * @author Flemming N. Larsen (original)
 */
public class TestHttpAttack extends RobocodeTestBed {

	private boolean messagedAccessDenied;
	
	@Override
	public String getRobotNames() {
		return "HttpAttack,sample.Target";
	}

	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		super.onTurnEnded(event);

		final String out = event.getTurnSnapshot().getRobots()[0].getOutputStreamSnapshot();

		if (out.contains("access denied (java.net.SocketPermission")
				|| out.contains("access denied (\"java.net.SocketPermission\"")) {
			messagedAccessDenied = true;	
		}	
	}

	@Override
	protected void runTeardown() {
		Assert.assertTrue("HTTP connection is not allowed", messagedAccessDenied);
	}

	@Override
	protected int getExpectedErrors() {
		return hasJavaNetURLPermission ? 2 : 1; // Security error must be reported as an error. Java 8 reports two errors.
	}
}
