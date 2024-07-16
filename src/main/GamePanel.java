package main;

import java.awt.Graphics;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel
{
	private MouseInputs mouseInputs;
	private int xDelta = 100, yDelta = 100;
	
	public GamePanel() 
	{
		mouseInputs = new MouseInputs(this);
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}
	
	public void addXDelta(int value)
	{
		this.xDelta += value;
		repaint();
	}
	public void addYDelta(int value)
	{
		this.yDelta += value;
		repaint();
	}
	public void setRectPosition(int x, int y)
	{
		this.xDelta = x;
		this.yDelta = y;
		repaint();
	}
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		g.fillRect(xDelta, yDelta, 200, 50);
	}
}
