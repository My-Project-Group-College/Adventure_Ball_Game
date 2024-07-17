package main;

import java.awt.Toolkit;

public class Game implements Runnable
{
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	static int HEIGHT;
	static int WIDTH;
	
	public Game() 
	{
		gamePanel = new GamePanel();
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		HEIGHT = gameWindow.frame.getContentPane().getSize().height;
		WIDTH = gameWindow.frame.getContentPane().getSize().width;
		startGameLoop();
	}
	
	private void startGameLoop()
	{
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() 
	{
		double timePerFrame = 1000000000.0 / FPS_SET;
		long lastFrame = System.nanoTime();
		long now = System.nanoTime();
		
		int frames = 0;
		long lastCheck = System.currentTimeMillis();
		
		while(true)
		{
			if(System.getProperty("os.name").equals("Linux"))
				Toolkit.getDefaultToolkit().sync();	// Linux Lags Fixed  
			now = System.nanoTime();
			if(now - lastFrame >= timePerFrame)
			{
				gamePanel.repaint();
				lastFrame = now;
				frames++;
			}
			
			if(System.currentTimeMillis() - lastCheck >= 1000)
			{
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
	}
}
