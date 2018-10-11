package geobattle;

import geobattle.core.Game;

public class Main {
	
	public static void main(String[] args) {
		// Use for enhanced graphic rendering, otherwise it'll become laggy
		System.setProperty("sun.java2d.opengl", "true");
		new Game().open();
	}

}
