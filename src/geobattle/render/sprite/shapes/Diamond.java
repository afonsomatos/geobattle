package geobattle.render.sprite.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.render.sprite.Sprite;

public class Diamond extends Sprite {

	public Diamond(int width, int height, Color color) {
		super(width, height, width/2, height/2);
		
		draw((Graphics2D gfx) -> {
			gfx.setStroke(new BasicStroke(10));
			gfx.setColor(color);
			gfx.rotate(Math.PI / 4);
			
			int side = (int) Math.sqrt(Math.pow(height/2, 2) + Math.pow(width/2, 2));
			gfx.fillRect(-side/2, -side/2, side, side);
		});
	}

}
