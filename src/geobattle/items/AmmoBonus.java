package geobattle.items;

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
		setWidth(20);
		setHeight(20);
		
		getSpriteRenderer().add(SpriteMap.AMMO);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Player) {
			Player p = (Player) collector;
			Weapon weapon = p.getWeapon();
			quantity = weapon.fillAmmo(quantity);
		}
		Log.i(quantity + "bullets left");
		if (quantity == 0)
			kill();
	}

}
