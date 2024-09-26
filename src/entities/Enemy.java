package entities;

import static utilities.Constants.EnemyConstants.*;

public abstract class Enemy extends Entity {

	private int animIndex, enemyType, enemyState = 1;
	private int aniTick, aniSpeed = 25;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitbox(x, y, width, height);
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			animIndex++;
			if (animIndex >= GetSpriteAmount(enemyType, enemyState))
				animIndex = 0;
		}
	}

	public void update() {
		updateAnimationTick();
	}

	public int getAnimIndex() {
		return animIndex;
	}

	public int getEnemyState() {
		return enemyState;
	}
}
