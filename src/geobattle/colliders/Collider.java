package geobattle.colliders;

import java.awt.Rectangle;

import geobattle.core.GameObject;
import geobattle.core.Tag;

public class Collider {

	private Tag tag;
	private GameObject gameObject = null;
	private boolean hey = false;
	
	private int width;
	private int height;
	
	private int offsetX = 0;
	private int offsetY = 0;
	
	public Collider(GameObject gameObject) {
		this(gameObject, Tag.Neutral);
	}

	public Collider(GameObject gameObject, Tag tag) {
		this.gameObject = gameObject;
		this.tag = tag;
	}
	
	public Collider(int width, int height, Tag tag) {
		this.width = width;
		this.height = height;
		this.tag = tag;
	}
	
	public void handleCollision(Collider other) {
		return;
	}
	
	public GameObject getGameObject() {
		return gameObject;
	}
	
	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	public void setWidth(int width) {
		this.width = width;
		hey = true;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public Rectangle getBounds() {
		if (!hey) {
			int width = gameObject.getWidth();
			int height = gameObject.getHeight();
			return new Rectangle(
					(int) (gameObject.getX()) - width / 2,
					(int) (gameObject.getY()) - height / 2,
					width, height);
		} else {
			
			
			return new Rectangle(
					(int) gameObject.getX() + offsetX,
					(int) gameObject.getY() + offsetY,
					width, height
					);
			
			
		}
	}
	
}
