/*******************************************************************************
 * Copyright (c) 2001, 2008 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Pavel Savara
 *     - Initial implementation
 *******************************************************************************/
package robots;


import helpers.RobotTestBed;
import helpers.Assert;
import org.junit.Test;
import org.junit.After;
import robocode.battle.events.TurnEndedEvent;

import java.io.File;


/**
 * @author Pavel Savara (original)
 */
public class TestAwtAttack extends RobotTestBed {
	boolean messagedAttack;
	boolean messagedBreakthru;

	@Test
	public void run() {
		super.run();
	}

	public void onTurnEnded(TurnEndedEvent event) {
		super.onTurnEnded(event);
		final String out = event.getTurnSnapshot().getRobots().get(1).getOutputStreamSnapshot();

		if (out.contains("Hacked!!!")) {
			messagedBreakthru = true;
		}
		if (out.contains("java.security.AccessControlException: Preventing testing.AwtAttack from access to threadgroup")) {
			messagedAttack = true;
		}
	}

	@Override
	public String getRobotNames() {
		return "sample.SittingDuck,testing.AwtAttack";
	}

	@After
	public void tearDownAttack() {
		Assert.assertFalse(messagedBreakthru);
		Assert.assertTrue(messagedAttack);
	}
}