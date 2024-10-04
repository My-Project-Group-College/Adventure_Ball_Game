package object;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import static utilities.Constants.SPIKE_DAMAGE;

import utilities.LoadSave;

public class ObjectManager {

	private Playing playing;
	private BufferedImage coinImg;
	private BufferedImage spikeImg;
	private ArrayList<Coin> coins;
	private ArrayList<Spike> spikes;

	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadImgs();
	}

	public void checkSpikeTouched(Player player) {
		for (Spike s : spikes)
			if (s.getHitbox().intersects(player.getHitbox()))
				player.changeHealth(-SPIKE_DAMAGE);

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
		spikes = newLevel.getSpikes();
	}

	private void loadImgs() {
		BufferedImage tempCoin = LoadSave.GetSpriteAtlas(LoadSave.COIN_IMG);
		coinImg = tempCoin.getSubimage(22, 7, 84, 84);// startX, startY, coinWidth, coinHeight

		spikeImg = LoadSave.GetSpriteAtlas(LoadSave.SPIKES_IMG);
	}

	public void update() {
		for (Coin c : coins)
			if (c.isActive())
				c.updateHover();
	}

	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawCoins(g, xLvlOffset, yLvlOffset);
		drawSpikes(g, xLvlOffset, yLvlOffset);
	}

	private void drawSpikes(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Spike s : spikes) {
			g.drawImage(spikeImg, (int) (s.getHitbox().x - s.getXOffset() - xLvlOffset),
					(int) (s.getHitbox().y - s.getYOffset() - yLvlOffset), Game.TILES_SIZE, Game.TILES_SIZE, null);
			if (Game.getHitboxStatus())
				s.drawHitbox(g, xLvlOffset, yLvlOffset);
		}
	}

	private void drawCoins(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Coin c : coins)
			if (c.isActive()) {
				g.drawImage(coinImg, (int) (c.hitbox.x + c.getXOffset() - xLvlOffset),
						(int) (c.hitbox.y + c.getYOffset() - yLvlOffset), (int) (Game.TILES_SIZE * 0.725),
						(int) (Game.TILES_SIZE * 0.725), null);
				if (Game.getHitboxStatus())
					c.drawHitbox(g, xLvlOffset, yLvlOffset);
			}
	}

	public void resetAllObjects() {
		loadObjects(playing.getLevelManager().getCurrentLevel());

		for (Coin c : coins)
			c.reset();

	}

	public void gameCompleted() {
		loadObjects(playing.getLevelManager().getCurrentLevel());
		resetAllObjects();
	}

}
