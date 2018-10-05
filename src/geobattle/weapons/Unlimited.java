package geobattle.weapons;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.util.Util;

public class Unlimited extends Weapon {

	private Random rand = new Random();
	
	public Unlimited(Game game, GameObject origin, Tag tag) {
		super(game, origin, tag);
		
		setDamage(15);
		setShotsFired(1);
		setRadius(85);
		
		setShotsFired(1);
		setFireSpeed(Weapon.MAX_SPEED);
		setReloadSpeed(Weapon.MAX_SPEED);
		setAmmoCapacity(Weapon.INFINITE_AMMO);
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
