package geobattle.render.sprite.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.render.sprite.Sprite;
import geobattle.util.Util;

public class Aura extends Sprite {

	public Aura(int radius, int thickness, Color color) {
		super(radius * 2 + thickness * 2, radius * 2 + thickness * 2, radius + thickness, radius + thickness);
		
		draw((Graphics2D gfx) -> {
			gfx.setColor(color);
			gfx.setStroke(new BasicStroke(thickness));
			Util.drawCircle(gfx, 0, 0, radius);
		});
	}

}
