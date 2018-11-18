package geobattle.item;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.WeaponHolder;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Diamond;
import geobattle.util.Palette;
import geobattle.weapon.Weapon;

public class AmmoItem extends UnitsItem {

	private final static Sprite SPRITE = new Diamond(30, 30, Palette.WHITE);
	private final static Color LABEL_COLOR = Palette.RED;
	
	public AmmoItem(Game game, int units) {
		super(game, units);
		setLabelColor(LABEL_COLOR);
		setSprite(SPRITE);
		getCollider().surround(SPRITE);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof WeaponHolder) {
			WeaponHolder weaponHolder = (WeaponHolder) collector;
			// set remaining units
			Weapon weapon = weaponHolder.getWeapon();
			if (weapon != null)
				setUnits(weapon.fillAmmo(getUnits()));
		}
	}

}
