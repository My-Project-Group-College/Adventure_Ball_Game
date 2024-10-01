package entities;

import static utilities.Constants.EnemyConstants.*;
import static utilities.Constants.Directions.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class Bablu extends Enemy {

	// Attack Hitbox
	private Rectangle2D.Float attackBox;
	private int attackBoxOffsetX;

	public Bablu(float x, float y) {
		super(x, y, BABLU_WIDTH, BABLU_HEIGHT, BABLU);
		initHitbox(x, y, (int) (30 * Game.SCALE * 0.75), (int) (33 * Game.SCALE * 0.75));
		initAttackBox(x, y);
	}

	private void initAttackBox(float x, float y) {
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

	public void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.setColor(Color.RED);
		g.drawRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y - yLvlOffset), (int) attackBox.width,
				(int) attackBox.height);
	}

	public int flipX() {
		if (walkDir == RIGHT)
			return width;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == RIGHT)
			return 1;
		else
			return -1;
	}
}
