package object;

import main.Game;

public class Coin extends GameObject {

	public Coin(int x, int y) {
		super(x, y);
		xOffset = (int) (10 * 0.725 * Game.SCALE);
		yOffset = (int) (10 * 0.725 * Game.SCALE);
		initHitbox((int) (52 * 0.725), (int) (52 * 0.725));
	}

}
