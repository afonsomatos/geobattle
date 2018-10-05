package geobattle.enemies;

import java.awt.Color;
import java.awt.Point;

import geobattle.core.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.specials.StarBurstSpecial;
import geobattle.specials.StarBurstSpecial.Style;
import geobattle.util.Counter;
import geobattle.weapons.Projectile;

public class Bubble extends Enemy {

	private StarBurstSpecial starBurst;
	private Counter attackCounter;

	private boolean exploded = false;
	
	public Bubble(Game game, int x, int y) {
		super(game, x, y, null);
		
		starBurst = new StarBurstSpecial(game, Tag.Enemy);
		starBurst.setStyle(Style.RAINBOW);
		starBurst.setProjectiles(8);
		
		setWidth(10);
		setHeight(10);
		setSpeed(0.2);
		setColor(Color.YELLOW);
		setHealth(300);
		
		setupAttack();
		setupCollider();
	}

	public void setupAttack() {
		attackCounter = new Counter(0.025, 1) {
			@Override
			public void fire() {
				starBurst.setPos(new Point((int)getX(), (int)getY()));
				starBurst.send();	
			}
		};
	}
	
	private void setupCollider() {
		Collider superCol = this.getCollider();
		Bubble bubble = this;
		setCollider(new Collider(this, Tag.Enemy) {
			@Override
			public void handleCollision(Collider other) {
				superCol.handleCollision(other);
				if (exploded) return;
				
				GameObject obj = other.getGameObject();
				if (obj instanceof Projectile) {
					Projectile p = (Projectile) obj;
					int damage = p.getDamage();
					bubble.setWidth(bubble.getWidth() + damage / 3);
					bubble.setHeight(bubble.getHeight() + damage / 3);
								
					if (bubble.isDead()) {
						starBurst.setPos(new Point((int)getX(), (int)getY()));
						starBurst.setProjectiles(20);
						starBurst.send();
						exploded = true;
					}
				}
			};
		});
	}

	@Override
	public void tick() {
		super.tick();
		attackCounter.tick();
	}
	
}
