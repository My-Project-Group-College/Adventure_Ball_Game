package main;

import javax.swing.JFrame;

public class GameWindow 
{
	
	JFrame frame;
	
	public GameWindow(GamePanel gamePanel) 
	{
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(500, 500);
		frame.setResizable(false);
		
		frame.add(gamePanel);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
