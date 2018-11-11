package geobattle.item;

import java.awt.Color;
import java.awt.Graphics2D;

import com.sun.glass.events.KeyEvent;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Player;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.CircleCross;
import geobattle.util.Log;
import geobattle.util.Palette;
import geobattle.util.Util;
import geobattle.weapon.WeaponSet;
import geobattle.weapon.Weapon;

class WeaponItem extends Item {

	private Weapon weapon;
	private boolean used = false;
	
	public WeaponItem(Game game, Sprite sprite, Weapon weapon) {
		super(game);
		this.weapon = weapon;
		setSprite(sprite);
		getCollider().surround(sprite);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Player) {
			Player player = (Player) collector;
			WeaponSet ars = player.getWeaponSet();
			Weapon selectedWeapon = ars.getSelectedWeapon();
			
			if (selectedWeapon != null) {
				if (game.getIOManager().getKeyInput().isPressingKey(KeyEvent.VK_G)) {
					if (used) return;
				} else {
					used = false;
					return;
				}
			}

			used = true;

			Color[] cols = Palette.randomWithout(2, Palette.BLACK);
			setSprite(new CircleCross(20, cols[0], cols[1]));
			
			if (selectedWeapon != null) {
				selectedWeapon.kill();
			} else {
				this.kill();
			}
			ars.store(ars.getSelected(), weapon);
			
			game.spawnGameObject(weapon);
			
			weapon = selectedWeapon;
		}
	}

}
