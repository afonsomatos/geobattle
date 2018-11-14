package geobattle.living.bots;

import java.awt.Color;
import java.awt.Point;

import geobattle.collider.Collider;
import geobattle.collider.CollisionHandler;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.extension.Follower;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Rect;
import geobattle.special.StarBurstSpecial;
import geobattle.util.Palette;

public class Creeper extends Bot {

	private static final Color COLOR = Palette.GREEN;
	private static final Sprite SPRITE = new Rect(16, 16, COLOR);
	private static final double SPEED = 1.0;
	private static final int HEALTH = 400;
	
	private StarBurstSpecial starBurst;
	private Follower follower = new Follower(null);
	
	public Creeper(Game game) {
		super(game);
		
		starBurst = new StarBurstSpecial(game);
		starBurst.setColor(COLOR);
		starBurst.setProjectiles(20);
		
		setColor(COLOR);
		setSpeed(SPEED);
		setHealth(HEALTH);

		addController(follower);
		
		setSprite(SPRITE);
		setupCollider();
		
		getTriggerMap().add("newTarget", () -> {
			follower.setTarget(getTarget());
		});
		
	}
	
	private void setupCollider() {
		Collider col = getCollider();
		col.addHandler(new CollisionHandler() {
			@Override
			public void handle(Collider other) {
				GameObject obj = other.getGameObject();
				if (getTarget() == obj)
					Creeper.this.explode();		
			}
		});
		col.surround(SPRITE);
	}
	
	public void explode() {
		starBurst.setPos(new Point((int)getX(), (int)getY()));
		starBurst.setTag(getTag());
		starBurst.send();
		kill();
	}

}
