package geobattle.colliders;

import java.awt.Rectangle;

import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.sprites.Sprite;

public class Collider {

	private Tag tag;
	private GameObject gameObject = null;
	
	private int width = 0;
	private int height = 0;
	
	private int offsetX = 0;
	private int offsetY = 0;
	
	public Collider(GameObject gameObject) {
		this(gameObject, Tag.Neutral);
	}
	
	public Collider(GameObject gameObject, Tag tag) {
		this(gameObject, tag, Box.OBJECT);
	}
	
	public Collider(GameObject gameObject, Tag tag, Box box) {
		this.gameObject = gameObject;
		this.tag = tag;
		surround(box);
	}

	public void handleCollision(Collider other) {
		
	}
	
	public void surround(Box box) {
		switch (box) {
			case SPRITE:
				Sprite sprite = gameObject.getSpriteRenderer().getSprite();
				width = sprite.getWidth();
				height = sprite.getHeight();
				offsetX = -sprite.getCenterX();
				offsetY = -sprite.getCenterY();
				break;
			case OBJECT:
				width = gameObject.getWidth();
				height = gameObject.getHeight();
				offsetX = -width/2;
				offsetY = -height/2;
				break;
		}
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

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(
				(int) gameObject.getX() + offsetX,
				(int) gameObject.getY() + offsetY,
				width, height
				);
	}
	
}
