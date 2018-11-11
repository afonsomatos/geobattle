package geobattle.living;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.infection.Infection;
import geobattle.schedule.Event;
import geobattle.triggers.TriggerMap;
import geobattle.util.Interval;
import geobattle.util.Log;
import geobattle.util.Palette;
import geobattle.util.Tank;
import geobattle.util.Util;
import geobattle.weapon.Projectile;

public abstract class Living extends GameObject {
	
	private Tank healthTank = new Tank();
	private boolean godmode = false;
	
	private Event sufferEvent = new Event(1500, false, () -> suffered = null);

	private Integer suffered = null;
	
	public Living(Game game) {
		super(game);
		setupCollider();
		addDrawer(this::draw);
		addDrawer(this::drawHit);
	}
	
	private final void setupCollider() {
		setCollider(new Collider(this) {
			@Override
			public void handleCollision(Collider other) {
				GameObject otherObj = other.getGameObject();
				if (otherObj instanceof Projectile) {
					Projectile projectile = (Projectile) otherObj;
					if (!godmode && !Living.this.isDead())
						Living.this.suffer(projectile.getDamage());
				}
			}
		});	
	}
	
	public void setGodmode(boolean godmode) {
		this.godmode = godmode;
	}
	
	public boolean hasGodMode() {
		return godmode;
	}
	
	public void die() {
		getTriggerMap().call("die");
	}
	
	public void suffer(int hit) {
		if (godmode) return;
		
		suffered = hit;
		game.getSchedule().add(sufferEvent);
		
		healthTank.take(hit);
		if (isDead()) {
			die();
		}
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
	
	private void drawHit(Graphics2D gfx) {
		if (suffered == null || suffered == 0) return;
		
		int x = (int) (getX());
		int y = (int) (getY()) - getCollider().getHeight() / 2 - 40;
		
		gfx.setColor(Palette.RED);
		Util.Graphics.drawStringCentered(gfx, x, y, "-" + suffered);
	}
	
	public void draw(Graphics2D gfx) {
		if (godmode) return;
		
		final int width = 40;
		final int height = 10;
		int x = (int) (getX()) - width / 2;
		int y = (int) (getY()) - getCollider().getHeight() / 2 - height * 2;
		
		final int health = healthTank.get();
		final int healthCapacity = healthTank.max();
		
		gfx.setColor(new Color(200, (int) 255.0 * health / healthCapacity, 0));
		gfx.fillRect(x, y, (int) (width * (double) health / healthCapacity), height);
		
		gfx.setColor(Palette.WHITE);
		gfx.setStroke(new BasicStroke(1));
		gfx.drawRect(x, y, width, height);

	}
	
}
