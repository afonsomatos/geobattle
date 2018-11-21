package geobattle.collider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import geobattle.core.Game;
import geobattle.core.GameObject;

/**
 * Handles all collisions between game objects according to a
 * {@link CollisionMatrix}.
 */
public class CollisionManager {

	private Game game;
	private CollisionMatrix collisionMatrix;

	public CollisionManager(Game game) {
		this.game = game;
		collisionMatrix = new CollisionMatrix();
	}

	/**
	 * Returns the {@code CollisionMatrix} associated.
	 * 
	 * @return collision matrix linked with this manager
	 */
	public CollisionMatrix getCollisionMatrix() {
		return collisionMatrix;
	}

	/**
	 * Traces all collisions of all game objects in the moment it is called, and
	 * updates every game object's {@code Collider} with this information.
	 * Colliders are responsible for handling collision events.
	 * 
	 * @see Collider
	 * @see CollisionHandler
	 */
	public void handleCollisions() {
		
		// Get all active colliders
		ArrayList<Collider> colliders =
				game.getGameObjects()
					.stream()
					.filter(GameObject::isActive)
					.map(GameObject::getCollider)
					.filter(Objects::nonNull)
					/*
					 * Possible improvement:
					 * () -> new ArrayList<>(gameObjects.size())
					 */
					.collect(Collectors.toCollection(ArrayList::new));
		
		Map<Collider, List<Collider>> collisions =
				new HashMap<Collider, List<Collider>>();

		// Set a new list for all colliders
		colliders.forEach(c -> collisions.put(c, new ArrayList<>()));
		
		// Loop through 
		int size = colliders.size();
		for (int i = 0; i < size; ++i) {
			Collider c1 = colliders.get(i);
			for (int j = i + 1; j < size; ++j) {
				Collider c2 = colliders.get(j);
				/*
				 * Check whether the two colliders are meant to collide and if
				 * their collision box clash.
				 */
				if (collisionMatrix.collidesWith(c1.getGameObject().getTag(),
						c2.getGameObject().getTag()) &&
						c1.getBounds().intersects(c2.getBounds())) {
					collisions.get(c1).add(c2);
					collisions.get(c2).add(c1);
				}	
			}
			c1.updateCollisions(collisions.get(c1));
		}
	}
}
