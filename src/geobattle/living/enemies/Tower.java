package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Shooter;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Square;
import geobattle.weapon.Weapon;

public class Tower extends Enemy {
	
	private static Sprite sprite = new Square(40, 40, Color.PINK);

	private Weapon weapon;
	
	public Tower(Game game, int x, int y, GameObject target) {
		super(game, x, y, target);
	
		setColor(Color.PINK);
		setHealth(400);
		setWidth(40);
		setHeight(40);
		setSpeed(0);

		weapon = buildWeapon(target);
		addExtension(new Shooter(target, weapon));
		setSprite(sprite);
		getCollider().surround(sprite);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void spawn() {
		game.spawnGameObject(weapon);
	}
	
}
