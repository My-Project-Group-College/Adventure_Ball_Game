package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import static utilities.Constants.Directions.*;
import static utilities.Constants.PlayerConstants.*;
import static utilities.HelpMethods.*;

import main.Game;
import utilities.LoadSave;

public class Player extends Entity
{
	private BufferedImage[][] animations;
	private int animTick, animIndex, animSpeed = 20;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false, dashing = false;//
	private boolean left, up, right, down, jump;
	private float playerSpeed = 2f;
	private int runningDirection = RIGHT , dashingdirection = RIGHT;//
	private int[][]lvlData;
	private float xDrawOffset = 6.25f * Game.SCALE;
	private float yDrawOffset = 10.5f * Game.SCALE;
	
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;
	
	public static final int PLAYER_WIDTH = (int) (Game.TILES_SIZE * 1.35);
	public static final int PLAYER_HEIGHT= (int) (Game.TILES_SIZE * 1.35);
	
	
	public Player(float x, float y, int width, int height) 
	{
		super(x, y, width, height);
		loadAnimations();
		initHitbox(x, y, 29 * Game.SCALE, 29 * Game.SCALE);
	}
	
	public void update()
	{
		updatePosition();
		updateAnimationTick();
		setAnimation();
	}
	
	public void render(Graphics g)
	{
		g.drawImage(animations[playerAction][animIndex], (int)(hitbox.x - xDrawOffset), (int)(hitbox.y - yDrawOffset), PLAYER_WIDTH, PLAYER_HEIGHT, null);
		drawHitbox(g);
	}
	
	private void updateAnimationTick() 
	{
		animTick++;
		if(animTick >= animSpeed)
		{
			animTick = 0;
			animIndex++;
			if(animIndex >= GetSpriteAmount(playerAction))
			{
				animIndex = 0;
				attacking = false;
			}
		}
	}
	
	public void setRunningDir(int runningDirection)
	{
		this.runningDirection = runningDirection;
	}

//	public void setDashingDir(int dashingDirection) {
//		this.dashingdirection=dashingDirection;
//	}

	public void setAnimation()
	{
		int startAnim = playerAction;
		
		
		if(moving)
		{
			if(runningDirection == RIGHT)
				playerAction = RUNNING;
			else if(runningDirection == LEFT)
				playerAction = RUNNING_REVERSE;
		}
		else
			playerAction = IDLE;
		
		if(inAir)
		{
			if(airSpeed < 0)
				playerAction = JUMP;
			else
				playerAction = FALLING;
		}
		
		if(attacking)
			playerAction = ATTACK;
		
		if(startAnim != playerAction)
			resetAnimTick();
	}
	
	private void resetAnimTick() 
	{
		animTick = 0;
		animIndex = 0;
	}

	public void updatePosition()
	{

		moving = false;
		
		if(jump) jump();
		if(!left && !right && !inAir) return;
		
		float xSpeed = 0;
		
		if(left)
			xSpeed -= playerSpeed;
		
		if(right)
			xSpeed += playerSpeed;
		
		if(!inAir && !IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		
		if(inAir)
		{
			if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData))
			{
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			}			
			else
			{
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if(airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
			}
		}
		else
			updateXPos(xSpeed);
		
		
		moving = true;
	}
	
	private void jump()
	{
		if(inAir) return;
		
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() 
	{
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) 
	{
		if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
				hitbox.x += xSpeed;
		else
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
	}

	
	private void loadAnimations() 
	{
			BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
			
			
			animations = new BufferedImage[9][8];
			for(int i = 0; i < animations.length - 1; i++)
			{
				for (int j = 0; j < animations[i].length; j++)
				{
					if(i != RUNNING_REVERSE)
						animations[i][j] = img.getSubimage(j * 64, i * 64, 64, 64);
				}
			}
			for(int i = 0; i < animations[RUNNING_REVERSE].length; i++)
			{
				animations[RUNNING_REVERSE][i] = animations[RUNNING][GetSpriteAmount(RUNNING) - 1 - i]; 
			}
	}
	
	public void loadLvlData(int[][] lvlData)
	{
		this.lvlData = lvlData;
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public void resetDirBooleans() 
	{
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void setAttacking(boolean attacking)
	{
		this.attacking = attacking;
	}
	
	public boolean isLeft() 
	{
		return left;
	}

	public void setLeft(boolean left) 
	{
		this.left = left;
	}

	public boolean isUp() 
	{
		return up;
	}

	public void setUp(boolean up) 
	{
		this.up = up;
	}

	public boolean isRight() 
	{
		return right;
	}

	public void setRight(boolean right) 
	{
		this.right = right;
	}

	public boolean isDown() 
	{
		return down;
	}

	public void setDown(boolean down) 
	{
		this.down = down;
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	
}
