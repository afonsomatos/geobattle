package geobattle.living.bots;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.extension.Follower;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Rect;
import geobattle.util.Interval;
import geobattle.util.Palette;

public class Zombie extends Bot {

	private final static Sprite SPRITE_ALIVE 	= new Rect(25, 25, Palette.PINK);
	private final static Sprite SPRITE_DEAD		= new Rect(35, 35, Palette.OLIVE);
	
	private final static long ZOMBIFY_DELAY = 2000;
	
	private final static int HEALTH_ALIVE 	= 200;
	private final static int HEALTH_DEAD	= 300;
	
	private final static double SPEED_ALIVE = 1.0;
	private final static double SPEED_DEAD	= 6.0;
	
	private final static Interval<Integer> RADAR_ALIVE = new Interval<>(100, null);
	private final static Interval<Integer> RADAR_DEAD = new Interval<>(null, null);
	
	private int damage = 200;
	
	private boolean zombie = false;
	private Follower follower;
	
	public Zombie(Game game) {
		super(game);
		getTriggerMap().clear("die");
		getTriggerMap().add("die", this::handleDeath);
		
		follower = new Follower(null);
		addController(follower);
		
		getTriggerMap().add("newTarget", () -> {
			follower.setTarget(getTarget());
		});
		
		setupCollider();
		humanoid();
	}

	
	public void setupCollider() {
		Collider superCol = getCollider();
		Collider newCol = new Collider(this) {

			@Override
			public void enterCollision(Collider other) {
				GameObject obj = other.getGameObject();
				Living target = Zombie.this.getTarget();
				if (obj != target) return;
				if (!zombie) return;
				target.suffer(damage);
				humanoid();
			}
			
			@Override
			public void handleCollision(Collider other) {
				superCol.handleCollision(other);
			}
			
		};
		setCollider(newCol);
	}
	private void handleDeath() {
		if (zombie)
			this.kill();
		else {
			setSpeed(0);
			game.getSchedule().next(ZOMBIFY_DELAY, () -> {
				if (!zombie) {
					zombify();
				}
			});
		}
	}
	
	private void humanoid() {
		zombie = false;
		follower.setRadar(RADAR_ALIVE);
		setSpeed(SPEED_ALIVE);
		setSprite(SPRITE_ALIVE);
		setHealth(HEALTH_ALIVE);
		getCollider().surround(SPRITE_ALIVE);
	}
	
	private void zombify() {
		zombie = true;
		follower.setRadar(RADAR_DEAD);
		setSprite(SPRITE_DEAD);
		setHealth(HEALTH_DEAD);
		setSpeed(SPEED_DEAD);
		getCollider().surround(SPRITE_DEAD);
	}
	
}
