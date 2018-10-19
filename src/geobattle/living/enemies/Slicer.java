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

public class Slicer extends Enemy {

	private double restRotationSpeed = 0.1;
	private double attackRotationSpeed = 0.4;
	
	private GameObject aim;
	private Event attackEvent;
	private static Sprite sprite = new Cross(50, 50, 5, Palette.ORANGE);
	
	private int damage = 10;
	private boolean canAttack = true;
	
	public Slicer(Game game, int x, int y, GameObject target) {
		super(game, x, y, target);
		setSprite(sprite);
		setHealth(600);
		setSpeed(10);
		setRotationSpeed(restRotationSpeed);
		
		Collider col = getCollider();
		setCollider(new Collider(this, Tag.Enemy) {
			@Override
			public void handleCollision(Collider other) {
				col.handleCollision(other);
				if (!canAttack) return;
				GameObject obj = other.getGameObject();
				if (obj instanceof Living) {
					((Living) obj).suffer(damage);
					canAttack = false;
					game.getSchedule().next(100, () -> canAttack = true);
				}
			}
		});
		getCollider().surround(sprite);
		
		aim = new Empty(game);
		aim.setActive(false);
		
		Follower follower = new Follower(aim);
		follower.setReached(() -> {
			setRotationSpeed(restRotationSpeed);
			aim.setActive(false);
		});
		addExtension(follower);
		
		attackEvent = new Event(5000, true, () -> {
			aim.setActive(true);
			aim.setX(target.getX());
			aim.setY(target.getY());
			setRotationSpeed(attackRotationSpeed);
		});

	}
	
	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void spawn() {
		game.getSchedule().add(attackEvent);
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void render(Graphics2D gfx) {
		// TODO Auto-generated method stub
		
	}

}
