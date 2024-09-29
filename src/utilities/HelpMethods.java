package utilities;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import main.Game;

public class HelpMethods {
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if (!IsSolid(x, y, lvlData) && !IsSolid(x + width, y + height, lvlData) && !IsSolid(x + width, y, lvlData)
				&& !IsSolid(x, y + height, lvlData))
			return true;
		return false;
	}

	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		Dimension lvlDimension = new Dimension(lvlData[0].length * Game.TILES_SIZE, lvlData.length * Game.TILES_SIZE);
		if (x < 0 || x >= lvlDimension.width)
			return true;
		if (y < 0 || y >= lvlDimension.height)
			return true;

		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		int value = lvlData[(int) yIndex][(int) xIndex];

		if (value >= 9 || value < 0 || (value != 7 && value != 8))
			return true;
		return false;
	}

	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) { // Right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else // Left
			return currentTile * Game.TILES_SIZE;
	}

	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) { // Falling - touching floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else // Jumping
			return currentTile * Game.TILES_SIZE;

	}

	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		// Check Pixel below bottomleft and bottomright
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)
				&& !IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
			return false;
		return true;
	}

	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}
	public static boolean IsSightClear(int[][] lvlData,Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox , int yTile) {
		int firstXTile= (int)(firstHitbox.x / Game.TILES_SIZE);
		int secondXTile= (int)(secondHitbox.x / Game.TILES_SIZE);
		
		
		return false;
	}
}
