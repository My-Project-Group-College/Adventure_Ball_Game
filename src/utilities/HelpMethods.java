package utilities;

import main.Game;

public class HelpMethods 
{
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData)
	{
		if(!IsSolid(x, y, lvlData))
			if(!IsSolid(x + width, y + height, lvlData))
				if(!IsSolid(x + width, y, lvlData))
					if(!IsSolid(x, y + height, lvlData))
						return true;
		return false;
	}
	
	private static boolean IsSolid(float x, float y,int[][] lvlData)
	{
		if(x < 0 || x >= Game.GAME_WIDTH)
			return true;
		if(y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		
		int value = lvlData[(int)yIndex][(int)xIndex];
		
		if(value >= 9 || value < 0 || (value != 7 && value != 8))
			return true;
		return false;
	}
}
