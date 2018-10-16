package geobattle.item;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Player;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Diamond;
import geobattle.weapon.Weapon;

class AmmoItem extends UnitsItem {

	private static Sprite sprite = new Diamond(30, 30, Color.WHITE);
	
	public AmmoItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		setLabelColor(Color.RED);
		setSprite(sprite);
		getCollider().surround(sprite);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Player) {
			Player p = (Player) collector;
			Weapon weapon = p.getWeapon();
			setUnits(weapon.fillAmmo(getUnits()));
		}
	}

	@Override
	protected void spawn() {
		// TODO Auto-generated method stub
		
	}

}
