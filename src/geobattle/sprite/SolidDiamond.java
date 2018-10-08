package geobattle.sprite;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class SolidDiamond extends Sprite {

	public SolidDiamond(int width, int height, Color color) {
		super(width, height, width/2, height/2);
		
		draw((Graphics2D gfx) -> {
			gfx.setStroke(new BasicStroke(10));
			gfx.setColor(color);
			gfx.rotate(Math.PI / 4);
			gfx.fillRect(-10, -10, 20, 20);
		});
	}

}
