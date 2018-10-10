package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import geobattle.collider.Box;
import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Follower;
import geobattle.living.Player;
import geobattle.render.sprite.SolidSquare;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.SpriteRenderer;
import geobattle.special.StarBurstSpecial;

public class Creeper extends Enemy {

	public static Sprite sprite = new SolidSquare(16, 16, Color.GREEN);

	private StarBurstSpecial starBurst;
	
	public Creeper(Game game, int x, int y, GameObject target) {
		super(game, x, y, target);
		
		starBurst = new StarBurstSpecial(game, Tag.Enemy);
		starBurst.setColor(Color.GREEN);
		starBurst.setProjectiles(20);
		
		setSpeed(1);
		setHealth(400);

		addExtension(new Follower(target));
		
		getSpriteRendererList().add(new SpriteRenderer(sprite));
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

	@Override
	public void update() {
		
	}

	@Override
	public void die() {
		
	}

	@Override
	public void render(Graphics2D gfx) {
		// TODO Auto-generated method stub
		
	}
	
}
