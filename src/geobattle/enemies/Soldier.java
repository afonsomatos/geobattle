package geobattle.enemies;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extensions.FollowExtension;
import geobattle.extensions.ShootExtension;
import geobattle.weapons.Weapon;

public class Soldier extends Enemy {

	public boolean follow = false;
	private Weapon weapon;

	public Soldier(Game game, int x, int y, GameObject target) {
		super(game, x, y, target);
		
		setWidth(25);
		setHeight(25);
		setSpeed(1.0);
		setHealth(100);
		setColor(Color.RED);
		
		weapon = buildWeapon(target);
		addBehavior(new ShootExtension(target, weapon));
		addBehavior(new FollowExtension(target, 300));
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
		
}
