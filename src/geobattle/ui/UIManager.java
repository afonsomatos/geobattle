package geobattle.ui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import geobattle.core.Game;
import geobattle.core.Score;
import geobattle.io.IOManager;
import geobattle.render.Renderable;
import geobattle.util.Log;

public class UIManager {

	private Game game;
	private UIStyle uiStyle;
	private IOManager ioManager;
	
	private Menu menu;
	private Play play;
	
	private Dimension size;
	
	private JFrame window;
	private JPanel container;
	private CardLayout cardLayout;
	
	public UIManager(Game game, Dimension size, int loadScreen, boolean fullscreen) {
		this.game = game;
		this.ioManager = game.getIOManager();
		this.size = size;
		this.uiStyle = new UIStyle();
		this.menu = new Menu(this);
		this.play = new Play(this);
		
		window = new JFrame();
		window.setTitle("Geometry Battle");
		container = (JPanel) window.getContentPane();
		setupScreen(loadScreen, fullscreen);
		setupLayout();
	}
	
	private void setupScreen(int loadScreen, boolean fullscreen) {
		// Get screen location and size
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[loadScreen];
		Rectangle screenBounds = gd.getDefaultConfiguration().getBounds();
		
		if (fullscreen) {
			window.setUndecorated(true);
			window.setExtendedState(JFrame.MAXIMIZED_BOTH);
	        gd.setFullScreenWindow(window);
		}

		// Center window in given screen
		Dimension dim = window.getSize();
		window.setLocation(
				(int)(screenBounds.getX() + screenBounds.getWidth()  / 2.0 - dim.getWidth()  / 2.0),
				(int)(screenBounds.getY() + screenBounds.getHeight() / 2.0 - dim.getHeight() / 2.0)
				);
		
	}
	
	private void setupLayout() {
		cardLayout = new CardLayout();
		container.setLayout(cardLayout);
		container.add(menu, "menu");
		container.add(play, "play");

		// Show menu at first
		cardLayout.show(container, "menu");
		
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
	}
	
	public void sendOpen() {
		Log.i("Opening!");
		window.setVisible(true);
		sendMenu();
	}
	
	void sendGameOver() {
		Log.i("Game ending");
		game.end();
	}
	
	public String sendScoreEnter() {
		String name;
		
		while (true) {
			name = JOptionPane.showInputDialog(container, "What's your nickname?", "Save Score", JOptionPane.PLAIN_MESSAGE);
			if (name == null || name.length() >= 3)
				break;
			JOptionPane.showMessageDialog(container, "Nicknames must be 3 letters minimum", "Invalid Nickname", JOptionPane.ERROR_MESSAGE);
		}
		
		return name;
	}
	
	Dimension getSize() {
		return size;
	}
	
	public ActionMap getActionMap() {
		return play.getActionMap();
	}
	
	public InputMap getInputMap() {
		return play.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	IOManager getIOManager() {
		return ioManager;
	}
	
	UIStyle getUIStyle() {
		return uiStyle;
	}
	
	List<Score> getScores() {
		return game.getScores();
	}
	
	void sendPauseToggle() {
		game.togglePause();
	}
	
	void sendPause(boolean pause) {
		game.setPause(pause);
	}
	
	public void askQuit() {
		sendPause(true);
		if (JOptionPane.showConfirmDialog(window, "Are you sure?", "Back to Menu", 
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
		{
			sendPause(false);
			sendGameOver();
		}
		sendPause(false);
	}
	
	public void sendMenu() {
		Log.i("Now showing menu");
		menu.updateHighScore();
		cardLayout.show(container, "menu");
		menu.requestFocus();
	}
	
	void sendPlay(String options) {
		Log.i("Now playing");
		cardLayout.show(container, "play");
		play.getReady();
		game.start(options);
	}
	
	void sendQuit() {
		Log.i("Application is exiting");
		System.exit(0);
	}
	
	public void renderFrame(Renderable renderable) {
		play.renderFrame(renderable);
	}
	
}
