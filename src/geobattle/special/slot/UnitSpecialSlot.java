package geobattle.special.slot;

import geobattle.core.Tag;
import geobattle.living.Player;
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
		Player player = special.getGame().getPlayer();
		Tag tag = player.getTag();
		
		special.setPos(player.getPos());
		special.setTag(tag);
		special.send();
	}
	
}
