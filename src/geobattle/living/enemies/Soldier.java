package geobattle.living.enemies;

import java.awt.Color;
import java.util.List;

import geobattle.collider.Box;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Extension;
import geobattle.extension.FollowExtension;
import geobattle.extension.ShootExtension;
import geobattle.sprite.SolidSquare;
import geobattle.sprite.Sprite;
import geobattle.sprite.SpriteRenderer;
import geobattle.weapon.Weapon;

public class Soldier extends Enemy {

	public static Sprite sprite = new SolidSquare(24, 24, Color.RED);

	public boolean follow = false;
	private Weapon weapon;

	public Soldier(Game game, int x, int y, GameObject target) {
		super(game, x, y, target);
		
		setSpeed(1.0);
		setHealth(100);
		
		weapon = buildWeapon(target);
		
		List<Extension> extensionList = getExtensionList();
		extensionList.add(new ShootExtension(target, weapon));
		extensionList.add(new FollowExtension(target, 300));

		getSpriteRendererList().add(new SpriteRenderer(sprite));
		getCollider().surround(Box.SPRITE);
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
