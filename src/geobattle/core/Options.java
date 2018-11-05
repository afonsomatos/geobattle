package geobattle.core;

public class Options {
	
	private int[] weapons;
	private int[] specials;
	private int[] powerups;

	private String settings;
	private int level;
	
	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int[] getWeapons() {
		return weapons;
	}
	
	public void setWeapons(int[] weapons) {
		this.weapons = weapons;
	}
	
	public int[] getSpecials() {
		return specials;
	}
	
	public void setSpecials(int[] specials) {
		this.specials = specials;
	}
	
	public int[] getPowerups() {
		return powerups;
	}
	
	public void setPowerups(int[] powerups) {
		this.powerups = powerups;
	}
	
}
