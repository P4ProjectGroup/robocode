/**
 * Copyright (c) 2001-2018 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package net.sf.robocode.test.robots;


import net.sf.robocode.test.helpers.Assert;
import net.sf.robocode.test.helpers.RobocodeTestBed;
import robocode.control.events.BattleFinishedEvent;
import robocode.control.events.RoundEndedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IScoreSnapshot;
import robocode.control.snapshot.ITurnSnapshot;


/**
 * This test is used for testing if the current scores and total scores are
 * calculated correctly. Current scores are tested at each turn, and the total
 * scores are tested per round and per battle.
 * 
 * @author Flemming N. Larsen (original)
 */
public class TestScoring extends RobocodeTestBed {

	ITurnSnapshot lastTurnSnapshot;

	@Override
	public String getRobotNames() {
		return "sample.Corners,sample.Crazy,sample.Fire,sample.RamFire,sample.SittingDuck,sample.SpinBot,sample.Tracker,sample.TrackFire,sample.Walls";
	}

	@Override
	public int getNumRounds() {
		return 10;
	}

	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		super.onTurnEnded(event);

		lastTurnSnapshot = event.getTurnSnapshot();

		for (IScoreSnapshot score : lastTurnSnapshot.getSortedTeamScores()) {
			// Notice that the is no such thing as a 'current total last
			// survivor bonus'
			double currentScore = score.getCurrentSurvivalScore() + score.getCurrentBulletDamageScore()
					+ score.getCurrentBulletKillBonus() + score.getCurrentRammingDamageScore()
					+ score.getCurrentRammingKillBonus() + score.getCurrentSurvivalBonus();

			Assert.assertNear(currentScore, score.getCurrentScore());
		}

		IScoreSnapshot winnerScore = lastTurnSnapshot.getSortedTeamScores()[0];
		double currentWinnerScore = winnerScore.getCurrentSurvivalScore() + winnerScore.getCurrentBulletDamageScore()
				+ winnerScore.getCurrentBulletKillBonus() + winnerScore.getCurrentRammingDamageScore()
				+ winnerScore.getCurrentRammingKillBonus() + winnerScore.getCurrentSurvivalBonus();

		Assert.assertNear(currentWinnerScore, lastTurnSnapshot.getCurrentWinner().getValue().getCurrentScore());

		IScoreSnapshot loserScore = lastTurnSnapshot.getSortedTeamScores()[lastTurnSnapshot.getSortedTeamScores().length - 1];
		double loserCurrentScore = loserScore.getCurrentSurvivalScore() + loserScore.getCurrentBulletDamageScore()
				+ loserScore.getCurrentBulletKillBonus() + loserScore.getCurrentRammingDamageScore()
				+ loserScore.getCurrentRammingKillBonus() + loserScore.getCurrentSurvivalBonus();

		Assert.assertNear(loserCurrentScore, lastTurnSnapshot.getCurrentLoser().getValue().getCurrentScore());
	}

	@Override
	public void onRoundEnded(RoundEndedEvent event) {
		super.onRoundEnded(event);

		for (IScoreSnapshot score : lastTurnSnapshot.getSortedTeamScores()) {

			double totalScore = score.getTotalSurvivalScore() + score.getTotalLastSurvivorBonus()
					+ score.getTotalBulletDamageScore() + score.getTotalBulletKillBonus() + score.getTotalRammingDamageScore()
					+ score.getTotalRammingKillBonus();

			Assert.assertNear(totalScore, score.getTotalScore());
		}
	}

	@Override
	public void onBattleFinished(BattleFinishedEvent event) {
		for (IScoreSnapshot score : lastTurnSnapshot.getSortedTeamScores()) {

			double totalScore = score.getTotalSurvivalScore() + score.getTotalLastSurvivorBonus()
					+ score.getTotalBulletDamageScore() + score.getTotalBulletKillBonus() + score.getTotalRammingDamageScore()
					+ score.getTotalRammingKillBonus();

			Assert.assertNear(totalScore, score.getTotalScore());
		}
	}
}
