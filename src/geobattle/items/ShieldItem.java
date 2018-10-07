package geobattle.items;

import geobattle.colliders.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Player;
import geobattle.sprites.SpriteMap;

public class ShieldItem extends UnitsItem {

	
	public ShieldItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		
		Collider col = getCollider();
		col.setWidth(50);
		col.setHeight(50);
		col.setOffsetX(-25);
		col.setOffsetY(-25);
		
		getSpriteRenderer().setSprite(SpriteMap.SHIELD);
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
