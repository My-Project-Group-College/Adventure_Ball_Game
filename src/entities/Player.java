package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Playing;

import static utilities.Constants.PlayerConstants.*;
import static utilities.Constants.*;
import static utilities.HelpMethods.*;

import main.Game;
import utilities.LoadSave;

public class Player extends Entity {
	private BufferedImage[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, hit, jump, dash;
	private int[][] lvlData;
	private float xDrawOffset = 5.75f * Game.SCALE;
	private float yDrawOffset = 9.0f * Game.SCALE;

	// Jumping / Gravity
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

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
	private int healthWidth = healthBarWidth;

	private int dashBarWidth = (int) (104 * Game.SCALE);
	private int dashBarHeight = (int) (2 * Game.SCALE);
	private int dashBarXStart = (int) (44 * Game.SCALE);
	private int dashBarYStart = (int) (34 * Game.SCALE);
	private int dashWidth = dashBarWidth;

	private int flipX = 0;
	private int flipW = 1;

	private boolean attackChecked;

	private Playing playing;

	public float dashCooldown = DASH_READY;

	public static final int PLAYER_WIDTH = (int) (Game.TILES_SIZE * 1.25);
	public static final int PLAYER_HEIGHT = (int) (Game.TILES_SIZE * 1.25);

	private String coinsString;
	private String timeString;
	private int totalTimeRemaining;
	private int timeInMilli, timeInSecs, timeInMins;

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 5;
		this.currHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.6f;
		loadAnimations();
		initHitbox(27, 27);
		initAttackBox();
		coinsString = "Coins : 0";
		timeString = "Time:  0:0:0";

	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (hitbox.width * 2.6), hitbox.height - (2 * Game.SCALE));
	}

	public void update() {
		updateTime();
		updateStrings();
		updateHealthBar();
		updateDashBar();

		if (currHealth <= 0 || playing.getLevelManager().getCurrentLevel().getTimePassed() >= playing.getLevelManager()
				.getCurrentLevel().getTotalTime()) {
			playing.setGameOver(true);
			playing.getGame().getAudioPlayer().stopSong();
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			return;
		}

		updateAttackBox();

		updatePosition();

		if (moving) {
			checkCoinTouched();
			checkSpikesTouched();
		}

		if (attacking)
			checkAttack();
		if (dash && dashCooldown >= DASH_READY)
			playing.checkEnemyHit(attackBox, PLAYER_DASH_DAMAGE);
		updateAnimationTick();
		setAnimation();

	}

	private void checkSpikesTouched() {
		playing.checkSpikeTouched(this);
	}

	private void updateTime() {
		totalTimeRemaining = playing.getLevelManager().getCurrentLevel().timeRemaining();
		timeInMilli = (totalTimeRemaining % 1000) / 10;
		timeInSecs = (totalTimeRemaining / 1000) % 60;
		timeInMins = (totalTimeRemaining / 60000) % 60;
	}

	private void updateStrings() {
		coinsString = "Coins: " + playing.getLevelManager().getCurrentLevel().getCoinsCollected();
		timeString = String.format("Time: %02d:%02d:%02d", timeInMins, timeInSecs, timeInMilli);
	}

	private void checkCoinTouched() {
		playing.checkCoinTouched(hitbox);
	}

	private void updateDashBar() {

		if (dashCooldown >= DASH_READY)
			dashCooldown = DASH_READY;
		else if (dashCooldown <= 0)
			dashCooldown = 0;

		dashWidth = (int) ((dashCooldown / (float) DASH_READY) * dashBarWidth);
	}

	private void checkAttack() {
		if (attackChecked || animIndex != 1)
			return;
		attackChecked = true;
		playing.getGame().getAudioPlayer().playAttackSound();
		playing.checkEnemyHit(attackBox, PLAYER_NORMAL_DAMAGE);
	}

	private void updateAttackBox() {
		if (right)
			attackBox.x = hitbox.x - (int) (hitbox.width * 0.6);
		else if (left)
			attackBox.x = hitbox.x - hitbox.width;
		else
			attackBox.x = hitbox.x - (int) (hitbox.width * 0.8);

		attackBox.y = hitbox.y + (Game.SCALE * 2);
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currHealth / (float) maxHealth) * healthBarWidth);
	}

	public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.drawImage(animations[state][animIndex], (int) (hitbox.x - xDrawOffset) - xLvlOffset,
				(int) (hitbox.y - yDrawOffset) - yLvlOffset, PLAYER_WIDTH, PLAYER_HEIGHT, null);
		if (Game.getHitboxStatus()) {
			drawHitbox(g, xLvlOffset, yLvlOffset);
			drawAttackBox(g, xLvlOffset, yLvlOffset);
		}
		drawUI(g);
	}

	private void drawUI(Graphics g) {
		// Status Bar
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		// Health Bar
		g.setColor(Color.RED);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

		// Status Bar
		g.setColor(Color.GREEN);
		if (dash && dashCooldown >= DASH_READY && animIndex == 2)
			g.setColor(Color.YELLOW);
		g.fillRect(dashBarXStart + statusBarX, dashBarYStart + statusBarY, dashWidth, dashBarHeight);

		// Coins And Time String
		g.setColor(new Color(0, 0, 0, 200));
		g.setFont(LoadSave.GetMinecraft_Font().deriveFont(Font.BOLD, 50));
		g.drawString(coinsString, Game.GAME_WIDTH - (int) (6 * Game.TILES_SIZE), (int)(Game.TILES_SIZE * 1.2));
		g.drawString(timeString, Game.GAME_WIDTH - (int) (10 * Game.TILES_SIZE), (int)(Game.TILES_SIZE * 2.4));
	}

	private void updateAnimationTick() {
		animTick++;
		if (animTick >= ANIM_SPEED) {
			animTick = 0;
			animIndex++;
			if (animIndex >= GetSpriteAmount(state)) {
				animIndex = 0;
				attacking = false;
				attackChecked = false;
				hit = false;
			}
		}
	}

	public void setAnimation() {
		int startAnim = state;
		if (moving) {
			if (right && !left)
				state = RUNNING;
			else if (left && !right)
				state = RUNNING_REVERSE;
		} else
			state = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				state = JUMP;
			else
				state = FALLING;
		}

		if (attacking)
			state = ATTACK;

		if (dash && (right || left || jump) && dashCooldown >= DASH_READY)
			state = DASH;
		if (hit)
			state = HIT;
		if (startAnim != state)
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
			xSpeed -= walkSpeed;
//		{
//			xSpeed -= playerSpeed;
//			flipX = width;
//			flipW = -1;
//		}

		if (right)
			xSpeed += walkSpeed;
//		{
//			xSpeed += playerSpeed;
//			flipX = 0;
//			flipW = 1;
//		}

		if (dash && dashCooldown >= DASH_READY) {
			if (left)
				xSpeed -= 1.5f + walkSpeed;
			else
				xSpeed += 1.5f + walkSpeed;

		}

		if (!inAir && !IsEntityOnFloor(hitbox, lvlData))
			inAir = true;

		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
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
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
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
		jump = false;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		state = IDLE;
		currHealth = maxHealth;
		dashCooldown = DASH_READY;
		dash = false;

		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public void resetDash() {
		dashCooldown = 0;
		state = IDLE;
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

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
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

	public void kill() {
		currHealth = 0;
	}

	public void setDash(boolean dash) {
		this.dash = dash;
	}

}
