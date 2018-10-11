package geobattle.render.sprite.shapes;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.render.sprite.Sprite;
import geobattle.util.Util;

public class Circle extends Sprite {

	public Circle(int radius, Color color) {
		super(radius * 2, radius * 2, radius, radius);
		
		draw((Graphics2D gfx) -> {
			gfx.setColor(color);
			Util.fillCircle(gfx, 0, 0, radius);
		});
	}

}
