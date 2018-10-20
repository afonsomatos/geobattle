package geobattle.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import geobattle.core.Game;
import geobattle.io.KeyInput;
import geobattle.io.MouseInput;
import geobattle.util.Log;

public class GameCanvas extends Canvas {
		
	private Game game;
	private MouseInput mouseInput;
	private KeyInput keyInput;
	
	GameCanvas(Window window) {
		this.game = window.getGame();
		
		setBackground(Color.BLACK);
		setBounds(0, 0, window.getRealWidth(), window.getRealHeight());
		
		mouseInput = new MouseInput(game);
		keyInput = new KeyInput(game);
		addKeyListener(keyInput);
		addMouseListener(mouseInput);
		addMouseMotionListener(mouseInput);
	}
	
	public MouseInput getMouseInput() {
		return mouseInput;
	}
	
	public KeyInput getKeyInput() {
		return keyInput;
	}
	
}
