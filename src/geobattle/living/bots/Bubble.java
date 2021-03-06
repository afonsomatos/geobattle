package geobattle.living.bots;

import java.awt.Color;
import java.awt.Point;

import geobattle.collider.Collider;
import geobattle.collider.CollisionHandler;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.render.sprite.shapes.Rect;
import geobattle.special.StarBurstSpecial;
import geobattle.special.StarBurstSpecial.Style;
import geobattle.util.Counter;
import geobattle.util.Palette;
import geobattle.weapon.Projectile;

public class Bubble extends Bot {

	private static final Color COLOR = Palette.YELLOW;
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
	
	public Bubble(Game game) {
		super(game);
		
		setColor(COLOR);
		setHealth(HEALTH);

		starBurst = new StarBurstSpecial(game);
		starBurst.setStyle(Style.RAINBOW);
		starBurst.setProjectiles(8);
		starBurst.setDamage(25);
		
		getTriggerMap().add("newTag", () -> starBurst.setTag(getTag()));
		
		setWidth(10);
		setHeight(10);
		
		setupCollider();
		updateSprite();
	}

	public void updateSprite() {
		setSprite(new Rect((int)getWidth(), (int)getHeight(), getColor()));
		getCollider().surround(getSprite());
	}
	
	public void enlarge(double increase) {
		setWidth(getWidth() + increase);
		setHeight(getHeight() + increase);
		getCollider().surround(this);
		updateSprite();
	}

	private void setupCollider() {
		Bubble bubble = this;
		getCollider().addHandler(new CollisionHandler() {
			@Override
			public void handle(Collider other) {
				if (exploded) return;
				
				GameObject obj = other.getGameObject();
				if (obj instanceof Projectile) {
					Projectile p = (Projectile) obj;
					int damage = p.getDamage();

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

}
