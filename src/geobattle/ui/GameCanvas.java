package geobattle.ui;

import java.awt.Canvas;
import java.awt.Color;

import geobattle.core.Game;
import geobattle.io.KeyInput;
import geobattle.io.MouseInput;

public class GameCanvas extends Canvas {
		
	private Game game;
	private MouseInput mouseInput;
	
	GameCanvas(Game game) {
		this.game = game;

		setBackground(Color.BLACK);
		setBounds(0, 0, game.getWidth(), game.getHeight());
		
		mouseInput = new MouseInput(game);
		addKeyListener(new KeyInput(game));
		addMouseListener(mouseInput);
		addMouseMotionListener(mouseInput);
		
	}
	
	public MouseInput getMouseInput() {
		return mouseInput;
	}
	
}
