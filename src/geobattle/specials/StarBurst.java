package geobattle.specials;

import java.awt.Color;
import java.util.Random;

import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.weapons.Bullet;
import geobattle.weapons.Projectile;

public class StarBurst extends Special {
	
	public enum Style {
		RAINBOW,
		SOLID,
	};
	
	private final Color[] rainbowColors = { Color.PINK, Color.RED, Color.GREEN, Color.YELLOW,
			Color.CYAN, Color.BLUE, Color.WHITE };
	
	private Random rand = new Random();
	private int projectiles = 5;
	private int radius = 1;
	private int speed = 20;
	private int damage = 100;
	
	private Style style = Style.SOLID;
	private Color color = Color.BLACK;
	
	public StarBurst(Game game, Tag tag) {
		super(game);
		setTag(tag);
	}
	
	public void setColor(Color color) {
		this.color = color;
		this.style = Style.SOLID;
	}
	
	public void setStyle(Style style) {
		this.style = style;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public void setProjectiles(int projectiles) {
		this.projectiles = projectiles;
	}
	
	@Override
	public void send() {
		
		Tag projectilesTag = getTag();

		double step = Math.PI * 2 / projectiles;
		double delta = rand.nextDouble() * (Math.PI * 2);
		
		for (int i = 0; i < projectiles; ++i) {
			Projectile p = new Bullet(game,
					(int) (pos.getX() + Math.cos(delta + step * i) * radius),
					(int) (pos.getY() + Math.sin(delta + step * i) * radius));
			
			p.getCollider().setTag(projectilesTag);
			p.setDamage(damage);
			p.setSpeed(speed);
			
			switch (style) {
				case RAINBOW:
					p.setColor(rainbowColors[rand.nextInt(rainbowColors.length)]);
					break;
				case SOLID:
					p.setColor(color);
					break;
			}

			p.setVelX(Math.cos(delta + step * i) * speed);
			p.setVelY(Math.sin(delta + step * i) * speed);
			game.spawnGameObject(p);
		}	
	}
	
}