package geobattle.enemies;

import geobattle.colliders.Collider;
import geobattle.core.AliveObject;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.weapons.Weapon;

public class Enemy extends AliveObject {
	
	private GameObject target = null;
	
	Enemy(Game game, int x, int y, GameObject target) {
		super(game, x, y);
		this.target = target;
		setTag(Tag.Enemy);
		
		getCollider().setTag(Tag.Enemy);
	}
	
	@Override
	public void kill() {
		super.kill();
	}
	
	@Override
	public void tick() {
		super.tick();
	
		if (this.isDead())
			this.kill();
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
