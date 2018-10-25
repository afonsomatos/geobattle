package geobattle.item;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Player;
import geobattle.living.WeaponHolder;
import geobattle.living.bots.Bot;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Diamond;
import geobattle.util.Palette;
import geobattle.weapon.Weapon;

class AmmoItem extends UnitsItem {

	private final static Sprite SPRITE = new Diamond(30, 30, Palette.WHITE);
	private final static Color LABEL_COLOR = Palette.RED;
	
	public AmmoItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		setLabelColor(LABEL_COLOR);
		setSprite(SPRITE);
		getCollider().surround(SPRITE);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof WeaponHolder) {
			WeaponHolder weaponHolder = (WeaponHolder) collector;
			// set remaining units
			setUnits(weaponHolder.getWeapon().fillAmmo(getUnits()));
		}
	}

}
