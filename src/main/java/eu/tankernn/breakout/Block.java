package eu.tankernn.breakout;

import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.entities.Entity2D;
import eu.tankernn.gameEngine.loader.textures.Texture;

public class Block extends Entity2D {

	public static final int ROWS = 8, COLUMNS = 14;

	int health = 1;

	public Block(Texture texture, int gridX, int gridY) {
		super(texture, new Vector2f(0, 0), new Vector2f(1.0f / (COLUMNS), 0.5f / (ROWS)));

		this.position.x = this.scale.x * 2f * (gridX) + this.scale.x - 1;
		this.position.y = this.scale.y * 2f * (gridY) + this.scale.y;
	}
	
	public Block setHealth(int health) {
		this.health = health;
		return this;
	}

	public void hit() {
		this.alive = --health > 0;
	}

}
