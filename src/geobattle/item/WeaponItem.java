package geobattle.item;

import java.awt.Graphics2D;

import com.sun.glass.events.KeyEvent;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Player;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.CircleCross;
import geobattle.util.Log;
import geobattle.util.Util;
import geobattle.weapon.Arsenal;
import geobattle.weapon.Weapon;

public class WeaponItem extends Item {

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
		if (game.getWindow().getGameCanvas().getKeyInput().isPressingKey(KeyEvent.VK_G)) {
			if (used) return;
			used = true;
		} else {
			used = false;
			return;
		}
		
		Log.i("weapon item");
		if (collector instanceof Player) {
			Player player = (Player) collector;
			Arsenal ars = player.getArsenal();
			
			setSprite(new CircleCross(20, Util.randomColor(), Util.randomColor()));
			Weapon weapon2 = ars.getSelectedWeapon();
			
			// Replace weapon
			ars.getSelectedWeapon().kill();
			ars.store(ars.getSelected(), weapon);
			
			game.spawnGameObject(weapon);
			
			weapon = weapon2;
		}
		
		// Make item disappear
		//this.kill();
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
