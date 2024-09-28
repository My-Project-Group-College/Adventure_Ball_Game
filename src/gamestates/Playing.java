package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverlay;
import utilities.LoadSave;

public class Playing extends State implements StateMethods {

	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private PauseOverlay pauseOverlay;
	private boolean paused = false;

	private int xLvlOffset, yLvlOffset;
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
	private int upBorder = (int) (0.15 * Game.GAME_HEIGHT);
	private int downBorder = (int) (0.875 * Game.GAME_HEIGHT);
	private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
	private int lvlTilesTall = LoadSave.GetLevelData().length;
	private int maxTilesOffsetX = lvlTilesWide - Game.TILES_IN_WIDTH;
	private int maxTilesOffsetY = lvlTilesTall - Game.TILES_IN_HEIGHT;
	private int maxLvlOffsetX = maxTilesOffsetX * Game.TILES_SIZE;
	private int maxLvlOffsetY = maxTilesOffsetY * Game.TILES_SIZE;

	public Playing(Game game) {
		super(game);
		initClasses();
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE));
		player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
		pauseOverlay = new PauseOverlay(this);
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}

	public void unpauseGame() {
		paused = false;
	}

	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int playerY = (int) player.getHitbox().y;
		int diffX = playerX - xLvlOffset;
		int diffY = playerY - yLvlOffset;

		if (diffX > rightBorder)
			xLvlOffset += diffX - rightBorder;
		if (diffX < leftBorder)
			xLvlOffset += diffX - leftBorder;
		if (diffY > downBorder)
			yLvlOffset += diffY - downBorder;
		if (diffY < upBorder)
			yLvlOffset += diffY - upBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;
		if (yLvlOffset > maxLvlOffsetY)
			yLvlOffset = maxLvlOffsetY;
		else if (yLvlOffset < 0)
			yLvlOffset = 0;

	}

	@Override
	public void update() {
		if (!paused) {
			levelManager.update();
			enemyManager.update(levelManager.getCurrentLevel().getLvlData());
			player.update();
			checkCloseToBorder();
		} else
			pauseOverlay.update();
	}

	@Override
	public void draw(Graphics g) {
		levelManager.draw(g, xLvlOffset, yLvlOffset);
		enemyManager.draw(g, xLvlOffset, yLvlOffset);
		player.render(g, xLvlOffset, yLvlOffset);
		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (paused)
			pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			player.setAttacking(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (paused)
			pauseOverlay.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (paused)
			pauseOverlay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (paused)
			pauseOverlay.mouseMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
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
		case KeyEvent.VK_SHIFT:
			player.setDash(true);
			break;
		case KeyEvent.VK_BACK_SPACE:
			player.resetDirBooleans();
			GameState.state = GameState.MENU;
			break;
////		case KeyEvent.VK_ESCAPE:
////			paused = !paused;
////			break;

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
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
		case KeyEvent.VK_SHIFT:
			player.setDash(false);
		}
	}
}
