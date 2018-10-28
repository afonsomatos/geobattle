package geobattle.weapon;

import java.awt.Color;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.infection.InfectionFactory;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Square;

public class ProjectileFactory {
	
	private int damage = 10;
	private Color color = Color.WHITE;
	private int width = 8;
	private int height = 8;
	private double speed = 1;
	private InfectionFactory infectionFactory = null;
	
	public ProjectileFactory() {
		
	}
	
	public ProjectileFactory setInfectionFactory(InfectionFactory infectionFactory) {
		this.infectionFactory = infectionFactory;
		return this;
	}
	
	public ProjectileFactory setSpeed(double speed) {
		this.speed = speed;
		return this;
	}
	
	public ProjectileFactory setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public ProjectileFactory setSize(int width, int height) {
		this.width = width;
		this.height = height;
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
		Sprite sprite = new Square(width, height, color);
		pro.setSprite(sprite);
		pro.getCollider().surround(sprite);
		return pro;
	}

}
