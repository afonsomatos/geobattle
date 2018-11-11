package geobattle.living.bots;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Shooter;
import geobattle.living.Living;
import geobattle.living.WeaponHolder;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Rect;
import geobattle.util.Interval;
import geobattle.util.Palette;
import geobattle.weapon.Weapon;
import geobattle.weapon.WeaponFactory;

public class Tower extends Bot implements WeaponHolder {
	
	private final static Color COLOR = Palette.PINK;
	private final static Sprite SPRITE = new Rect(40, 40, COLOR);
	private final static int HEALTH = 400;
	
	private final static Interval<Integer> SHOOT_DELAY = new Interval<Integer>(1000, 2000); 
	private final static Interval<Integer> SHOOT_RADAR = new Interval<Integer>(0, 300);
	
	private Shooter shooter;
	private Weapon weapon;
	
	public Tower(Game game, int x, int y) {
		super(game, x, y);
	
		setColor(COLOR);
		setHealth(HEALTH);
		setSpeed(0);

		weapon = WeaponFactory.SNIPER.create(game, this, Tag.Enemy);
		
		shooter = new Shooter(weapon);
		shooter.setDelay(SHOOT_DELAY);
		shooter.setRadar(SHOOT_RADAR);
		addController(shooter);
		
		setSprite(SPRITE);
		getCollider().surround(SPRITE);
		
		getTriggerMap().add("kill", weapon::kill);
		getTriggerMap().add("newTarget", this::targetLock);
		getTriggerMap().add("spawn", this::spawnWeapon);
	}
	
	private void targetLock() {
		Living target = getTarget();
		shooter.setTarget(target);
		weapon.setLock(target);
	}
	

	private void spawnWeapon() {
		game.spawnGameObject(weapon);
	}

	@Override
	public Weapon getWeapon() {
		return weapon;
	}

}
