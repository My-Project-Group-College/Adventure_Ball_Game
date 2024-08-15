package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.Player;
import levels.LevelManager;
import main.Game;

public class Playing extends State implements StateMethods
{
	private Player player;
	private LevelManager levelManger;
	private int left = 0, right = 2;

	public Playing(Game game) 
	{
		super(game);
		initClasses();
	}

	private void initClasses() 
	{
		levelManger = new LevelManager(game);
		player = new Player(200, 200,(int) (64 * Game.SCALE), (int) (40 * Game.SCALE));
		player.loadLvlData(levelManger.getCurrentLevel().getLvlData());
	}
	
	public void windowFocusLost() 
	{
		player.resetDirBooleans();
	}

	public Player getPlayer()
	{
		return player;
	}

	@Override
	public void update() 
	{
		levelManger.update();
		player.update();
	}

	@Override
	public void draw(Graphics g) 
	{
		levelManger.draw(g);
		player.render(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON1)
			player.setAttacking(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_W:
			player.setJump(true);
			break;
		case KeyEvent.VK_A:
			player.setLeft(true);
			break;
		case KeyEvent.VK_D:		
			player.setRight(true);
			break;
		case KeyEvent.VK_BACK_SPACE:
			GameState.state = GameState.MENU;
			break;
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_W:
			player.setJump(false);
			break;
		case KeyEvent.VK_A:
			player.setLeft(false);
			break;
		case KeyEvent.VK_D:		
			player.setRight(false);
			break;
//		case KeyEvent.VK_SHIFT:
//			player.setDash(false);
		}
	}
}
