package geobattle.sprites;

import java.awt.Color;
import java.awt.Graphics2D;

public class SolidSquare extends Sprite {

	public SolidSquare(int width, int height, Color color) {
		super(width, height, width/2, height/2);
		
		Graphics2D gfx = (Graphics2D) getGraphics();
		gfx.setColor(color);
		gfx.fillRect(0, 0, width, height);
		gfx.dispose();
	}

}
