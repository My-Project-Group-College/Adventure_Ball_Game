package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;

public class KeyboardInputs implements KeyListener
{
	private int left = 0, right = 2;
	private GamePanel gamePanel;
	public KeyboardInputs(GamePanel gamePanel) 
	{
		this.gamePanel = gamePanel;
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) 
	{
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_W:
			gamePanel.getGame().getPlayer().setUp(true);
			gamePanel.getGame().getPlayer().setRunningDir(right);
			break;
		case KeyEvent.VK_A:
			gamePanel.getGame().getPlayer().setLeft(true);
			gamePanel.getGame().getPlayer().setRunningDir(left);
			break;
		case KeyEvent.VK_S:
			gamePanel.getGame().getPlayer().setDown(true);
			gamePanel.getGame().getPlayer().setRunningDir(left);
			break;
		case KeyEvent.VK_D:		
			gamePanel.getGame().getPlayer().setRight(true);
			gamePanel.getGame().getPlayer().setRunningDir(right);
			break;
		case KeyEvent.VK_SPACE:
			gamePanel.getGame().getPlayer().setJump(true);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_W:
			gamePanel.getGame().getPlayer().setUp(false);
			break;
		case KeyEvent.VK_A:
			gamePanel.getGame().getPlayer().setLeft(false);
			break;
		case KeyEvent.VK_S:
			gamePanel.getGame().getPlayer().setDown(false);
			break;
		case KeyEvent.VK_D:		
			gamePanel.getGame().getPlayer().setRight(false);
			break;
		case KeyEvent.VK_SPACE:
			gamePanel.getGame().getPlayer().setJump(false);
			break;
		}
		
	}

}
