package eu.tankernn.breakout;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.GameLauncher;
import eu.tankernn.gameEngine.TankernnGame;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.gui.GuiRenderer;
import eu.tankernn.gameEngine.renderEngine.gui.GuiTexture;
import eu.tankernn.gameEngine.util.InternalFile;

public class Breakout extends TankernnGame {

	public static final String GAME_NAME = "Breakout";

	private GuiRenderer renderer;

	private Texture[] colors;
	private Texture white;
	private Texture steel;

	private List<Block> blocks = new ArrayList<Block>();
	private Ball ball;
	private Pad pad;

	private int mapIndex = 1;

	public Breakout() {
		super(GAME_NAME);

		renderer = new GuiRenderer(loader);

		try {
			white = loader.loadTexture("white.png");
			steel = loader.loadTexture("steel.png");
			String[] colorNames = { "blue", "brown", "cyan", "green", "pink", "red", "yellow" };
			colors = Arrays.stream(colorNames).map(s -> {
				try {
					return loader.loadTexture(s + ".png");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			}).toArray(size -> new Texture[size]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		// Ball + pad

		pad = new Pad(white, new Vector2f(0, -0.8f), Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT);
		ball = new Ball(white, pad, blocks, Keyboard.KEY_SPACE);

		// Setup blocks
		nextMap();

	}

	public void update() {
		blocks.removeIf(b -> !b.isAlive());

		if (blocks.isEmpty())
			nextMap();

		pad.update();
		ball.update();
	}

	public void render() {
		glClearColor(0, 0, 0, 1);
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		List<GuiTexture> entities = new ArrayList<GuiTexture>();

		for (GuiTexture texture : blocks)
			entities.add(texture);

		entities.add(ball);
		entities.add(pad);

		renderer.render(entities);

		super.render();
	}

	private void nextMap() {
		try {
			loadMap(new InternalFile("/maps/" + mapIndex++ + ".map"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadMap(InternalFile file) throws IOException {
		blocks.clear();
		BufferedReader read = file.getReader();
		int colorCount = 0;
		for (int y = Block.ROWS; 0 < y; y--) {
			String row = read.readLine();
			for (int x = 0; x < Block.COLUMNS; x++) {
				Block blk;
				switch (row.charAt(x)) {
				case 'X':
					blk = new Block(colors[colorCount++], x, y);
					colorCount %= colors.length;
					break;
				case 'S':
					blk = new Block(steel, x, y).setHealth(2);
					break;
				case '0':
				default:
					continue;
				}
				blocks.add(blk);
			}
		}
	}

	public void cleanUp() {
		renderer.cleanUp();
		super.cleanUp();
	}

	public static void main(String[] args) {
		GameLauncher.init(GAME_NAME, 800, 800);
		GameLauncher.launch(new Breakout());
	}

}
