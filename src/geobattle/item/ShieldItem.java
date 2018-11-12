package geobattle.item;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Player;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Cross;

public class ShieldItem extends UnitsItem {

	private static Sprite sprite = new Cross(30, 30, Color.CYAN);
	
	public ShieldItem(Game game, int units) {
		super(game, units);
		setSprite(sprite);
		getCollider().surround(sprite);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Player) {
			Player p = (Player) collector;
			setUnits(p.giveShield(getUnits()));
		}
	}

}
