package geobattle.item;

import java.awt.Color;

import geobattle.collider.Box;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Living;
import geobattle.sprite.SolidCross;
import geobattle.sprite.Sprite;
import geobattle.sprite.SpriteRenderer;

public class HealthItem extends UnitsItem {

	private static Sprite sprite = new SolidCross(30, 30, Color.GREEN);
	
	public HealthItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		
		setLabelColor(Color.GREEN);
		getSpriteRendererList().add(new SpriteRenderer(sprite));
		getCollider().surround(Box.SPRITE);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Living) {
			Living obj = (Living) collector;
			setUnits(obj.giveHealth(getUnits()));
		}
		super.collected(collector);
	}

}
