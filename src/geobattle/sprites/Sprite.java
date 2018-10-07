package geobattle.sprites;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Sprite {
	
	private BufferedImage image;
	private Point center;
	
	public Sprite(int width, int height, Point center) {
		this.center = center;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public Sprite(int width, int height) {
		this(width, height, new Point(0, 0));
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public void setCenter(Point center) {
		this.center = center;
	}
	
	public Point getCenter() {
		return center;
	}
	
	
}
