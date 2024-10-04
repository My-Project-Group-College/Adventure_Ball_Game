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
	private int totalTime, timePassed;
	private int coinsCollected;
	private int highScore = 0;

	public Level(BufferedImage img) {
		this.img = img;
		createLvlData();
		createEnimies();
		createCoins();
		createSpikes();
		calcLvlOffsets();
		calcPlayerSpawn();
		totalTime = 120000;
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

	public void reset() {
		timePassed = 0;
		coinsCollected = 0;
		calcPlayerSpawn();
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

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}

}
