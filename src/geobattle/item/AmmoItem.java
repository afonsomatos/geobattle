package geobattle.item;

import java.awt.Color;

import geobattle.collider.Box;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Player;
import geobattle.sprite.SolidDiamond;
import geobattle.sprite.Sprite;
import geobattle.weapon.Weapon;

public class AmmoItem extends UnitsItem {

	private static Sprite sprite = new SolidDiamond(30, 30, Color.WHITE);
	
	public AmmoItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		
		setLabelColor(Color.RED);
		getSpriteRenderer().setSprite(sprite);
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
