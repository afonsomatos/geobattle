package geobattle.living.bots;

import java.awt.Color;
import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.extension.Follower;
import geobattle.extension.Shooter;
import geobattle.living.Living;
import geobattle.living.WeaponHolder;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Rect;
import geobattle.util.Interval;
import geobattle.util.Palette;
import geobattle.weapon.Weapon;
import geobattle.weapon.WeaponFactory;

public class Soldier extends Bot implements WeaponHolder {

	private final static Color COLOR = Palette.RED;
	private final static Sprite SPRITE = new Rect(24, 24, COLOR);
	private final static int HEALTH = 100;
	private final static double SPEED = 1.0;
	private final static Interval<Integer> SHOOT_DELAY = new Interval<Integer>(1000, 2000); 
	
	private final static Interval<Integer> RADAR = new Interval<Integer>(150, null);
	
	private Shooter shooter;
	private Follower follower;

	private Weapon weapon;

	public Soldier(Game game) {
		super(game);
		
		setColor(COLOR);
		setSpeed(SPEED);
		setHealth(HEALTH);
		
		weapon = WeaponFactory.RIFLE.create(game, this, Tag.Enemy);
		
		shooter = new Shooter(weapon);
		shooter.setDelay(SHOOT_DELAY);
		
		follower = new Follower();
		follower.setRadar(RADAR);
		addController(shooter);
		addController(follower);
		
		setSprite(SPRITE);
		getCollider().surround(SPRITE);
		
		getTriggerMap().add("kill", weapon::kill);
		getTriggerMap().add("spawn", () -> {
			game.spawnGameObject(weapon);
		});
		
		getTriggerMap().add("newTarget", () -> {
			Living newTarget = getTarget();
			follower.setTarget(newTarget);
			shooter.setTarget(newTarget);
			weapon.setLock(newTarget);
		});
		
	}

	@Override
	public Weapon getWeapon() {
		return weapon;
	}
	
}
