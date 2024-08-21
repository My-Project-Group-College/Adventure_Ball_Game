package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverlay;

public class Playing extends State implements StateMethods
{
	
	private Player player;
	private LevelManager levelManger;
	private PauseOverlay pauseOverlay;
	private boolean paused = false;

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
		pauseOverlay = new PauseOverlay(this);
	}
	
	public void windowFocusLost() 
	{
		player.resetDirBooleans();
	}

	public Player getPlayer()
	{
		return player;
	}

	public void unpauseGame()
	{
		paused = false;
	}
	
	@Override
	public void update() 
	{
		if(!paused)
		{
			levelManger.update();
			player.update();			
		}
		else
			pauseOverlay.update();
	}

	@Override
	public void draw(Graphics g) 
	{
		levelManger.draw(g);
		player.render(g);
		if(paused)
			pauseOverlay.draw(g);
	}

	public void mouseDragged(MouseEvent e)
	{
		if(paused)
			pauseOverlay.mouseDragged(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON1)
			player.setAttacking(true);
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		if(paused)
			pauseOverlay.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if(paused)
			pauseOverlay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		if(paused)
			pauseOverlay.mouseMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			player.setJump(true);
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			player.setLeft(true);
			break;
		case KeyEvent.VK_D:		
		case KeyEvent.VK_RIGHT:
			player.setRight(true);
			break;
		case KeyEvent.VK_ESCAPE:
			paused = !paused;
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
		case KeyEvent.VK_UP:
			player.setJump(false);
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			player.setLeft(false);
			break;
		case KeyEvent.VK_D:		
		case KeyEvent.VK_RIGHT:
			player.setRight(false);
			break;
//		case KeyEvent.VK_SHIFT:
//			player.setDash(false);
		}
	}
}
