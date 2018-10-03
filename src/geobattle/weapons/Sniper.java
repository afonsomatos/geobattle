package geobattle.weapons;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;

public class Sniper extends Weapon {

	public Sniper(Game game, GameObject origin, Tag tag) {
		super(game, origin, tag);
		
		setDamage(100);
		setShotsFired(1);
		setFireSpeed(0.01);
		setRadius(70);
		setReloadSpeed(0.007);
		setAmmoSaved(32);
		setAmmoCapacity(7);
		setColor(Color.GREEN);
		setProjectileColor(Color.RED);
		setProjectileSpeed(30.0f);
		fill();
	}
	
}
