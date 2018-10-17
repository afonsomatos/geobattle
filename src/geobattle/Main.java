package geobattle;

import geobattle.core.Game;
import geobattle.launcher.Launcher;

public class Main {
	
	public static void main(String[] args) {
		// Use for enhanced graphic rendering, otherwise it'll become laggy
		System.setProperty("sun.java2d.opengl", "true");

		Game game = new Game();
		new Launcher(game);
	}

}