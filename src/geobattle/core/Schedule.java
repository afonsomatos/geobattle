package geobattle.core;

import java.util.LinkedList;

class Schedule {

	private LinkedList<Event> timers = new LinkedList<Event>();

	public void tick() {
		long now = System.currentTimeMillis();
		LinkedList<Event> timers2 = new LinkedList<Event>(timers);
		timers2.removeIf(t -> {
			if (t.isOff())
				return true;
			if (now - t.getStart() >= t.getDelay()) {
				t.getRunnable().run();
				if (!t.isRepeat())
					return true;
				t.setStart(now);
			}
			return false;
		});
		timers = timers2;
	}
	
	public void next(Runnable runnable) {
		Event newEvent = new Event();
		newEvent.setDelay(0);
		newEvent.setRepeat(false);
		newEvent.setRunnable(runnable);
		this.add(newEvent);
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
