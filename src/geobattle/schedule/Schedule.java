package geobattle.schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Schedule {

	private List<Event> timers = new LinkedList<Event>();
	private long startPause;
	private boolean paused = false;
	
	public void tick() {
		if (paused) return;
	
			
		List<Event> toRemove = new ArrayList<Event>();
		for (Event t : new ArrayList<>(timers)) {
			if (t.isOff()) {
				toRemove.add(t);
				continue;
			}
			
			long now = System.currentTimeMillis();
			long elapsed = (now - t.getExtraDelay() - t.getStart());
			t.setElapsed(elapsed);
			
			if (t.getDelay() <= elapsed) {
				t.removeExtraDelay();
				t.run();
				
				if (!t.isRepeat()) {
					toRemove.add(t);
					continue;
				}
				
				t.setStart(now);
			}
		}

		timers.removeAll(toRemove);
	}
	
	public void pause() {
		startPause = System.currentTimeMillis();
		paused = true;
	}
	
	public void unpause() {
		if (!paused) {
			throw new IllegalStateException("Can't unpaused what's not paused");
		}
		for (Event t : timers)
			t.addExtraDelay(System.currentTimeMillis() - startPause);
		paused = false;
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
		if (!timers.contains(event))
			timers.add(event);
	}

}
