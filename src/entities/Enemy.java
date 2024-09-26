package entities;

import static utilities.Constants.EnemyConstants.*;
import static utilities.HelpMethods.*;
import static utilities.Constants.Directions.*;
import main.Game;

public abstract class Enemy extends Entity {

	private int animIndex, enemyType, enemyState = 1;
	private int aniTick, aniSpeed = 20;
	private boolean firstUpdate = true;
	private boolean inAir;
	private float fallSpeed;
	private float gravity = 0.04f * Game.SCALE;
	private float walkSpeed = 0.35f * Game.SCALE;
	private int walkDir = LEFT;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitbox(x, y, width, height);
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			animIndex++;
			if (animIndex >= GetSpriteAmount(enemyType, enemyState))
				animIndex = 0;
		}
	}

	public void update(int[][] lvlData) {
		updateMove(lvlData);
		updateAnimationTick();
	}

	private void updateMove(int[][] lvlData) {
		if (firstUpdate) {
			if (!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;
			firstUpdate = false;
		}
		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += fallSpeed;
				fallSpeed += gravity;
			} else {
				inAir = false;
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
			}
		} else {
			switch (enemyState) {
			case IDLE:
				enemyState = RUNNING;
				break;
			case RUNNING:
			case RUNNING_REVERSE:
				float xSpeed = 0;

				if (walkDir == LEFT)
					xSpeed = -walkSpeed;
				else
					xSpeed = walkSpeed;

				if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
					if (IsFloor(hitbox, xSpeed, lvlData)) {
						hitbox.x += xSpeed;
						return;
					}

				changeWalkDir();

				break;
			}
		}
	}

	private void changeWalkDir() {
		if (walkDir == LEFT) {
			enemyState = RUNNING;
			walkDir = RIGHT;
		} else {
			enemyState = RUNNING_REVERSE;
			walkDir = LEFT;
		}
	}

	public int getAnimIndex() {
		return animIndex;
	}

	public int getEnemyState() {
		return enemyState;
	}
}
