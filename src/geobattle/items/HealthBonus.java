package geobattle.items;

import geobattle.colliders.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Player;
import geobattle.sprites.SpriteMap;

public class HealthBonus extends Item {

	private int quantity;
	
	public HealthBonus(Game game, double x, double y, int quantity) {
		super(game, x, y);
		this.quantity = quantity;

		Collider col = getCollider();
		col.setWidth(50);
		col.setHeight(50);
		col.setOffsetX(-25);
		col.setOffsetY(-25);
		
		getSpriteRenderer().add(SpriteMap.HEALTH);
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Player) {
			Player p = (Player) collector;
			quantity = p.giveHealth(quantity);
		}
		if (quantity == 0)
			kill();
	}

}
