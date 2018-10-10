package geobattle.item;

import java.awt.Color;

import geobattle.collider.Box;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Player;
import geobattle.render.sprite.SolidCross;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.SpriteRenderer;

public class ShieldItem extends UnitsItem {

	private static SpriteRenderer srend;
	
	static {
		Sprite sprite = new SolidCross(30, 30, Color.CYAN);
		srend = new SpriteRenderer(sprite);
	}
	
	public ShieldItem(Game game, double x, double y, int units) {
		super(game, x, y, units);
		
		getCollider().surround(srend.getSprite());
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
