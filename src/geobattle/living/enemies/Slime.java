package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Follower;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Square;
import geobattle.util.Interval;
import geobattle.util.Palette;
import geobattle.util.Util;

public class Slime extends Enemy {
	
	private static final Color COLOR = Palette.ORANGE;
	
	private enum Type {
		SMALL	(new Square(16, 16, COLOR), 050, 015, 2, 500, null),
		MEDIUM	(new Square(32, 32, COLOR), 100, 050, 1.5, 1000, SMALL),
		LARGE	(new Square(64, 64, COLOR), 250, 150, 1, 1500, MEDIUM);
		
		final Sprite sprite;
		final int health;
		final int damage;
		final double speed;
		
		// Interval between strikes (ms)
		final int attackDelay;
		
		// Type of children
		final Type child;
		
		Type(Sprite sprite, int health, int damage, double speed, int attackDelay, Type child) {
			this.sprite = sprite;
			this.speed 	= speed;
			this.health = health;
			this.child 	= child;
			this.damage = damage;
			this.attackDelay = attackDelay;
		}
	
	}
	
	// Spawning distance from entity
	private Interval<Double> spawnRadius = new Interval<Double>(30.0, 90.0);
	
	// Flag toggled between strikes
	private boolean canAttack = true;
	
	private final Type type;
	
	public Slime(Game game, int x, int y, Living target) {
		this(game, x, y, target, Type.LARGE);
	}
	
	Slime(Game game, int x, int y, Living target, Type type) {
		super(game, x, y, target);
		this.type = type;
		setColor(COLOR);
		setHealth(type.health);
		setSpeed(type.speed);
		setSprite(type.sprite);
		addExtension(new Follower(target));
		setupCollider();
	}

	private void setupCollider() {
		Collider superCol = getCollider();
		Collider newCol = new Collider(this, Tag.Enemy) {
			@Override
			public void handleCollision(Collider other) {
				superCol.handleCollision(other);
				GameObject obj = other.getGameObject();
				Living target = getTarget();
				if (obj != target || !canAttack) return;
				canAttack = false;
				target.suffer(type.damage);
				game.getSchedule().next(type.attackDelay, () -> { canAttack = true; });
			}
		};
		newCol.surround(type.sprite);
		setCollider(newCol);
	}
	
	public void spawn(Type child) {
		Point pos = Util.randomVec(spawnRadius);
		int x = (int) (pos.x + getX());
		int y = (int) (pos.y + getY());
		game.spawnGameObject(new Slime(game, x, y, getTarget(), child));
	}
	
	@Override
	public void die() {
		if (type.child != null) {
			spawn(type.child);
			spawn(type.child);
		}
 	}

	@Override
	protected void spawn() {
		
	}

	@Override
	protected void update() {
		
	}

	@Override
	protected void render(Graphics2D gfx) {
		
	}

	@Override
	protected void handleNewTarget(Living target) {
		// TODO Auto-generated method stub
		
	}

}
