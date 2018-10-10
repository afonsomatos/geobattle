package geobattle.schedule;

public class Event {
	
	private boolean repeat = false;
	private boolean off = false;
	
	private long start = 0;
	private long delay = 0;
	
	private Runnable runnable;
	
	public Event() {
		
	}
	
	public Event(long delay, boolean repeat, Runnable runnable) {
		this.delay = delay;
		this.repeat = repeat;
		this.runnable = runnable;
	}
	
	public long getElapsed() {
		return System.currentTimeMillis() - start;
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