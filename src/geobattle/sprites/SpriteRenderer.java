package geobattle.sprites;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import geobattle.core.GameObject;
import geobattle.core.Renderer;

public class SpriteRenderer implements Renderer {

	private LinkedList<Sprite> sprites = new LinkedList<Sprite>();
	private GameObject gameObject;
	
	public SpriteRenderer(GameObject gameObject) {
		this.gameObject = gameObject;
	}
	
	@Override
	public void render(Graphics2D superGfx) {
		Graphics2D gfx = (Graphics2D) superGfx.create();
		
		final int x = (int) gameObject.getX();
		final int y = (int) gameObject.getY();
		
		for (Sprite s : sprites) {
			Point center = s.getCenter();
			BufferedImage image = s.getImage();
			gfx.translate(x - center.x, y - center.y);
			gfx.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
			gfx.dispose();
		}
		
		gfx.dispose();
	}
	
	public void add(Sprite s) {
		sprites.add(s);
	}

}
