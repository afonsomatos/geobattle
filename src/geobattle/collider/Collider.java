package geobattle.collider;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.render.sprite.Sprite;

public class Collider {

	private GameObject gameObject;
	
	private int width = 0;
	private int height = 0;

	private int offsetX = 0;
	private int offsetY = 0;
	
	private Set<Collider> currentCollisions = new HashSet<Collider>();
	
	public Collider(GameObject gameObject) {
		this.gameObject = gameObject;
	}

	void updateCollisions(Set<Collider> updatedCollisions) {
		
		// Check gained collisions
		updatedCollisions
			.stream()
			.filter(b -> !currentCollisions.contains(b))
			.forEach(this::enterCollision);
		
		// Check lost collisions
		currentCollisions
			.stream()
			.filter(a -> !updatedCollisions.contains(a))
			.forEach(this::leaveCollision);
		
		currentCollisions = updatedCollisions;		
		updatedCollisions.forEach(this::handleCollision);
	}
	
	public void enterCollision(Collider other) 		{}
	public void leaveCollision(Collider other) 		{}
	public void handleCollision(Collider other) 	{}
	
	public void surround(Sprite sprite) {
		width = sprite.getWidth();
		height = sprite.getHeight();
		offsetX = -sprite.getCenterX();
		offsetY = -sprite.getCenterY();
	}
	
	public void surround(GameObject gameObject) {
		width = gameObject.getWidth();
		height = gameObject.getHeight();
		offsetX = -width/2;
		offsetY = -height/2;
	}
	
	public GameObject getGameObject() {
		return gameObject;
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
