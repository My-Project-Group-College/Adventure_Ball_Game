package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel
{
	private MouseInputs mouseInputs;
	private Dimension size;
	private Game game;
	
	public GamePanel(Game game) 
	{
		mouseInputs = new MouseInputs(this);
		this.game = game;
		
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
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
	
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		game.render(g);
	}
	
	public Game getGame()
	{
		return game;
	}
}
