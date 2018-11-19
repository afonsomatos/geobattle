package geobattle.core;

/**
 * Holds custom options loaded from the menu.
 */
public class Options {
	
	/*
	 * Arrays that hold the indexes of unlocked items to be chosen. Their length
	 * corresponds to the amount of items picked of its type.
	 */
	private int[] weapons;
	private int[] specials;
	private int[] powerups;

	private String settings;
	private int level;
	
	/**
	 * Returns the string that represents the extra settings.
	 * 
	 * @return string with extra settings
	 */
	public String getSettings() {
		return settings;
	}
	
	/**
	 * Sets the string that represents the extra settings.
	 * 
	 * @param string with extra settings
	 */
	public void setSettings(String settings) {
		this.settings = settings;
	}

	/**
	 * Returns the level that is intended to be played.
	 * 
	 * @return level to be played
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level that is intended to be played.
	 * 
	 * @param level to be played
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * Returns an array with the chosen weapons' indexes.
	 * 
	 * @return weapons index array
	 */
	public int[] getWeapons() {
		return weapons;
	}
	
	/**
	 * Sets the current choices of weapons.
	 * 
	 * @param weapons index array
	 */
	public void setWeapons(int[] weapons) {
		this.weapons = weapons;
	}
	
	/**
	 * Returns an array with the chosen specials' indexes.
	 * 
	 * @return specials index array
	 */
	public int[] getSpecials() {
		return specials;
	}
	
	/**
	 * Sets the current choices of specials.
	 * 
	 * @param specials index array
	 */
	public void setSpecials(int[] specials) {
		this.specials = specials;
	}
	
	/**
	 * Returns an array with the chosen powerups' indexes.
	 * 
	 * @return powerups index array
	 */
	public int[] getPowerups() {
		return powerups;
	}
	
	/**
	 * Sets the current choices of powerups.
	 * 
	 * @param powerups index array
	 */
	public void setPowerups(int[] powerups) {
		this.powerups = powerups;
	}
	
}
