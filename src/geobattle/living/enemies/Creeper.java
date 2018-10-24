package geobattle.living.enemies;

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

public class Creeper extends Enemy {

	private static final Color COLOR = Palette.GREEN;
	private static final Sprite SPRITE = new Square(16, 16, COLOR);
	private static final double SPEED = 1.0;
	private static final int HEALTH = 400;
	
	private StarBurstSpecial starBurst;
	
	public Creeper(Game game, int x, int y, Living target) {
		super(game, x, y, target);
		
		starBurst = new StarBurstSpecial(game, Tag.Enemy);
		starBurst.setColor(getColor());
		starBurst.setProjectiles(20);
		
		setColor(COLOR);
		setSpeed(SPEED);
		setHealth(HEALTH);

		addExtension(new Follower(target));
		
		setSprite(SPRITE);
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
		getCollider().surround(SPRITE);
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
		
	}

	@Override
	protected void spawn() {
		
	}

	@Override
	protected void handleNewTarget(Living target) {
		// TODO Auto-generated method stub
		
	}
	
}
