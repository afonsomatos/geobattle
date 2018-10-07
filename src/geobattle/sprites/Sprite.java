package geobattle.sprites;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import geobattle.core.Renderer;

public class Sprite extends BufferedImage {
	
	private int centerX = 0;
	private int centerY = 0;
	
	public Sprite(int width, int height, int centerX, int centerY) {
		super(width, height, BufferedImage.TYPE_INT_ARGB);
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
	
	public int getCenterY() {
		return centerY;
	}
	
	public void draw(Renderer renderer) {
		Graphics2D gfx = (Graphics2D) getGraphics();
		gfx.translate(getCenterX(), getCenterY());
		renderer.render(gfx);
		gfx.dispose();
	}
	
}
