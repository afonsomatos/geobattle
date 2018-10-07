package geobattle.sprites;

import java.awt.image.BufferedImage;

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
	
}
