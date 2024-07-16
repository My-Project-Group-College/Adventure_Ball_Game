package main;

public class Game 
{
	
	private GameWindow gameWindow;
	public Game() 
	{
		GamePanel gamePanel = new GamePanel();
		gameWindow = new GameWindow(gamePanel);
	}
}
