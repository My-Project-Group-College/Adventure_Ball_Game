package entities;

import static utilities.Constants.EnemyConstants.*;
import main.Game;

public class Bablu extends Enemy {

	public Bablu(float x, float y) {
		super(x, y, BABLU_WIDTH, BABLU_HEIGHT, BABLU);
		initHitbox(x, y, (int) (30 * Game.SCALE * 0.75), (int) (33 * Game.SCALE * 0.75));
	}

}
