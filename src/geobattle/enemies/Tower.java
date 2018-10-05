package geobattle.enemies;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extensions.ShootExtension;
import geobattle.weapons.Weapon;

public class Tower extends Enemy {
	
	private Weapon weapon;
	
	public Tower(Game game, int x, int y, GameObject target) {
		super(game, x, y, target);
	
		setHealth(400);
		setWidth(40);
		setHeight(40);
		setSpeed(0);
		setColor(Color.PINK);

		weapon = buildWeapon(target);
		addBehavior(new ShootExtension(target, weapon));
	}

	public Weapon buildWeapon(GameObject target) {
		Weapon weapon = new Weapon(getGame(), this, Tag.Enemy);
		
		weapon.setDamage(80);
		weapon.setReloadSpeed(0.0025);
		weapon.setFireSpeed(0.005);
		weapon.setAmmoCapacity(50);
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
	
}
