package geobattle.render.sprite;

import java.awt.Graphics2D;

public class SpriteRenderer {

	private Sprite sprite;
	private double rotation = 0;
	private boolean active = true;
	
	public SpriteRenderer() {
		this(null);
	}
	
	public SpriteRenderer(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void render(int x, int y, Graphics2D superGfx) {
		if (sprite == null)
			return;
		
		Graphics2D gfx = (Graphics2D) superGfx.create();
		int centerX = sprite.getCenterX();
		int centerY = sprite.getCenterY();
		
		// Is translation not accumulative?
		gfx.translate(x - sprite.getCenterX(), y - sprite.getCenterY());
		gfx.rotate(rotation, centerX, centerY);
		gfx.drawImage(sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), null);
		gfx.dispose();
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean getActive() {
		return active;
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
