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
package robocode.peer.proxies;


import robocode.robotinterfaces.peer.IStandardRobotPeer;
import robocode.peer.RobotPeer;
import robocode.peer.RobotStatics;
import robocode.peer.robot.EventManager;


/**
 * @author Pavel Savara (original)
 */
public class StandardRobotProxy extends BasicRobotProxy implements IStandardRobotPeer {

    private boolean isStopped;
    private double saveAngleToTurn;
    private double saveDistanceToGo;
    private double saveGunAngleToTurn;
    private double saveRadarAngleToTurn;

	public StandardRobotProxy(RobotPeer peer, RobotStatics statics) {
		super(peer, statics);
	}

    @Override
    public void initialize(){
        super.initialize();
        isStopped = true;
    }

	// blocking actions
	public void stop(boolean overwrite) {
        setStopImpl(overwrite);
        execute();
	}

	public void resume() {
        setResumeImpl();
        execute();
	}

	public void rescan() {
        boolean reset = false;
        boolean resetValue = false;

        final EventManager eventManager = peer.getEventManager();
        
        if (eventManager.getCurrentTopEventPriority() == eventManager.getScannedRobotEventPriority()) {
            reset = true;
            resetValue = eventManager.getInterruptible(eventManager.getScannedRobotEventPriority());
            eventManager.setInterruptible(eventManager.getScannedRobotEventPriority(), true);
        }

        commands.setScan(true);
        executeImpl();
        if (reset) {
            eventManager.setInterruptible(eventManager.getScannedRobotEventPriority(), resetValue);
        }
	}

	public void turnRadar(double radians) {
        setTurnRadarImpl(radians);
        do {
            execute(); // Always tick at least once
        } while (getRadarTurnRemaining() != 0);
	}

	// fast setters
	public void setAdjustGunForBodyTurn(boolean newAdjustGunForBodyTurn) {
		setCall();
		commands.setAdjustGunForBodyTurn(newAdjustGunForBodyTurn);
	}

	public void setAdjustRadarForGunTurn(boolean newAdjustRadarForGunTurn) {
		setCall();
        commands.setAdjustRadarForGunTurn(newAdjustRadarForGunTurn);
        if (!commands.isAdjustRadarForBodyTurnSet()) {
            commands.setAdjustRadarForBodyTurn(newAdjustRadarForGunTurn);
        }
	}

	public void setAdjustRadarForBodyTurn(boolean newAdjustRadarForBodyTurn) {
		setCall();
        commands.setAdjustRadarForBodyTurn(newAdjustRadarForBodyTurn);
        commands.setAdjustRadarForBodyTurnSet(true);
	}

    protected final void setResumeImpl() {
        if (isStopped) {
            isStopped = false;
            commands.setDistanceRemaining(saveDistanceToGo);
            commands.setBodyTurnRemaining(saveAngleToTurn);
            commands.setGunTurnRemaining(saveGunAngleToTurn);
            commands.setRadarTurnRemaining(saveRadarAngleToTurn);
        }
    }

    protected final void setStopImpl(boolean overwrite) {
        if (!isStopped || overwrite) {
            this.saveDistanceToGo = getDistanceRemaining();
            this.saveAngleToTurn = getBodyTurnRemaining();
            this.saveGunAngleToTurn = getGunTurnRemaining();
            this.saveRadarAngleToTurn = getRadarTurnRemaining();
        }
        isStopped = true;

        commands.setDistanceRemaining(0);
        commands.setBodyTurnRemaining(0);
        commands.setGunTurnRemaining(0);
        commands.setRadarTurnRemaining(0);
    }
}
