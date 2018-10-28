package geobattle.special;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.render.sprite.shapes.Square;
import geobattle.util.Palette;
import geobattle.util.Util;
import geobattle.weapon.Projectile;
import geobattle.weapon.ProjectileFactory;

public class StarBurstSpecial extends Special {
	
	public enum Style {
		RAINBOW,
		SOLID,
	};
	
	private Random rand = new Random();
	private int projectiles = 5;
	private int radius = 0;
	private int speed = 20;
	
	private Style style = Style.SOLID;
	private Color color = Color.BLACK;
	
	private ProjectileFactory projectileFactory = new ProjectileFactory();
	
	public StarBurstSpecial(Game game) {
		super(game);
	}
	
	public void setColor(Color color) {
		projectileFactory.setColor(color);
		this.style = Style.SOLID;
	}
	
	public void setStyle(Style style) {
		this.style = style;
	}
	
	public void setDamage(int damage) {
		projectileFactory.setDamage(damage);
	}
	
	public void setSpeed(int speed) {
		projectileFactory.setSpeed(speed);
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public void setProjectiles(int projectiles) {
		this.projectiles = projectiles;
	}
	
	@Override
	public void send() {
		
		Tag tag = getTag();
		Game game = getGame();
		Point pos = getPos();
		double step = Math.PI * 2 / projectiles;
		double delta = rand.nextDouble() * (Math.PI * 2);
		
		for (int i = 0; i < projectiles; ++i) {
			
			switch (style) {
				case RAINBOW:
					projectileFactory.setColor(Palette.randomWithout(Color.BLACK));
					break;
				case SOLID:
					projectileFactory.setColor(color);
					break;
			}
			
			Projectile p = projectileFactory.create(game,
					(int) (pos.x + Math.cos(delta + step * i) * radius),
					(int) (pos.y + Math.sin(delta + step * i) * radius)
					);

			p.setTag(tag);
			p.setVelX(Math.cos(delta + step * i) * speed);
			p.setVelY(Math.sin(delta + step * i) * speed);
			game.spawnGameObject(p);
		}	
	}
	
}