package geobattle.timer;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Timer {
	
	private Set<Clock> clocks = new LinkedHashSet<>();
	
	private long startPause = 0;
	private boolean paused = false;
	
	public void start(long delay, boolean repeat, Clock.Routine routine) {
		start(new Clock(delay, repeat, routine));
	}
	
	public void start(Clock clock) {
		clock.setExtraDelay(0);
		clock.setActive(true);
		clock.setStart(System.currentTimeMillis());
		clocks.add(clock);
	}
	
	public void pause() {
		startPause = System.currentTimeMillis();
		paused = true;
	}
	
	public void unpause() {
		if (!paused)
			throw new IllegalStateException("Can't unpause unpaused timer");
		
		for (Clock c : clocks) {
			// We only add the time the clock was paused
			long delay = System.currentTimeMillis() - Math.max(c.getStart(), startPause);
			c.setExtraDelay(c.getExtraDelay() + delay);
		}
		
		paused = false;
	}
	
	public void tick() {
		if (paused) return;
		
		// Remove expired clocks
		clocks.removeAll(clocks.stream().filter(c -> {
			
			// If the clock ended then no need to track it anymore
			if (!c.isActive())
				return true;
			
			long now = System.currentTimeMillis();
			long elapsed = (now - c.getExtraDelay() - c.getStart());
			c.setElapsed(elapsed);
			
			long delay = c.getDelay();
			
			// Never ending clock
			if (delay == Clock.STATIC)
				return false;
			
			if (elapsed >= delay) {
				c.run();
				
				if (!c.isRepeat())
					return true;
				
				// reset
				c.setExtraDelay(0);
				c.setStart(now);
			}
			
			return false;
			
		}).collect(Collectors.toList()));
	}
	
}