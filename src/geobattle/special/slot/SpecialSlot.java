package geobattle.special.slot;

import geobattle.special.Special;

public abstract class SpecialSlot {
	
	private Special special;
	
	SpecialSlot(Special special) {
		this.special = special;
	}
	
	public abstract String getIndicator();
	public abstract void invoke();
	
	public Special getSpecial() {
		return special;
	}
	
}
