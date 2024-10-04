package object;

import main.Game;

public class Spike extends GameObject {

	public Spike(int x, int y) {
		super(x, y);

		initHitbox(32, 32);
		xOffset = 0;
		yOffset = (int) (Game.SCALE * 4);// 8

		hitbox.y += yOffset;
	}

}
