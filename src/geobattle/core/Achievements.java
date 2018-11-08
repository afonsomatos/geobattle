package geobattle.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import geobattle.special.AsteroidSpecial;
import geobattle.special.BombSpecial;
import geobattle.special.Special;
import geobattle.special.TroopsSpecial;
import geobattle.special.slot.SpecialSlot;
import geobattle.special.slot.TimedSpecialSlot;
import geobattle.special.slot.UnitSpecialSlot;
import geobattle.util.Log;
import geobattle.weapon.WeaponFactory;

public class Achievements {

	private List<WeaponFactory> weapons = new ArrayList<>();
	private List<SpecialSlotFactory> specials = new ArrayList<>();
	
	private int specialSlots = 0;
	private int weaponSlots = 0;
	private int level = 0;
	
	public static class SpecialSlotFactory {
		
		private final String name;
		private final Function<Game, SpecialSlot> func;
		
		SpecialSlotFactory(String name, Function<Game, SpecialSlot> func) {
			this.name = name;
			this.func = func;
		}
		
		public String getName() {
			return name;
		}
		
		public SpecialSlot create(Game game) {
			return func.apply(game);
		}
	}
	
	private static final SpecialSlotFactory[] ALL_SPECIALS = new SpecialSlotFactory[] {
			
			new SpecialSlotFactory("Sentry x5", game -> {
				Special special = new TroopsSpecial(game, Tag.Player, Tag.Enemy);
				return new UnitSpecialSlot(special, 5);		
			}),
			
			new SpecialSlotFactory("Asteroid 15s", game -> {
				Special special = new AsteroidSpecial(game , game.getPlayer(), 5000);
				return new TimedSpecialSlot(special, 15_000);		
			}),
			
			new SpecialSlotFactory("Bombs x5", game -> {
				Special special = new BombSpecial(game, Tag.Void);
				return new UnitSpecialSlot(special, 5);
			})
			
	};
	

	private static final WeaponFactory[] ALL_WEAPONS = new WeaponFactory[] {
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
	
	void unlockLevel() {
		level += 1;
		Log.i("Level unlocked: " + level);
		
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
