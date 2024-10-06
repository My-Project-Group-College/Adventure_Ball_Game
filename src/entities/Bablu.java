package entities;

import static utilities.Constants.EnemyConstants.*;
import static utilities.Constants.Directions.*;

import java.awt.geom.Rectangle2D;

public class Bablu extends Enemy {

	private int attackBoxOffsetX;

	public Bablu(float x, float y) {
		super(x, y, BABLU_WIDTH, BABLU_HEIGHT, BABLU);
		initHitbox((int) (30 * 0.75), (int) (32 * 0.75));
		initAttackBox();
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (hitbox.width * 3), hitbox.height);
		attackBoxOffsetX = (int) (hitbox.width);

	}

	public void update(int[][] lvlData, Player player) {
		updateBehaviour(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
	}

	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
	}

	private void updateBehaviour(int[][] lvlData, Player player) {
		if (firstUpdate)
			firstupdateCheck(lvlData);
		if (inAir) {
			updateInAir(lvlData);
		} else {
			switch (state) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
			case RUNNING_REVERSE:
				if (canSeePlayer(lvlData, player)) {
					turnTowardsPlayer(player);
					if (isPlayerCloseForAttack(player))
						newState(ATTACK);
				}
				move(lvlData);
				break;
			case ATTACK:
				if (animIndex == 0)
					attackChecked = false;
				if (animIndex == 1 && !attackChecked)
					checkEnemyHit(attackBox, player);
				break;
			case HIT:
				break;
			}
		}
	}

	public int flipX() {
		if (walkDir == RIGHT)
			return width;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}
}
