package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilities.LoadSave;
import static utilities.Constants.UI.URMButtons.*;

public class URMButton extends PauseButton 
{
	private BufferedImage[] imgs;
	private int rowIndex, index;
	private boolean mouseOver, mousePressed;
	
	public URMButton(int x, int y, int width, int height, int rowIndex) 
	{
		super(x, y, width, height);
		this.rowIndex = rowIndex;
		loadImages();
	}

	private void loadImages() 
	{
		BufferedImage tmp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
		imgs = new BufferedImage[3];
		for(int i = 0; i < imgs.length; i++)
			imgs[i] = tmp.getSubimage(i * URM_B_DEFAULT_SIZE, rowIndex * URM_B_DEFAULT_SIZE,
					URM_B_DEFAULT_SIZE, URM_B_DEFAULT_SIZE);
	}

	public void update()
	{
		index = 0;
		if(mouseOver)
			index = 1;
		if(mousePressed)
			index = 2;
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(imgs[index], x, y, URM_B_SIZE, URM_B_SIZE, null);
	}
	
	public void resetBools()
	{
		mouseOver = false;
		mousePressed = false;
	}

	public boolean isMouseOver() 
	{
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) 
	{
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() 
	{
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) 
	{
		this.mousePressed = mousePressed;
	}
	
	
}
