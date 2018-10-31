package geobattle.living.bots;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.extension.Follower;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Square;
import geobattle.util.Palette;

public class Zombie extends Bot {

	private final static Sprite SPRITE_ALIVE 	= new Square(25, 25, Palette.PINK);
	private final static Sprite SPRITE_DEAD		= new Square(35, 35, Palette.OLIVE);
	
	private final static long ZOMBIFY_DELAY = 2000;
	
	private final static int HEALTH_ALIVE 	= 200;
	private final static int HEALTH_DEAD	= 300;
	
	private final static double SPEED_ALIVE = 1.0;
	private final static double SPEED_DEAD	= 6.0;
	
	private int damage = 200;
	
	private boolean zombie = false;
	private Follower follower;
	
	public Zombie(Game game, int x, int y) {
		super(game, x, y);
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
		follower.setMinDistance(100);
		setSpeed(SPEED_ALIVE);
		setSprite(SPRITE_ALIVE);
		setHealth(HEALTH_ALIVE);
		getCollider().surround(SPRITE_ALIVE);
	}
	
	private void zombify() {
		zombie = true;
		follower.setMinDistance(0);
		setSprite(SPRITE_DEAD);
		setHealth(HEALTH_DEAD);
		setSpeed(SPEED_DEAD);
		getCollider().surround(SPRITE_DEAD);
	}
	
}
