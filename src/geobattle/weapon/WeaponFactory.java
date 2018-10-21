package geobattle.weapon;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.render.Renderable;
import geobattle.util.Palette;

public class WeaponFactory {

	public final static WeaponFactory Rifle;
	public final static WeaponFactory Sniper;
	public final static WeaponFactory Shotgun;
	public final static WeaponFactory Unlimited;

	static {
		// Ready to create weapons
		Rifle = new WeaponFactory()
				.setDamage(45)
				.setProjectiles(1)
				.setRadius(70)
				.setFireSpeed(0.1)
				.setReloadSpeed(0.01)
				.setRecoil(0.1)
				.setProjectileSpeed(10.0f)
				.setProjectileColor(Palette.YELLOW)
				.setColor(Palette.PURPLE)
				.setAmmoLoad(30)
				.setAmmoSaved(90);

		Shotgun = new WeaponFactory()
				.setDamage(50)
				.setProjectiles(8)
				.setRadius(70)
				.setFireSpeed(0.02)
				.setReloadSpeed(0.01)
				.setRecoil(0.05)
				.setFireAmplitude(Math.PI / 4)
				.setProjectileSpeed(15.0f)
				.setProjectileColor(Palette.GREY)
				.setColor(Palette.GREY)
				.setAmmoLoad(7)
				.setAmmoSaved(32);
		
		Sniper = new WeaponFactory()
				.setDamage(300)
				.setProjectiles(1)
				.setFireSpeed(0.02)
				.setRadius(70)
				.setReloadSpeed(0.007)
				.setAmmoSaved(32)
				.setAmmoLoad(7)
				.setColor(Palette.GREEN)
				.setProjectileColor(Palette.RED)
				.setProjectileSpeed(20.0f);
	
		Unlimited = new WeaponFactory()
				.setRadius(85)
				.setProjectiles(1)
				.setFireSpeed(Weapon.MAX_SPEED)
				.setReloadSpeed(Weapon.MAX_SPEED)
				.setAmmoLoad(Weapon.INFINITE_AMMO)
				.setAmmoSaved(Weapon.INFINITE_AMMO)
				.setRecoil(Math.PI / 72)
				.setProjectileColor(Palette.ORANGE)
				.setProjectileSpeed(20.0f)
				.setDamage(10000)
				.setProjectileSize(30)
				.setDrawer(gfx -> {
					int side = 30;
					int x[] = {0, side, side};
					int y[] = {0, side, -side};
					gfx.translate(15, 0);
					gfx.setColor(Color.RED);
					gfx.fillPolygon(x, y, 3);
				});
	}
	
	private Renderable drawer		= null;
	private double fireSpeed		= 0.1;
	private double reloadSpeed		= 0.01;
	private int projectiles 		= 1;
	private int damage 				= 10;
	private int projectileSize 		= 8;
	private double projectileSpeed 	= 2.0f;
	private Color projectileColor 	= Color.CYAN;
	private Color color				= Color.WHITE;
	private double fireAmplitude 	= Math.PI / 4;
	private double radius 			= 70;
	private double fireAngle 		= 32;
	private double recoil 			= 0;
	private int ammoSaved			= 60;
	private int ammoLoad			= 30;
	
	private WeaponFactory() {
		
	}
	
	private WeaponFactory setProjectiles(int projectiles) {
		this.projectiles = projectiles;
		return this;
	}

	private WeaponFactory setAmmoSaved(int ammoSaved) {
		this.ammoSaved = ammoSaved;
		return this;
	}
	
	private WeaponFactory setDrawer(Renderable drawer) {
		this.drawer = drawer;
		return this;
	}
	
	private WeaponFactory setAmmoLoad(int ammoLoad) {
		this.ammoLoad = ammoLoad;
		return this;
	}
	
	private WeaponFactory setDamage(int damage) {
		this.damage = damage;
		return this;
	}

	private WeaponFactory setColor(Color color) {
		this.color = color;
		return this;
	}
	
	private WeaponFactory setReloadSpeed(double reloadSpeed) {
		this.reloadSpeed = reloadSpeed;
		return this;
	}
	
	private WeaponFactory setFireSpeed(double fireSpeed) {
		this.fireSpeed = fireSpeed;
		return this;
	}

	private WeaponFactory setProjectileSize(int projectileSize) {
		this.projectileSize = projectileSize;
		return this;
	}

	private WeaponFactory setProjectileSpeed(double projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
		return this;
	}

	private WeaponFactory setProjectileColor(Color projectileColor) {
		this.projectileColor = projectileColor;
		return this;
	}

	private WeaponFactory setFireAmplitude(double fireAmplitude) {
		this.fireAmplitude = fireAmplitude;
		return this;
	}

	private WeaponFactory setRadius(double radius) {
		this.radius = radius;
		return this;
	}

	private WeaponFactory setRecoil(double recoil) {
		this.recoil = recoil;
		return this;
	}
	
	public Weapon create(Game game, GameObject origin, Tag tag) {
		Weapon weapon = new Weapon(game, origin, tag);
		if (drawer != null)
			weapon.setDrawer(drawer);
		weapon.setDamage(damage);
		weapon.setProjectiles(projectiles);
		weapon.setRadius(radius);
		weapon.setFireSpeed(fireSpeed);
		weapon.setReloadSpeed(reloadSpeed);
		weapon.setFireAngle(fireAngle);
		weapon.setFireAmplitude(fireAmplitude);
		weapon.setProjectileSize(projectileSize);
		weapon.setProjectileSpeed(projectileSpeed);
		weapon.setProjectileColor(projectileColor);
		weapon.setAmmoLoad(ammoLoad);
		weapon.setAmmoSaved(ammoSaved);
		weapon.setColor(color);
		weapon.setRecoil(recoil);
		weapon.fill();
		return weapon;
	}

}
