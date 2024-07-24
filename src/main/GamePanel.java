package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static utilities.Constants.PlayerConstants.*;
import static utilities.Constants.Directions.*;

public class GamePanel extends JPanel
{
	private MouseInputs mouseInputs;
	private float xDelta = 100, yDelta = 100;
	private Dimension size;
	private BufferedImage img;
	private BufferedImage[][] animations;
	int animTick, animIndex, animSpeed = 15;
	private int playerAction = IDLE;
	private int playerDir = -1;
	private boolean moving = false;

	
	public GamePanel() 
	{
		mouseInputs = new MouseInputs(this);
		
		importImg();
		loadAnimations();
		
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}
	
	private void loadAnimations()
	{
		animations = new BufferedImage[8][8];
		for(int i = 0; i < animations.length; i++)
		{
			for (int j = 0; j < animations[i].length; j++)
			{
				animations[i][j] = img.getSubimage(j * 64, i * 64, 64, 64); 
			}
		}
		
	}
	
	private void importImg()
	{
		InputStream inpStream = getClass().getResourceAsStream("/sprites/playerSprite.png");
		
		try 
		{
			img = ImageIO.read(inpStream);
		} 
		catch (IOException e) 
		{
			System.out.println("Error While Loading Player Sprites");
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				inpStream.close();
			}
			catch (IOException e) 
			{
				System.out.println("Error While Closing Input Stream");
				e.printStackTrace();
			}
		}
	}
	
	private void updateAnimationTick()
	{
		animTick++;
		if(animTick >= animSpeed)
		{
			animTick = 0;
			animIndex++;
			if(animIndex >= GetSpriteAmount(playerAction))
				animIndex = 0;
		}
	}
	
	private void setPanelSize()
	{
		size = new Dimension(1280, 800);
		//1 tile 32 x 32 pixels
		//40 tiles width & 25 tiles height
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
	}	
	
	public void setDirection(int direction)
	{
		this.playerDir = direction;
		moving = true;
	}
	
	public void setMoving(Boolean moving)
	{
		this.moving = moving;
	}
	
	public void setAnimation()
	{
		if(moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;
	}
	
	public void updatePosition()
	{
		if(moving)
		{
			switch(playerDir)
			{
			case LEFT:
				xDelta -= 5;
				break;
			case UP:
				yDelta -= 5;
				break;
			case RIGHT:
				xDelta += 5;
				break;
			case DOWN:
				yDelta += 5;
				break;
			}
		}
	}
	
	public void updateGame()
	{
		updateAnimationTick();
		setAnimation();
		updatePosition();
	}
	
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		

		
		g.drawImage(animations[playerAction][animIndex], (int)xDelta, (int)yDelta, 160, 160, null);
	}
}
