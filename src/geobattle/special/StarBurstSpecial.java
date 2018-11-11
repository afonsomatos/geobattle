package geobattle.special;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Rect;
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
	
	private Sprite defaultSprite = new Rect(8, 8, Color.WHITE);
	
	private static Sprite[] rainbowSprites;
	
	static {
		Color[] rainbow = Palette.randomWithout(8, Color.BLACK);
		rainbowSprites = new Sprite[rainbow.length];
		for (int i = 0; i < rainbow.length; ++i)
			rainbowSprites[i] = new Rect(8, 8, rainbow[i]);
	}
	
	private ProjectileFactory projectileFactory = new ProjectileFactory();
	
	public StarBurstSpecial(Game game) {
		super(game);
	}
	
	public void setColor(Color color) {
		defaultSprite = new Rect(8, 8, color);
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
					projectileFactory.setSprite(Util.random(rainbowSprites));
					break;
				case SOLID:
					projectileFactory.setSprite(defaultSprite);
					break;
			}
			
			Projectile p = projectileFactory.create(game);
			p.moveTo((int) (pos.x + Math.cos(delta + step * i) * radius),
					 (int) (pos.y + Math.sin(delta + step * i) * radius));
			p.setTag(tag);
			p.setVelX(Math.cos(delta + step * i) * speed);
			p.setVelY(Math.sin(delta + step * i) * speed);
			game.spawnGameObject(p);
		}	
	}
	
}