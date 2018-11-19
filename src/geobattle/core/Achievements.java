package geobattle.core;

import java.util.ArrayList;
import java.util.List;

import geobattle.living.bots.powerups.HealthPowerup;
import geobattle.living.bots.powerups.Powerup;
import geobattle.living.bots.powerups.SpeedPowerup;
import geobattle.special.slot.SpecialSlotFactory;
import geobattle.weapon.WeaponFactory;

/**
 * Stores and unlocks items during a game's progress.
 */
public class Achievements {

	// Unlock all levels till this one at start
	private final static int START_LEVEL = 30;
	
	/*
	 * No more than these amounts of slots will be unlocked for each item.
	 */
	private final static int MAX_WEAPON_SLOTS  = 5;
	private final static int MAX_SPECIAL_SLOTS = 5;
	private final static int MAX_POWERUP_SLOTS = 5;
	
	/*
	 * Unlocked items.
	 */
	private List<Powerup> powerups 				= new ArrayList<>();
	private List<WeaponFactory> weapons 		= new ArrayList<>();
	private List<SpecialSlotFactory> specials 	= new ArrayList<>();
	
	/*
	 * Unlocked number of slots.
	 */
	private int powerupSlots = 0;
	private int specialSlots = 0;
	private int weaponSlots = 0;
		
	// Last unlocked level
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
		if (level % 5 == 0 && powerupSlots < MAX_POWERUP_SLOTS)
			powerupSlots++;
		
		// Unlock a new powerup every 5 levels
		if (level % 5 == 0)
			powerups.add(ALL_POWERUPS[(level / 5) % ALL_POWERUPS.length]);
	}

	private void unlockSpecials() {
		// Unlock a new special slot every 4 levels
		if (level % 4 == 0 && specialSlots < MAX_SPECIAL_SLOTS)
			specialSlots++;
		
		// Unlock a new special every 4 levels
		if (level % 4 == 0)
			specials.add(ALL_SPECIALS[(level / 4) % ALL_SPECIALS.length]);
	}

	private void unlockWeapons() {
		// Unlock a new weapon each even level
		if (level % 2 == 1)
			weapons.add(ALL_WEAPONS[(level / 2) % ALL_WEAPONS.length]);
		
		// Unlock a new weapon slot every 3 levels
		if (weaponSlots < MAX_WEAPON_SLOTS)
			weaponSlots = 1 + level / 3;
	}
	
	/**
	 * Returns the unlocked special slots till now.
	 * 
	 * @return n unlocked special slots
	 */
	public int getSpecialSlots() {
		return specialSlots;
	}
	
	/**
	 * Returns the unlocked powerup slots till now.
	 * 
	 * @return n unlocked powerup slots
	 */
	public int getPowerupSlots() {
		return powerupSlots;
	}
	
	/**
	 * Returns the unlocked weapon slots till now.
	 * 
	 * @return n unlocked weapon slots
	 */
	public int getWeaponSlots() {
		return weaponSlots;
	}

	/**
	 * Returns the greatest level that has been unlocked.
	 * 
	 * @return the max unlocked level
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Returns a list with the unlocked powerups.
	 * 
	 * @return list with the unlocked powerups.
	 */
	public List<Powerup> getPowerups() {
		return powerups;
	}
	
	/**
	 * Returns a list with the unlocked specials.
	 * 
	 * @return list with the unlocked specials.
	 */
	public List<SpecialSlotFactory> getSpecials() {
		return specials;
	}

	/**
	 * Returns a list with the unlocked weapons.
	 * 
	 * @return list with the unlocked weapons.
	 */
	public List<WeaponFactory> getWeapons() {
		return weapons;
	}
	
}
