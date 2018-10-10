package geobattle.render.sprite;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.util.Util;

public class SolidCircle extends Sprite {

	public SolidCircle(int radius, Color color) {
		super(radius * 2, radius * 2, radius, radius);
		
		draw((Graphics2D gfx) -> {
			gfx.setColor(color);
			Util.fillCircle(gfx, 0, 0, radius);
		});
	}

}
