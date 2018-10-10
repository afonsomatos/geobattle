package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import geobattle.collider.Box;
import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.render.sprite.SolidSquare;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.SpriteRenderer;
import geobattle.special.StarBurstSpecial;
import geobattle.special.StarBurstSpecial.Style;
import geobattle.util.Counter;
import geobattle.util.Log;
import geobattle.weapon.projectile.Projectile;

public class Bubble extends Enemy {

	private StarBurstSpecial starBurst;
	private Counter attackCounter = new Counter(0.025, 1) {
		@Override
		public void fire() {
			starBurst.setPos(new Point((int)getX(), (int)getY()));
			starBurst.send();	
		}
	};

	private boolean exploded = false;
	private SpriteRenderer spriteRenderer;
	
	public Bubble(Game game, int x, int y) {
		super(game, x, y, null);
		
		starBurst = new StarBurstSpecial(game, Tag.Enemy);
		starBurst.setStyle(Style.RAINBOW);
		starBurst.setProjectiles(8);
		
		setWidth(10);
		setHeight(10);
		setSpeed(0.2);
		setHealth(300);
		
		setupCollider();
		
		spriteRenderer = new SpriteRenderer();
		getSpriteRendererList().add(spriteRenderer);
		updateSprite();
	}

	public void updateSprite() {
		spriteRenderer.setSprite(new SolidSquare(getWidth(), getHeight(), Color.YELLOW));
	}
	
	public void enlarge(double increase) {
		setWidth(getWidth() + increase);
		setHeight(getHeight() + increase);
		getCollider().surround(Box.OBJECT);
		updateSprite();
	}

	private void setupCollider() {
		Collider superCol = this.getCollider();
		Bubble bubble = this;
		setCollider(new Collider(this, Tag.Enemy) {
			@Override
			public void handleCollision(Collider other) {
				if (exploded) return;
				
				GameObject obj = other.getGameObject();
				if (obj instanceof Projectile) {
					Projectile p = (Projectile) obj;
					int damage = p.getDamage();

					// Suffer damage
					superCol.handleCollision(other);

					if (bubble.isDead()) {
						starBurst.setPos(new Point((int)getX(), (int)getY()));
						starBurst.setProjectiles(20);
						starBurst.send();
						exploded = true;
						return;
					}
					
					enlarge(damage/3.0);
				}
			};
		});
	}

	@Override
	public void update() {
		attackCounter.tick();
	}

	@Override
	public void die() {
		
	}

	@Override
	public void render(Graphics2D gfx) {
		
	}
	
}
