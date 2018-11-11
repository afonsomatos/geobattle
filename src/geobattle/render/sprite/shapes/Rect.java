package geobattle.render.sprite.shapes;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.render.sprite.Sprite;

public class Rect extends Sprite {

	public Rect(int width, int height, Color color) {
		super(width, height, width/2, height/2);
		
		draw(0, 0, (Graphics2D gfx) -> {
			gfx.setColor(color);
			gfx.fillRect(0, 0, width, height);
		});
	}

}
