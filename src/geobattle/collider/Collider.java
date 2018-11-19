package geobattle.collider;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import geobattle.core.GameObject;
import geobattle.render.sprite.Sprite;

/**
 * Creates a rectangular collision box linked to a {@code GameObject}.
 */
public class Collider {

	private GameObject gameObject;

	private int width = 0;
	private int height = 0;
	private int offsetX = 0;
	private int offsetY = 0;

	private List<Collider> currentCollisions = new LinkedList<Collider>();
	private List<CollisionHandler> handlers = new LinkedList<CollisionHandler>();

	public Collider(GameObject gameObject) {
		this.gameObject = gameObject;
	}

	void updateCollisions(List<Collider> updatedCollisions) {

		// Check gained collisions
		updatedCollisions.stream().filter(b -> !currentCollisions.contains(b))
				.forEach(col -> handlers.forEach(h -> h.enter(col)));

		// Check lost collisions
		currentCollisions.stream().filter(a -> !updatedCollisions.contains(a))
				.forEach(col -> handlers.forEach(h -> h.leave(col)));

		for (CollisionHandler h : handlers)
			updatedCollisions.forEach(h::handle);

		currentCollisions = updatedCollisions;
	}

	/**
	 * Adds a {@code CollisionHandler} to this {@code Collider}.
	 * 
	 * @param handler
	 */
	public void addHandler(CollisionHandler handler) {
		handlers.add(handler);
	}

	/**
	 * Removes a {@code CollisionHandler} to this {@code Collider}.
	 * 
	 * @param handler
	 */
	public void removeHandler(CollisionHandler handler) {
		handlers.remove(handler);
	}

	/**
	 * Makes the collider's rectangular box surround a given {@code Sprite}.
	 * This is useful when the size of the sprite is meant to be the size of the
	 * linked object. The collider will be placed at the center of the sprite
	 * and have the sprite's {@code width} and {@code length}.
	 * 
	 * @param sprite
	 */
	public void surround(Sprite sprite) {
		width = sprite.getWidth();
		height = sprite.getHeight();
		offsetX = -sprite.getCenterX();
		offsetY = -sprite.getCenterY();
	}

	/**
	 * Makes the collider's rectangular box surround a certain
	 * {@code GameObject}. This is useful when an object's size keeps changing.
	 * The collider will be centered at the center of the object and have its
	 * {@code width} and {@code height}.
	 * 
	 * @param gameObject
	 */
	public void surround(GameObject gameObject) {
		width = (int) gameObject.getWidth();
		height = (int) gameObject.getHeight();
		offsetX = -width / 2;
		offsetY = -height / 2;
	}

	/**
	 * Returns the {@code GameObject} associated with the collider.
	 * 
	 * @return gameObject
	 */
	public GameObject getGameObject() {
		return gameObject;
	}

	/**
	 * Returns the width of the collision box.
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the collision box.
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Shifts a collider in the horizontal axis relative to the linked game
	 * object.
	 * 
	 * @param offsetX
	 */
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * Shifts a collider in the vertical axis relative to the linked game
	 * object.
	 * 
	 * @param offsetY
	 */
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	/**
	 * Returns the collision box of the collider.
	 * 
	 * @return {@code Shape} that represents the collision box.
	 */
	public Rectangle getBounds() {
		return new Rectangle(
				(int) gameObject.getX() + offsetX,
				(int) gameObject.getY() + offsetY,
				width, height);
	}

}
