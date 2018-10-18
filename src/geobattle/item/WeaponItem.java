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
import geobattle.weapon.Arsenal;
import geobattle.weapon.Weapon;

class WeaponItem extends Item {

	private Weapon weapon;
	private boolean used = false;
	
	public WeaponItem(Game game, double x, double y, Sprite sprite, Weapon weapon) {
		super(game, x, y);
		this.weapon = weapon;
		setSprite(sprite);
		getCollider().surround(sprite);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Player) {
			Player player = (Player) collector;
			Arsenal ars = player.getArsenal();
			Weapon selectedWeapon = ars.getSelectedWeapon();
			
			if (selectedWeapon != null) {
				if (game.getWindow().getGameCanvas().getKeyInput().isPressingKey(KeyEvent.VK_G)) {
					if (used) return;
				} else {
					used = false;
					return;
				}
			}

			used = true;
			Log.i("weapon item");

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

	@Override
	protected void spawn() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void render(Graphics2D gfx) {
		// TODO Auto-generated method stub
		
	}

}
