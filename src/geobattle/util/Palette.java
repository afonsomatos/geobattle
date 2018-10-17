package geobattle.util;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public enum Palette {
	RED		(new Color(230, 25, 75)),
	GREEN	(new Color(60, 180, 75)),
	YELLOW	(new Color(255, 225, 25)),
	BLUE	(new Color(0, 130, 200)),
	ORANGE	(new Color(245, 130, 48)),
	PURPLE	(new Color(145, 30, 180)),
	CYAN	(new Color(70, 240, 240)),
	MAGENTA	(new Color(240, 50, 230)),
	LIME	(new Color(210, 245, 60)),
	PINK	(new Color(250, 190, 190)),
	TEAL	(new Color(0, 128, 128)),
	LAVENDER(new Color(230, 190, 255)),
	BROWN	(new Color(170, 110, 40)),
	BEIGE	(new Color(255, 250, 200)),
	MAROON	(new Color(128, 0, 0)),
	MINT	(new Color(170, 255, 195)),
	OLIVE	(new Color(128, 128, 0)),
	APRICOT	(new Color(255, 215, 180)),
	NAVY	(new Color(0, 0, 128)),
	GREY	(new Color(128, 128, 128)),
	WHITE	(new Color(255, 255, 255)),
	BLACK	(new Color(0, 0, 0))
	
	;

	private static Palette[] all = Palette.values();
	private static Random rand = new Random();
	private Color color;
	
	Palette(Color col) {
		this.color = col;
	}
	
	public Color getColor() {
		return color;
	}
	
	public static Palette random() {
		return all[rand.nextInt(all.length)];
	}
	
	public static Palette[] random(int x) {
		Palette[] res = new Palette[x];
		while (x-- > 0)
			res[x] = all[rand.nextInt(all.length)];
		return res;
	}
	
	public static Palette[] randomUnique(int x) {
		List<Palette> all = Arrays.asList(Palette.values());
		Collections.shuffle(all);
		return all.subList(0, x).toArray(new Palette[x]);
	}
	
}
