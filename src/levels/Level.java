package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Bablu;
import main.Game;
import static utilities.HelpMethods.*;

public class Level {
	private BufferedImage img;
	private int[][] lvlData;
	private ArrayList<Bablu> bablus;
	private int lvlTilesWide;
	private int lvlTilesTall;
	private int maxTilesOffsetX;
	private int maxTilesOffsetY;
	private int maxLvlOffsetX;
	private int maxLvlOffsetY;
	private Point playerSpawn;

	public Level(BufferedImage img) {
		this.img = img;
		createLvlData();
		createEnimies();
		calcLvlOffsets();
		calcPlayerSpawn();
	}

	private void calcPlayerSpawn() {
		playerSpawn = GetPlayerSpawn(img);
	}

	private void calcLvlOffsets() {
		lvlTilesWide = img.getWidth();
		lvlTilesTall = img.getHeight();
		maxTilesOffsetX = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxTilesOffsetY = lvlTilesTall - Game.TILES_IN_HEIGHT;
		maxLvlOffsetX = maxTilesOffsetX * Game.TILES_SIZE;
		maxLvlOffsetY = maxTilesOffsetY * Game.TILES_SIZE;
	}

	private void createEnimies() {
		bablus = GetBablu(img);
	}

	private void createLvlData() {
		lvlData = GetLevelData(img);
	}

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public int[][] getLvlData() {
		return lvlData;
	}

	public int getXLvlOffset() {
		return maxLvlOffsetX;
	}

	public int getYLvlOffset() {
		return maxLvlOffsetY;
	}

	public ArrayList<Bablu> getBablus() {
		return bablus;
	}

	public Point getPlayerSpawn() {
		return playerSpawn;
	}

}
