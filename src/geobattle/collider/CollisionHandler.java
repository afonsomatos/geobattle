package geobattle.collider;

/**
 * Handles a collision event sent by a {@link CollisionManager}.
 */
public class CollisionHandler {

	/**
	 * Handles the case when the collider starts colliding with {@code other}.
	 * This method will always be followed by a call to
	 * {@link #handle(Collider)}.
	 * 
	 * @param other
	 */
	public void enter(Collider other) {
	}

	/**
	 * Handles the case when the collider stops colliding with {@code other}.
	 * 
	 * @param other
	 */
	public void leave(Collider other) {
	};

	/**
	 * Handles the case when the collider is currently colliding with
	 * {@code other}.
	 *
	 * @param other
	 */
	public void handle(Collider other) {
	};

}
