package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Shooter;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Square;
import geobattle.weapon.Weapon;

public class Tower extends Enemy {
	
	private final static Sprite SPRITE = new Square(40, 40, Color.PINK);
	private final static int HEALTH = 400;

	private Weapon weapon;
	
	public Tower(Game game, int x, int y, Living target) {
		super(game, x, y, target);
	
		setHealth(HEALTH);
		setSpeed(0);

		weapon = buildWeapon(target);
		addExtension(new Shooter(target, weapon));
		
		setSprite(SPRITE);
		getCollider().surround(SPRITE);
	}

	public Weapon buildWeapon(GameObject target) {
		Weapon weapon = new Weapon(getGame(), this, Tag.Enemy);
		
		weapon.setDamage(80);
		weapon.setReloadSpeed(0.0025);
		weapon.setFireSpeed(0.005);
		weapon.setAmmoLoad(50);
		weapon.setColor(Color.MAGENTA);
		weapon.setProjectileColor(Color.MAGENTA);
		weapon.setProjectileSpeed(10.0);
		weapon.setLock(target);
		weapon.fill();
		
		return weapon;
	}
	
	@Override
	public void kill() {
		super.kill();
		weapon.kill();
	}

	@Override
	public void update() {
	}

	@Override
	public void die() {
		
	}

	@Override
	public void render(Graphics2D gfx) {
		
	}

	@Override
	protected void spawn() {
		game.spawnGameObject(weapon);
	}
	
}
