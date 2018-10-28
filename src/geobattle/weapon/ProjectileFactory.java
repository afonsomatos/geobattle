package geobattle.weapon;

import java.awt.Color;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Square;

public class ProjectileFactory {
	
	private int damage = 10;
	private Color color = Color.WHITE;
	private int width = 8;
	private int height = 8;
	private double speed = 1;
	
	public ProjectileFactory() {
		
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
	
	public Projectile create(Game game, int x, int y) {
		Projectile pro = new Projectile(game, x, y);
		pro.setDamage(damage);
		Sprite sprite = new Square(width, height, color);
		pro.setSprite(sprite);
		pro.setSpeed(speed);
		Collider col = new Collider(pro);
		pro.setCollider(col);
		col.surround(sprite);
		return pro;
	}

}
