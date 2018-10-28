package geobattle.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
import geobattle.core.Game.State;
import geobattle.util.Log;

@SuppressWarnings("serial")
public class Window extends JFrame {
	
	private final String CANVAS = "canvas";
	private final String MENU	= "menu";
	
	private int realWidth;
	private int realHeight;

	private Menu menu;
	private GameCanvas gameCanvas;
	private JPanel container;
	private CardLayout cardLayout;
	
	private Game game;
	
	public Window(Game game, int screen, boolean fullscreen, int width, int height) {
		this.game 	= game;
		this.realWidth = width;
		this.realHeight = height;
		setTitle("Geometry Battle");
		
		menu = new Menu();
		menu.requestFocus();

		gameCanvas = new GameCanvas(this);
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
	
	public int getRealWidth() {
		return realWidth;
	}

	public void setRealWidth(int realWidth) {
		this.realWidth = realWidth;
	}

	public int getRealHeight() {
		return realHeight;
	}

	public void setRealHeight(int realHeight) {
		this.realHeight = realHeight;
	}



	public Game getGame() {
		return game;
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
		
		private JPanel optionsPanel = new JPanel();
		private JTextArea optionsTxt = new JTextArea(10, 20);
		private JLabel highScoreLabel = new JLabel();
		
		private String options = "";
		
		Menu() {
			Font font = new Font("arial", Font.PLAIN, 16);
			Color fg = Color.WHITE;
			
			setBackground(Color.BLACK);

			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBorder(new EmptyBorder(10, 10, 10, 10));
			
			String text = "<html>Welcome to Geometry Battle!";
			text += "<br>Please press [ENTER] to start playing!";
			text += "<br>Press Q to quit.";
			
			JLabel welcomeLabel = new JLabel(text);
			welcomeLabel.setForeground(fg);
			welcomeLabel.setFont(font);

			optionsPanel = new JPanel();
			optionsPanel.add(new JScrollPane(optionsTxt));
			
			highScoreLabel = new JLabel();
			highScoreLabel.setForeground(fg);
			highScoreLabel.setFont(font);
			
			updateHighScore();
			
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), "quit");
			getActionMap().put("quit", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog(Menu.this, "Are you sure?", "Quit", 
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
						System.exit(0);
				}				
			});

			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK, false), "option");
			getActionMap().put("option", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					writeOptions();
				}
			});
			
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "enter");
			getActionMap().put("enter", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameCanvas.createBufferStrategy(3);
					cardLayout.show(container, CANVAS);
					gameCanvas.requestFocusInWindow();
					game.start(options);
				}
			});
			
			add(welcomeLabel);
			add(highScoreLabel);
		}
		
		private void writeOptions() {
			if (JOptionPane.showConfirmDialog(Menu.this, optionsPanel, "Options", 
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
				options = optionsTxt.getText();
			}
		}
		
		private void updateHighScore() {
			List<Score> scores = game.getScores();
			scores.sort((Score o1, Score o2) -> o2.score - o1.score);

			String highScoreTxt = "<html>Times played: " + game.getRounds();
			
			int n = 0;
			for (Score s : scores) {
				if (n >= 15) break;
				String txt = String.format("#%d ~ %d by %s", s.round, s.score, s.name);
				highScoreTxt += "<br>" + txt;
				n++;
			}
			
			highScoreLabel.setText(highScoreTxt);
		}
	}
	
}
