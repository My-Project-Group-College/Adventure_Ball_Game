package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.GameState;
import main.Game;
import utilities.LoadSave;

public class LevelManager {
	private Game game;
	private BufferedImage[] levelSprite;
	private ArrayList<Level> levels;
	private int lvlIndex = 0;
	private BufferedImage[] bg;
	private int totalTime, timePassed;
	private int coinsCollected;

	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		levels = new ArrayList<Level>();
		loadBackgrounds();
		buildAllLevels();
		totalTime = 180000;
	}

	private void loadBackgrounds() {
		bg = new BufferedImage[2];
		bg[0] = LoadSave.GetSpriteAtlas(LoadSave.LVL1_BACKGROUND);
		bg[1] = LoadSave.GetSpriteAtlas(LoadSave.LVL2_BACKGROUND);
	}

	public void loadNextLevel() {
		game.getPlaying().addCoinsToTotal(coinsCollected);
		coinsCollected = 0;
		timePassed = 0;
		lvlIndex++;
		if (lvlIndex >= levels.size()) {
			// Game Completed Here
			lvlIndex = 0;
			System.out.println("Game Completed");
			System.out.println("Coins: " + game.getPlaying().getTotalCoinColected());
			System.out.println("Time: " + game.getPlaying().getTotalTimeUsed() / 1000 + " Secs");
			GameState.state = GameState.MENU;
		}
		Level newLevel = levels.get(lvlIndex);
		game.getPlaying().getEnemyManager().loadEnemies(newLevel);
		game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
		game.getPlaying().setMaxLvlOffsets(newLevel.getXLvlOffset(), newLevel.getYLvlOffset());
		game.getPlaying().getObjectManager().loadObjects(newLevel);
	}

	private void buildAllLevels() {
		BufferedImage[] allLevels = LoadSave.GetAllLevels();
		for (BufferedImage img : allLevels)
			levels.add(new Level(img));

	}

	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[9];// 3 wide 3 height , 3 * 3 = 9
		for (int j = 0; j < 3; j++)
			for (int i = 0; i < 3; i++) {
				int index = j * 3 + i;
				levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
			}
	}

	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.drawImage(bg[lvlIndex], 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		for (int j = 0; j < levels.get(lvlIndex).getLvlData().length; j++)
			for (int i = 0; i < levels.get(lvlIndex).getLvlData()[0].length; i++) {
				int index = levels.get(lvlIndex).getSpriteIndex(i, j);
				g.drawImage(levelSprite[index], i * Game.TILES_SIZE - xLvlOffset, j * Game.TILES_SIZE - yLvlOffset,
						Game.TILES_SIZE, Game.TILES_SIZE, null);
			}

	}

	public void update() {

	}

	public void resetAll() {
		timePassed = 0;
		coinsCollected = 0;
	}

	public Level getCurrentLevel() {
		return levels.get(lvlIndex);
	}

	public int getAmountOfLevels() {
		return levels.size();
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public int getTimePassed() {
		return timePassed;
	}

	public void setTimePassed(int timePassed) {
		this.timePassed = timePassed;
	}

	public void timeTick() {
		timePassed += 10;
		game.getPlaying().addTimeToTotal(10);
	}

	public int timeRemaining() {
		return totalTime - timePassed;
	}

	public void addCoinCollected() {
		this.coinsCollected++;
	}

	public int getCoinsCollected() {
		return coinsCollected;
	}

}