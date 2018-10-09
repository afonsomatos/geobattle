package geobattle.weapon.projectile;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;

public abstract class Projectile extends GameObject {

	private int damage = 10;
	
	public Projectile(Game game, int x, int y) {
		super(game, x, y);
		this.setDamage(damage);
		this.setCollider(new Collider(this));
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}
	
	@Override
	public void update() {
		
		if (getX() < -10 || getY() < -10 || getX() > 10 + getGame().getWidth()
				|| getY() > 10 + getGame().getHeight())
			this.kill();
	}
}
