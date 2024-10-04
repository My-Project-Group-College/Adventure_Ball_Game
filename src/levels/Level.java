package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Bablu;
import main.Game;
import object.Coin;
import object.Spike;
import utilities.HelpMethods;

import static utilities.HelpMethods.*;

public class Level {
	private BufferedImage img;
	private int[][] lvlData;
	private ArrayList<Bablu> bablus;
	private ArrayList<Coin> coins;
	private ArrayList<Spike> spikes;
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
		createCoins();
		createSpikes();
		calcLvlOffsets();
		calcPlayerSpawn();
//		// Testing Coins
//		coins = new ArrayList<Coin>();
//		coins.add(new Coin(32 * 6, 32 * 10));
	}

	private void createSpikes() {
		spikes = HelpMethods.GetSpikes(img);
	}

	private void createCoins() {
		coins = HelpMethods.GetCoins(img);
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

	public ArrayList<Coin> getCoins() {
		return coins;
	}

	public ArrayList<Spike> getSpikes() {
		return spikes;
	}

}
