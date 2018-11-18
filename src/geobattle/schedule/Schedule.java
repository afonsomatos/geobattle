package geobattle.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Schedule {
	
	private List<Event> events = new ArrayList<>();
	
	private long startPause = 0;
	private boolean paused = false;
	
	public void next(long delay, Runnable runnable) {
		start(new Event(delay, false, runnable));
	}
	
	public void start(long delay, boolean repeat, Event.Routine routine) {
		start(new Event(delay, repeat, routine));
	}
	
	public void start(Event event) {
		event.setExtraDelay(0);
		event.setActive(true);
		event.setStart(System.currentTimeMillis());
		events.add(event);
	}
	
	public void pause() {
		startPause = System.currentTimeMillis();
		paused = true;
	}
	
	public void unpause() {
		if (!paused)
			throw new IllegalStateException("Can't unpause unpaused timer");
		
		for (Event e : events) {
			// We only add the time the clock was paused
			long delay = System.currentTimeMillis() - Math.max(e.getStart(), startPause);
			e.setExtraDelay(e.getExtraDelay() + delay);
		}
		
		paused = false;
	}
	
	public void tick() {
		if (paused) return;
		
		// Remove expired clocks
		events.removeAll(new ArrayList<>(events).stream().filter(e -> {
			
			// If the clock ended then no need to track it anymore
			if (!e.isActive())
				return true;
			
			long now = System.currentTimeMillis();
			long elapsed = (now - e.getExtraDelay() - e.getStart());
			e.setElapsed(elapsed);
			
			long delay = e.getDelay();
			
			// Never ending clock
			if (delay == Event.STATIC)
				return false;
			
			if (elapsed >= delay) {
				e.run();
				
				if (!e.isRepeat())
					return true;
				
				// reset
				e.setExtraDelay(0);
				e.setStart(now);
			}
			
			return false;
			
		}).collect(Collectors.toList()));
	}
	
	public void clear() {
		events.clear();
	}
	
}