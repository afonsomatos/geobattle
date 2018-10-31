package geobattle.extension;

import geobattle.core.GameObject;
import geobattle.util.Interval;
import geobattle.util.Util;
import geobattle.weapon.Weapon;

public class Shooter implements Controller {

	private Weapon weapon = null;
	private GameObject target = null;
	private boolean automatic = true;

	// Delay to fire and reload the weapon
	private Interval<Integer> delay;
	
	// Minimum distance and max distance from target in order to shoot
	private Interval<Integer> radar; 
	
	private boolean pausing = false;
	
	public Shooter(Weapon weapon) {
		this.weapon = weapon;
	}
	
	public Shooter(GameObject target, Weapon weapon) {
		this.target = target;
		this.weapon = weapon;
	}
	
	public Interval<Integer> getRadar() {
		return radar;
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
		weapon.setLock(target);
	}
	
	public void setRadar(Interval<Integer> radar) {
		this.radar = radar;
	}
	
	public void setDelay(Interval<Integer> delay) {
		this.delay = delay;
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	public void setAutomatic(boolean automatic) {
		this.automatic = false;
	}
	
	private boolean canShoot(GameObject gameObject) {
		// Target is not available
		if (target == null || !target.isActive())
			return false;
		
		// Shooting is suspended
		if (!automatic || pausing)
			return false;
		
		// Target is not in radar
		if (radar != null) {
			double dist = gameObject.distance(target);
			if (dist < radar.start || dist > radar.end)
				return false;
		}
		
		return true;
	}
	
	@Override
	public void update(GameObject gameObject) {
		if (!canShoot(gameObject)) return;
		
		if (weapon.getAmmoLoad() == 0 && weapon.getAmmoSaved() > 0)
			weapon.reload();
		else if (weapon.canFire())
			weapon.fire(target);
		else
			; // Cannot use weapon
		
		if (delay != null) {
			int wait = Util.randomInteger(delay);
			if (wait > 0) {
				pausing = true;
				gameObject.getGame().getSchedule().next(wait, () -> pausing = false);
			}
		}
	}
	
}
