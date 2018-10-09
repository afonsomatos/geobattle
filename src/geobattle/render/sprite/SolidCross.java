package geobattle.render.sprite;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class SolidCross extends Sprite {

	public SolidCross(int width, int height, Color color) {
		super(width, height, width/2, height/2);
		
		draw((Graphics2D gfx) -> {
			gfx.setColor(color);
			gfx.setStroke(new BasicStroke(10));
			gfx.drawLine(0, -height/2, 0, height);
			gfx.drawLine(-width/2, 0, width, 0);
		});
	}

}
