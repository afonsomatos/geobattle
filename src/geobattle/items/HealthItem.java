package geobattle.items;

import geobattle.colliders.Collider;
import geobattle.core.AliveObject;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.sprites.SpriteMap;

public class HealthItem extends UnitsItem {

	public HealthItem(Game game, double x, double y, int units) {
		super(game, x, y, units);

		Collider col = getCollider();
		col.setWidth(50);
		col.setHeight(50);
		col.setOffsetX(-25);
		col.setOffsetY(-25);
		
		getSpriteRenderer().add(SpriteMap.HEALTH);
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
