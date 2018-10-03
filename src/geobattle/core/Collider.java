package geobattle.core;

import java.awt.Rectangle;

public class Collider {

	private Tag tag;
	private GameObject gameObject;
	
	public Collider(GameObject gameObject) {
		this(gameObject, Tag.Neutral);
	}

	public Collider(GameObject gameObject, Tag tag) {
		this.gameObject = gameObject;
		this.tag = tag;
	}

	public void handleCollision(Collider other) {
		// TODO: Log collisions
	}
	
	public GameObject getGameObject() {
		return gameObject;
	}
	
	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	public Rectangle getBounds() {
		int width = gameObject.getWidth();
		int height = gameObject.getHeight();
		return new Rectangle(
				(int) (gameObject.getX()) - width / 2,
				(int) (gameObject.getY()) - height / 2,
				width, height);
	}
	
}
