package geobattle.living.enemies;

import java.awt.Graphics2D;

import geobattle.collider.Collider;
import geobattle.core.Empty;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Follower;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Cross;
import geobattle.schedule.Event;
import geobattle.util.Palette;
import geobattle.util.Util;

public class Slicer extends Enemy {
	
	private enum State {
		PURSUIT(0.1),
		REST(0.05),
		SLICING(0.3);
		
		private double rotationSpeed;
		
		// Angular velocity for each stage of its activity
		State(double rotationSpeed) {
			this.rotationSpeed = rotationSpeed;
		}
		
		double getRotationSpeed() {
			return rotationSpeed;
		}
	};
	
	private static final Sprite SPRITE = new Cross(50, 50, 5, Palette.ORANGE);
	
	private static final int HEALTH = 600;
	private static final double SPEED = 3.0;
		
	// Ignore targets over this distance
	private double radarDistance = 400;
		
	// Max distance from the target after an attack
	private int aimMaxError = 0;
	
	// Interval between attacks in (ms)
	private int attackDelay = 3000;
	private int attackDelayError = 500;
	private Event attackEvent;
			
	// Interval between slices (ms)
	private int sliceDelay = 100;

	// Damage it takes when slicing
	private int damage = 10;
	
	// Flag toggled between slices
	private boolean canSlice = true;
	
	private State state = State.REST;
	private Follower follower;
	
	public Slicer(Game game, int x, int y, Living target) {
		super(game, x, y, target);
		setSprite(SPRITE);
		setHealth(HEALTH);
		setSpeed(SPEED);
		setupCollider();
		setupAttackBehavior();
	}
	
	public void setupCollider() {
		Collider superCol = getCollider();
		Collider newCol = new Collider(this, Tag.Enemy) {

			@Override
			public void enterCollision(Collider other) {
				GameObject obj = other.getGameObject();
				GameObject target = Slicer.this.getTarget();
				if (obj != target) return;
				state = State.SLICING;
			}
			
			@Override
			public void leaveCollision(Collider other) {
				GameObject obj = other.getGameObject();
				GameObject target = Slicer.this.getTarget();
				if (obj != target) return;
				state = State.REST;
			}
			
			@Override
			public void handleCollision(Collider other) {
				superCol.handleCollision(other);
				if (!canSlice) return;
				
				GameObject obj = other.getGameObject();
				GameObject target = Slicer.this.getTarget();
				if (obj != target) return;
				
				Living alive = (Living) target;
				alive.suffer(damage);
				canSlice = false;
				game.getSchedule().next(sliceDelay, () -> canSlice = true);
			}
		};
		newCol.surround(SPRITE);
		setCollider(newCol);
	}
	
	public void setupAttackBehavior() {
		GameObject target = getTarget();
		GameObject aim = new Empty(game);
		
		// Perform pursuit every X random seconds
		attackEvent = new Event(Util.insertRandomError(attackDelay, attackDelayError), true, () -> {
			aim.setX(Util.insertRandomError(target.getX(), aimMaxError));
			aim.setY(Util.insertRandomError(target.getY(), aimMaxError));
			attackEvent.setDelay(Util.insertRandomError(attackDelay, attackDelayError));
			follower.setActive(true);
		});
		
		follower = new Follower(aim, 0, radarDistance);
		follower.setActive(false);
		follower.setReached(() -> {
			if (state != State.SLICING)
				state = State.REST;
			follower.setActive(false);
		});
		
		addExtension(follower);
	}
	
	@Override
	public void die() {
		
	}

	@Override
	protected void spawn() {
		game.getSchedule().add(attackEvent);
	}

	@Override
	protected void update() {
		if (follower.isActive() && state != State.SLICING)
			state = State.PURSUIT;
		setRotationSpeed(state.getRotationSpeed());
	}

	@Override
	protected void render(Graphics2D gfx) {
		
	}

}
