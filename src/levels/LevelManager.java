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

	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		levels = new ArrayList<Level>();
		loadBackgrounds();
		buildAllLevels();
	}

	private void loadBackgrounds() {
		bg = new BufferedImage[2];
		bg[0] = LoadSave.GetSpriteAtlas(LoadSave.LVL1_BACKGROUND);
		bg[1] = LoadSave.GetSpriteAtlas(LoadSave.LVL2_BACKGROUND);
	}

	public void loadNextLevel() {
		lvlIndex++;
		if (lvlIndex >= levels.size()) {
			// Game Completed Here
			lvlIndex = 0;
			System.out.println("Game Completed");
			GameState.state = GameState.MENU;
		}
		Level newLevel = levels.get(lvlIndex);
		game.getPlaying().getEnemyManager().loadEnemies(newLevel);
		game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
		game.getPlaying().setMaxLvlOffsets(newLevel.getXLvlOffset(), newLevel.getYLvlOffset());
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

	public Level getCurrentLevel() {
		return levels.get(lvlIndex);
	}

	public int getAmountOfLevels() {
		return levels.size();
	}
}