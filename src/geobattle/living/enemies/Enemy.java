package geobattle.living.enemies;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.extension.Extension;
import geobattle.living.Living;
import geobattle.weapon.Weapon;

public abstract class Enemy extends Living {
	
	private GameObject target = null;
	
	Enemy(Game game, int x, int y, GameObject target) {
		super(game, x, y);
		this.target = target;
		setTag(Tag.Enemy);
		
		getCollider().setTag(Tag.Enemy);
		getExtensionList().add(new Extension<Enemy>() {
			@Override
			public void tick(Enemy enemy) {
				if (enemy.isDead())
					enemy.kill();
			}
		});
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
