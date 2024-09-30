package entities;

import static utilities.Constants.EnemyConstants.*;
import static utilities.HelpMethods.*;
import static utilities.Constants.Directions.*;
import main.Game;

public abstract class Enemy extends Entity {

	protected int animIndex, enemyType, enemyState;
	protected int aniTick, aniSpeed = 20;
	protected boolean firstUpdate = true;
	protected boolean inAir;
	protected float fallSpeed;
	protected float gravity = 0.04f * Game.SCALE;
	protected float walkSpeed = 0.35f * Game.SCALE;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = 1 * Game.TILES_SIZE;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitbox(x, y, width, height);
	}

	protected void firstupdateCheck(int[][] lvlData) {
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}

	protected void updateInAir(int[][] lvlData) {
		if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += fallSpeed;
			fallSpeed += gravity;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
			tileY = (int) hitbox.y / Game.TILES_SIZE;
		}
	}

	protected void move(int[][] lvlData) {
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

	}

	protected void turnTowardsPlayer(Player player) {
		if (player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}

	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int) player.getHitbox().y / Game.TILES_SIZE;
		if (playerTileY == tileY)
			if (playerInRange(player))
				if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
		return false;
	}

	protected boolean playerInRange(Player player) {
		int absDistValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absDistValue <= attackDistance * 5;// range of view is 5 times the attackDistance
	}

	protected boolean isPlayerCloseForAttack(Player player) {
		int absDistValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absDistValue <= attackDistance;
	}

	protected void newState(int enemyState) {
		this.enemyState = enemyState;
		aniTick = 0;
		animIndex = 0;
	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			animIndex++;
			if (animIndex >= GetSpriteAmount(enemyType, enemyState)) {
				animIndex = 0;
				if (enemyState == ATTACK)
					enemyState = IDLE;
			}
		}
	}

	protected void changeWalkDir() {
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
