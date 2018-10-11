package geobattle.core;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import geobattle.input.KeyInput;
import geobattle.input.MouseInput;

class Window extends Canvas {

	private static final long serialVersionUID = -2003953519044767223L;
	
	private String title = "Geometry Battle";
	private MouseInput mouseInput;
	
	public Window(Game game) {
		
		int width = game.getWidth();
		int height = game.getHeight();
		JFrame container = new JFrame(title);
		
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(width, height));
		panel.setLayout(null);
		
		setBounds(0, 0, width, height);
		panel.add(this);
		
		container.pack();
		container.setResizable(false);
		container.setLocationRelativeTo(null);
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.setVisible(true);

		requestFocus();
		createBufferStrategy(3);
		
		mouseInput = new MouseInput(game);
		addKeyListener(new KeyInput(game));
		addMouseListener(mouseInput);
		addMouseMotionListener(mouseInput);
	}
	
	public MouseInput getMouseInput() {
		return mouseInput;
	}
	
}
