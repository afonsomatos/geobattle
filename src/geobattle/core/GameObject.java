package geobattle.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import geobattle.behaviors.Behavior;

public class GameObject implements Renderable {

	private double width 	= 0;
	private double height 	= 0;
	private double x 		= 0;
	private double y 		= 0;
	private double speed 	= 0;
	private double velX		= 0;
	private double velY		= 0;
	
	private double accX = 0;
	private double accY = 0;

	private boolean active = true;
	private boolean hidden 	= false;
	private boolean freezed	= false;
	
	private Color color 	= Color.BLACK;
	private Tag tag			= Tag.Neutral;
	
	private Collider collider = null;
	private LinkedList<Behavior> behaviors = new LinkedList<Behavior>();

	protected Game game;
	
	public GameObject(Game game) {
		this(game, 0, 0);
	}
	
	public GameObject(Game game, double x, double y) {
		this.setGame(game);
		this.setX(x);
		this.setY(y);
	}

	public boolean isOutOfBorders() {
		return getX() > getGame().getWidth() || getY() > getGame().getHeight() || getX() < 0 || getY() < 0;
	}

	public void addBehavior(Behavior b) {
		behaviors.add(b);
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void render(Graphics2D gfx) {
		
		Collider col = this.getCollider();
		if (col != null) {
			gfx.setColor(this.getColor());
			gfx.fill(col.getBounds());
		}
		
	}
	
	public void tick() {
		
		for (Behavior b : behaviors)
			b.behave(this);
		
		velX += accX;
		velY += accY;
		
		this.move();
	}
	
	public void setCollider(Collider collider) {
		this.collider = collider;
	}

	public Collider getCollider() {
		return collider;
	}

	public void move() {
		if (!this.isFreezed()) {
			this.x += this.velX;
			this.y += this.velY;
		}
	}

	public boolean isFreezed() {
		return freezed;
	}

	public void setFreezed(boolean freezed) {
		this.freezed = freezed;
	}

	public double getX() {
		return x;
	}
	
	public double getAccX() {
		return accX;
	}

	public void setAccX(double accX) {
		this.accX = accX;
	}

	public double getAccY() {
		return accY;
	}

	public void setAccY(double accY) {
		this.accY = accY;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public double getVelX() {
		return velX;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}
	
	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public Tag getTag() {
		return this.tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public double getSpeed() {
		return this.speed;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int getWidth() {
		return (int) width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public int getHeight() {
		return (int) height;
	}
	
	public void kill() {
		this.getGame().killGameObject(this);
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public Game getGame() {
		return game;
	}

	private void setGame(Game game) {
		this.game = game;
	}
	
	public double distance(GameObject obj) {
		return Math.sqrt(
				Math.pow(obj.getX() - getX(), 2) +
				Math.pow(obj.getY() - getY(), 2)
				);
	}
	
	public void setDirection(double x, double y) {
		double diffX = x - this.getX();
		double diffY = y - this.getY();
		double distance = Math.sqrt(diffX * diffX + diffY * diffY);
		
		if (distance <= this.getSpeed()) {
			this.setVelX(0);
			this.setVelY(0);
		} else {
			this.setVelX(this.getSpeed() / distance * diffX);
			this.setVelY(this.getSpeed()/ distance * diffY);
		}
		
	}
	
	public void setDirection(GameObject obj) {
		setDirection(obj.getX(), obj.getY());
	}

}
