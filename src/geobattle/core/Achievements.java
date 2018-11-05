package geobattle.core;

import java.util.ArrayList;
import java.util.List;

import geobattle.special.Special;
import geobattle.weapon.WeaponFactory;

public class Achievements {

	private List<WeaponFactory> weapons = new ArrayList<>();
	private List<Special> specials = new ArrayList<>();
	
	private int weaponSlots = 3;
	private int specialSlots = 3;
	private int level = 1;
	
	void setLevel(int level) {
		this.level = level;
	}
	
	public int getWeaponSlots() {
		return weaponSlots;
	}

	public void setWeaponSlots(int weaponSlots) {
		this.weaponSlots = weaponSlots;
	}

	public int getSpecialSlots() {
		return specialSlots;
	}

	public void setSpecialSlots(int specialSlots) {
		this.specialSlots = specialSlots;
	}

	void addWeapon(WeaponFactory weapon) {
		weapons.add(weapon);
	}
	
	void addSpecial(Special special) {
		specials.add(special);
	}
	
	public int getLevel() {
		return level;
	}
	
	public List<Special> getSpecials() {
		return specials;
	}
	
	public List<WeaponFactory> getWeapons() {
		return weapons;
	}
	
}
