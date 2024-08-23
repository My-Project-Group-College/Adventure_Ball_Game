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

	public void draw(Graphics g, int xLvlOffset, int yLvlOffset)
	{
		for(int j = 0; j < levelOne.getLvlData().length; j++)
			for(int i = 0; i < levelOne.getLvlData()[0].length; i++)
			{
				int index = levelOne.getSpriteIndex(i, j);
				g.drawImage(levelSprite[index], i * Game.TILES_SIZE - xLvlOffset, j * Game.TILES_SIZE - yLvlOffset,
							Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
				
	}
	public void update()
	{
		
	}
	public Level getCurrentLevel()
	{
		return levelOne;
	}
}