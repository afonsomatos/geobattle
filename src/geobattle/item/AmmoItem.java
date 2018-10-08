package geobattle.item;

import geobattle.collider.Box;
import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Player;
import geobattle.sprite.SpriteMap;
import geobattle.util.Log;
import geobattle.weapon.Weapon;

public class AmmoItem extends UnitsItem {

	public AmmoItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		
		getSpriteRenderer().setSprite(SpriteMap.AMMO);
		getCollider().surround(Box.SPRITE);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Player) {
			Player p = (Player) collector;
			Weapon weapon = p.getWeapon();
			setUnits(weapon.fillAmmo(getUnits()));
		}
		super.collected(collector);
	}

}
