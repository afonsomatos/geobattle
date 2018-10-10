package geobattle.item;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Player;
import geobattle.render.sprite.SolidCross;
import geobattle.render.sprite.Sprite;

public class ShieldItem extends UnitsItem {

	private static Sprite sprite = new SolidCross(30, 30, Color.CYAN);
	
	public ShieldItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
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
