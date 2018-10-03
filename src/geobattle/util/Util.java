package geobattle.util;

public final class Util {

	public static double clamp(double min, double val, double max) {
		if (val < min)
			return min;
		if (val > max)
			return max;
		return val;
	}
	
	public static int clamp(int min, int val, int max) {
		return (int) clamp(min, (double) val, max);
	}
	
	public static <T> boolean contains(T[] array, T item) {
		for (T i : array)
			if (i == item)
				return true;
		return false;
	}

}
