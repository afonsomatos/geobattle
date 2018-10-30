package geobattle.living.bots;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.infection.Infection;
import geobattle.infection.InfectionFactory;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Cross;
import geobattle.render.sprite.shapes.Square;
import geobattle.util.Palette;
import geobattle.util.Util;

public class Fly extends Bot {

	private final static int SIZE = 60;
	private final static int BODY_SIZE = 15;
	private final static Color COLOR = Palette.YELLOW;
	private final static Color WING_COLOR = Palette.BROWN;
	private final static Sprite SPRITE = new Sprite(SIZE, SIZE, SIZE/2, SIZE/2);

	private final static InfectionFactory BITE = new InfectionFactory()
												.setColor(Palette.MAGENTA)
												.setDamage(10)
												.setHits(4)
												.setSpikes(3)
												.setDelay(500);
	
	private final static int HEALTH = 200;
	private final static double SPEED = 2.0;
	
	private double moveSpeed = Util.randomDouble(Math.PI / 60, Math.PI / 30);
	private double moveScale = Util.randomDouble(5, 10);
	
	private double delta = 0;
	private int damage = 50;
	
	private boolean canBite = true;
	private int biteDelay = 2000;
	
	private Infection lastInfection = null;
	
	static {
		// Draw wings and shell
		SPRITE.draw(new Cross(SIZE, SIZE, 1, WING_COLOR));
		SPRITE.draw(new Square(BODY_SIZE + 4, BODY_SIZE + 4, WING_COLOR));
		// Draw body
		SPRITE.draw(new Square(BODY_SIZE, BODY_SIZE, COLOR));
	}
	
	public Fly(Game game, int x, int y) {
		super(game, x, y);
		setSprite(SPRITE);
		setHealth(HEALTH);
		setColor(COLOR);
		setSpeed(SPEED);
		setWidth(BODY_SIZE);
		setHeight(BODY_SIZE);
		setupCollider();
	}
	
	private void bite() {
		if (!canBite) return;
		Living target = getTarget();
		// Remove last infection
		if (lastInfection != null && !lastInfection.isGone())
			lastInfection.destroy();
		// Spread new infection
		Infection infection = BITE.create(game, target);
		game.spawnGameObject(infection);
		lastInfection = infection;
		// Cause damage
		target.suffer(damage);
		canBite = false;
		game.getSchedule().next(biteDelay, () -> { canBite = true; });
	}
	
	private void setupCollider() {
		Collider superCol = getCollider();
		Collider newCol = new Collider(this) {
			@Override
			public void handleCollision(Collider other) {
				// Take damage
				superCol.handleCollision(other);
				GameObject obj = other.getGameObject();
				Living target = getTarget();
				if (obj != target) return;
				bite();
			}
		};
		newCol.surround(this);
		setCollider(newCol);
	}

	@Override
	protected void update() {

		// Figure8 movement
		delta += moveSpeed;
		delta %= Math.PI * 2;
		double vx = -Math.sin(delta) * moveScale;
		double vy = Math.cos(2 * delta) * moveScale;
		
		// Move him towards the target
		Living target = getTarget();
		if (target != null) {
			double angle = pointAngle(target);
			vx += Math.cos(angle) * getSpeed();
			vy += Math.sin(angle) * getSpeed();	
		}
		
		setVelX(vx);
		setVelY(vy);
	}

}
