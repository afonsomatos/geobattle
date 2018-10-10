package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import geobattle.collider.Box;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Extension;
import geobattle.extension.Follower;
import geobattle.extension.Shooter;
import geobattle.render.sprite.SolidSquare;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.SpriteRenderer;
import geobattle.weapon.Weapon;

public class Soldier extends Enemy {

	public static Sprite sprite = new SolidSquare(24, 24, Color.RED);

	public boolean follow = false;
	private Weapon weapon;

	public Soldier(Game game, int x, int y, GameObject target) {
		super(game, x, y, target);
		
		setSpeed(1.0);
		setHealth(100);
		
		weapon = buildWeapon(target);
		
		addExtension(new Shooter(target, weapon));
		addExtension(new Follower(target, 300));
		
		setSprite(sprite);
		getCollider().surround(sprite);
	}
	
	public Weapon buildWeapon(GameObject target) {
		Weapon weapon = new Weapon(getGame(), this, Tag.Enemy);
		
		weapon.setDamage(20);
		weapon.setColor(Color.RED);
		weapon.setAmmoLoad(30);
		weapon.setReloadSpeed(0.005);
		weapon.setFireSpeed(0.01);
		weapon.setProjectileColor(Color.GRAY);
		weapon.setProjectileSpeed(8.0);
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
		
}
