package geobattle.util;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public final class Util {

	private static Random rand = new Random();
	
	public static double clamp(double min, double val, double max) {
		return val < min ? min : val > max ? max : val;
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
	
	public static void drawStringOutline(Graphics2D gfx, String str, int x, int y) {
		gfx.drawString(str, x - 1, y - 1);
		gfx.drawString(str, x - 1, y + 1);
		gfx.drawString(str, x + 1, y - 1);
		gfx.drawString(str, x + 1, y + 1);
	}
	
	public static double insertRandomError(double value, double maxError) {
		return value + randomDouble(-maxError, maxError);
	}
	
	public static int insertRandomError(int value, int maxError) {
		return value + randomInteger(-maxError, maxError);
	}
	
	public static Point randomVec(Interval<Double> inter) {
		return randomVec(inter.start, inter.end);
	}
	
	public static Point randomVec(double norm) {
		double theta = Util.randomDouble(0, Math.PI * 2);
		return new Point((int) (norm * Math.cos(theta)), (int) (norm * Math.sin(theta)));
	}
	
	public static Point randomVec(double minNorm, double maxNorm) {
		return randomVec(randomDouble(minNorm, maxNorm));
	}
	
	public static int randomAbs(int value) {
		return randomInteger(-value, value);
	}
	
	public static int randomInteger(Interval<Integer> inter) {
		return randomInteger(inter.start, inter.end);
	}
	
	public static int randomInteger(int lower, int upper) {
		if (lower >= upper)
			return lower;
		return lower + rand.nextInt(upper - lower);
	}
	
	public static boolean randomBoolean() {
		return rand.nextBoolean();
	}
	
	public static double randomDouble(Interval<Double> inter) {
		return randomDouble(inter.start, inter.end);
	}
	
	public static double randomDouble(double lower, double upper) {
		return lower + rand.nextDouble() * (upper - lower);
	}
	
	public static String repeatString(int n, String string) {
		return new String(new char[n]).replace("\0", string);
	}
	
	private Util() {
		
	}
	
	public final static class Graphics {
		
		public static void drawStringCentered(Graphics2D gfx, int x, int y, String txt) {
		    FontRenderContext frc = 
		            new FontRenderContext(null, true, true);

		    Rectangle2D r2D = gfx.getFont().getStringBounds(txt, frc);
		    int rWidth = (int) Math.round(r2D.getWidth());
		    int rHeight = (int) Math.round(r2D.getHeight());
		    int rX = (int) Math.round(r2D.getX());
		    int rY = (int) Math.round(r2D.getY());

		    int a = x - (rWidth / 2) - rX;
		    int b = y - (rHeight / 2) - rY;

		    gfx.drawString(txt, a, b);
		}
		
		public static void drawCircle(Graphics2D gfx, int x, int y, int radius) {
			gfx.drawOval(x - radius, y - radius, radius * 2, radius * 2);
		}

		public static void fillCircle(Graphics2D gfx, int x, int y, int radius) {
			gfx.fillOval(x - radius, y - radius, radius * 2, radius * 2);
		}
		
		public static Ellipse2D.Double getEllipse(int x, int y, int width, int height) {
			return new Ellipse2D.Double(x - width/2, y - height/2, width, height);
		}
		
		private Graphics() {
			
		}
		
	}
	
}
