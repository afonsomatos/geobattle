package geobattle.item;

import geobattle.collider.Box;
import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.AliveObject;
import geobattle.sprite.SpriteMap;

public class HealthItem extends UnitsItem {

	public HealthItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		
		getSpriteRenderer().setSprite(SpriteMap.HEALTH);
		getCollider().surround(Box.SPRITE);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof AliveObject) {
			AliveObject obj = (AliveObject) collector;
			setUnits(obj.giveHealth(getUnits()));
		}
		super.collected(collector);
	}

}
