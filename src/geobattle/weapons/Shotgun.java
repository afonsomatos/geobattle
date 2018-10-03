package geobattle.weapons;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;

public class Shotgun extends Weapon {

	public Shotgun(Game game, GameObject origin, Tag tag) {
		super(game, origin, tag);
		
		setDamage(100);
		setFireSpeed(0.007);
		setReloadSpeed(0.003);
		setFireAmplitude(Math.PI / 4);
		setRadius(70);
		setShotsFired(8);
		setAmmoSaved(32);
		setColor(Color.LIGHT_GRAY);
		setAmmoCapacity(7);
		setProjectileColor(Color.GRAY);
		setProjectileSpeed(15.0f);
		fill();
	}

}
