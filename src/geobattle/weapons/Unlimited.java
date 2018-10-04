package geobattle.weapons;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;

public class Unlimited extends Weapon {

	public Unlimited(Game game, GameObject origin, Tag tag) {
		super(game, origin, tag);
		
		setDamage(15);
		setShotsFired(1);
		setRadius(70);
		
		setShotsFired(1);
		setFireSpeed(Weapon.MAX_SPEED);
		setReloadSpeed(Weapon.MAX_SPEED);
		setAmmoCapacity(Weapon.INFINITE_AMMO);
		setAmmoSaved(Weapon.INFINITE_AMMO);
		
		setColor(Color.RED);
		setProjectileColor(Color.RED);
		setProjectileSpeed(20.0f);
		setDamage(10000);
		setProjectileSize(35);
		fill();
	}
	
}
