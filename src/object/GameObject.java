package object;

import static utilities.Constants.ANIM_SPEED;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class GameObject {
	protected int x, y;
	protected Rectangle2D.Float hitbox;
	protected boolean active = true;
	protected int xOffset, yOffset;

	public GameObject(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void drawHitbox(Graphics g, int xLvlOffset, int yLvlOffset) {
		// Temporary For Debugging HitBox
		g.setColor(Color.RED);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y - yLvlOffset, (int) hitbox.width, (int) hitbox.height);
	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x - xOffset, y - yOffset, (int) (width * Game.SCALE),
				(int) (height * Game.SCALE));
	}

	public void reset() {
		active = true;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getXOffset() {
		return xOffset;
	}

	public int getYOffset() {
		return yOffset;
	}

}
