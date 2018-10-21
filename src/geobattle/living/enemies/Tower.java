package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.extension.Shooter;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Square;
import geobattle.util.Interval;
import geobattle.util.Palette;
import geobattle.weapon.Weapon;
import geobattle.weapon.WeaponFactory;

public class Tower extends Enemy {
	
	private final static Color COLOR = Palette.PINK;
	private final static Sprite SPRITE = new Square(40, 40, COLOR);
	private final static int HEALTH = 400;
	private final static Interval<Integer> SHOOT_DELAY = new Interval<Integer>(1000, 2000); 
	
	private Weapon weapon;
	
	public Tower(Game game, int x, int y, Living target) {
		super(game, x, y, target);
	
		setColor(COLOR);
		setHealth(HEALTH);
		setSpeed(0);

		weapon = WeaponFactory.Sniper.create(game, this, Tag.Enemy);
		weapon.setLock(target);
		
		Shooter shooter = new Shooter(target, weapon);
		shooter.setDelay(SHOOT_DELAY);
		addExtension(shooter);
		
		setSprite(SPRITE);
		getCollider().surround(SPRITE);
	}
	
	@Override
	public void kill() {
		super.kill();
		weapon.kill();
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
		game.spawnGameObject(weapon);
	}
	
}
