package geobattle.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import geobattle.specials.Special;
import geobattle.specials.WaveSpecial;
import geobattle.util.Counter;
import geobattle.weapons.Arsenal;
import geobattle.weapons.Weapon;

public class Player extends AliveObject {
	
	private int shieldCapacity = 300;
	private int shield = 300;
	private boolean firing = false;
	
	private Arsenal arsenal = new Arsenal(4);
	private GameObject target = null;
	
	private Special special;
	private boolean specialReady = true;
	private Counter specialCounter = new Counter(0.01) {
		@Override
		public void fire() {
			specialReady = true;
		}
	};
	
	public Player(Game game) {
		this(game, 0, 0);
	}
	
	public Player(Game game, int x, int y) {
		super(game, x, y);
		
		setWidth(40);
		setHeight(40);
		setSpeed(4.0f);
		setColor(Color.CYAN);
		setHealth(1000);
		
		WaveSpecial waveSpecial = new WaveSpecial(game, Tag.Player);
		waveSpecial.setDamage(10000);
		special = waveSpecial;
		getCollider().setTag(Tag.Player);
	}
	
	@Override
	public void tick() {
		super.tick();

		if (!specialReady)
			specialCounter.tick();
		
		Weapon weapon = getWeapon();
		if (weapon != null)
			weapon.setLock(target);
		
		if (firing)
			fire();
	}
	
	public boolean isSpecialReady() {
		return specialReady;
	}
	
	public void fire() {
		Weapon weapon = getWeapon();
		if (weapon == null) return;
		if (weapon.getAmmoLoad() == 0)
			weapon.reload();
		else if (weapon.canFire())
			weapon.fire(target);
	}
	
	
	@Override
	public void suffer(int hit) {
		game.playerGotHit();
		shield -= hit;
		if (shield < 0) {
			super.suffer(- (int) shield);
			shield = 0;
		}
	}
	
	public void sendSpecial() {
		if (!specialReady) return;
		special.setPos(new Point((int)getX(), (int)getY()));
		special.send();
		specialReady = false;
	}
	
	public Arsenal getArsenal() {
		return arsenal;
	}
	
	public void setFiring(boolean firing) {
		this.firing = firing;
	}
	
	public Weapon getWeapon() {
		return arsenal.getSelectedWeapon();
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}

	public int giveShield(int givenShield) {
		final int given = Math.min(shieldCapacity - shield, givenShield);
		setShield(shield + given);
		return givenShield - given;
	}
	
	public void setShield(int shield) {
		if (shield > shieldCapacity)
			shieldCapacity = shield;
		
		this.shield = Math.max(shield, 0);
	}
	
	public int getShield() {
		return (int) shield;
	}
	
	@Override
	public void render(Graphics2D gfx) {
		super.render(gfx);
		if (this.shield > 0) {
			gfx.setColor(Color.BLUE);
			int thickness = 5;
			gfx.setStroke(new BasicStroke(thickness));
			
			Rectangle rect = (Rectangle) this.getCollider().getBounds();
			gfx.drawRect(rect.x + thickness / 2, rect.y + thickness / 2, rect.width - thickness, rect.height - thickness);
		}
		
	}

}
