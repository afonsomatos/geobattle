package geobattle.special.slot;

import geobattle.core.Tag;
import geobattle.living.Player;
import geobattle.schedule.Event;
import geobattle.special.Special;

public class TimedSpecialSlot extends SpecialSlot {

	// Time till it takes to load
	private boolean used = false;
	private Event chargeEvent = new Event();
	
	public TimedSpecialSlot(Special special, long time) {
		super(special);
		chargeEvent.setDelay(time);
		chargeEvent.setRunnable(() -> used = false);
	}
	
	public String getIndicator() {
		int perc = (int) (chargeEvent.getPercentage() * 100);
		return used ? perc + "%" : "*";
	}

	@Override
	public void invoke() {
		if (used) return;
		used = true;
		
		getSpecial().getGame().getSchedule().start(chargeEvent);
		
		Special special = getSpecial();
		Player player = special.getGame().getPlayer();
		Tag tag = player.getTag();
		
		special.setPos(player.getPos());
		special.setTag(tag);
		special.send();
	}
	
}
