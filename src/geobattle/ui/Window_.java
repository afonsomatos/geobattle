package geobattle.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import geobattle.core.Game;
import geobattle.core.Score;
import geobattle.render.sprite.shapes.CircleCross;
import geobattle.render.sprite.shapes.Cross;
import geobattle.core.Game.State;
import geobattle.util.Log;
import geobattle.util.Palette;

@SuppressWarnings("serial")
public class Window_ extends JFrame {
/*
	private Game game;
	
	public Window_(Game game, int screen, boolean fullscreen, int width, int height) {
		this.game 	= game;
		this.realWidth = width;
		this.realHeight = height;
		setTitle("Geometry Battle");
		
		menu = new Menu();
		menu.requestFocus();

		gameCanvas = new Play(this);
		container = (JPanel) getContentPane();
		
		cardLayout = new CardLayout();
		container.setLayout(cardLayout);
		
		container.add(menu, MENU);
		container.add(gameCanvas, CANVAS);

		// Show menu at first
		cardLayout.show(this.getContentPane(), MENU);
		
		// Get screen location and size
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screen];
		Rectangle screenBounds = gd.getDefaultConfiguration().getBounds();
		
		if (fullscreen) {
			setExtendedState(MAXIMIZED_BOTH);
			setUndecorated(true);
	        gd.setFullScreenWindow(this);
		}
		
		pack();

		// Center window in given screen
		Dimension dim = getSize();
		setLocation(
				(int)(screenBounds.getX() + screenBounds.getWidth() / 2.0 - dim.getWidth() / 2.0),
				(int)(screenBounds.getY() + screenBounds.getHeight() / 2.0 - dim.getHeight() / 2.0));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		// here ...
		setVisible(true);
		
		container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK, false), "menu");
		container.getActionMap().put("menu", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (game.getState() != State.PLAYING) return;
				game.pause();
				if (JOptionPane.showConfirmDialog(Window.this, "Are you sure?", "Back to Menu", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
				{
					game.unpause();
					game.end();
				}
				game.unpause();
				
			}				
		});
		
	}

	private void hideMouse() {
		container.setCursor(container.getToolkit().createCustomCursor(
                new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),
                new Point(),
                null ) );
	}
	
	private void showMouse() {
		container.setCursor(Cursor.getDefaultCursor());
	}

	public void sendGameOver(int score) {

	}
	*/
}
