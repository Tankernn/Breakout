package eu.tankernn.breakout;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.entities.Entity2D;
import eu.tankernn.gameEngine.loader.textures.Texture;

public class Pad extends Entity2D {

	private static final float SPEED = 0.05f;

	private int leftKey, rightKey;

	public Pad(Texture texture, Vector2f position, int leftKey, int rightKey) {
		super(texture, position, new Vector2f(0.3f, 0.02f));
		this.leftKey = leftKey;
		this.rightKey = rightKey;
	}

	@Override
	public void update() {
		if (Keyboard.isKeyDown(leftKey) && position.x - scale.x > -1) {
			this.setVelocity(new Vector2f(-SPEED, 0));
		} else if (Keyboard.isKeyDown(rightKey) && position.x + scale.x < 1) {
			this.setVelocity(new Vector2f(SPEED, 0));
		} else {
			this.setVelocity(new Vector2f(0, 0));
		}

		super.update();
	}
}
