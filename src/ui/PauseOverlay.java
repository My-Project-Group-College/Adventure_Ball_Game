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

public class PauseOverlay {
	private Playing playing;
	private BufferedImage backgroundImg;
	private int bgX, bgY, bgWidth, bgHeight;
	private AudioOptions audioOptions;
	private URMButton menuB, replayB, unpauseB;

	public PauseOverlay(Playing playing) {
		this.playing = playing;
		loadBackground();
		audioOptions = playing.getGame().getAudioOptions();
		createURMButtons();
	}

	private void createURMButtons() {
		int menuX = (int) (313 * Game.SCALE);
		int replayX = (int) (387 * Game.SCALE);
		int unpauseX = (int) (462 * Game.SCALE);
		int urmY = (int) (325 * Game.SCALE);

		menuB = new URMButton(menuX, urmY, URM_B_SIZE, URM_B_SIZE, 2);
		replayB = new URMButton(replayX, urmY, URM_B_SIZE, URM_B_SIZE, 1);
		unpauseB = new URMButton(unpauseX, urmY, URM_B_SIZE, URM_B_SIZE, 0);
	}

	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		bgHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
		bgY = (int) (25 * Game.SCALE);

	}

	public void draw(Graphics g) {
		// Background Of Pause Screen
		g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);

		// URM buttons
		menuB.draw(g);
		replayB.draw(g);
		unpauseB.draw(g);

		audioOptions.draw(g);
	}

	public void update() {
		menuB.update();
		replayB.update();
		unpauseB.update();
		audioOptions.update();
	}

	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuB))
			menuB.setMousePressed(true);
		else if (isIn(e, replayB))
			replayB.setMousePressed(true);
		else if (isIn(e, unpauseB))
			unpauseB.setMousePressed(true);
		else
			audioOptions.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed()) {
				GameState.state = GameState.MENU;
				playing.unpauseGame();
			}
		} else if (isIn(e, replayB)) {
			if (replayB.isMousePressed()) {
				playing.resetAll();
				playing.unpauseGame();
			}
		} else if (isIn(e, unpauseB)) {
			if (unpauseB.isMousePressed())
				playing.unpauseGame();
		}
		audioOptions.mouseReleased(e);

		menuB.resetBools();
		replayB.resetBools();
		unpauseB.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);
		replayB.setMouseOver(false);
		unpauseB.setMouseOver(false);

		if (isIn(e, menuB))
			menuB.setMouseOver(true);
		else if (isIn(e, replayB))
			replayB.setMouseOver(true);
		else if (isIn(e, unpauseB))
			unpauseB.setMouseOver(true);

		audioOptions.mouseMoved(e);
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
}
