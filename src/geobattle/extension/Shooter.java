package geobattle.extension;

import geobattle.core.GameObject;
import geobattle.weapon.Weapon;

public class Shooter implements Extension {

	private Weapon weapon = null;
	private GameObject target = null;
	private boolean automatic = true;

	public Shooter(GameObject target, Weapon weapon) {
		this.target = target;
		this.weapon = weapon;
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
		weapon.setLock(target);
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	public void setAutomatic(boolean automatic) {
		this.automatic = false;
	}
	
	@Override
	public void update(GameObject gameObject) {

		if (!automatic) return;
				
		if (weapon.getAmmoLoad() == 0 && weapon.getAmmoSaved() > 0)
			weapon.reload();
		else if (weapon.canFire())
			weapon.fire(target);
	}
	
}
