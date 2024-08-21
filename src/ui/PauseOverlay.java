package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.GameState;
import gamestates.Playing;
import main.Game;
import utilities.LoadSave;
import static utilities.Constants.UI.PauseButtons.*;
import static utilities.Constants.UI.URMButtons.*;
import static utilities.Constants.UI.VolumeButtons.*;


public class PauseOverlay 
{
	private Playing playing; 
	private BufferedImage backgroundImg;
	private int bgX, bgY, bgWidth, bgHeight;
	private SoundButtons musicButton, sfxButton;
	private URMButton menuB, replayB, unpauseB;
	private VolumeButton volumeButton;
	
	public PauseOverlay(Playing playing) 
	{
		this.playing = playing;
		loadBackground();
		createSoundButtons();
		createURMButtons();
		createVolumeButton();
	}
	
	private void createVolumeButton() 
	{
		int vX = (int) (309 * Game.SCALE);
		int vY = (int) (278 * Game.SCALE);
		volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_B_HEIGHT);
	}

	private void createURMButtons() 
	{
		int menuX = (int) (313 * Game.SCALE);
		int replayX = (int) (387 * Game.SCALE);
		int unpauseX = (int) (462 * Game.SCALE);
		int urmY = (int) (325 * Game.SCALE);
		
		menuB = new URMButton(menuX, urmY, URM_B_SIZE, URM_B_SIZE, 2);
		replayB = new URMButton(replayX, urmY, URM_B_SIZE, URM_B_SIZE, 1);
		unpauseB = new URMButton(unpauseX, urmY, URM_B_SIZE, URM_B_SIZE, 0);
	}

	private void createSoundButtons() 
	{
		int soundX = (int) (450 * Game.SCALE);
		int musicY = (int) (140 * Game.SCALE);
		int sfxY = (int) (186 * Game.SCALE);
		musicButton = new SoundButtons(soundX, musicY, SOUND_B_SIZE, SOUND_B_SIZE);
		sfxButton = new SoundButtons(soundX, sfxY, SOUND_B_SIZE, SOUND_B_SIZE);
	}

	private void loadBackground() 
	{
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		bgHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
		bgY = (int)(25 * Game.SCALE);
		
	}

	public void draw(Graphics g)
	{
		//Background Of Pause Screen
		g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);
		
		//Sound Buttons
		musicButton.draw(g);
		sfxButton.draw(g);
		
		//URM buttons
		menuB.draw(g);
		replayB.draw(g);
		unpauseB.draw(g);
		
		//Volume Slider
		volumeButton.draw(g);
	}
	
	public void update()
	{
		musicButton.update();
		sfxButton.update();
		menuB.update();
		replayB.update();
		unpauseB.update();
		volumeButton.update();
	}
	
	public void mouseDragged(MouseEvent e) 
	{
		if(volumeButton.isMousePressed())
			volumeButton.changeX(e.getX());
	}
	
	public void mousePressed(MouseEvent e) 
	{
		if(isIn(e, musicButton))
			musicButton.setMousePressed(true);
		else if(isIn(e, sfxButton))
			sfxButton.setMousePressed(true);
		else if(isIn(e, menuB))
			menuB.setMousePressed(true);
		else if(isIn(e, replayB))
			replayB.setMousePressed(true);
		else if(isIn(e, unpauseB))
			unpauseB.setMousePressed(true);
		else if(isIn(e, volumeButton))
			volumeButton.setMousePressed(true);
	}

	public void mouseReleased(MouseEvent e) 
	{
		if(isIn(e, musicButton))
		{
			if(musicButton.isMousePressed())
				musicButton.setMuted(!musicButton.isMuted());
		}
		else if(isIn(e, sfxButton))
		{
			if(sfxButton.isMousePressed())
				sfxButton.setMuted(!sfxButton.isMuted());
		}
		else if(isIn(e, menuB))
		{
			if(menuB.isMousePressed()) 
			{
				GameState.state = GameState.MENU;
				playing.unpauseGame();
			}
		}
		else if(isIn(e, replayB))
		{
			if(replayB.isMousePressed())
				System.out.println("Replay Level");
		}
		else if(isIn(e, unpauseB))
		{
			if(unpauseB.isMousePressed())
				playing.unpauseGame();
		}
		
		musicButton.resetBools();
		sfxButton.resetBools();
		menuB.resetBools();
		replayB.resetBools();
		unpauseB.resetBools();
		volumeButton.resetBools();
	}

	public void mouseMoved(MouseEvent e) 
	{
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		menuB.setMouseOver(false);
		replayB.setMouseOver(false);
		unpauseB.setMouseOver(false);
		volumeButton.setMouseOver(false);
		
		if(isIn(e, musicButton))
			musicButton.setMouseOver(true);
		else if(isIn(e, sfxButton))
			sfxButton.setMouseOver(true);
		else if(isIn(e, menuB))
			menuB.setMouseOver(true);
		else if(isIn(e, replayB))
			replayB.setMouseOver(true);
		else if(isIn(e, unpauseB))
			unpauseB.setMouseOver(true);
		else if(isIn(e, volumeButton))
			volumeButton.setMouseOver(true);
	}
	
	private boolean isIn(MouseEvent e, PauseButton b) 
	{
		return b.getBounds().contains(e.getX(), e.getY());
	}
}
