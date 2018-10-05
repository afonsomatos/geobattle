package geobattle.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.util.Counter;
import geobattle.util.Util;

public class Weapon extends GameObject {
	
	private boolean reloading = false;
	private boolean pausing = false;	// Is in middle of firing bullets
	
	public static final int MAX_SPEED = 1;
	public static final int INFINITE_AMMO = Integer.MAX_VALUE;

	private double recoil = 0;
	
	private Counter reloadCounter;
	private Counter fireCounter;
	
	private int ammoLoad = 0;
	private int ammoCapacity = INFINITE_AMMO;
	private int ammoSaved = INFINITE_AMMO;
	
	private int damage = 10;
	private int projectileSize = 8;
	private double projectileSpeed = 2.0f;
	private Color projectileColor = Color.CYAN;
	
	private GameObject lock = null;		// Where bullets will go
	private GameObject origin = null;	// Who is holding the weapon
	
	private int shotsFired = 1;
	private double fireAmplitude = Math.PI / 4;
	
	private double radius = 70;		// Pixels between weapon and origin
	private double fireAngle = 0;	// Position is calculated with cos/sin(fireAngle) * radius
	
	public Weapon(Game game, GameObject origin, Tag tag) {
		super(game);
		this.origin = origin;
		
		setTag(tag);
		setWidth(15);
		setHeight(15);
		fill();
		setCollider(new Collider(this));
		
		setupCounters();
		
		game.spawnGameObject(this);
	}
	
	public void setupCounters() {
		// Handle reloading delay and effect
		reloadCounter = new Counter(0.3) {
			@Override
			public void fire() {
				reloading = false;
				
				int addedLoad = ammoCapacity - ammoLoad;
				if (ammoSaved != INFINITE_AMMO) {
					addedLoad = Math.min(ammoSaved, addedLoad);
					ammoSaved -= addedLoad;
				}
				
				ammoLoad += addedLoad;	
			}
		};
		
		// Handle firing delay
		fireCounter = new Counter(0.3) {
			@Override
			public void fire() {
				pausing = false;
			}
		};
	}
	
	public void setRecoil(double recoil) {
		this.recoil = recoil;
	}
	
	protected void draw(Graphics2D gfx) {
		// (0, 0) is the firing point
		int side = 10;
		
		int x[] = {0, side, side};
		int y[] = {0, side, -side};
		
		gfx.setColor(getColor());
		gfx.fillPolygon(x, y, 3);
	}
	
	@Override
	public void render(Graphics2D gfx) {
		Graphics2D g = (Graphics2D) gfx.create();
		g.rotate(fireAngle, (int) getX(), (int) getY());
		g.translate(getX(), getY());
		draw(g);
		g.dispose();
	}
	
	public void fill() {
		ammoLoad = ammoCapacity;
	}
	
	public GameObject getLock() {
		return lock;
	}
	
	public boolean isReloading() {
		return this.reloading;
	}

	public int getAmmoSaved() {
		return ammoSaved;
	}
	
	public double getFireAngle() {
		return fireAngle;
	}

	public void setFireAngle(double fireAngle) {
		this.fireAngle = fireAngle;
	}

	public void setAmmoSaved(int ammoSaved) {
		this.ammoSaved = ammoSaved;
	}
	
	public void setProjectileSize(int projectileSize) {
		this.projectileSize = projectileSize;
	}

	public void reload() {
		if (ammoLoad != INFINITE_AMMO && ammoSaved != 0 && ammoLoad < ammoCapacity)
			reloading = true;
	}
	
	public void setFireSpeed(double fireSpeed) {
		fireCounter.setStep(fireSpeed);
	}

	public int getAmmoCapacity() {
		return ammoCapacity;
	}

	public int getAmmoLoad() {
		return ammoLoad;
	}

	public void setAmmoLoad(int ammoLoad) {
		this.ammoLoad = ammoLoad;
	}

	public void setAmmoCapacity(int ammoCapacity) {
		this.ammoCapacity = ammoCapacity;
	}
	
	public void setShotsFired(int shotsFired) {
		this.shotsFired = shotsFired;
	}
	
	public void tickFiring() {
		if (ammoLoad <= 0 && ammoLoad != INFINITE_AMMO)
			return;
	}

	public void tickReloading() {
		if (!reloading) return;
		reloadCounter.tick();
	}
	
	public void updatePosition() {
		if (origin == null || lock == null)
			return;
		
		if (origin.distance(lock) > radius) {

			double distY = origin.getY() - lock.getY();
			double distX = origin.getX() - lock.getX();
			double dist = origin.distance(lock);

			double dy = radius * distY / dist;
			double dx = radius * distX / dist;
			
			setX(origin.getX() - dx);
			setY(origin.getY() - dy);
			
			fireAngle = Math.acos(
					Util.clamp(-1, (origin.getX() - getX()) / radius, 1));

			if (getY() > origin.getY())
				fireAngle *= -1;
			
		} else {
			setX(origin.getX() + Math.cos(fireAngle + Math.PI) * radius);
			setY(origin.getY() + Math.sin(fireAngle + Math.PI) * radius);
		}
	}
	
	public void tick() {
		super.tick();
		
		if (pausing)
			fireCounter.tick();
		
		if (reloading)
			reloadCounter.tick();
		
		updatePosition();
		tickReloading();
		tickFiring();
	}

	public void setLock(GameObject lock) {
		this.lock = lock;
	}

	public void setOrigin(GameObject origin) {
		this.origin = origin;
	}

	public void setReloadSpeed(double reloadSpeed) {
		reloadCounter.setStep(reloadSpeed);
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public void setFireAmplitude(double fireAmplitude) {
		this.fireAmplitude = Util.clamp(0, fireAmplitude, Math.PI * 2);
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void setProjectileSpeed(double projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
	}

	public void setProjectileColor(Color projectileColor) {
		this.projectileColor = projectileColor;
	}
	
	public boolean canFire() {
		return !reloading && !pausing &&
				(ammoLoad > 0 || ammoSaved > 0 || ammoLoad == INFINITE_AMMO);
	}
	
	public void fire(GameObject obj) {
		this.fire((int)obj.getX(), (int)obj.getY());
	}
	
	public void fire(int x, int y) {
		Game game = this.getGame();
		
		final double step = fireAmplitude / (shotsFired - 1);

		for (int i = 0; i < shotsFired; ++i) {
			
			Projectile p = new Bullet(game, (int) this.getX(), (int) this.getY());
			p.getCollider().setTag(getTag());
			p.setSpeed(projectileSpeed);
			p.setColor(projectileColor);
			p.setDamage(damage);
			p.setWidth(projectileSize);
			p.setHeight(projectileSize);
			
			final double fireRecoil = Util.randomDouble(-recoil, recoil);
			final double delta = fireRecoil + (shotsFired == 1 ? 0 : step * i - fireAmplitude / 2);
			p.setVelX(-Math.cos(fireAngle + delta) * p.getSpeed());
			p.setVelY(-Math.sin(fireAngle + delta) * p.getSpeed());
		
			game.spawnGameObject(p);
		}

		pausing = true;
		if (ammoLoad != INFINITE_AMMO)
			ammoLoad--;
	}
	
}