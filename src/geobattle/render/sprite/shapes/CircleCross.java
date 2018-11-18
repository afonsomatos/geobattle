package geobattle.render.sprite.shapes;

import java.awt.Color;

public class CircleCross extends Circle {

	public CircleCross(int radius, Color colorBg, Color colorFg) {
		super(radius, colorBg);
		
		int size = (int) (radius * 1.3);
		draw(new Cross(size, size, 5, colorFg));
	}

}
