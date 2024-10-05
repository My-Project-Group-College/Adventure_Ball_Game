package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.GameState;
import gamestates.Playing;
import main.Game;
import utilities.HelpMethods;
import utilities.LoadSave;

import static utilities.Constants.HIGHSCORE;
import static utilities.Constants.NEW_HIGHSCORE;
import static utilities.Constants.SCORE;
import static utilities.Constants.UI.URMButtons.*;

public class GameCompletedOverlay {

	private Playing playing;
	private URMButton menu;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;
	public static boolean GameCompleted = false;
	private boolean newHighScore, firstCheck;
	private int score, highScore;

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

		g.setColor(new Color(255, 255, 255, 200));
		g.setFont(LoadSave.GetMinecraft_Font().deriveFont(Font.BOLD, 50));
		if (newHighScore)
			g.drawString("New", NEW_HIGHSCORE.x, NEW_HIGHSCORE.y);
		g.drawString("HighScore: " + highScore, HIGHSCORE.x, HIGHSCORE.y);
		g.drawString("Your Score: " + score, SCORE.x, SCORE.y);
	}

	private boolean checkAndUpdateScores() {
		score = (int) HelpMethods.CalcScore(playing.getTotalCoinCollected(), playing.getTotalTimeRemaining());
		highScore = playing.getHighScore();
		if (score > highScore) {
			playing.setHighScore(score);
			highScore = score;
			return true;
		}
		return false;
	}

	public void update() {
		if (!firstCheck) {
			newHighScore = checkAndUpdateScores();
			firstCheck = true;
		}
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
				firstCheck = false;
				LevelCompletedOverlay.GameCompleted = false;
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
