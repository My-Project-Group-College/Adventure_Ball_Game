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

/*
 * TODO 
 * Get Proper Image file and adjust accordingly
 */
public class GamePanel extends JPanel
{
	private MouseInputs mouseInputs;
	private float xDelta = 100, yDelta = 100;
	private Dimension size;
	private BufferedImage img, subImg;

	
	public GamePanel() 
	{
		mouseInputs = new MouseInputs(this);
		
		importImg();
		
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}
	
	private void importImg()
	{
		InputStream inpStream = getClass().getResourceAsStream("/sprites/mainBall.png");
		
		try 
		{
			img = ImageIO.read(inpStream);
		} 
		catch (IOException e) 
		{
			System.out.println("Error While Loading Player Sprites");
			e.printStackTrace();
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
		
		subImg = img.getSubimage(0*64, 0*64, 64, 40);
		g.drawImage(subImg, (int)xDelta, (int)yDelta, 128, 80, null);
	}
}
