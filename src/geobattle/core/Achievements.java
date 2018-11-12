package geobattle.core;

import java.util.ArrayList;
import java.util.List;

import geobattle.living.bots.powerups.HealthPowerup;
import geobattle.living.bots.powerups.Powerup;
import geobattle.living.bots.powerups.SpeedPowerup;
import geobattle.special.slot.SpecialSlotFactory;
import geobattle.weapon.WeaponFactory;

public class Achievements {

	private final static int START_LEVEL = 10;
	
	private final static int MAX_WEAPON_SLOTS  = 5;
	private final static int MAX_SPECIAL_SLOTS = 5;
	private final static int MAX_POWERUP_SLOTS = 5;
	
	private List<Powerup> powerups 				= new ArrayList<>();
	private List<WeaponFactory> weapons 		= new ArrayList<>();
	private List<SpecialSlotFactory> specials 	= new ArrayList<>();
	
	private int powerupSlots = 0;
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
			WeaponFactory.PISTOL,
			WeaponFactory.RIFLE,
			WeaponFactory.SHOTGUN,
			WeaponFactory.SNIPER,
			WeaponFactory.MACHINEGUN,
			WeaponFactory.VIRUS,
	};
	
	private static final Powerup[] ALL_POWERUPS = new Powerup[] {
			new HealthPowerup(50),
			new SpeedPowerup(1.0),
	};
	
	Achievements() {
		for (int i = 0; i < START_LEVEL; ++i)
			unlockLevel();
	}
	
	void unlockLevel() {
		level += 1;
		unlockWeapons();
		unlockSpecials();
		unlockPowerups();
	}
	
	private void unlockPowerups() {
		// Unlock a new powerup slot every 5 levels
		if (level % 4 == 0 && level < MAX_POWERUP_SLOTS)
			powerupSlots++;
		
		// Unlock a new powerup every 3 levels
		if (level % 3 == 0)
			powerups.add(ALL_POWERUPS[(level - 1) % ALL_POWERUPS.length]);
	}

	private void unlockSpecials() {
		// Unlock a new special slot every 4 levels
		if (level >= 4 && level % 4 == 0 && specialSlots < MAX_SPECIAL_SLOTS)
			specialSlots++;
		
		// Unlock a new special every 3 levels
		if (level % 3 == 0)
			specials.add(ALL_SPECIALS[(level / 3) % ALL_SPECIALS.length]);
	}

	private void unlockWeapons() {
		// Unlock a new weapon each even level
		if (level % 2 == 1)
			weapons.add(ALL_WEAPONS[(level / 2) % ALL_WEAPONS.length]);
		
		// Unlock a new weapon slot every 3 levels
		if (weaponSlots < MAX_WEAPON_SLOTS)
			weaponSlots = 1 + level / 3;
	}
	
	public int getSpecialSlots() {
		return specialSlots;
	}
	
	public int getPowerupSlots() {
		return powerupSlots;
	}
	
	public int getWeaponSlots() {
		return weaponSlots;
	}

	public int getLevel() {
		return level;
	}
	
	public List<Powerup> getPowerups() {
		return powerups;
	}
	
	public List<SpecialSlotFactory> getSpecials() {
		return specials;
	}
	
	public List<WeaponFactory> getWeapons() {
		return weapons;
	}
	
}
