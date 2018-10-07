package geobattle.sprites;

import java.awt.Graphics2D;
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
		
		int x = (int) gameObject.getX();
		int y = (int) gameObject.getY();
		
		for (Sprite s : sprites) {
			// Is translation not accumulative?
			gfx.translate(x - s.getCenterX(), y - s.getCenterY());
			BufferedImage image = s.getBufferedImage();
			gfx.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		}
		
		gfx.dispose();
	}
	
	public void add(Sprite s) {
		sprites.add(s);
	}

}
