package geobattle.sprites;

import java.awt.image.BufferedImage;

public class Sprite {
	
	private BufferedImage bufferedImage;
	
	private int centerX = 0;
	private int centerY = 0;
	
	public Sprite(int width, int height, int centerX, int centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public BufferedImage getBufferedImage() {
		return bufferedImage;
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
