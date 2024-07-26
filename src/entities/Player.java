package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import utilities.LoadSave;
import static utilities.Constants.Directions.*;
import static utilities.Constants.PlayerConstants.*;
import main.Game;

public class Player extends Entity
{
	private BufferedImage[][] animations;
	private int animTick, animIndex, animSpeed = 25;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down;
	private float playerSpeed = 1.75f;
	private int runningDirection = RIGHT;
	
	public static final int PLAYER_WIDTH = (int) (Game.TILES_SIZE * 1.72);
	public static final int PLAYER_HEIGHT= (int) (Game.TILES_SIZE * 1.72);
	
	
	public Player(float x, float y, int width, int height) 
	{
		super(x, y, width, height);
		loadAnimations();
	}
	
	public void update()
	{
		updatePosition();
		updateAnimationTick();
		setAnimation();
	}
	
	public void render(Graphics g)
	{
		g.drawImage(animations[playerAction][animIndex], (int)x, (int)y, PLAYER_WIDTH, PLAYER_HEIGHT, null);
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
	
	public void RunningDir(int runningDirection)
	{
		this.runningDirection = runningDirection;
	}

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
		
		if(left && !right)
		{
			x -= playerSpeed;
			moving = true;
		}
		else if(right && !left)
		{
			x += playerSpeed;
			moving = true;
		}
		
		if(up && !down)
		{
			y -= playerSpeed;
			moving = true;
		}
		else if(down && !up)
		{
			y += playerSpeed;
			moving = true;
		}
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

	
}
