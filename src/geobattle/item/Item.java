package geobattle.item;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;

public abstract class Item extends GameObject {
	
	public Item(Game game) {
		super(game);
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
