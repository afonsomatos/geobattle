package geobattle.util;

import java.util.Random;

public final class Util {

	private static Random rand = new Random();
	
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
	
	public static int randomInteger(int lower, int upper) {
		return lower + rand.nextInt(upper - lower);
	}

	public static double randomDouble(double lower, double upper) {
		return lower + rand.nextDouble() * (upper - lower);
	}
	
}
