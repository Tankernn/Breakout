package eu.tankernn.breakout;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.entities.Entity2D;
import eu.tankernn.gameEngine.loader.textures.Texture;

public class Ball extends Entity2D {

	private static final float SPEED = 0.03f;

	private Pad pad;
	private List<Block> blocks;
	private Block lastHit;
	private boolean locked;
	private int unlockKey;

	public Ball(Texture texture, Pad p1, List<Block> blocks, int unlockKey) {
		super(texture, new Vector2f(0, 0), new Vector2f(0.01f, 0.01f));
		this.pad = p1;
		this.blocks = blocks;
		locked = true;
		this.unlockKey = unlockKey;
	}

	@Override
	public void update() {
		if (locked) {
			this.position = Vector2f.add(pad.getPosition(), new Vector2f(0, this.scale.y), null);
			if (Keyboard.isKeyDown(unlockKey))
				this.locked = false;
			return;
		}

		if (position.y + scale.y > 1) {
			velocity.y *= -1;
			lastHit = null;
			position.y = Math.max(position.y, -1 + scale.y);
			position.y = Math.min(position.y, 1 - scale.y);
		} else if (position.y - scale.y < -1) {
			this.alive = false;
		}

		if (position.x + scale.x > 1 || position.x - scale.x < -1) {
			velocity.x *= -1;
			lastHit = null;
			position.x = Math.max(position.x, -1 + scale.x);
			position.x = Math.min(position.x, 1 - scale.x);
		}

		if (this.collides(pad)) {
			lastHit = null;
			Vector2f padPos = pad.getPosition();
			Vector2f delta = Vector2f.sub(position, padPos, null);
			double angle = Math.atan(delta.x / Math.abs(delta.y));
			this.setAngle(angle);
		}

		for (Block b : blocks) {
			if (this.collides(b) && !b.equals(lastHit)) {

				// Under and over
				float w = (this.scale.x + b.getScale().x);
				float h = (this.scale.y + b.getScale().y);
				float dx = this.position.x - b.getPosition().x;
				float dy = this.position.y - b.getPosition().y;

				if (Math.abs(dx) <= w && Math.abs(dy) <= h && !b.equals(lastHit)) {
					/* collision! */
					this.lastHit = b;
					b.hit();
					float wy = w * dy;
					float hx = h * dx;

					if (wy > hx)
						if (wy > -hx) {
							// Top
							velocity.y *= -1;
							position.y = b.getPosition().y + h;
						} else {
							// Left
							velocity.x *= -1;
							position.x = b.getPosition().x - w;
						}
					else if (wy > -hx) {
						// Right
						velocity.x *= -1;
						position.x = b.getPosition().x + w;
					} else {
						// Bottom
						velocity.y *= -1;
						position.y = b.getPosition().y - h;
					}
				}
			}
		}

		super.update();
	}

	private void setAngle(double angle) {
		this.setVelocity(new Vector2f((float) (SPEED * Math.sin(angle)), (float) (SPEED * Math.cos(angle))));
	}
}
