package geobattle.weapon;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.infection.InfectionFactory;
import geobattle.object.WaveFactory;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Rect;

public class ProjectileFactory {
	
	private int damage = 10;
	private double speed = 1;
	private WaveFactory waveFactory = null;
	private InfectionFactory infectionFactory = null;
	private Sprite sprite = new Rect(8, 8, Color.WHITE);
	
	public ProjectileFactory() {
		
	}
	
	public ProjectileFactory setWaveFactory(WaveFactory waveFactory) {
		this.waveFactory = waveFactory;
		return this;
	}
	
	public ProjectileFactory setInfectionFactory(InfectionFactory infectionFactory) {
		this.infectionFactory = infectionFactory;
		return this;
	}
	
	public ProjectileFactory setSprite(Sprite sprite) {
		this.sprite = sprite;
		return this;
	}
	
	public ProjectileFactory setSpeed(double speed) {
		this.speed = speed;
		return this;
	}
	
	public ProjectileFactory setDamage(int damage) {
		this.damage = damage;
		return this;
	}
	
	public Projectile create(Game game) {
		Projectile pro = new Projectile(game);
		pro.setDamage(damage);
		pro.setSpeed(speed);
		pro.setInfectionFactory(infectionFactory);
		pro.setWaveFactory(waveFactory);
		pro.setSprite(sprite);
		pro.getCollider().surround(sprite);
		return pro;
	}

}
