package geobattle.living;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.util.Tank;
import geobattle.weapon.projectile.Projectile;

public abstract class Living extends GameObject {

	private Tank healthTank = new Tank();
	private boolean godmode = false;
	
	public Living(Game game, double x, double y) {
		super(game, x, y);
		setupCollider();
	}
	
	private final void setupCollider() {
		setCollider(new Collider(this) {
			@Override
			public void handleCollision(Collider other) {
				GameObject otherObj = other.getGameObject();
				if (otherObj instanceof Projectile) {
					Projectile projectile = (Projectile) otherObj;
					if (!godmode)
						Living.this.suffer(projectile.getDamage());
					projectile.kill();
				}
			}
		});	
	}
	
	public void setGodmode(boolean godmode) {
		this.godmode = godmode;
	}
	
	public abstract void die();
	
	public void suffer(int hit) {
		healthTank.take(hit);
		if (this.isDead())
			this.die();
	}
	
	public void restoreHealth() {
		healthTank.fill();
	}
	
	public int giveHealth(int health) {
		return healthTank.fill(health);
	}
	
	public void setHealth(int health) {
		healthTank.set(health);
	}
	
	public int getHealth() {
		return healthTank.get();
	}

	public boolean isDead() {
		return healthTank.get() == 0;
	}
	
	@Override
	public void render_(Graphics2D superGfx) {
		super.render_(superGfx);
		Graphics2D gfx = (Graphics2D) superGfx.create();
		
		final int width = 40;
		final int height = 10;
		int x = (int) (getX()) - width / 2;
		int y = (int) (getY()) - getCollider().getHeight() / 2 - height * 2;
		
		final int health = healthTank.get();
		final int healthCapacity = healthTank.max();
		gfx.setColor(new Color(200, (int) 255.0 * health / healthCapacity, 0));
		gfx.fillRect(x, y, (int) (width * (double) health / healthCapacity), height);
		
		gfx.setStroke(new BasicStroke(1));
		gfx.setColor(Color.WHITE);
		gfx.drawRect(x, y, width, height);
		gfx.dispose();
	}
	
}
