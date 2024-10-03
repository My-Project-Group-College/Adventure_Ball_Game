package object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import main.Game;
import utilities.LoadSave;

public class ObjectManager {

	private Playing playing;
	private BufferedImage coinImg;
	private ArrayList<Coin> coins;

	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadImgs();
	}

	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for (Coin c : coins)
			if (c.isActive()) {
				if (hitbox.intersects(c.getHitbox())) {
					c.setActive(false);
					applyCoinToPlayer();
				}
			}
	}

	public void applyCoinToPlayer() {
		playing.getLevelManager().getCurrentLevel().addCoinCollected();
	}

	public void loadObjects(Level newLevel) {
		coins = newLevel.getCoins();
	}

	private void loadImgs() {
		BufferedImage tempCoin = LoadSave.GetSpriteAtlas(LoadSave.COIN_IMG);
		coinImg = tempCoin.getSubimage(22, 7, 84, 84);// startX, startY, coinWidth, coinHeight
	}

	public void update() {
		for (Coin c : coins)
			if (c.isActive())
				c.updateHover();
	}

	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawCoins(g, xLvlOffset, yLvlOffset);
	}

	private void drawCoins(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Coin c : coins)
			if (c.isActive()) {
				g.drawImage(coinImg, (int) (c.hitbox.x + c.getXOffset() - xLvlOffset),
						(int) (c.hitbox.y + c.getYOffset() - yLvlOffset), (int) (Game.TILES_SIZE * 0.725),
						(int) (Game.TILES_SIZE * 0.725), null);
				c.drawHitbox(g, xLvlOffset, yLvlOffset);
			}
	}

	public void resetAllObjects() {
		for (Coin c : coins)
			c.reset();

	}

}
