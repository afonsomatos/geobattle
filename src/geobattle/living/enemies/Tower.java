package geobattle.living.enemies;

import java.awt.Color;

import geobattle.collider.Box;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.ShootExtension;
import geobattle.render.SolidSquare;
import geobattle.render.Sprite;
import geobattle.render.SpriteRenderer;
import geobattle.weapon.Weapon;

public class Tower extends Enemy {
	
	private static Sprite sprite = new SolidSquare(40, 40, Color.PINK);

	private Weapon weapon;
	
	public Tower(Game game, int x, int y, GameObject target) {
		super(game, x, y, target);
	
		setHealth(400);
		setWidth(40);
		setHeight(40);
		setSpeed(0);

		weapon = buildWeapon(target);
		getExtensionList().add(new ShootExtension(target, weapon));
		getSpriteRendererList().add(new SpriteRenderer(sprite));
		getCollider().surround(Box.SPRITE);
	}

	public Weapon buildWeapon(GameObject target) {
		Weapon weapon = new Weapon(getGame(), this, Tag.Enemy);
		
		weapon.setDamage(80);
		weapon.setReloadSpeed(0.0025);
		weapon.setFireSpeed(0.005);
		weapon.setAmmoLoad(50);
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
