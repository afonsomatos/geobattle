package geobattle.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Renderer;
import geobattle.util.Log;

public final class SpriteMap {
	
	public final static Sprite PLAYER	= new SolidSquare(40, 40, Color.CYAN);

	// Items
	public final static Sprite SHIELD 	= new Sprite(50, 50, 25, 25);
	public final static Sprite HEALTH 	= new Sprite(50, 50, 25, 25);
	public final static Sprite AMMO		= new Sprite(24, 24, 12, 12);
	
	// Troops
	public final static Sprite TOWER 	= new SolidSquare(40, 40, Color.PINK);
	public final static Sprite CREEPER 	= new SolidSquare(16, 16, Color.GREEN);
	public final static Sprite SOLDIER 	= new SolidSquare(24, 24, Color.RED);
	
	// Create all sprites programatically
	static {
		
		Log.i("Loading sprites");
		
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
	
	private static void draw(Sprite sprite, int dx, int dy, Renderer renderer) {
		Graphics2D gfx = (Graphics2D) sprite.getGraphics();
		gfx.translate(dx, dy);
		renderer.render(gfx);
		gfx.dispose();	
	}
	
	private static void draw(Sprite sprite, Renderer renderer) {
		draw(sprite, sprite.getCenterX(), sprite.getCenterY(), renderer);
	}
	
	private SpriteMap() {
		
	}
	
}
