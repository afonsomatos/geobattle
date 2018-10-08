package geobattle.object;

import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.sprite.SpriteMap;

public class Compass extends GameObject {

	private double lastAngle = 0;
	private GameObject target;
	
	public Compass(Game game, int x, int y) {
		this(game, x, y, null);
	}
	
	public Compass(Game game, int x, int y, GameObject target) {
		super(game, x, y);
		this.target = target;
		getSpriteRenderer().setSprite(SpriteMap.COMPASS);
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	@Override
	public void render(Graphics2D gfx) {
		// Change angle before rendering
		double angle;
		if (target == null || Double.isNaN(angle = pointAngle(target)))
			angle = lastAngle;
		getSpriteRenderer().setRotation(angle);
		super.render(gfx);
	}
	
}
