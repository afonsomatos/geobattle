package geobattle.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.weapons.Projectile;

public class AliveObject extends GameObject {

	private int health = 0;
	private int healthCapacity = 0;
	
	public AliveObject(Game game, double x, double y) {
		super(game, x, y);
		setupCollider();
	}
	
	private void setupCollider() {
		setCollider(new Collider(this) {
			@Override
			public void handleCollision(Collider other) {
				GameObject otherObj = other.getGameObject();
				if (otherObj instanceof Projectile) {
					Projectile projectile = (Projectile) otherObj;
					AliveObject.this.suffer(projectile.getDamage());
					projectile.kill();
				}
			}
		});	
	}
	
	public void suffer(int hit) {
		health = health > hit ? health - hit : 0;
	}
	
	public int giveHealth(int givenHealth) {
		final int given = Math.min(healthCapacity - health, givenHealth);
		setHealth(health + given);
		return givenHealth - given;
	}
	
	public void setHealth(int health) {
		if (health > healthCapacity)
			healthCapacity = health;
		
		this.health = Math.max(health, 0);
	}
	
	public int getHealth() {
		return health;
	}

	public boolean isDead() {
		return health == 0;
	}
	
	@Override
	public void render(Graphics2D gfx) {
		super.render(gfx);

		int width = 40;
		int height = 10;
		int x = (int) (getX()) - width / 2;
		int y = (int) (getY()) - getHeight() / 2 - height * 2;
		
		gfx.setColor(new Color(200, (int) 255.0 * health / healthCapacity, 0));
		gfx.fillRect(x, y, (int) (width * (double) health / healthCapacity), height);
		
		gfx.setStroke(new BasicStroke(1));
		gfx.setColor(Color.WHITE);
		gfx.drawRect(x, y, width, height);
	}
	
}
