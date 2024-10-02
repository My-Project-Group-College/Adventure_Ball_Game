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

		coins = new ArrayList<Coin>();
		coins.add(new Coin(100, 200));
		coins.add(new Coin(150, 300));
		coins.add(new Coin(10, 20));
	}

//	public void checkObjectTouched(Rectangle2D.Float hitbox) {
//		for (Potion p : potions)
//			if (p.isActive()) {
//				if (hitbox.intersects(p.getHitbox())) {
//					p.setActive(false);
//					applyEffectToPlayer(p);
//				}
//			}
//	}
//
//	public void applyEffectToPlayer(Potion p) {
//		if (p.getObjType() == RED_POTION)
//			playing.getPlayer().changeHealth(RED_POTION_VALUE);
//		else
//			playing.getPlayer().changePower(BLUE_POTION_VALUE);
//	}

//	public void loadObjects(Level newLevel) {
//		potions = newLevel.getPotions();
//		containers = newLevel.getContainers();
//	}

	private void loadImgs() {
		BufferedImage tempCoin = LoadSave.GetSpriteAtlas(LoadSave.COIN_IMG);
		coinImg = tempCoin.getSubimage(22, 7, 84, 84);// startX, startY, coinWidth, coinHeight
	}

	public void update() {
	}

	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawCoins(g, xLvlOffset, yLvlOffset);
	}

	private void drawCoins(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Coin c : coins)
			if (c.isActive()) {
				g.drawImage(coinImg, (int) (c.x - xLvlOffset), (int) (c.y - yLvlOffset),
						(int) (Game.TILES_SIZE * 0.725), (int) (Game.TILES_SIZE * 0.725), null);
				c.drawHitbox(g, xLvlOffset, yLvlOffset);
			}
	}

	public void resetAllObjects() {
		for (Coin c : coins)
			c.reset();

	}

}
