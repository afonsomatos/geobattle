package geobattle.timer;

public class Clock {
	
	public interface Routine {
		public void exec(Clock self);
	}
	
	// Does not trigger any routine
	public final static int STATIC = -1;
	
	/* Used for timer */
	private long start = 0;
	private long extraDelay = 0;
	
	private long delay = STATIC;
	private long elapsed = 0;
	private boolean active = true;
	private boolean repeat = false;

	private Routine routine;
	
	public Clock() {

	}
	
	public Clock(long delay, boolean repeat, Routine routine) {
		this.repeat = repeat;
		this.delay = delay;
		this.routine = routine;
	}
	
	public void setRoutine(Routine routine) {
		this.routine = routine;
	}
	
	public void run() {
		routine.exec(this);
	}
	
	public Routine getRoutine() {
		return routine;
	}
	
	public boolean isRepeat() {
		return repeat;
	}
	
	long getExtraDelay() {
		return extraDelay;
	}
	
	void setExtraDelay(long extraDelay) {
		this.extraDelay = extraDelay;
	}
	
	void setStart(long start) {
		this.start = start;
	}
	
	long getStart() {
		return start;
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	
	void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}
	
	public long getElapsed() {
		return elapsed;
	}
	
	boolean isActive() {
		return active;
	}
	
	public void off() {
		setActive(false);
	}
	
	void setActive(boolean active) {
		this.active = active;
	}
	
}