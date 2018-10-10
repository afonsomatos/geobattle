package geobattle.item;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Living;
import geobattle.render.sprite.SolidCross;
import geobattle.render.sprite.Sprite;

public class HealthItem extends UnitsItem {

	private static Sprite sprite = new SolidCross(30, 30, Color.GREEN);
	
	public HealthItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		setLabelColor(Color.GREEN);
		setSprite(sprite);
		getCollider().surround(sprite);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Living) {
			Living obj = (Living) collector;
			setUnits(obj.giveHealth(getUnits()));
		}
	}

	@Override
	protected void spawn() {
		// TODO Auto-generated method stub
		
	}

}
