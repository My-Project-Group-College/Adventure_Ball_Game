package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import main.Game;
import utilities.Constants;
import utilities.LoadSave;
import static utilities.Constants.EnemyConstants.*;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] babluArr;
	private ArrayList<Bablu> bablus = new ArrayList<Bablu>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		LoadEnemyImages();
	}

	public void loadEnemies(Level level) {
		bablus = level.getBablus();
		System.out.println("Number Of Bablu: " + bablus.size());

	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for (Bablu b : bablus)
			if (b.isActive()) {
				b.update(lvlData, player);
				isAnyActive = true;
			}
		if (!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawBablu(g, xLvlOffset, yLvlOffset);
	}

	private void drawBablu(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Bablu b : bablus)
			if (b.isActive()) {
				g.drawImage(babluArr[b.getState()][b.getAnimIndex()],
						(int) b.getHitbox().x - xLvlOffset - BABLU_DRAWOFFSET_X,
						(int) b.getHitbox().y - yLvlOffset - BABLU_DRAWOFFSET_Y, BABLU_WIDTH, BABLU_HEIGHT, null);
				if (Game.getHitboxStatus()) {
					b.drawHitbox(g, xLvlOffset, yLvlOffset);
					b.drawAttackBox(g, xLvlOffset, yLvlOffset);
				}
			}
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox, int damage) {
		for (Bablu b : bablus)
			if (b.isActive())
				if (attackBox.intersects(b.getHitbox())) {
					b.hurt(damage);
					return;
				}
	}

	private void LoadEnemyImages() {
		babluArr = new BufferedImage[5][8];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BABLU_SPRITE);
		for (int j = 0; j < babluArr.length - 1; j++)
			for (int i = 0; i < babluArr[j].length; i++)
				babluArr[j][i] = temp.getSubimage(i * BABLU_WIDTH_DEFAULT, j * BABLU_HEIGHT_DEFAULT,
						BABLU_WIDTH_DEFAULT, BABLU_HEIGHT_DEFAULT);

		for (int i = 0; i < babluArr[RUNNING_REVERSE].length; i++)
			babluArr[RUNNING_REVERSE][i] = babluArr[RUNNING][GetSpriteAmount(BABLU, RUNNING) - 1 - i];
	}

	public void resetAllEnemies() {
		for (Bablu b : bablus)
			b.resetEnemy();

	}

}
