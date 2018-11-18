package geobattle.collider;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import geobattle.core.GameObject;
import geobattle.render.sprite.Sprite;

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
		updatedCollisions
			.stream()
			.filter(b -> !currentCollisions.contains(b))
			.forEach(col -> {
				handlers.forEach(h -> h.enter(col));
			});
			
		// Check lost collisions
		currentCollisions
			.stream()
			.filter(a -> !updatedCollisions.contains(a))
			.forEach(col -> {
				handlers.forEach(h -> h.leave(col));
			});
		
		for (CollisionHandler h : handlers)
			updatedCollisions.forEach(h::handle);
	
		currentCollisions = updatedCollisions;
	}
	
	public void addHandler(CollisionHandler handler) {
		handlers.add(handler);
	}
	
	public void removeHandler(CollisionHandler handler) {
		handlers.remove(handler);
	}
	
	public void surround(Sprite sprite) {
		width = sprite.getWidth();
		height = sprite.getHeight();
		offsetX = -sprite.getCenterX();
		offsetY = -sprite.getCenterY();
	}
	
	public void surround(GameObject gameObject) {
		width = (int) gameObject.getWidth();
		height = (int) gameObject.getHeight();
		offsetX = -width/2;
		offsetY = -height/2;
	}
	
	public GameObject getGameObject() {
		return gameObject;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public Rectangle getBounds() {
		return new Rectangle(
				(int) gameObject.getX() + offsetX,
				(int) gameObject.getY() + offsetY,
				width, height
				);
	}
	
}
