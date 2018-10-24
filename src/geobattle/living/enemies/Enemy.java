package geobattle.living.enemies;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.living.Living;
import geobattle.weapon.Weapon;

public abstract class Enemy extends Living {
	
	private Living target;
	private Weapon weapon;
	
	Enemy(Game game, int x, int y, Living target) {
		super(game, x, y);
		this.target = target;
		setTag(Tag.Enemy);
		
		getCollider().setTag(Tag.Enemy);
		
		addExtension((GameObject gameObject) -> {
			Enemy enemy = (Enemy) gameObject;
			// Enemies are killable
			if (enemy.isDead()) {
				enemy.kill();
			}
		});
	}
	
	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public void setTarget(Living target) {
		this.target = target;
		// update activites towards target
		handleNewTarget(target);
	}
	
	public Living getTarget() {
		return this.target;
	}

	public Weapon buildWeapon() {
		return null;
	}
	
	protected abstract void handleNewTarget(Living target);
	
}
