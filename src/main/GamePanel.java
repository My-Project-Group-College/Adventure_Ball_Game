package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel
{
	private MouseInputs mouseInputs;
	private float xDelta = 100, yDelta = 100;
	private float xDir = 1, yDir = 1;
	private int frames = 0;
	private long lastCheck = 0;
	private Color color = new Color(150, 20, 90);
	private Random random;

	
	public GamePanel() 
	{
		random = new Random();
		mouseInputs = new MouseInputs(this);
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}
	
	public void addXDelta(int value)
	{
		this.xDelta += value;
	}
	public void addYDelta(int value)
	{
		this.yDelta += value;
	}
	public void setRectPosition(int x, int y)
	{
		this.xDelta = x;
		this.yDelta = y;
	}
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		updateRect();
		g.setColor(color);
		g.fillRect((int)xDelta, (int)yDelta, 200, 50);
	}

	private void updateRect() 
	{
		xDelta += xDir;
		if((xDelta + 200) > Game.WIDTH || xDelta < 0)
		{			
			xDir *= -1;
			color = getRndmColor();
		}
		yDelta += yDir;
		if((yDelta + 50) > Game.HEIGHT - 3 || yDelta < 0)	//for some reason it has 3 extra pixels below bottom
		{			
			yDir *= -1;
			color = getRndmColor();
		}
	}

	private Color getRndmColor() 
	{
		int r = random.nextInt(255);
		int g = random.nextInt(255);
		int b = random.nextInt(255);
		
		return new Color(r,g,b);
	}
}
