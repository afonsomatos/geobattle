package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Follower;
import geobattle.extension.Shooter;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Aura;
import geobattle.render.sprite.shapes.Square;
import geobattle.util.Interval;
import geobattle.util.Log;
import geobattle.util.Palette;
import geobattle.weapon.Weapon;
import geobattle.weapon.WeaponFactory;

public class Sentry extends Enemy {

	private final static Color COLOR = Palette.BEIGE;
	private final static Color AURA_COLOR = Palette.MINT;
	private final static int AURA_RADIUS = 30;
	private final static Sprite SPRITE = new Aura(AURA_RADIUS, AURA_RADIUS / 5, AURA_COLOR);
	
	static {
		SPRITE.draw(gfx -> {
			double theta = Math.PI / 3;
			int xleft = (int) (Math.cos(theta) * AURA_RADIUS);
			int yleft = (int) (Math.sin(theta) * AURA_RADIUS);
			int x[] = {-xleft, -xleft, AURA_RADIUS};
			int y[] = {-yleft, yleft, 0};
			gfx.setColor(COLOR);
			gfx.fillPolygon(x, y, 3);
		});
	}
	
	private final static int HEALTH = 30000;
	private final static double SPEED = 1.0;
	
	private Tag targetTag = null;
	
	private Shooter shooter;
	private Weapon weapon;

	public Sentry(Game game, int x, int y, Tag targetTag) {
		super(game, x, y, null);
		this.targetTag = targetTag;
		setColor(COLOR);
		setSpeed(SPEED);
		setHealth(HEALTH);
		
		weapon = WeaponFactory.Unlimited.create(game, this, Tag.Player);
		shooter = new Shooter(null, weapon);
		
		addExtension(shooter);
		
		setSprite(SPRITE);
		getCollider().surround(SPRITE);
	}
	
	@Override
	public void kill() {
		super.kill();
		weapon.kill();
	}

	@Override
	public void update() {
		
		Interval<Integer> radar = shooter.getRadar();
		double maxDistance = radar != null ? radar.end : Double.POSITIVE_INFINITY;
		
		// find nearmost target
		double min = maxDistance;
		GameObject obj = null;
		for (GameObject g : game.getGameObjects()) {
			// we only aim at the living
			if (g == this) continue;
			if (!(g instanceof Living)) continue;
			// if it doesnt have the target tag discart
			if (g.getTag() != targetTag) continue;
			// see if this is the nearest
			double t = distance(g);
			if (t < min) {
				min = t;
				obj = g;
			}
		}

		// have we found anything new?
		Living target = getTarget();
		if (min < maxDistance && obj != null && obj != target) {
			// ok change target
			target = (Living) obj;
			setTarget(target);
		} else if (obj == null) {
			setTarget(null);
		}
				
		// face him if he's here and alive
		setRotation(weapon.getFireAngle() + Math.PI);
		
	}

	@Override
	public void die() {
		
	}

	@Override
	public void render(Graphics2D sgfx) {

	}

	@Override
	protected void spawn() {
		game.spawnGameObject(weapon);
	}

	@Override
	protected void handleNewTarget(Living target) {
		shooter.setTarget(target);
		weapon.setLock(target);
	}
		
}
