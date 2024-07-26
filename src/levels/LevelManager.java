package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import utilities.LoadSave;

public class LevelManager 
{
	private Game game;
	private BufferedImage[] levelSprite;
	private Level levelOne;
	
	public LevelManager(Game game) 
	{
		this.game = game;
		importOutsideSprites();
		levelOne = new Level(LoadSave.GetLevelData());
	}
	
	private void importOutsideSprites() 
	{
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[9];//3 wide 3 wide , 3 * 3 = 9
		for (int j = 0; j < 3; j++)
			for (int i = 0; i < 3; i++) {
				int index = j * 3 + i;
				levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
			}
	}

	public void draw(Graphics g)
	{
		for(int j = 0; j < Game.TILES_IN_HEIGHT; j++)
			for(int i = 0; i < Game.TILES_IN_WIDTH; i++)
			{
				int index = levelOne.getSpriteIndex(i, j);
				g.drawImage(levelSprite[index], i * Game.TILES_SIZE, j * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
				
	}
	public void update()
	{
		
	}
}