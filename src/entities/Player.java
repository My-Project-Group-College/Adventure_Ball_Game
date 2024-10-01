package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import gamestates.Playing;

import static utilities.Constants.Directions.*;
import static utilities.Constants.PlayerConstants.*;
import static utilities.HelpMethods.*;

import main.Game;
import utilities.LoadSave;

public class Player extends Entity {
	private BufferedImage[][] animations;
	private int animTick, animIndex, animSpeed = 22;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down, hit, jump, dash;
	private float playerSpeed = 1.6f * Game.SCALE;
	private int[][] lvlData;
	private float xDrawOffset = 6.25f * Game.SCALE;
	private float yDrawOffset = 10.5f * Game.SCALE;

	// Jumping / Gravity
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;

	// Status Bar
	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);

	private int maxHealth = 5; // Player Health
	private int currHealth = maxHealth;
	private int healthWidth = healthBarWidth;

	// Attack Hitbox
	private Rectangle2D.Float attackBox;

	private int flipX = 0;
	private int flipW = 1;

	private boolean attackChecked;

	private Playing playing;

	public float dashCooldown = 0;

	public static final int PLAYER_WIDTH = (int) (Game.TILES_SIZE * 1.35);
	public static final int PLAYER_HEIGHT = (int) (Game.TILES_SIZE * 1.35);

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		loadAnimations();
		initHitbox(x, y, (int) (29 * Game.SCALE), (int) (29 * Game.SCALE));
		initAttackBox(x, y);
	}

	private void initAttackBox(float x, float y) {
		attackBox = new Rectangle2D.Float(x, y, (int) (hitbox.width * 0.75), hitbox.height);
	}

	public void update() {
		updateHealthBar();

		if (currHealth <= 0) {
			playing.setGameOver(true);
			return;
		}
		updatePosition();
		updateAttackBox();
		if (attacking)
			checkAttack();
		updateAnimationTick();
		setAnimation();

	}

	private void checkAttack() {
		if (attackChecked || animIndex != 1)
			return;
		attackChecked = true;
		playing.checkEnemyHit(attackBox);
	}

	private void updateAttackBox() {
		if (right)
			attackBox.x = hitbox.x + attackBox.width + (int) (Game.SCALE * 11);
		else if (left)
			attackBox.x = hitbox.x - attackBox.width - (int) (Game.SCALE * 2);

		attackBox.y = hitbox.y + (Game.SCALE * 2);
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currHealth / (float) maxHealth) * healthBarWidth);
	}

	public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.drawImage(animations[playerAction][animIndex], (int) (hitbox.x - xDrawOffset) - xLvlOffset,
				(int) (hitbox.y - yDrawOffset) - yLvlOffset, PLAYER_WIDTH, PLAYER_HEIGHT, null);
		drawHitbox(g, xLvlOffset, yLvlOffset);
		drawAttackBox(g, xLvlOffset, yLvlOffset);
		drawUI(g);
	}

	private void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.setColor(Color.RED);
		g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y - yLvlOffset, (int) attackBox.width,
				(int) attackBox.height);

	}

	private void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.RED);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}

	private void updateAnimationTick() {
		animTick++;
		if (animTick >= animSpeed) {
			animTick = 0;
			animIndex++;
			if (animIndex >= GetSpriteAmount(playerAction)) {
				animIndex = 0;
				attacking = false;
				attackChecked = false;
				hit = false;
			}
		}
	}

	public void setAnimation() {
		int startAnim = playerAction;
		if (moving) {
			if (right && !left)
				playerAction = RUNNING;
			else if (left && !right)
				playerAction = RUNNING_REVERSE;
		} else
			playerAction = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				playerAction = JUMP;
			else
				playerAction = FALLING;
		}

		if (attacking)
			playerAction = ATTACK;

		if (dash && (right || left || jump) && dashCooldown == 0)
			playerAction = DASH;
		if (hit)
			playerAction = HIT;
		if (startAnim != playerAction)
			resetAnimTick();
	}

	private void resetAnimTick() {
		animTick = 0;
		animIndex = 0;
	}

	public void updatePosition() {

		moving = false;

		if (jump)
			jump();
		if (!inAir && left == right)
			return;

		float xSpeed = 0;

		if (left)
			xSpeed -= playerSpeed;
//		{
//			xSpeed -= playerSpeed;
//			flipX = width;
//			flipW = -1;
//		}

		if (right)
			xSpeed += playerSpeed;
//		{
//			xSpeed += playerSpeed;
//			flipX = 0;
//			flipW = 1;
//		}

		if (dash && dashCooldown == 0) {
			if (left)
				xSpeed -= 1.5f + playerSpeed;
			xSpeed += 1.5f + playerSpeed;
		}

		if (!inAir && !IsEntityOnFloor(hitbox, lvlData))
			inAir = true;

		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
			}
		} else
			updateXPos(xSpeed);

		moving = true;
	}

	private void jump() {
		if (inAir)
			return;

		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			hitbox.x += xSpeed;
		else
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
	}

	public void changeHealth(int val) {
		currHealth += val;
		if (currHealth <= 0)
			currHealth = 0;
		else if (currHealth > maxHealth)
			currHealth = maxHealth;
	}

	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

		animations = new BufferedImage[10][8];
		for (int i = 0; i < animations.length - 1; i++) {
			for (int j = 0; j < animations[i].length; j++) {
				if (i != RUNNING_REVERSE)
					animations[i][j] = img.getSubimage(j * 64, i * 64, 64, 64);
			}
		}
		for (int i = 0; i < animations[RUNNING_REVERSE].length; i++) {
			animations[RUNNING_REVERSE][i] = animations[RUNNING][GetSpriteAmount(RUNNING) - 1 - i];
		}

		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
		jump = false;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		playerAction = IDLE;
		currHealth = maxHealth;

		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public void resetDash() {
		dashCooldown = 1.5f;
		playerAction = IDLE;
	}

	public void gotHit() {
		hit = true;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public boolean isDash() {
		return dash;
	}

	public void setDash(boolean dash) {
		this.dash = dash;
	}

}
