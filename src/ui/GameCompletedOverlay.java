package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.GameState;
import gamestates.Playing;
import main.Game;
import utilities.LoadSave;
import static utilities.Constants.UI.URMButtons.*;

public class GameCompletedOverlay {

	private Playing playing;
	private URMButton menu;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;

	public GameCompletedOverlay(Playing playing) {
		this.playing = playing;
		initImg();
		initButtons();
	}

	private void initButtons() {
		int menuX = (int) (bgX + bgW / 2 - URM_B_SIZE / 2);// 390 * Game.SCALE
		int y = (int) (195 * Game.SCALE);
		menu = new URMButton(menuX, y, URM_B_SIZE, URM_B_SIZE, 2);
	}

	private void initImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.GAME_COMPLETED_IMG);
		bgW = (int) (img.getWidth() * Game.SCALE);
		bgH = (int) (img.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (75 * Game.SCALE);
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(img, bgX, bgY, bgW, bgH, null);
		menu.draw(g);
	}

	public void update() {
		menu.update();
	}

	private boolean isIn(URMButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		menu.setMouseOver(false);
		if (isIn(menu, e))
			menu.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(menu, e))
			if (menu.isMousePressed()) {
				// Game Completed Here
				System.out.println("Game Completed");
				System.out.println("Coins: " + playing.getGame().getPlaying().getTotalCoinCollected());
				System.out.println("Time: " + playing.getGame().getPlaying().getTotalTimeUsed() / 1000 + " Secs");
				playing.getGame().getAudioPlayer().lvlCompleted();
				playing.gameCompleted();
				playing.setGameState(GameState.MENU);
			}
		menu.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(menu, e))
			menu.setMousePressed(true);
	}

}
