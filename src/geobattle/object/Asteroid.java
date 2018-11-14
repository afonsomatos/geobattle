package geobattle.object;

import java.util.HashSet;
import java.util.Set;

import geobattle.collider.Collider;
import geobattle.collider.CollisionHandler;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Circle;
import geobattle.util.Palette;

public class Asteroid extends GameObject {

	private final static Sprite SPRITE = new Circle(10, Palette.GREY);
	
	// Time between strikes to each victim
	private int smashInterval = 200;
	
	// List of victims that have been recently attacked
	private Set<Living> stopAttack = new HashSet<Living>();
	
	private int damage = 100;
	
	public Asteroid(Game game) {
		super(game);
		setSprite(SPRITE);
		
		Collider col = makeCollider();
		col.surround(SPRITE);
		setCollider(col);
	}
	
	public void setSmashInterval(int smashInterval) {
		this.smashInterval = smashInterval;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	private Collider makeCollider() {
		Collider col = new Collider(this);
		col.addHandler(new CollisionHandler() {
			@Override
			public void handle(Collider other) {
				GameObject obj = other.getGameObject();
				if (!(obj instanceof Living)) return;
				Living living = (Living) obj;
				if (stopAttack.contains(living)) return;
				living.suffer(damage);
				stopAttack.add(living);
				game.getSchedule().next(smashInterval, () -> stopAttack.remove(living));
			}
		});
		return col;
	}

}
