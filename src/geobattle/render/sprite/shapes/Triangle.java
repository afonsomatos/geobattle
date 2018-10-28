package geobattle.render.sprite.shapes;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.render.sprite.Sprite;

public class Triangle extends Sprite {

	public Triangle(int width, int height, Color color) {
		super(width, height, width/2, height/2);
		
		draw(0, 0, (Graphics2D gfx) -> {
			int[] xs = { 0, 0, width/2 };
			int[] ys = { 0, width, width/2 };
			gfx.setColor(color);
			gfx.fillPolygon(xs, ys, 3);
		});
	}

}
