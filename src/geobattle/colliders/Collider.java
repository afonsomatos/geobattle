package geobattle.colliders;

import java.awt.Rectangle;

import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.sprites.Sprite;

public class Collider {

	private Tag tag;
	private GameObject gameObject = null;
	
	private int width;
	private int height;
	
	private int offsetX = 0;
	private int offsetY = 0;
	
	public Collider(GameObject gameObject, Tag tag) {
		this(gameObject, gameObject.getWidth(), gameObject.getHeight(), tag);
	}

	public Collider(GameObject gameObject) {
		this(gameObject, Tag.Neutral);
	}
	
	public Collider(GameObject gameObject, int width, int height, Tag tag) {
		this.gameObject = gameObject;
		this.width = width;
		this.height = height;
		this.tag = tag;
	}
	
	public void handleCollision(Collider other) {
		return;
	}
	
	public void surround(Box box) {
		if (box == Box.SPRITE) {
			Sprite sprite = gameObject.getSpriteRenderer().getSprite();
			width = sprite.getWidth();
			height = sprite.getHeight();
			offsetX = -sprite.getCenterX();
			offsetY = -sprite.getCenterY();
		} else if (box == Box.OBJECT) {
			width = gameObject.getWidth();
			height = gameObject.getHeight();
			offsetX = -width/2;
			offsetY = -height/2;
		}
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
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(
				(int) gameObject.getX() + offsetX,
				(int) gameObject.getY() + offsetY,
				width, height
				);
	}
	
}
