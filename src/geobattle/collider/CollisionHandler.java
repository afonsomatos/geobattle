package geobattle.collider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;

public final class CollisionHandler {

	private Game game;
	private CollisionMatrix collisionMatrix;
	
	public CollisionHandler(Game game) {
		this.game = game;
		collisionMatrix = new CollisionMatrix()
				.add(Tag.Player, Tag.Enemy)
				.add(Tag.Player, Tag.Item)
				.add(Tag.Void, Tag.Player)
				.add(Tag.Void, Tag.Enemy)
				.add(Tag.Enemy, Tag.PlayerOrbit);
	}
	
	public CollisionMatrix getCollisionMatrix() {
		return collisionMatrix;
	}
	
	public void handleCollisions() {
		
		List<Collider> colliders = getColliders();
		
		HashMap<Collider, Set<Collider>> collisions = new HashMap<Collider, Set<Collider>>();
		
		// get all collisions
		int size = colliders.size();
		for (int i = 0; i < size; ++i) {
			Collider c1 = colliders.get(i);
			collisions.putIfAbsent(c1, new HashSet<Collider>());
			
			for (int j = i + 1; j < size; j++) {
				Collider c2 = colliders.get(j);
				if (collisionMatrix.collidesWith(c1.getTag(), c2.getTag()) &&
						c1.getBounds().intersects(c2.getBounds())) {
					
					collisions.get(c1).add(c2);
					collisions.putIfAbsent(c2, new HashSet<Collider>());
					collisions.get(c2).add(c1);
					
				}
			}
		}
		
		// update colliders info
		for (Collider col : colliders)
			col.updateCollisions(collisions.get(col));
		
	}
	
	public List<Collider> getColliders() {
		List<Collider> colliders = new ArrayList<Collider>();
		for (GameObject g : game.getGameObjects()) {
			Collider c = g.getCollider();
			if (c != null)
				colliders.add(c);
		}
		return colliders;
	}

}
