package geobattle.schedule;

import geobattle.util.Util;

public class Event implements Runnable {
	
	private boolean repeat = false;
	private boolean off = false;
	
	private long start = 0;
	private long delay = 0;
	private long elapsed = 0;
	
	private long extraDelay = 0;
	
	private Runnable runnable;
	
	public Event() {
		
	}
	
	public Event(long delay, boolean repeat) {
		this.delay = delay;
		this.repeat = repeat;
	}
	
	public Event(long delay, boolean repeat, Runnable runnable) {
		this.delay = delay;
		this.repeat = repeat;
		this.runnable = runnable;
	}
	
	@Override
	public void run() {
		runnable.run();
	}
	
	void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}
	
	void removeExtraDelay() {
		this.extraDelay = 0;
	}
	
	void addExtraDelay(long extraDelay) {
		this.extraDelay += extraDelay;
	}
	
	long getExtraDelay() {
		return extraDelay;
	}
	
	public double getPercentage() {
		return Util.clamp(0, (double) elapsed / delay, 1);
	}
	
	public long getElapsed() {
		return elapsed;
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

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

}