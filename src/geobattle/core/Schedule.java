package geobattle.core;

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
	
	public void next(Runnable runnable) {
		next(0, runnable);
	}
	
	public void add(Event event) {
		event.setStart(System.currentTimeMillis());
		event.setOff(false);
		timers.add(event);
	}
	
	public static class Event {
		
		private boolean repeat, off;
		private long start, delay;
		private Runnable runnable;
		
		public Event() {
			
		}
		
		public Event(long delay, boolean repeat, Runnable runnable) {
			this.delay = delay;
			this.repeat = repeat;
			this.runnable = runnable;
		}
		
		public boolean isRepeat() {
			return repeat;
		}

		public void setRepeat(boolean repeat) {
			this.repeat = repeat;
		}

		public boolean isOff() {
			return off;
		}

		public void setOff(boolean off) {
			this.off = off;
		}

		public long getStart() {
			return start;
		}

		public void setStart(long start) {
			this.start = start;
		}

		public long getDelay() {
			return delay;
		}

		public void setDelay(long delay) {
			this.delay = delay;
		}

		public Runnable getRunnable() {
			return runnable;
		}

		public void setRunnable(Runnable runnable) {
			this.runnable = runnable;
		}

	}

}
