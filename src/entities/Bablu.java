package entities;

import static utilities.Constants.EnemyConstants.*;
import main.Game;

public class Bablu extends Enemy {

	public Bablu(float x, float y) {
		super(x, y, BABLU_WIDTH, BABLU_HEIGHT, BABLU);
		initHitbox(x, y, (int) (30 * Game.SCALE * 0.75), (int) (33 * Game.SCALE * 0.75));
	}

	public void update(int[][] lvlData, Player player) {
		updateMove(lvlData, player);
		updateAnimationTick();
	}

	private void updateMove(int[][] lvlData, Player player) {
		if (firstUpdate)
			firstupdateCheck(lvlData);
		if (inAir) {
			updateInAir(lvlData);
		} else {
			switch (enemyState) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
			case RUNNING_REVERSE:
				if (canSeePlayer(lvlData, player))
					turnTowardsPlayer(player);
				if (isPlayerCloseForAttack(player))
					newState(ATTACK);

				move(lvlData);
				break;
			}
		}
	}
}
