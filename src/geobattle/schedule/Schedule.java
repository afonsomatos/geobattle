package geobattle.schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Schedule {

	private List<Event> timers = new ArrayList<Event>();
	private long startPause;
	private boolean paused = false;
	
	public synchronized void tick() {
		if (paused) return;
		
		long now = System.currentTimeMillis();
		
		List<Event> toRemove = new ArrayList<Event>();
		for (Event t : new ArrayList<Event>(timers)) {
			if (t.isOff()) {
				toRemove.add(t);
				continue;
			}
			long elapsed = (now - t.getExtraDelay() - t.getStart());
			t.setElapsed(elapsed);
			if (elapsed >= t.getDelay()) {
				t.removeExtraDelay();
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
	
	public void pause() {
		startPause = System.currentTimeMillis();
		paused = true;
	}
	
	public void unpause() {
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
