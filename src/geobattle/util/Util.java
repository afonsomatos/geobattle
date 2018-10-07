package geobattle.util;

import java.awt.Graphics2D;
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
		return (int) clamp(min, val, max);
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
	
	public static void drawCircle(Graphics2D gfx, int x, int y, int radius) {
		gfx.drawOval(x - radius, y - radius, radius * 2, radius * 2);
	}
	
	public static void fillCircle(Graphics2D gfx, int x, int y, int radius) {
		gfx.fillOval(x - radius, y - radius, radius * 2, radius * 2);
	}
	
	public static String repeatString(int n, String string) {
		return new String(new char[n]).replace("\0", string);
	}
	
}
