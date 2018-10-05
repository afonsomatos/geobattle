package geobattle.objects;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.util.Util;

public class Compass extends GameObject {

	private double lastAngle = 0;
	private int radius = 20;
	private Color backgroundColor = new Color(255, 0, 0, 175);
	private Color arrowColor = Color.WHITE;
	private GameObject target;
	
	public Compass(Game game, int x, int y) {
		this(game, x, y, null);
	}
	
	public Compass(Game game, int x, int y, GameObject target) {
		super(game, x, y);
		this.target = target;
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public void setArrowColor(Color arrowColor) {
		this.arrowColor = arrowColor;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	private void drawArrow(Graphics2D superGfx) {
		final double theta = Math.PI / 4;
		final int p1 = (int) (Math.cos(theta) * radius);
		final int p2 = (int) (Math.sin(theta) * radius);
		final int x[] = {0, -p1, radius, -p1};
		final int y[] = {0, -p2, 0, p2};
		
		Graphics2D gfx = (Graphics2D) superGfx.create();
		gfx.setColor(arrowColor);
		gfx.fillPolygon(x, y, 4);
		gfx.dispose();
	}
	
	@Override
	public void render(Graphics2D superGfx) {
		Graphics2D gfx = (Graphics2D) superGfx.create();
		gfx.translate(getX(), getY());
		gfx.setColor(backgroundColor);
		Util.fillCircle(gfx, 0, 0, radius);
		
		double angle;
		if (target == null || Double.isNaN(angle = pointAngle(target)))
			angle = lastAngle;
		gfx.rotate(angle);
		
		drawArrow(gfx);
		gfx.dispose();
	}
	
}
