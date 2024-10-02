package entities;

import static utilities.Constants.EnemyConstants.*;
import static utilities.Constants.*;
import static utilities.HelpMethods.*;

import java.awt.geom.Rectangle2D;

import static utilities.Constants.Directions.*;
import main.Game;

public abstract class Enemy extends Entity {

	protected int enemyType;
	protected boolean firstUpdate = true;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = 0.75f * Game.TILES_SIZE;
	protected boolean active = true;
	protected boolean attackChecked;
	protected int animSpeed = (int) (ANIM_SPEED / 0.5);

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		maxHealth = GetMaxHealth(enemyType);
		currHealth = maxHealth;
		walkSpeed = 0.35f * Game.SCALE;
	}

	protected void firstupdateCheck(int[][] lvlData) {
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}

	protected void updateInAir(int[][] lvlData) {
		if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
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
		return absDistValue <= attackDistance * 10;// range of view is 10 times the attackDistance
	}

	protected boolean isPlayerCloseForAttack(Player player) {
		int absDistValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absDistValue <= attackDistance;
	}

	protected void newState(int enemyState) {
		this.state = enemyState;
		animTick = 0;
		animIndex = 0;
	}

	public void hurt(int amount) {
		currHealth -= amount;
		if (currHealth <= 0)
			active = false;
		else
			newState(HIT);
	}

	protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
		if (attackBox.intersects(player.hitbox)) {
			player.changeHealth(-GetEnemyDmg(enemyType));
			player.gotHit();
		}
		attackChecked = true;
	}

	protected void updateAnimationTick() {
		animTick++;
		if (animTick >= animSpeed) {
			animTick = 0;
			animIndex++;
			if (animIndex >= GetSpriteAmount(enemyType, state)) {
				animIndex = 0;
				switch (state) {
				case ATTACK, HIT -> state = IDLE;
				}
			}
		}
	}

	protected void changeWalkDir() {
		if (walkDir == LEFT) {
			state = RUNNING;
			walkDir = RIGHT;
		} else {
			state = RUNNING_REVERSE;
			walkDir = LEFT;
		}
	}

	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currHealth = maxHealth;
		newState(IDLE);
		active = true;
		airSpeed = 0;
	}

	public boolean isActive() {
		return active;
	}
}
