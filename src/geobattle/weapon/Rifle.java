package geobattle.weapon;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;

public class Rifle extends Weapon {

	public Rifle(Game game, GameObject origin, Tag tag) {
		super(game, origin, tag);
		
		setDamage(45);
		setProjectiles(1);
		setRadius(70);
		
		setFireSpeed(0.1);
		setReloadSpeed(0.01);
		setRecoil(0.1);
		setAmmoLoad(30);
		setAmmoSaved(90);
		
		setColor(new Color(189, 45, 200));
		setProjectileColor(Color.YELLOW);
		setProjectileSpeed(10.0f);
		fill();
	}
	
}
