package geobattle.weapon;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.infection.InfectionFactory;
import geobattle.render.Renderable;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Circle;
import geobattle.render.sprite.shapes.Rect;
import geobattle.render.sprite.shapes.Triangle;
import geobattle.util.Counter;
import geobattle.util.Log;
import geobattle.util.Tank;
import geobattle.util.Util;

public class Weapon extends GameObject {
	
	private Sprite sprite;
	
	private boolean reloading = false;
	private boolean pausing = false;
	
	public static final int MAX_SPEED = 1;
	public static final int INFINITE_AMMO = Integer.MAX_VALUE;
	
	private Counter reloadCounter;
	private Counter fireCounter;

	private Tank loadTank = new Tank();
	private Tank ammoTank = new Tank();
	
	private ProjectileFactory projectileFactory = null;
	
	private int size				= 10;
	private int projectiles 		= 1;
	
	private GameObject lock = null;
	private GameObject origin = null;
	
	private double fireAmplitude = Math.PI / 4;
	private double radius = 70;
	private double fireAngle = 32;
	private double recoil = 0;
	private int padding = 0;
	
	private Renderable drawer;
	
	Weapon(Game game) {
		this(game, null, Tag.Neutral);
	}
	
	Weapon(Game game, GameObject origin, Tag tag) {
		super(game);
		this.origin = origin;
		
		paint();

		setTag(tag);
		setWidth(15);
		setHeight(15);
		fill();
		
		setupCounters();
		
		addController(this::update);
	}
	
	public void paint() {
		sprite = new Sprite(size * 2, size * 2, size, size);
		sprite.draw((Graphics2D superGfx) -> {
			Graphics2D gfx = (Graphics2D) superGfx.create();
			gfx.setColor(getColor());
			// (0, 0) is the firing point
			final int x[] = {0, size, size};
			final int y[] = {0, size, -size};
			gfx.translate(0, 0);
			gfx.fillPolygon(x, y, 3);
			gfx.dispose();
		});
		sprite.setCenterX(padding);
		setSprite(sprite);
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
	}
	
	public void setupCounters() {
		// Handle reloading delay and effect
		reloadCounter = new Counter(0.3) {
			@Override
			public void fire() {
				reloading = false;
				int newLoad = loadTank.free();
				
				if (ammoTank.get() != INFINITE_AMMO)
					newLoad -= ammoTank.take(newLoad);
				
				loadTank.fill(newLoad);
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
	
	public void setProjectileFactory(ProjectileFactory projectileFactory) {
		this.projectileFactory = projectileFactory;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setRecoil(double recoil) {
		this.recoil = recoil;
	}
	
	public void setDrawer(Renderable drawer) {
		this.drawer = drawer;
	}
	
	public void fill() {
		loadTank.fill();
	}
	
	public GameObject getLock() {
		return lock;
	}
	
	public boolean isReloading() {
		return this.reloading;
	}

	public int getAmmoSaved() {
		return ammoTank.get();
	}
	
	public double getFireAngle() {
		return fireAngle;
	}

	public void setFireAngle(double fireAngle) {
		this.fireAngle = fireAngle;
	}

	public int fillAmmo(int ammo) {
		return ammoTank.fill(ammo);
	}
	
	public void setAmmoSaved(int ammo) {
		ammoTank.set(ammo);
	}
	
	public void reload() {
		if (loadTank.get() != INFINITE_AMMO && ammoTank.get() != 0 && !loadTank.full())
			reloading = true;
	}
	
	public void setFireSpeed(double fireSpeed) {
		fireCounter.setStep(fireSpeed);
	}

	public int getAmmoCapacity() {
		return loadTank.max();
	}

	public int getAmmoLoad() {
		return loadTank.get();
	}
	
	public void setAmmoCapacity(int capacity) {
		loadTank.setMax(capacity);
	}

	public void setAmmoLoad(int ammoLoad) {
		loadTank.set(ammoLoad);
	}
	
	public void setProjectiles(int projectiles) {
		this.projectiles = projectiles;
	}
	
	public void tickReloading() {
		if (!reloading) return;
		reloadCounter.tick();
	}
	
	public void updatePosition() {
		if (origin == null)		
			return;
		
		if (lock != null && origin.distance(lock) > radius) {

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
	
	private void update(GameObject gameObject) {
		
		if (pausing)
			fireCounter.tick();
		
		if (reloading)
			reloadCounter.tick();
		
		updatePosition();
		tickReloading();
		
		setRotation(fireAngle);
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

	public boolean canFire() {
		if (reloading || pausing)
			return false;
		
		if (ammoTank.empty() && loadTank.empty())
			return false;
		
		return true;
	}
	
	public void fire(GameObject obj) {
		this.fire((int)obj.getX(), (int)obj.getY());
	}
	
	public void fire(int x, int y) {
		if (projectileFactory == null) return;
		
		Game game = this.getGame();
		
		final double step = fireAmplitude / (projectiles - 1);

		for (int i = 0; i < projectiles; ++i) {
			
			Projectile p = projectileFactory.create(game);
			p.moveTo(this);
			p.setTag(getTag());
			
			final double fireRecoil = Util.randomDouble(-recoil, recoil);
			final double delta = fireRecoil + (projectiles == 1 ? 0 : step * i - fireAmplitude / 2);
			p.setVelX(-Math.cos(fireAngle + delta) * p.getSpeed());
			p.setVelY(-Math.sin(fireAngle + delta) * p.getSpeed());
		
			game.spawnGameObject(p);
		}

		pausing = true;
		if (loadTank.get() != INFINITE_AMMO)
			loadTank.take(1);
	}

}