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
import static utilities.Constants.UI.URMButtons.*;
import static utilities.Constants.HIGHSCORE;
import static utilities.Constants.NEW_HIGHSCORE;
import static utilities.Constants.SCORE;

public class LevelCompletedOverlay {

	private Playing playing;
	private URMButton menu, next;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;
	private GameCompletedOverlay gameCompletedOverlay;
	public static boolean GameCompleted = false;
	private boolean newHighScore, firstCheck;
	private int score, highScore;

	public LevelCompletedOverlay(Playing playing) {
		this.playing = playing;
		initImg();
		initButtons();
		gameCompletedOverlay = new GameCompletedOverlay(playing);
	}

	private void initButtons() {
		int menuX = (int) (330 * Game.SCALE);
		int nextX = (int) (445 * Game.SCALE);
		int y = (int) (195 * Game.SCALE);
		next = new URMButton(nextX, y, URM_B_SIZE, URM_B_SIZE, 0);
		menu = new URMButton(menuX, y, URM_B_SIZE, URM_B_SIZE, 2);
	}

	private void initImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_COMPLETED_IMG);
		bgW = (int) (img.getWidth() * Game.SCALE);
		bgH = (int) (img.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (75 * Game.SCALE);
	}

	public void draw(Graphics g) {
		if (GameCompleted) {
			gameCompletedOverlay.draw(g);
			return;
		}
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(img, bgX, bgY, bgW, bgH, null);
		next.draw(g);
		menu.draw(g);

		g.setColor(new Color(255, 255, 255, 200));
		g.setFont(LoadSave.GetMinecraft_Font().deriveFont(Font.BOLD, 50));
		if (newHighScore)
			g.drawString("New", NEW_HIGHSCORE.x, NEW_HIGHSCORE.y);
		g.drawString("HighScore: " + highScore, HIGHSCORE.x, HIGHSCORE.y);
		g.drawString("Your Score: " + score, SCORE.x, SCORE.y);
	}

	private boolean checkAndUpdateScores() {
		score = (int) HelpMethods.CalcScore(playing.getLevelManager().getCurrentLevel().getCoinsCollected(),
				playing.getLevelManager().getCurrentLevel().timeRemaining());
		highScore = playing.getLevelManager().getCurrentLevel().getHighScore();
		if (score > highScore) {
			playing.getLevelManager().getCurrentLevel().setHighScore(score);
			highScore = score;
			return true;
		}
		return false;
	}

	public void update() {
		if (GameCompleted) {
			gameCompletedOverlay.update();
			return;
		}
		if (!firstCheck) {
			newHighScore = checkAndUpdateScores();
			firstCheck = true;
		}
		next.update();
		menu.update();
	}

	private boolean isIn(URMButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		if (GameCompleted) {
			gameCompletedOverlay.mouseMoved(e);
			return;
		}
		next.setMouseOver(false);
		menu.setMouseOver(false);

		if (isIn(menu, e))
			menu.setMouseOver(true);
		else if (isIn(next, e))
			next.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (GameCompleted) {
			gameCompletedOverlay.mouseReleased(e);
			return;
		}
		if (isIn(menu, e)) {
			if (menu.isMousePressed()) {
				if (checkForGameCompleted())
					return;
				playing.resetAll();
				firstCheck = false;
				playing.setGameState(GameState.MENU);
			}
		} else if (isIn(next, e))
			if (next.isMousePressed()) {
				if (checkForGameCompleted())
					return;
				playing.loadNextLevel();
				firstCheck = false;
				playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
			}
		menu.resetBools();
		next.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (GameCompleted) {
			gameCompletedOverlay.mousePressed(e);
			return;
		}
		if (isIn(menu, e))
			menu.setMousePressed(true);
		else if (isIn(next, e))
			next.setMousePressed(true);
	}

	public boolean checkForGameCompleted() {
		if (playing.getLevelManager().getLvlIndex() >= playing.getLevelManager().getAmountOfLevels() - 1) {
			GameCompleted = true;
			return true;
		}
		return false;
	}

}
