package geobattle.enemies;

import java.awt.Color;
import java.awt.Point;

import geobattle.colliders.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Player;
import geobattle.core.Tag;
import geobattle.extensions.FollowExtension;
import geobattle.specials.StarBurstSpecial;

public class Creeper extends Enemy {

	private StarBurstSpecial starBurst;
	
	public Creeper(Game game, int x, int y, GameObject target) {
		super(game, x, y, target);
		
		starBurst = new StarBurstSpecial(game, Tag.Enemy);
		starBurst.setColor(Color.GREEN);
		starBurst.setProjectiles(20);
		
		addBehavior(new FollowExtension(target));
		
		setColor(Color.GREEN);
		setWidth(15);
		setHeight(15);
		setSpeed(1);
		setHealth(400);
		
		setupCollider();
	}
	
	private void setupCollider() {
		Collider superCol = this.getCollider();
		setCollider(new Collider(this, Tag.Enemy) {
			@Override
			public void handleCollision(Collider other) {
				superCol.handleCollision(other);
				GameObject obj = other.getGameObject();
				if (obj instanceof Player)
					Creeper.this.explode();		
			}
		});
	}
	
	public void explode() {
		starBurst.setPos(new Point((int)getX(), (int)getY()));
		starBurst.send();
		this.kill();
	}
	
}
