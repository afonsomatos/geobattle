package geobattle.collider;

import java.util.LinkedList;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;

public class CollisionHandler {

	private Game game;
	private CollisionMatrix collisionMatrix;
	
	public CollisionHandler(Game game) {
		this.game = game;
		
		collisionMatrix = new CollisionMatrix()
				.add(Tag.Player, Tag.Enemy)
				.add(Tag.Player, Tag.Item)
				.add(Tag.Enemy, Tag.PlayerOrbit);
	}
	
	public CollisionMatrix getCollisionMatrix() {
		return collisionMatrix;
	}
	
	public void handleCollisions() {
		
		LinkedList<Collider> colliders = getColliders();
		
		for (int i = 0; i < colliders.size(); ++i) {
			for (int j = i + 1; j < colliders.size(); j++) {
				Collider c1 = colliders.get(i);
				Collider c2 = colliders.get(j);
				if (collisionMatrix.collidesWith(c1.getTag(), c2.getTag()) &&
						c1.getBounds().intersects(c2.getBounds())) {
					c1.handleCollision(c2);
					c2.handleCollision(c1);
				}
			}
		}
	}
	
	public LinkedList<Collider> getColliders() {
		
		LinkedList<Collider> colliders = new LinkedList<Collider>();
		
		for (GameObject g : game.getGameObjects()) {
			Collider c = g.getCollider();
			if (c != null)
				colliders.add(c);
		}
		
		return colliders;
	}

}
