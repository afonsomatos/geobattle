package geobattle.special;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.render.sprite.shapes.Square;
import geobattle.weapon.Projectile;

public class StarBurstSpecial extends Special {
	
	public enum Style {
		RAINBOW,
		SOLID,
	};
	
	private static final Color[] rainbowColors = { Color.PINK, Color.RED, Color.GREEN, Color.YELLOW,
			Color.CYAN, Color.BLUE, Color.WHITE };
	
	private Random rand = new Random();
	private int projectiles = 5;
	private int radius = 1;
	private int speed = 20;
	private int damage = 100;
	
	private Style style = Style.SOLID;
	private Color color = Color.BLACK;
	
	public StarBurstSpecial(Game game) {
		super(game);
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
		Game game = getGame();
		Point pos = getPos();
		double step = Math.PI * 2 / projectiles;
		double delta = rand.nextDouble() * (Math.PI * 2);
		
		for (int i = 0; i < projectiles; ++i) {
			Projectile p = new Projectile(game,
					(int) (pos.getX() + Math.cos(delta + step * i) * radius),
					(int) (pos.getY() + Math.sin(delta + step * i) * radius),
					8, 8);

			p.setTag(projectilesTag);
			p.setDamage(damage);
			p.setSpeed(speed);
			p.getCollider().surround(p);
			
			switch (style) {
				case RAINBOW:
					p.setColor(rainbowColors[rand.nextInt(rainbowColors.length)]);
					break;
				case SOLID:
					p.setColor(color);
					break;
			}
			
			p.setSprite(new Square(8, 8, p.getColor()));
			p.setVelX(Math.cos(delta + step * i) * speed);
			p.setVelY(Math.sin(delta + step * i) * speed);
			game.spawnGameObject(p);
		}	
	}
	
}