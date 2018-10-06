package geobattle.util;

public abstract class Counter {

	private double start, step, end;
	private double value;
	
	public Counter(double start, double step, double end) {
		value = this.start = start;
		this.step = step;
		this.end = end;
	}
	
	public Counter(double step) {
		this(0, step, 1);
	}
	
	public Counter(double step, double end) {
		this(0, step, end);
	}
	
	public double getValue() {
		return value;
	}
	
	public void reset() {
		value = start;
	}
	
	public void setEnd(double end) {
		this.end = end;
	}
	
	public void setStep(double step) {
		this.step = step;
	}
	
	public void tick() {
		value += step;
		if (start >= end && value <= end || start <= end && value >= end) {
			value = start;
			fire();
		}
	}
	
	public abstract void fire();

}
