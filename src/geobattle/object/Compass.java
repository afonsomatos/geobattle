package geobattle.object;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.SpriteRenderer;
import geobattle.util.Log;
import geobattle.util.Util;

public class Compass extends GameObject {

	private static Sprite sprite = new Sprite(40, 40, 20, 20);
	
	private double lastAngle = 0;
	private GameObject target = null;

	static {
		sprite.draw((Graphics2D gfx) -> {
			int radius = 20;
			
			// Draw capsule
			gfx.setColor(new Color(255, 0, 0, 175));
			Util.fillCircle(gfx, 0, 0, radius);

			// Draw arrow
			double theta = Math.PI / 4;
			int p1 = (int) (Math.cos(theta) * radius);
			int p2 = (int) (Math.sin(theta) * radius);
			int x[] = {0, -p1, radius, -p1};
			int y[] = {0, -p2, 0, p2};
			
			gfx.setColor(Color.WHITE);
			gfx.fillPolygon(x, y, 4);
			gfx.dispose();
		});
	}
	
	public Compass(Game game, int x, int y, GameObject target) {
		super(game, x, y);
		this.target = target;
		setSprite(sprite);
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	@Override
	public void update() {
		// Change angle before rendering
		double angle = 0;
		if (target == null || Double.isNaN(angle = pointAngle(target)))
			angle = lastAngle;
		setRotation(angle);
	}

	@Override
	public void render(Graphics2D gfx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void spawn() {
		// TODO Auto-generated method stub
		
	}
	
}
