package geobattle.item;

import geobattle.collider.Box;
import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Player;
import geobattle.sprite.SpriteMap;

public class ShieldItem extends UnitsItem {

	public ShieldItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		
		getSpriteRenderer().setSprite(SpriteMap.SHIELD);
		getCollider().surround(Box.SPRITE);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Player) {
			Player p = (Player) collector;
			setUnits(p.giveShield(getUnits()));
		}
		super.collected(collector);
	}

}
