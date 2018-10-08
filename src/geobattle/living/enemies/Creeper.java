package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Point;

import geobattle.collider.Box;
import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.FollowExtension;
import geobattle.living.Player;
import geobattle.special.StarBurstSpecial;
import geobattle.sprite.SpriteMap;

public class Creeper extends Enemy {

	private StarBurstSpecial starBurst;
	
	public Creeper(Game game, int x, int y, GameObject target) {
		super(game, x, y, target);
		
		starBurst = new StarBurstSpecial(game, Tag.Enemy);
		starBurst.setColor(Color.GREEN);
		starBurst.setProjectiles(20);
		
		addBehavior(new FollowExtension(target));
		
		setSpeed(1);
		setHealth(400);

		getSpriteRenderer().setSprite(SpriteMap.CREEPER);
		setupCollider();
	}
	
	private void setupCollider() {
		Collider superCol = this.getCollider();
		setCollider(new Collider(this, Tag.Enemy, Box.SPRITE) {
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