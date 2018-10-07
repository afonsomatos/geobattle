package geobattle.sprites;

import java.awt.image.BufferedImage;

public class Sprite {
	
	private BufferedImage image;
	
	public Sprite(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
	}

	public BufferedImage getImage() {
		return image;
	}

}
