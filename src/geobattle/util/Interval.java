package geobattle.util;

public class Interval<T extends Number> {
	
	public final T start;
	public final T end;
	
	public Interval(T start, T end) {
		this.start = start;
		this.end = end;
	}
	
}
