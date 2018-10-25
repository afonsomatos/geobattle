package geobattle.item;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Cross;
import geobattle.util.Palette;

class HealthItem extends UnitsItem {

	private final static Sprite SPRITE = new Cross(30, 30, Palette.GREEN);
	private final static Color LABEL_COLOR = Palette.GREEN;
	
	public HealthItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		setLabelColor(LABEL_COLOR);
		setSprite(SPRITE);
		getCollider().surround(SPRITE);
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
