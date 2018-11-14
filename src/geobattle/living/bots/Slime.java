package geobattle.living.bots;

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
import geobattle.render.sprite.shapes.Rect;
import geobattle.util.Interval;
import geobattle.util.Palette;
import geobattle.util.Util;

public class Slime extends Bot {
	
	private static final Color COLOR = Palette.ORANGE;
	
	private enum Type {
		SMALL	(new Rect(16, 16, COLOR), 050, 015, 2, 500, null, 0),
		MEDIUM	(new Rect(32, 32, COLOR), 100, 050, 1.5, 1000, SMALL, 2),
		LARGE	(new Rect(64, 64, COLOR), 250, 150, 1, 1500, MEDIUM, 10);
		
		final Sprite sprite;
		final int health;
		final int damage;
		final double speed;
		final int childs;
		
		// Interval between strikes (ms)
		final int attackDelay;
		
		// Type of children
		final Type child;
		
		Type(Sprite sprite, int health, int damage, double speed, int attackDelay, Type child, int childs) {
			this.sprite = sprite;
			this.speed 	= speed;
			this.health = health;
			this.child 	= child;
			this.damage = damage;
			this.attackDelay = attackDelay;
			this.childs = childs;
		}
	
	}
	
	// Spawning distance from entity
	private Interval<Double> spawnRadius = new Interval<Double>(30.0, 90.0);
	
	// Flag toggled between strikes
	private boolean canAttack = true;
	
	private final Type type;
	
	public Slime(Game game) {
		this(game, Type.LARGE);
	}
	
	Slime(Game game, Type type) {
		super(game);
		this.type = type;
		setColor(COLOR);
		setHealth(type.health);
		setSpeed(type.speed);
		setSprite(type.sprite);
		
		// String newName = String.format("%s<%s>", getName(), type.toString().toLowerCase());
		// setName(newName);
		
		Follower follower = new Follower(null);
		addController(follower);

		getTriggerMap().add("die", this::divide);
		getTriggerMap().add("newTarget", () -> {
			follower.setTarget(getTarget());
		});
		
		setupCollider();
	}
	
	private void divide() {
		if (type.child != null) {
			for (int i = 0; i < type.childs; ++i) {
				Slime s = getChild(type.child);
				game.spawnGameObject(s);
			}
		}
	}

	private void setupCollider() {
		Collider superCol = getCollider();
		Collider newCol = new Collider(this) {
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
	
	private Slime getChild(Type child) {
		Point pos = Util.randomVec(spawnRadius);
		int x = (int) (pos.x + getX());
		int y = (int) (pos.y + getY());
		Slime slime = new Slime(game, child);
		slime.setTarget(getTarget());
		slime.setTag(getTag());
		slime.moveTo(x, y);
		return slime;
	}
	
}
