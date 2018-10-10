package geobattle.weapon;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;

public class Unlimited extends Weapon {

	public Unlimited(Game game, GameObject origin, Tag tag) {
		super(game, origin, tag);
		
		setDamage(15);
		setProjectiles(1);
		setRadius(85);
		
		setProjectiles(1);
		setFireSpeed(Weapon.MAX_SPEED);
		setReloadSpeed(Weapon.MAX_SPEED);
		setAmmoLoad(Weapon.INFINITE_AMMO);
		setAmmoSaved(Weapon.INFINITE_AMMO);
		setRecoil(Math.PI / 72);
		
		setProjectileColor(Color.ORANGE);
		setProjectileSpeed(20.0f);
		setDamage(10000);
		setProjectileSize(30);
		fill();
	}
	
	@Override
	public void draw(Graphics2D gfx) {
		// (0, 0) is the firing point
		int side = 30;
		
		int x[] = {0, side, side};
		int y[] = {0, side, -side};
		
		gfx.translate(15, 0);
		gfx.setColor(Color.RED);
		gfx.fillPolygon(x, y, 3);
	}
	
}
