package object;

import main.Game;

public class Coin extends GameObject {

	private float hoverOffset;
	private int maxHoverOffset, hoverDir = 1;

	public Coin(int x, int y) {
		super(x, y);
		xOffset = (int) (10 * 0.725 * Game.SCALE);
		yOffset = (int) (10 * 0.725 * Game.SCALE);
		initHitbox((int) (52 * 0.725), (int) (52 * 0.725));

		maxHoverOffset = (int) (10 * Game.SCALE);
	}

	public void updateHover() {
		hoverOffset += (0.075f * Game.SCALE * hoverDir);
		if (hoverOffset >= maxHoverOffset)
			hoverDir = -1;
		else if (hoverOffset < 0)
			hoverDir = 1;

		hitbox.y = y + hoverOffset;
	}
}
