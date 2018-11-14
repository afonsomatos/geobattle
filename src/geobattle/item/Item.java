package geobattle.item;

import geobattle.collider.Collider;
import geobattle.collider.CollisionHandler;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;

public abstract class Item extends GameObject {
	
	public Item(Game game) {
		super(game);
		setCollider(new Collider(this));
		getCollider().addHandler(new CollisionHandler() {
			@Override
			public void handle(Collider other) {
				collected(other.getGameObject());
			}
		});
		setTag(Tag.Item);
	}
	
	public abstract void collected(GameObject collector);
	
}
