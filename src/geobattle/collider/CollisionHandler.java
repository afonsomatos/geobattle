package geobattle.collider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.util.Log;

public class CollisionHandler {

	private Game game;
	private CollisionMatrix collisionMatrix;
	
	public CollisionHandler(Game game) {
		this.game = game;
		collisionMatrix = new CollisionMatrix();
	}
	
	public CollisionMatrix getCollisionMatrix() {
		return collisionMatrix;
	}
	
	public void handleCollisions() {
		
		// Get all the colliders
		List<GameObject> gameObjects = game.getGameObjects();
		
		List<Collider> colliders = 
		gameObjects
			.parallelStream()
			.map(g -> g.getCollider())
			.filter(Objects::nonNull)
			.collect(Collectors.toCollection(() -> new ArrayList<>(gameObjects.size())));
		
		HashMap<Collider, List<Collider>> collisions = new HashMap<Collider, List<Collider>>();
		
		// get all collisions
		int size = colliders.size();
		for (int i = 0; i < size; ++i) {
			Collider c1 = colliders.get(i);
			collisions.putIfAbsent(c1, new ArrayList<Collider>());
			
			for (int j = i + 1; j < size; j++) {
				Collider c2 = colliders.get(j);
				if (collisionMatrix.collidesWith(c1.getGameObject().getTag(), c2.getGameObject().getTag()) &&
						c1.getBounds().intersects(c2.getBounds())) {
					
					collisions.get(c1).add(c2);
					collisions.putIfAbsent(c2, new ArrayList<Collider>());
					collisions.get(c2).add(c1);
					
				}
			}
			c1.updateCollisions(collisions.get(c1));
		}
	}

}
