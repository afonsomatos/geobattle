package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.render.sprite.shapes.Square;
import geobattle.special.StarBurstSpecial;
import geobattle.special.StarBurstSpecial.Style;
import geobattle.util.Counter;
import geobattle.util.Palette;
import geobattle.weapon.projectile.Projectile;

public class Bubble extends Enemy {

	private final static int HEALTH = 500;
	
	private StarBurstSpecial starBurst;
	private Counter attackCounter = new Counter(0.010, 1) {
		@Override
		public void fire() {
			starBurst.setPos(new Point((int)getX(), (int)getY()));
			starBurst.send();	
		}
	};

	private boolean exploded = false;
	
	public Bubble(Game game, int x, int y) {
		super(game, x, y, null);
		
		setHealth(HEALTH);

		starBurst = new StarBurstSpecial(game, Tag.Enemy);
		starBurst.setStyle(Style.RAINBOW);
		starBurst.setProjectiles(8);
		starBurst.setDamage(25);
		setColor(Palette.YELLOW);
		
		setWidth(10);
		setHeight(10);
		
		setupCollider();
		updateSprite();
	}

	public void updateSprite() {
		setSprite(new Square(getWidth(), getHeight(), getColor()));
		getCollider().surround(getSprite());
	}
	
	public void enlarge(double increase) {
		setWidth(getWidth() + increase);
		setHeight(getHeight() + increase);
		getCollider().surround(this);
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

	@Override
	protected void spawn() {
		// TODO Auto-generated method stub
		
	}
	
}
