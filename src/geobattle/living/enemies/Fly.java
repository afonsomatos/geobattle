package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Cross;
import geobattle.render.sprite.shapes.Square;
import geobattle.util.Palette;
import geobattle.util.Util;

public class Fly extends Enemy {

	private final static int SIZE = 80;
	private final static int BODY_SIZE = 20;
	private final static Color COLOR = Palette.YELLOW;
	private final static Color WING_COLOR = Palette.BROWN;
	private final static Sprite SPRITE = new Sprite(SIZE, SIZE, SIZE/2, SIZE/2);
	
	private final static int HEALTH = 200;
	private final static double SPEED = 2.0;
	
	private double moveSpeed = Util.randomDouble(Math.PI / 60, Math.PI / 30);
	private double moveScale = Util.randomDouble(5, 10);
	
	private double delta = 0;
	private int damage = 50;
	
	private boolean canBite = true;
	private int biteDelay = 2000;
	
	static {
		// Draw wings and shell
		SPRITE.draw(new Cross(SIZE, SIZE, 1, WING_COLOR));
		SPRITE.draw(new Square(BODY_SIZE + 4, BODY_SIZE + 4, WING_COLOR));
		
		// Draw body
		SPRITE.draw(new Square(BODY_SIZE, BODY_SIZE, COLOR));
	}
	
	public Fly(Game game, int x, int y, Living target) {
		super(game, x, y, target);
		setSprite(SPRITE);
		setHealth(HEALTH);
		setColor(COLOR);
		setSpeed(SPEED);
		setWidth(BODY_SIZE);
		setHeight(BODY_SIZE);
		setupCollider();
	}
	
	private void setupCollider() {
		Collider superCol = getCollider();
		Collider newCol = new Collider(this, Tag.Enemy) {
			@Override
			public void handleCollision(Collider other) {
				// take damage
				superCol.handleCollision(other);
				GameObject obj = other.getGameObject();
				Living target = getTarget();
				if (obj != target || !canBite) return;
				// bite!
				target.suffer(damage);
				canBite = false;
				game.getSchedule().next(biteDelay, () -> { canBite = true; });
			}
		};
		newCol.surround(this);
		setCollider(newCol);
	}

	@Override
	public void die() {
		
	}

	@Override
	protected void spawn() {
		
	}

	@Override
	protected void update() {

		// Move him towards the target
		double angle = pointAngle(getTarget());
		double vx, vy;
		vx = Math.cos(angle) * getSpeed();
		vy = Math.sin(angle) * getSpeed();
		
		// Add figure8 movement
		delta += moveSpeed;
		delta %= Math.PI * 2;
		vx += -Math.sin(delta) * moveScale;
		vy += Math.cos(2 * delta) * moveScale;
		
		setVelX(vx);
		setVelY(vy);
	}

	@Override
	protected void render(Graphics2D gfx) {
		
	}

}
