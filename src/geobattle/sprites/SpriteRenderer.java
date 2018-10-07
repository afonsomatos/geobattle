package geobattle.sprites;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import geobattle.core.GameObject;
import geobattle.core.Renderer;

public class SpriteRenderer implements Renderer {

	private Sprite sprite;
	private double rotation = 0;
	private GameObject gameObject;
	
	public SpriteRenderer(GameObject gameObject) {
		this.gameObject = gameObject;
	}
	
	@Override
	public void render(Graphics2D superGfx) {
		if (sprite == null)
			return;
		
		Graphics2D gfx = (Graphics2D) superGfx.create();
		int x = (int) gameObject.getX();
		int y = (int) gameObject.getY();
		int centerX = sprite.getCenterX();
		int centerY = sprite.getCenterY();
		
		// Is translation not accumulative?
		gfx.translate(x - sprite.getCenterX(), y - sprite.getCenterY());
		gfx.rotate(rotation, centerX, centerY);
		gfx.drawImage(sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), null);
		gfx.dispose();
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

}
