package geobattle.living.bots;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Follower;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Cross;
import geobattle.schedule.Event;
import geobattle.util.Log;
import geobattle.util.Palette;
import geobattle.util.Util;

public class Slicer extends Bot {
	
	private enum State {
		PURSUIT(0.2),
		REST(0.05),
		SLICING(0.3);
		
		public final double rotationSpeed;
		
		// Angular velocity for each stage of its activity
		State(double rotationSpeed) {
			this.rotationSpeed = rotationSpeed;
		}
		
	};

	private static final Color COLOR = Palette.ORANGE;
	private static final Sprite SPRITE = new Cross(50, 50, 5, COLOR);
	
	private static final int HEALTH = 600;
	private static final double SPEED = 3.0;
		
	// Ignore targets over this distance
	private double radarDistance = 400;
		
	// Max distance from the target after an attack
	private int aimMaxError = 30;
	
	// Interval between attacks in (ms)
	private int attackDelay = 1000;
	private int attackDelayError = 500;
	private Event attackEvent;
			
	// Interval between slices (ms)
	private int sliceDelay = 100;

	// Damage it takes when slicing
	private int damage = 20;
	
	// Flag toggled between slices
	private boolean canSlice = true;
	
	private State state;
	private Follower follower;
	
	public Slicer(Game game, int x, int y) {
		super(game, x, y);
		setSprite(SPRITE);
		setHealth(HEALTH);
		setSpeed(SPEED);
		setColor(COLOR);
		setState(State.REST);
		setupCollider();
		setupAttackBehavior();
		
		getTriggerMap().add("spawn", () -> {
			game.getSchedule().add(attackEvent);
		});
	}
	
	private void setState(State state) {
		this.state = state;
		setRotationSpeed(state.rotationSpeed);
	}
	
	public void setupCollider() {
		Collider superCol = getCollider();
		Collider newCol = new Collider(this) {

			@Override
			public void enterCollision(Collider other) {
				GameObject obj = other.getGameObject();
				Living target = Slicer.this.getTarget();
				if (obj != target) return;
				setState(State.SLICING);
			}
			
			@Override
			public void leaveCollision(Collider other) {
				GameObject obj = other.getGameObject();
				Living target = Slicer.this.getTarget();
				if (obj != target) return;
				setState(State.REST);
			}
			
			@Override
			public void handleCollision(Collider other) {
				superCol.handleCollision(other);
				if (!canSlice) return;
				
				GameObject obj = other.getGameObject();
				Living target = Slicer.this.getTarget();
				if (obj != target) return;
				
				target.suffer(damage);
				canSlice = false;
				game.getSchedule().next(sliceDelay, () -> canSlice = true);
			}
		};
		newCol.surround(SPRITE);
		setCollider(newCol);
	}
	
	public void setupAttackBehavior() {
		GameObject aim = new GameObject(game);
		
		// Perform pursuit every X random seconds
		attackEvent = new Event(Util.insertRandomError(attackDelay, attackDelayError), true, () -> {
			// No need to move if he's already slicing
			if (state == State.SLICING)
				return;
			
			Living target = getTarget();
			if (target == null)
				return;
			
			aim.setX(Util.insertRandomError(target.getX(), aimMaxError));
			aim.setY(Util.insertRandomError(target.getY(), aimMaxError));
			attackEvent.setDelay(Util.insertRandomError(attackDelay, attackDelayError));
			follower.setActive(true);
		});
		
		follower = new Follower(aim, 0, radarDistance);
		follower.setActive(false);
		follower.setReached(() -> {
			if (state != State.SLICING)
				setState(State.REST);
			follower.setActive(false);
		});
		
		addExtension(follower);
	}

	@Override
	protected void update() {
		if (follower.isActive() && state != State.SLICING)
			setState(State.PURSUIT);
	}

}
