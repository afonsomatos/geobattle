package geobattle.living;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.core.Game.State;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Square;
import geobattle.special.Special;
import geobattle.special.WaveSpecial;
import geobattle.util.Counter;
import geobattle.util.Tank;
import geobattle.weapon.Arsenal;
import geobattle.weapon.Weapon;

public class Player extends Living {
	
	public static Sprite sprite	= new Square(40, 40, Color.CYAN);
	public static Sprite shieldSprite = new Sprite(40, 40, 20, 20);
	
	static {
		shieldSprite.draw(0, 0, (Graphics2D gfx) -> {
			gfx.drawImage(sprite.getImage(), 0, 0, null);
			gfx.setColor(Color.BLUE);
			final int thickness = 5;
			gfx.setStroke(new BasicStroke(5));
			gfx.drawRect(thickness / 2, thickness / 2, 40 - thickness, 40 - thickness);
		});
	}

	private Tank shieldTank = new Tank(300);
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
		setHealth(100);
		
		WaveSpecial waveSpecial = new WaveSpecial(game, Tag.Player);
		waveSpecial.setDamage(10000);
		special = waveSpecial;
		
		setSprite(sprite);

		Collider col = getCollider();
		col.setTag(Tag.Player);
		col.surround(sprite);		
	}
	
	@Override
	public void update() {
		setSprite(shieldTank.get() > 0 ? shieldSprite : sprite);

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
		int remainder = shieldTank.take(hit);
		if (remainder > 0)
			super.suffer(remainder);
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

	public int giveShield(int shield) {
		return shieldTank.fill(shield);
	}
	
	public void setShield(int shield) {
		shieldTank.set(shield);
	}
	
	public int getShield() {
		return shieldTank.get();
	}
	
	@Override
	public void die() {
		game.setState(State.MENU);
	}

	@Override
	public void render(Graphics2D gfx) {
	}

	@Override
	protected void spawn() {
		// TODO Auto-generated method stub
		
	}

}
