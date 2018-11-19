package geobattle.collider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	 * 
	 * @see {@link Collider}
	 * @see {@link CollisionHandler}
	 */
	public void handleCollisions() {
		
		// TODO Make this prettier
		
		List<GameObject> gameObjects = game.getGameObjects();

		List<Collider> colliders = gameObjects
				.parallelStream()
				.map(GameObject::getCollider)
				.filter(Objects::nonNull)
				.collect(Collectors.toCollection(
						() -> new ArrayList<>(gameObjects.size())));

		HashMap<Collider, List<Collider>> collisions = new HashMap<Collider, List<Collider>>();

		int size = colliders.size();
		for (int i = 0; i < size; ++i) {
			Collider c1 = colliders.get(i);
			collisions.putIfAbsent(c1, new ArrayList<Collider>());

			for (int j = i + 1; j < size; j++) {
				Collider c2 = colliders.get(j);
				if (collisionMatrix.collidesWith(c1.getGameObject().getTag(),
						c2.getGameObject().getTag()) &&
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
