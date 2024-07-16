package main;

import javax.swing.JFrame;

public class GameWindow 
{
	
	JFrame frame;
	
	public GameWindow(GamePanel gamePanel) 
	{
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		
		frame.add(gamePanel);
		frame.setVisible(true);
	}
}
