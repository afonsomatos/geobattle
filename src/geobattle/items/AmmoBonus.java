package geobattle.items;

import geobattle.colliders.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Player;
import geobattle.sprites.SpriteMap;
import geobattle.util.Log;
import geobattle.weapons.Weapon;

public class AmmoBonus extends Item {

	private int quantity;
	
	public AmmoBonus(Game game, double x, double y, int quantity) {
		super(game, x, y);
		this.quantity = quantity;
		
		Collider col = getCollider();
		col.setWidth(25);
		col.setHeight(25);
		col.setOffsetX(-13);
		col.setOffsetY(-13);
		getSpriteRenderer().add(SpriteMap.AMMO);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Player) {
			Player p = (Player) collector;
			Weapon weapon = p.getWeapon();
			quantity = weapon.fillAmmo(quantity);
		}
		if (quantity == 0)
			kill();
	}

}
