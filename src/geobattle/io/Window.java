package geobattle.io;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import geobattle.core.Game;

public class Window extends Canvas {

	private static final long serialVersionUID = -2003953519044767223L;
	
	private String title = "Geometry Battle";
	private MouseInput mouseInput;
	
	public Window(Game game) {
		
		int width = game.getWidth();
		int height = game.getHeight();
		JFrame container = new JFrame(title);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(width, height));
		panel.setLayout(null);
		
		JTextField textInput = new JTextField(20);
		textInput.setBounds(width/2 - 50, height/2 - 10, 100, 20);
		//panel.add(textInput);
		
		setBounds(0, 0, width, height);
		panel.add(this);
		
		container.add(panel);
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
