package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import utilities.LoadSave;
import static utilities.Constants.EnemyConstants.*;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] babluArr;
	private ArrayList<Bablu> bablus = new ArrayList<Bablu>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		LoadEnemyImages();
		addEnemies();
	}

	private void addEnemies() {
		bablus = LoadSave.GetBablu();
		System.out.println("Number Of Bablu: " + bablus.size());

	}

	public void update() {
		for (Bablu c : bablus)
			c.update();
	}

	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawBablu(g, xLvlOffset, yLvlOffset);
		for (Bablu c : bablus)
			c.drawHitbox(g);
	}

	private void drawBablu(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Bablu c : bablus)
			g.drawImage(babluArr[c.getEnemyState()][c.getAnimIndex()], (int) c.getHitbox().x - xLvlOffset,
					(int) c.getHitbox().y - yLvlOffset, BABLU_WIDTH, BABLU_HEIGHT, null);
	}

	private void LoadEnemyImages() {
		babluArr = new BufferedImage[4][8];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BABLU_SPRITE);
		for (int j = 0; j < babluArr.length; j++)
			for (int i = 0; i < babluArr[j].length; i++)
				babluArr[j][i] = temp.getSubimage(i * BABLU_WIDTH_DEFAULT, j * BABLU_HEIGHT_DEFAULT,
						BABLU_WIDTH_DEFAULT, BABLU_HEIGHT_DEFAULT);
	}

}
