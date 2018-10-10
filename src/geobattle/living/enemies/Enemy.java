package geobattle.living.enemies;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.living.Living;
import geobattle.weapon.Weapon;

public abstract class Enemy extends Living {
	
	private GameObject target = null;
	private Weapon weapon;
	
	Enemy(Game game, int x, int y, GameObject target) {
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

	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	public GameObject getTarget() {
		return this.target;
	}

	public Weapon buildWeapon() {
		return null;
	}
	
}
