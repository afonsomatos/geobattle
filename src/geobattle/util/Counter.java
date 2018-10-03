package geobattle.util;

public abstract class Counter {

	private double step;
	private double end;
	private double val = 0;
	
	public Counter(double step) {
		this(step, 1);
	}
	
	public Counter(double step, double end) {
		this.step = step;
		this.end = end;
	}
	
	public void setStep(double step) {
		this.step = step;
	}
	
	public void tick() {
		val += step;
		if (val >= end) {
			val = 0;
			fire();
		}
	}
	
	public abstract void fire();

}
