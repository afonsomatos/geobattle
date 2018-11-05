package geobattle.special.slot;

import geobattle.special.Special;
import geobattle.util.Tank;

public class UnitSpecialSlot extends SpecialSlot {

	// Quantity that can be used
	private Tank units = new Tank(5);
	
	public UnitSpecialSlot(Special special, int units) {
		super(special);
		this.units = new Tank(units);
	}
	
	public String getIndicator() {
		return units.get() + "";
	}

	@Override
	public void invoke() {
		if (units.empty())
			return;
		
		units.take(1);
		Special special = getSpecial();
		// TODO: Make this abstract for every gameObject not such player
		special.setPos(special.getGame().getPlayer().getPos());
		special.send();
	}
	
}
