package geobattle.weapon;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.infection.InfectionFactory;
import geobattle.render.Renderable;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Aura;
import geobattle.render.sprite.shapes.Circle;
import geobattle.render.sprite.shapes.Diamond;
import geobattle.render.sprite.shapes.Square;
import geobattle.render.sprite.shapes.Triangle;
import geobattle.special.WaveFactory;
import geobattle.special.WaveSpecial;
import geobattle.util.Log;
import geobattle.util.Palette;

public class WeaponFactory {

	public final static WeaponFactory Rifle;
	public final static WeaponFactory Sniper;
	public final static WeaponFactory Shotgun;
	public final static WeaponFactory MachineGun;
	public final static WeaponFactory Virus;

	static {
		
		ProjectileFactory pRifle = new ProjectileFactory()
				.setSprite(new Circle(4, Palette.YELLOW))
				.setSpeed(10.0f)
				.setDamage(45);
		
		// Ready to create weapons
		Rifle = new WeaponFactory()
				.setProjectiles(1)
				.setRadius(70)
				.setFireSpeed(0.1)
				.setReloadSpeed(0.01)
				.setProjectileFactory(pRifle)
				.setRecoil(0.1)
				.setColor(Palette.PURPLE)
				.setAmmoLoad(30)
				.setAmmoSaved(90);

		
		ProjectileFactory pShotgun = new ProjectileFactory()
				.setSprite(new Square(8, 8, Palette.GREY))
				.setSpeed(15.0f)
				.setDamage(50);
		
		Shotgun = new WeaponFactory()
				.setProjectiles(8)
				.setRadius(70)
				.setFireSpeed(0.02)
				.setReloadSpeed(0.01)
				.setRecoil(0.05)
				.setFireAmplitude(Math.PI / 4)
				.setColor(Palette.GREY)
				.setAmmoLoad(7)
				.setProjectileFactory(pShotgun)
				.setAmmoSaved(32);

		ProjectileFactory pSniper = new ProjectileFactory()
				.setSprite(new Triangle(16, 16, Palette.RED))
				.setSpeed(20.0f)
				.setDamage(300);
		
		Sniper = new WeaponFactory()
				.setProjectiles(1)
				.setFireSpeed(0.02)
				.setRadius(70)
				.setReloadSpeed(0.007)
				.setAmmoSaved(32)
				.setAmmoLoad(7)
				.setColor(Palette.GREEN)
				.setProjectileFactory(pSniper);

		WaveFactory wMachineGun = new WaveFactory()
				.setDamage(100)
				.setThickness(15)
				.setRadius(100)
				.setColor(Palette.TEAL)
				.setSpeed(0.02);
		
		ProjectileFactory pMachineGun = new ProjectileFactory()
				.setSprite(new Square(30, 5, Palette.TEAL))
				.setSpeed(15.0f)
				.setWaveFactory(wMachineGun)
				.setDamage(20);
		
		MachineGun = new WeaponFactory()
				.setRadius(70)
				.setProjectiles(1)
				.setFireSpeed(0.2)
				.setReloadSpeed(0.005)
				.setAmmoLoad(100)
				.setAmmoSaved(1000)
				.setRecoil(0)
				.setColor(Palette.BLUE)
				.setSize(10)
				.setProjectileFactory(pMachineGun);
		
		InfectionFactory virus = new InfectionFactory()
				.setColor(Palette.MINT)
				.setDamage(60)
				.setHits(5)
				.setDelay(500);
		
		ProjectileFactory pVirus = new ProjectileFactory()
				.setSprite(new Aura(12, 3, Palette.MINT))
				.setSpeed(9.0f)
				.setDamage(0)
				.setInfectionFactory(virus);
		
		Virus = new WeaponFactory()
				.setColor(Color.WHITE)
				.setSize(15)
				.setProjectileFactory(pVirus)
				.setAmmoSaved(20)
				.setAmmoLoad(4)
				.setFireSpeed(0.02)
				.setReloadSpeed(0.08);
	}
	
	private double fireSpeed		= 0.1;
	private double reloadSpeed		= 0.01;
	private int projectiles 		= 1;
	private double fireAmplitude 	= Math.PI / 4;
	private double radius 			= 70;
	private double fireAngle 		= 32;
	private double recoil 			= 0;
	private int padding				= 0;
	private int ammoSaved			= 60;
	private int ammoLoad			= 30;
	private int size				= 10;
	
	private Sprite sprite						= null;
	private Color color							= Color.WHITE;
	private ProjectileFactory projectileFactory	= null;
	
	private WeaponFactory() {
		
	}
	
	private WeaponFactory setProjectiles(int projectiles) {
		this.projectiles = projectiles;
		return this;
	}
	
	private WeaponFactory setProjectileFactory(ProjectileFactory projectileFactory) {
		this.projectileFactory = projectileFactory;
		return this;
	}

	private WeaponFactory setAmmoSaved(int ammoSaved) {
		this.ammoSaved = ammoSaved;
		return this;
	}
	
	private WeaponFactory setPadding(int padding) {
		this.padding = padding;
		return this;
	}
	
	private WeaponFactory setSize(int size) {
		this.size = size;
		return this;
	}
	
	private WeaponFactory setAmmoLoad(int ammoLoad) {
		this.ammoLoad = ammoLoad;
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
		weapon.setProjectiles(projectiles);
		weapon.setRadius(radius);
		weapon.setFireSpeed(fireSpeed);
		weapon.setReloadSpeed(reloadSpeed);
		weapon.setFireAngle(fireAngle);
		weapon.setFireAmplitude(fireAmplitude);
		weapon.setAmmoLoad(ammoLoad);
		weapon.setAmmoSaved(ammoSaved);
		weapon.setColor(color);
		weapon.setRecoil(recoil);
		weapon.setSize(size);
		weapon.setProjectileFactory(projectileFactory);
		weapon.setPadding(padding);
		
		if (sprite == null)
			weapon.paint();
		else
			weapon.setSprite(sprite);
		
		weapon.fill();
		return weapon;
	}

}
