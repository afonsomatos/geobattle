package geobattle.util;

public class Interval<T extends Number> {
	
	public final T start;
	public final T end;
	
	public Interval(T start, T end) {
		this.start = start;
		this.end = end;
	}
	
	public boolean contains(T val) {
		double dval 	= val.doubleValue();
		
		double dstart 	= start == null ? Double.MIN_VALUE : start.doubleValue();
		double dend		= end == null ? Double.MAX_VALUE : end.doubleValue();
		
		return dstart <= dval && dval <= dend;
	}
	
}
