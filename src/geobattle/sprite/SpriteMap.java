package geobattle.sprite;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Renderer;
import geobattle.util.Log;
import geobattle.util.Util;

public final class SpriteMap {
	
	public final static Sprite PLAYER	= new SolidSquare(40, 40, Color.CYAN);
	public final static Sprite COMPASS	= new Sprite(40, 40, 20, 20);
	
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
		
		SHIELD.draw((Graphics2D gfx) -> {
			gfx.setStroke(new BasicStroke(10));
			gfx.setColor(Color.CYAN);
			gfx.drawLine(0, -50, 0, 50);
			gfx.drawLine(-50, 0, 50, 0);
		});
		
		HEALTH.draw((Graphics2D gfx) -> {
			gfx.setStroke(new BasicStroke(10));
			gfx.setColor(Color.GREEN);
			gfx.drawLine(0, -50, 0, 50);
			gfx.drawLine(-50, 0, 50, 0);
		});
		
		AMMO.draw((Graphics2D gfx) -> {
			gfx.setStroke(new BasicStroke(10));
			gfx.setColor(Color.WHITE);
			gfx.rotate(Math.PI / 4);
			gfx.fillRect(-10, -10, 20, 20);
		});
		
		COMPASS.draw((Graphics2D gfx) -> {
			int radius = 20;
			
			// draw capsule
			gfx.setColor(new Color(255, 0, 0, 175));
			Util.fillCircle(gfx, 0, 0, radius);

			// drawing arrow
			double theta = Math.PI / 4;
			int p1 = (int) (Math.cos(theta) * radius);
			int p2 = (int) (Math.sin(theta) * radius);
			int x[] = {0, -p1, radius, -p1};
			int y[] = {0, -p2, 0, p2};
			
			gfx.setColor(Color.WHITE);
			gfx.fillPolygon(x, y, 4);
			gfx.dispose();
		});

	}
	
	private SpriteMap() {
		
	}
	
}
