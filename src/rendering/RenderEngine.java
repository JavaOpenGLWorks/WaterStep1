package rendering;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import display.Window;
import terrains.Terrain;
import utils.OpenGlUtils;
import water.WaterTile;
import waterRendering.WaterRenderer;

public class RenderEngine {

	private final Window window;
	private final WaterRenderer waterRenderer;

	public RenderEngine(int fps, int displayWidth, int displayHeight) {
		this.window = Window.newWindow(displayWidth, displayHeight, fps).antialias(true).create();
		this.waterRenderer = new WaterRenderer();
	}

	public void render(Terrain terrain, WaterTile water, ICamera camera, Light light) {
		doMainRenderPass(terrain, water, camera, light);
	}

	public Window getWindow() {
		return window;
	}

	public void close() {
		waterRenderer.cleanUp();
		window.destroy();
	}

	private void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);
		OpenGlUtils.cullBackFaces(true);
		OpenGlUtils.enableDepthTesting(true);
		OpenGlUtils.antialias(true);

	}

	private void doMainRenderPass(Terrain terrain, WaterTile water, ICamera camera, Light light) {
		prepare();
		terrain.render(camera, light);
		OpenGlUtils.goWireframe(Keyboard.isKeyDown(Keyboard.KEY_G));
		waterRenderer.render(water, camera, light);
		OpenGlUtils.goWireframe(false);
		window.update();
	}

}
