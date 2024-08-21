package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.GameState;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener
{
	private GamePanel gamepanel;
	
	public MouseInputs(GamePanel gamePanel) 
	{
		this.gamepanel = gamePanel;
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		switch(GameState.state)
		{
		case PLAYING:
			gamepanel.getGame().getPlaying().mouseDragged(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		switch(GameState.state)
		{
		case MENU:
			gamepanel.getGame().getMenu().mouseMoved(e);
			break;
		case PLAYING:
			gamepanel.getGame().getPlaying().mouseMoved(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		switch(GameState.state)
		{
		case MENU:
			gamepanel.getGame().getMenu().mouseClicked(e);
			break;
		case PLAYING:
			gamepanel.getGame().getPlaying().mouseClicked(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		switch(GameState.state)
		{
		case MENU:
			gamepanel.getGame().getMenu().mousePressed(e);
			break;
		case PLAYING:
			gamepanel.getGame().getPlaying().mousePressed(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		switch(GameState.state)
		{
		case MENU:
			gamepanel.getGame().getMenu().mouseReleased(e);
			break;
		case PLAYING:
			gamepanel.getGame().getPlaying().mouseReleased(e);
			break;
		default:
			break;
		}
	} 

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
