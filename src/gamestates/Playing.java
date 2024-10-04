package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.Timer;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import object.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import static utilities.Constants.PlayerConstants.DASH_READY;

public class Playing extends State implements StateMethods {

	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private boolean paused = false;

	private int xLvlOffset, yLvlOffset;
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
	private int upBorder = (int) (0.15 * Game.GAME_HEIGHT);
	private int downBorder = (int) (0.875 * Game.GAME_HEIGHT);
	private int maxLvlOffsetX;
	private int maxLvlOffsetY;
	private Timer timer;
	private ActionListener changesPerTick;

	private boolean gameOver;
	private boolean levelCompleted;
	private int totalCoinCollected;
	private int totalTimeUsed;

	public Playing(Game game) {
		super(game);
		initClasses();
		calcLvlOffset();
		loadStartLevel();
	}

	public void loadNextLevel() {
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		resetAll();
	}

	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getXLvlOffset();
		maxLvlOffsetY = levelManager.getCurrentLevel().getYLvlOffset();
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

		pauseOverlay = new PauseOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);

		changesPerTick = new ActionListener() {
			private float count = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!paused && !gameOver && !levelCompleted && GameState.state == GameState.PLAYING)
					levelManager.getCurrentLevel().timeTick();
				if (player.dashCooldown < DASH_READY)
					player.dashCooldown += 0.015f;
//				System.out.println(player.dashCooldown);
				if (player.dashCooldown >= DASH_READY) {
					count += 0.01;
					player.dashCooldown = DASH_READY;
				}
				if (count >= 1.5 && player.dashCooldown >= DASH_READY) {
					player.resetDash();
					count = 0;

				}

			}
		};
		timer = new Timer(10, changesPerTick);
		timer.start();

	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}

	public void setLevelCompleted(boolean levelCompleted) {
		this.levelCompleted = levelCompleted;
		if (levelCompleted)
			game.getAudioPlayer().lvlCompleted();
	}

	public void setMaxLvlOffsets(int xLvlOffset, int yLvlOffset) {
		this.maxLvlOffsetX = xLvlOffset;
		this.maxLvlOffsetY = yLvlOffset;
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
		if (paused)
			pauseOverlay.update();
		else if (levelCompleted)
			levelCompletedOverlay.update();
		else if (gameOver)
			gameOverOverlay.update();
		else if (!gameOver) {
			levelManager.update();
			objectManager.update();
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLvlData(), player);
			checkCloseToBorder();
		}
	}

	@Override
	public void draw(Graphics g) {
		levelManager.draw(g, xLvlOffset, yLvlOffset);
		enemyManager.draw(g, xLvlOffset, yLvlOffset);
		objectManager.draw(g, xLvlOffset, yLvlOffset);
		player.render(g, xLvlOffset, yLvlOffset);
		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver)
			gameOverOverlay.draw(g);
		else if (levelCompleted)
			levelCompletedOverlay.draw(g);
	}

	public void resetAll() {
		gameOver = false;
		paused = false;
		levelCompleted = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
		levelManager.getCurrentLevel().reset();
	}

	public void gameCompleted() {
		totalCoinCollected = 0;
		totalTimeUsed = 0;
		gameOver = false;
		paused = false;
		levelCompleted = false;
		levelManager.gameCompleted();
		enemyManager.gameCompleted();
		objectManager.gameCompleted();
		player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		player.resetAll();
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox, int damage) {
		enemyManager.checkEnemyHit(attackBox, damage);
	}

	public void checkCoinTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}

	public void checkSpikeTouched(Player p) {
		objectManager.checkSpikeTouched(p);
	}

	public void mouseDragged(MouseEvent e) {
		if (!gameOver && !levelCompleted)
			if (paused)
				pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver && !levelCompleted)
			if (e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mousePressed(e);
			else if (levelCompleted)
				levelCompletedOverlay.mousePressed(e);
		} else
			gameOverOverlay.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseReleased(e);
			else if (levelCompleted)
				levelCompletedOverlay.mouseReleased(e);
		} else
			gameOverOverlay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseMoved(e);
			else if (levelCompleted)
				levelCompletedOverlay.mouseMoved(e);
		} else
			gameOverOverlay.mouseMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver)
			gameOverOverlay.keyPressed(e);
		else if (!levelCompleted)
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
				setGameState(GameState.MENU);
				break;
			case KeyEvent.VK_P:
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;

			// For Testing Purposes
			case KeyEvent.VK_T:
				if (Game.TEST) {
					System.out.println("Total Time: " + totalTimeUsed);
					System.out.println("Total Coins: " + totalCoinCollected);
					System.out.println("Current Time Used: " + levelManager.getCurrentLevel().getTimePassed());
					System.out.println("Current Coins: " + levelManager.getCurrentLevel().getCoinsCollected());
				}
				break;
			case KeyEvent.VK_G:
				if (Game.TEST) {
					levelCompleted = true;
				}
				break;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver && !levelCompleted)
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
//			if (player.dashCooldown == DASH_READY)
//				player.resetDash();
				break;

			}
	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public void addCoinsToTotal(int coins) {
		this.totalCoinCollected += coins;
	}

	public void addTimeToTotal(int time) {
		this.totalTimeUsed += time;
	}

	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public int getTotalCoinCollected() {
		return totalCoinCollected;
	}

	public int getTotalTimeUsed() {
		return totalTimeUsed;
	}

}
