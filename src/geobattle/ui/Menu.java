package geobattle.ui;

import java.awt.Color;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import geobattle.core.Score;

@SuppressWarnings("serial")
class Menu extends JPanel {
	
	private JPanel optionsPanel 	= new JPanel();
	private JTextArea optionsTxt 	= new JTextArea(10, 20);
	private JLabel highScoreLabel 	= new JLabel();
	
	private String options = "";
	
	private UIManager uiManager;
	
	Menu(UIManager uiManager) {
		this.uiManager = uiManager;
		
		UIStyle uiStyle = uiManager.getUIStyle();
		Font font 	= uiStyle.getFont();
		Color fg 	= uiStyle.getForeground();
		Color bg	= uiStyle.getBackground();
		
		setBackground(bg);

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
		
		// Q to quit
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), "quit");
		getActionMap().put("quit", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(Menu.this, "Are you sure?", "Quit", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
					uiManager.sendQuit();
			}				
		});

		// Ctrl-O to set options
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK, false), "option");
		getActionMap().put("option", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writeOptions();
			}
		});
		
		// Enter to play
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "enter");
		getActionMap().put("enter", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				uiManager.sendPlay(options);
			}
		});
		
		add(welcomeLabel);
		add(highScoreLabel);
	}
	
	private void writeOptions() {
		if (JOptionPane.showConfirmDialog(Menu.this, optionsPanel, "Options", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION)
			options = optionsTxt.getText();
	}
	
	void updateHighScore() {
		List<Score> scores = uiManager.getScores();
		scores.sort((Score o1, Score o2) -> o2.score - o1.score);

		String highScoreTxt = "<html>Times played: " + scores.size();
		
		int n = 1;
		for (Score s : scores) {
			if (n > 15) break;
			String txt = String.format("#%d ~ %d by %s", n, s.score, s.name);
			highScoreTxt += "<br>" + txt;
			n++;
		}
		
		highScoreLabel.setText(highScoreTxt);
	}

}