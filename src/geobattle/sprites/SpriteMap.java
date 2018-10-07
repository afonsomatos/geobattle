package geobattle.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import geobattle.core.Renderer;

public final class SpriteMap {
	
	// Collectable item shield
	public static Sprite SHIELD = new Sprite(50, 50, new Point(25, 25));
	
	// Collectable item health
	public static Sprite HEALTH = new Sprite(50, 50, new Point(25, 25));
	
	// Collectable item ammo
	public static Sprite AMMO = new Sprite(100, 100, new Point(50, 50));
	
	// Create all sprites programatically
	static {
		
		draw(SHIELD, (Graphics2D gfx) -> {
			gfx.setStroke(new BasicStroke(10));
			gfx.setColor(Color.CYAN);
			gfx.drawLine(0, -50, 0, 50);
			gfx.drawLine(-50, 0, 50, 0);
		});
		
		draw(HEALTH, (Graphics2D gfx) -> {
			gfx.setStroke(new BasicStroke(10));
			gfx.setColor(Color.GREEN);
			gfx.drawLine(0, -50, 0, 50);
			gfx.drawLine(-50, 0, 50, 0);
		});
		
		draw(AMMO, (Graphics2D gfx) -> {
			gfx.setStroke(new BasicStroke(10));
			gfx.setColor(Color.WHITE);
			gfx.rotate(Math.PI / 4);
			gfx.fillRect(-10, -10, 20, 20);
		});
		
	}
	
	private SpriteMap() {
		
	}
	
	private static void draw(Sprite sprite, Renderer renderer) {
		Graphics2D gfx = (Graphics2D) sprite.getImage().getGraphics();
		Point center = sprite.getCenter();
		gfx.translate(center.x, center.y);
		renderer.render(gfx);
		gfx.dispose();
	}
	
}
