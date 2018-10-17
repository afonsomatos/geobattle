package geobattle.living.enemies;

import java.awt.Graphics2D;
import java.util.Arrays;

import com.sun.xml.internal.bind.v2.model.util.ArrayInfoUtil;

import geobattle.util.Log;
import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Follower;
import geobattle.living.Player;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Square;
import geobattle.schedule.Event;
import geobattle.util.Palette;
import geobattle.util.Util;

public class Slime extends Enemy {
	
	private enum Type {
		SMALL(new Square(16, 16, Palette.ORANGE), 50, 100),
		MEDIUM(new Square(32, 32, Palette.ORANGE), 100, 250),
		LARGE(new Square(64, 64, Palette.ORANGE), 250, 500)
		;
		
		private Sprite sprite;
		private int health;
		private int damage;
		
		Type(Sprite sprite, int health, int damage) {
			this.sprite = sprite;
			this.health = health;
			this.damage = damage;
		}
		
		Sprite getSprite() {
			return sprite;
		}
		
		int getHealth() {
			return health;
		}
		
		int getDamage() {
			return damage;
		}
		
	}
	
	private double spawnRadius = 60;
	private Type type = Type.LARGE;
	private boolean canAttack = true;
	
	public Slime(Game game, int x, int y, GameObject target) {
		this(game, x, y, target, Type.LARGE);
	}
	
	Slime(Game game, int x, int y, GameObject target, Type type) {
		super(game, x, y, target);
		this.type = type;
		setHealth(type.getHealth());
		Sprite sprite = type.getSprite();
		setSprite(sprite);
		setSpeed(1);
		addExtension(new Follower(target));
		
		Collider superCol = this.getCollider();
		setCollider(new Collider(this, Tag.Enemy) {
			@Override
			public void handleCollision(Collider other) {
				superCol.handleCollision(other);
				if (!canAttack) return;
				canAttack = false;
				GameObject obj = other.getGameObject();
				if (obj instanceof Player) {
					Player p = (Player) obj;
					p.suffer(type.getDamage());
					game.getSchedule().next(2000, () -> { canAttack = true; });
				}
			}
		});
		getCollider().surround(sprite);
	}

	public void spawnSmaller() {
		// get random pos
		double theta = Util.randomDouble(0, Math.PI * 2);
		double x = getX() + Math.cos(theta) * spawnRadius;
		double y = getY() + Math.sin(theta) * spawnRadius;
		
		Type typeSmaller = type == Type.LARGE ? Type.MEDIUM : Type.SMALL;
		Slime slime = new Slime(game, (int)x, (int)y, getTarget(), typeSmaller);
		EnemySpawner enemySpawner = new EnemySpawner(game, slime, 200);
		game.spawnGameObject(slime);
	}
	
	@Override
	public void die() {
		if (type != Type.SMALL) {
			spawnSmaller();
			spawnSmaller();
		}
 	}

	@Override
	protected void spawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void render(Graphics2D gfx) {
		// TODO Auto-generated method stub
		
	}

}
