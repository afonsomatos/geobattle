package geobattle.weapon;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;

public class Shotgun extends Weapon {

	public Shotgun(Game game, GameObject origin, Tag tag) {
		super(game, origin, tag);
		
		setRecoil(0.05);
		setDamage(50);
		setFireSpeed(0.02);
		setReloadSpeed(0.01);
		setFireAmplitude(Math.PI / 4);
		setRadius(70);
		setProjectiles(8);
		setAmmoSaved(32);
		setColor(Color.LIGHT_GRAY);
		setAmmoLoad(7);
		setProjectileColor(Color.GRAY);
		setProjectileSpeed(15.0f);
		fill();
	}

}
