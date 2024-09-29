package entities;

import static utilities.Constants.Directions.LEFT;
import static utilities.Constants.EnemyConstants.*;
import static utilities.HelpMethods.CanMoveHere;
import static utilities.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static utilities.HelpMethods.IsEntityOnFloor;
import static utilities.HelpMethods.IsFloor;

import main.Game;

public class Bablu extends Enemy {

	public Bablu(float x, float y) {
		super(x, y, BABLU_WIDTH, BABLU_HEIGHT, BABLU);
		initHitbox(x, y, (int) (30 * Game.SCALE * 0.75), (int) (33 * Game.SCALE * 0.75));
	}
	public void update(int[][] lvlData) {
		updateMove(lvlData);
		updateAnimationTick();
	}
	private void updateMove(int[][] lvlData) {
		if (firstUpdate) 
			firstUpdateCheck(lvlData);
		if (inAir) {
			updateInAir(lvlData);
		} else {
			switch (enemyState) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
			case RUNNING_REVERSE:
                move(lvlData);
				break;
			}
		}
	}
}
