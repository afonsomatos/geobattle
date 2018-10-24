package geobattle.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public final class Palette {
	
	public static final Color RED 		= new Color(230, 25, 75);
	public static final Color GREEN 	= new Color(60, 180, 75);
	public static final Color YELLOW 	= new Color(255, 225, 25);
	public static final Color BLUE 		= new Color(0, 130, 200);
	public static final Color ORANGE 	= new Color(245, 130, 48);
	public static final Color PURPLE 	= new Color(145, 30, 180);
	public static final Color CYAN 		= new Color(70, 240, 240);
	public static final Color MAGENTA 	= new Color(240, 50, 230);
	public static final Color LIME 		= new Color(210, 245, 60);
	public static final Color PINK 		= new Color(250, 190, 190);
	public static final Color TEAL 		= new Color(0, 128, 128);
	public static final Color LAVENDER 	= new Color(230, 190, 255);
	public static final Color BROWN 	= new Color(170, 110, 40);
	public static final Color BEIGE 	= new Color(255, 250, 200);
	public static final Color MAROON 	= new Color(128, 0, 0);
	public static final Color MINT 		= new Color(170, 255, 195);
	public static final Color OLIVE 	= new Color(128, 128, 0);
	public static final Color APRICOT 	= new Color(255, 215, 180);
	public static final Color NAVY 		= new Color(0, 0, 128);
	public static final Color GREY 		= new Color(128, 128, 128);
	public static final Color WHITE 	= new Color(255, 255, 255);
	public static final Color BLACK 	= new Color(0, 0, 0);

	private static Color[] palette = {
			RED, GREEN, YELLOW, BLUE, ORANGE, PURPLE, CYAN, MAGENTA, LIME, PINK, TEAL,
			LAVENDER, BROWN, BEIGE, MAROON, MINT, OLIVE, APRICOT, NAVY, GREY, WHITE, BLACK
	};
	
	private static Random rand = new Random();
	
	private Palette() {
		
	}

	public static Color[] randomWithout(int x, Color... colors) {
		List<Color> all = new LinkedList<>(Arrays.asList(palette));
		all.removeAll(Arrays.asList(colors));
		Collections.shuffle(all);
		return all.subList(0, x).toArray(new Color[x]);
	}
	
	public static Color randomWithout(Color... colors) {
		List<Color> all = new LinkedList<>(Arrays.asList(palette));
		all.removeAll(Arrays.asList(colors));
		return all.get(rand.nextInt(all.size()));
	}
	
	public static Color random() {
		return palette[rand.nextInt(palette.length)];
	}
	
	public static Color[] random(int x) {
		List<Color> all = Arrays.asList(palette);
		Collections.shuffle(all);
		return all.subList(0, x).toArray(new Color[x]);
	}
	
}
