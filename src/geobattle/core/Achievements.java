package geobattle.core;

import java.util.ArrayList;
import java.util.List;

import geobattle.special.Special;
import geobattle.util.Log;
import geobattle.weapon.WeaponFactory;

public class Achievements {

	private List<WeaponFactory> weapons = new ArrayList<>();
	
	private int weaponSlots;
	private int level = 0;
	
	private final WeaponFactory[] allWeapons = new WeaponFactory[] {
				WeaponFactory.Rifle,
				WeaponFactory.Shotgun,
				WeaponFactory.Sniper,
				WeaponFactory.MachineGun,
				WeaponFactory.Virus
	};
	
	Achievements() {
		// Go straight to level 1
		unlockLevel();
	}
	
	void setLevel(int level) {
		this.level = level;
	}
	
	void unlockLevel() {
		level += 1;
		Log.i("Level unlocked: " + level);
		
		// Unlock a new weapon
		if (allWeapons.length <= level)
			weapons.add(allWeapons[level - 1]);
		
		// Unlock a new slot every 3 levels
		weaponSlots = 1 + level / 3;
	}
	
	public int getWeaponSlots() {
		return weaponSlots;
	}

	public void setWeaponSlots(int weaponSlots) {
		this.weaponSlots = weaponSlots;
	}

	void addWeapon(WeaponFactory weapon) {
		weapons.add(weapon);
	}
	
	public int getLevel() {
		return level;
	}
	
	public List<WeaponFactory> getWeapons() {
		return weapons;
	}
	
}
