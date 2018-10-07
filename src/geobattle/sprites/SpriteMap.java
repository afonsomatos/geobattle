package geobattle.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public final class SpriteMap {
	
	// Collectable item shield
	public static Sprite SHIELD = new Sprite(100, 100);
	
	// Collectable item health
	public static Sprite HEALTH = new Sprite(100, 100);
	
	// Collectable item ammo
	public static Sprite AMMO = new Sprite(100, 100);
	
	// Create all sprites programatically
	static {
		
		draw(SHIELD, (Graphics2D gfx) -> {
			gfx.setStroke(new BasicStroke(10));
			gfx.setColor(Color.PINK);
			gfx.drawLine(0, -50, 0, 50);
			gfx.drawLine(-50, 0, 50, 0);
		});
		
		draw(HEALTH, (Graphics2D gfx) -> {
			gfx.setStroke(new BasicStroke(10));
			gfx.setColor(Color.YELLOW);
			gfx.drawLine(0, -50, 0, 50);
			gfx.drawLine(-50, 0, 50, 0);
		});
		
		draw(AMMO, (Graphics2D gfx) -> {
			gfx.setStroke(new BasicStroke(10));
			gfx.setColor(Color.ORANGE);
			gfx.rotate(Math.PI / 4);
			gfx.fillRect(-50, -50, 100, 100);
		});
		
	}
	
	private SpriteMap() {
		
	}
	
	private static void draw(Sprite sprite, Drawer drawer) {
		Graphics2D gfx = (Graphics2D) sprite.getImage().getGraphics();
		drawer.draw(gfx);
		gfx.dispose();
	}
	
}
