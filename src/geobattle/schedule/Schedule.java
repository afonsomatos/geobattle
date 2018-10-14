package geobattle.schedule;

import java.util.ArrayList;
import java.util.LinkedList;

public class Schedule {

	private LinkedList<Event> timers = new LinkedList<Event>();

	public void tick() {
		long now = System.currentTimeMillis();
		
		LinkedList<Event> toRemove = new LinkedList<Event>();
		for (Event t : new ArrayList<Event>(timers)) {
			if (t.isOff()) {
				toRemove.add(t);
				continue;
			}
			if (now - t.getStart() >= t.getDelay()) {
				t.getRunnable().run();
				if (!t.isRepeat()) {
					toRemove.add(t);
					continue;
				}
				t.setStart(now);
			}
		}

		timers.removeAll(toRemove);
	}
	
	public void next(long delay, Runnable runnable) {
		Event newEvent = new Event();
		newEvent.setDelay(delay);
		newEvent.setRepeat(false);
		newEvent.setRunnable(runnable);
		this.add(newEvent);		
	}
	
	public void clear() {
		timers.clear();
	}
	
	public void next(Runnable runnable) {
		next(0, runnable);
	}
	
	public void add(Event event) {
		event.setStart(System.currentTimeMillis());
		event.setOff(false);
		timers.add(event);
	}

}
