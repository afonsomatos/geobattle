package geobattle.core;

import java.util.ArrayList;
import java.util.List;

import geobattle.special.slot.SpecialSlotFactory;
import geobattle.weapon.WeaponFactory;

public class Achievements {

	private List<WeaponFactory> weapons = new ArrayList<>();
	private List<SpecialSlotFactory> specials = new ArrayList<>();
	
	private int specialSlots = 0;
	private int weaponSlots = 0;
	private int level = 0;
	
	private static final SpecialSlotFactory[] ALL_SPECIALS = new SpecialSlotFactory[] {
			SpecialSlotFactory.WAVE,
			SpecialSlotFactory.SENTRY_5S,
			SpecialSlotFactory.ASTEROID_15S,
			SpecialSlotFactory.BOMBS_X5,
	};
	
	private static final WeaponFactory[] ALL_WEAPONS = new WeaponFactory[] {
			WeaponFactory.RIFLE,
			WeaponFactory.SHOTGUN,
			WeaponFactory.SNIPER,
			WeaponFactory.MACHINEGUN,
			WeaponFactory.VIRUS
	};
	
	Achievements() {
		// Go straight to level 1
		unlockLevel();
	}
	
	void unlockLevel() {
		level += 1;
		
		// Unlock a new weapon each level
		weapons.add(ALL_WEAPONS[(level - 1) % ALL_WEAPONS.length]);
		
		// Unlock a new weapon slot every 3 levels
		weaponSlots = 1 + level / 3;
		
		// Unlock a new special slot every 4 levels
		if (level >= 4 && level % 4 == 0)
			specialSlots++;
		
		// Unlock a new special every 3 levels
		if (level % 3 == 0)
			specials.add(ALL_SPECIALS[(level / 3) % ALL_SPECIALS.length]);
	}
	
	public int getSpecialSlots() {
		return specialSlots;
	}
	
	public int getWeaponSlots() {
		return weaponSlots;
	}

	public int getLevel() {
		return level;
	}
	
	public List<SpecialSlotFactory> getSpecials() {
		return specials;
	}
	
	public List<WeaponFactory> getWeapons() {
		return weapons;
	}
	
}
