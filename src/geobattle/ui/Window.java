package geobattle.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import geobattle.core.Game;
import geobattle.core.Score;

@SuppressWarnings("serial")
public class Window extends JFrame {
	
	private final String CANVAS = "canvas";
	private final String MENU	= "menu";
	
	private Menu menu;
	private GameCanvas gameCanvas;
	private JPanel container;
	private CardLayout cardLayout;
	
	private Game game;
	
	public Window(Game game, int screen, boolean fullscreen) {
		this.game = game;
		setTitle("Geometry Battle");
		
		menu = new Menu();
		menu.requestFocus();

		gameCanvas = new GameCanvas(game);
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
		setVisible(true);
	}
	
	public void sendGameOver(int score) {
		String name;
		
		while (true) {
			name = JOptionPane.showInputDialog(this, "What's your nickname?", "Save Score", JOptionPane.QUESTION_MESSAGE);
			if (name == null || name.length() >= 3)
				break;
			JOptionPane.showMessageDialog(this, "Nicknames must be 3 letters minimum", "Invalid Nickname", JOptionPane.ERROR_MESSAGE);
		}
		
		if (name != null) {
			game.saveScore(name, score);
			menu.updateHighScore();		
		}
		
		cardLayout.show(container, MENU);
	}
	
	public GameCanvas getGameCanvas() {
		return gameCanvas;
	}
	
	private class Menu extends JPanel {
		
		private JLabel highScoreLabel = new JLabel();
		
		Menu() {
			Font font = new Font("arial", Font.PLAIN, 16);
			Color fg = Color.WHITE;
			
			setBackground(Color.BLACK);

			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBorder(new EmptyBorder(10, 10, 10, 10));
			
			String text = "<html>Welcome to Geometry Battle!";
			text += "<br>Please press [ENTER] to start playing!";
			
			JLabel welcomeLabel = new JLabel(text);
			welcomeLabel.setForeground(fg);
			welcomeLabel.setFont(font);

			highScoreLabel = new JLabel();
			highScoreLabel.setForeground(fg);
			highScoreLabel.setFont(font);
			
			updateHighScore();
			
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "enter");
			getActionMap().put("enter", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameCanvas.createBufferStrategy(3);
					cardLayout.show(container, CANVAS);
					gameCanvas.requestFocusInWindow();
					game.start();
				}
			});
			
			add(welcomeLabel);
			add(highScoreLabel);
		}
		
		private void updateHighScore() {
			List<Score> scores = game.getScores();
			scores.sort((Score o1, Score o2) -> o2.getScore() - o1.getScore());

			String highScoreTxt = "<html>Times played: " + game.getRounds();
			
			int n = 0;
			for (Score s : scores) {
				if (n >= 15) break;
				String txt = String.format("#%d ~ %d by %s", s.getRound(), s.getScore(), s.getName());
				highScoreTxt += "<br>" + txt;
				n++;
			}
			
			highScoreLabel.setText(highScoreTxt);
		}
	}
	
}
