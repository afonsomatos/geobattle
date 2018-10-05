package geobattle.extensions;

import geobattle.core.GameObject;
import geobattle.weapons.Weapon;

public class ShootExt extends Extension {

	private Weapon weapon;
	private GameObject target;
	private boolean automatic = true;

	public ShootExt() {
		this(null, null);
	}
	
	public ShootExt(GameObject target, Weapon weapon) {
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
	public void tick(GameObject gameObject) {

		if (!automatic) return;
				
		if (weapon.getAmmoLoad() == 0 && weapon.getAmmoSaved() > 0)
			weapon.reload();
		else if (weapon.canFire())
			weapon.fire(target);
	}
	
}
