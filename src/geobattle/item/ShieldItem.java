package geobattle.item;

import java.awt.Color;

import geobattle.collider.Box;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Player;
import geobattle.sprite.SolidCross;
import geobattle.sprite.Sprite;
import geobattle.sprite.SpriteRenderer;

public class ShieldItem extends UnitsItem {

	private static Sprite sprite = new SolidCross(30, 30, Color.CYAN);
	
	public ShieldItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		
		getSpriteRendererList().add(new SpriteRenderer(sprite));
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
