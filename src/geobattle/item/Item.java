package geobattle.item;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;

abstract class Item extends GameObject {
	
	public Item(Game game, double x, double y) {
		super(game, x, y);
		setCollider(new Collider(this) {
			@Override
			public void handleCollision(Collider other) {
				super.handleCollision(other);
				collected(other.getGameObject());
			}
		});
		setTag(Tag.Item);
	}
	
	public abstract void collected(GameObject collector);
	
}
