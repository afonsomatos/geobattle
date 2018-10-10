package geobattle.render.sprite;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import geobattle.render.Renderer;

public class Sprite {
	
	private static final GraphicsConfiguration GFX_CONFIG =
			GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration();

	private int centerX = 0;
	private int centerY = 0;
	
	private BufferedImage image;
	
	public Sprite(int width, int height, int centerX, int centerY) {
		image = GFX_CONFIG.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public int getCenterX() {
		return centerX;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public int getCenterY() {
		return centerY;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void draw(int dx, int dy, Renderer renderer) {
		Graphics2D gfx = (Graphics2D) image.getGraphics();
		gfx.translate(dx, dy);
		renderer.render(gfx);
		gfx.dispose();	
	}
	
	public void draw(Renderer renderer) {
		draw(getCenterX(), getCenterY(), renderer);
	}
	
}
