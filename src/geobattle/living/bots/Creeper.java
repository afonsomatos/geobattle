package geobattle.living.bots;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Follower;
import geobattle.living.Living;
import geobattle.living.Player;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Square;
import geobattle.special.StarBurstSpecial;
import geobattle.util.Palette;

public class Creeper extends Bot {

	private static final Color COLOR = Palette.GREEN;
	private static final Sprite SPRITE = new Square(16, 16, COLOR);
	private static final double SPEED = 1.0;
	private static final int HEALTH = 400;
	
	private StarBurstSpecial starBurst;
	private Follower follower = new Follower(null);
	
	public Creeper(Game game, int x, int y) {
		super(game, x, y);
		
		starBurst = new StarBurstSpecial(game);
		starBurst.setColor(COLOR);
		starBurst.setProjectiles(20);
		
		setColor(COLOR);
		setSpeed(SPEED);
		setHealth(HEALTH);

		addExtension(follower);
		
		setSprite(SPRITE);
		setupCollider();
		
		getTriggerMap().add("newTarget", () -> {
			follower.setTarget(getTarget());
			
		});
		
	}
	
	private void setupCollider() {
		Collider superCol = getCollider();
		setCollider(new Collider(this) {
			@Override
			public void handleCollision(Collider other) {
				superCol.handleCollision(other);
				GameObject obj = other.getGameObject();
				if (getTarget() == obj)
					Creeper.this.explode();		
			}
		});
		getCollider().surround(SPRITE);
	}
	
	public void explode() {
		starBurst.setPos(new Point((int)getX(), (int)getY()));
		starBurst.setTag(getTag());
		starBurst.send();
		kill();
	}

}
