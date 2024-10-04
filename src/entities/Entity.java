package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.BoxLayout;

import main.Game;

public abstract class Entity {
	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;
	protected int animTick, animIndex;
	protected int state;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHealth;
	protected int currHealth;
	protected Rectangle2D.Float attackBox;
	protected float walkSpeed = 1f * Game.SCALE;

	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	protected void drawHitbox(Graphics g, int xLvlOffset, int yLvlOffset) {
		// Temporary For Debugging HitBox
		g.setColor(Color.RED);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y - yLvlOffset, (int) hitbox.width, (int) hitbox.height);
	}

	protected void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.setColor(Color.RED);
		g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y - yLvlOffset, (int) attackBox.width,
				(int) attackBox.height);

	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (Game.SCALE * height));
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public int getState() {
		return this.state;
	}

	public int getAnimIndex() {
		if (animIndex >= 8)
			animIndex = 8;
		return animIndex;
	}

}
