package geobattle.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import geobattle.collider.Collider;
import geobattle.extension.Controller;
import geobattle.render.Renderable;
import geobattle.render.sprite.Sprite;
import geobattle.triggers.TriggerMap;

public class GameObject {

	protected Game game;

	private double width 	= 0;
	private double height 	= 0;
	private double x 		= 0;
	private double y 		= 0;
	private double speed 	= 0;
	private double velX		= 0;
	private double velY		= 0;
	private double accX 	= 0;
	private double accY 	= 0;
	private double rotation			= 0;
	private double rotationSpeed 	= 0;
	
	private boolean active 	= true;
	private boolean hidden 	= false;
	private boolean spawned = false;
	private boolean freezed	= false;
	
	private Color color = Color.WHITE;
	private Tag tag		= Tag.Neutral;
	
	private Collider collider 	= null;
	private Sprite sprite		= null;
	
	private List<Renderable> drawers		= new ArrayList<Renderable>();
	private List<Controller> controllers	= new ArrayList<Controller>();

	private TriggerMap triggerMap = new TriggerMap();
	
	public GameObject(Game game) {
		this.game = game;
	}

	public void addDrawer(Renderable drawer) {
		drawers.add(drawer);
	}
	
	public void removeDrawer(Renderable drawer) {
		drawers.remove(drawer);
	}
	
	public void removeController(Controller controller) {
		controllers.remove(controller);
	}
	
	public void addController(Controller controller) {
		controllers.add(controller);
	}
	
	public TriggerMap getTriggerMap() {
		return triggerMap;
	}

	public boolean hasSpawned() {
		return spawned;
	}
	
	public void spawn() {
		spawned = true;
		getTriggerMap().call("spawn");
	}
	
	public boolean isOutOfBorders() {
		return isOutOfBorders(0);
	}
	
	public boolean isOutOfBorders(int margin) {
		return 	getX() > (getGame().getWidth()  - margin) 	||
				getY() > (getGame().getHeight() - margin) 	||
				getX() < margin 							||
				getY() < margin;
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation % (Math.PI * 2);
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void render(Graphics2D superGfx) {
		if (hidden) return;

		Graphics2D gfx;

		// Render sprites
		if (sprite != null) {
			gfx = (Graphics2D) superGfx.create();
			gfx.translate(x, y);
			gfx.rotate(rotation, 0, 0);
			sprite.render(gfx);
			gfx.dispose();
		}
		
		// Render all extra drawers
		for (Renderable drawer : drawers) {
			gfx = (Graphics2D) superGfx.create();
			drawer.render(gfx);
			gfx.dispose();
		}
		
	}
	
	public void update() {
		if (!active) return;
		
		// Act on all controllers
		for (Controller c : controllers)
			c.update(this);
		
		move();
	}
	
	public void stop() {
		// Stops all movement
		accX = accY = velX = velY = 0;
	}
	
	public void moveTo(Point point) {
		moveTo(point.x, point.y);
	}
	
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void moveTo(GameObject obj) {
		x = obj.x;
		y = obj.y;
	}
	
	public void setCollider(Collider collider) {
		this.collider = collider;
	}

	public Collider getCollider() {
		return collider;
	}
	
	public void invertRotation() {
		rotationSpeed *= -1;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public void setRotationSpeed(double rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	private void move() {
		if (freezed) return;
		rotation += rotationSpeed;
		rotation %= Math.PI * 2;
		x += (velX += accX);
		y += (velY += accY);
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
	
	public double getVel() {
		return Math.sqrt(velX * velX + velY * velY);
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
		getTriggerMap().call("newTag");
	}

	public double getSpeed() {
		return this.speed;
	}
	
	public double getY() {
		return y;
	}
	
	public Point getPos() {
		return new Point((int) x, (int) y);
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getWidth() {
		return width;
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

	public double getHeight() {
		return height;
	}
	
	public void kill() {
		getTriggerMap().call("kill");
		game.killGameObject(this);
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public Game getGame() {
		return game;
	}

	public double pointAngle(GameObject obj) {
		double distX = obj.getX() - getX();
		double fireAngle = Math.acos(distX /  distance(obj));
		
		if (getY() > obj.getY())
			fireAngle *= -1;
		
		return fireAngle;
	}
	
	public double distance(GameObject obj) {
		return Math.sqrt(
				Math.pow(obj.getX() - getX(), 2) +
				Math.pow(obj.getY() - getY(), 2)
				);
	}
	
	public void invertDirection() {
		velX *= -1;
		velY *= -1;
	}
	
	// TODO: Change this to point vectors
	public boolean setDirection(double x, double y) {
		double diffX = x - this.getX();
		double diffY = y - this.getY();
		double distance = Math.sqrt(diffX * diffX + diffY * diffY);
		
		if (distance <= this.getSpeed()) {
			this.setVelX(0);
			this.setVelY(0);
			return false;
		} else {
			this.setVelX(this.getSpeed() / distance * diffX);
			this.setVelY(this.getSpeed()/ distance * diffY);
			return true;
		}
		
	}
	
	public boolean setDirection(GameObject obj) {
		return setDirection(obj.getX(), obj.getY());
	}
	
}
