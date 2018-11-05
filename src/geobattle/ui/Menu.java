package geobattle.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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

		String text = "<html>Welcome to Geometry Battle!";
		
		JLabel welcomeLabel = new JLabel(text);
		welcomeLabel.setForeground(fg);
		welcomeLabel.setFont(font);

		Button playBtn = new Button("Play");
		playBtn.setFont(font);
		playBtn.setForeground(bg);
		playBtn.setBackground(fg);
		playBtn.setPressedBackground(bg);
		playBtn.setPressedForeground(fg);
		
		Button optsBtn = new Button("Options");
		optsBtn.setFont(font);
		optsBtn.setForeground(bg);
		optsBtn.setBackground(fg);
		optsBtn.setPressedBackground(bg);
		optsBtn.setPressedForeground(fg);
		
		Button quitBtn = new Button("Quit");
		quitBtn.setFont(font);
		quitBtn.setForeground(bg);
		quitBtn.setBackground(fg);
		quitBtn.setPressedBackground(bg);
		quitBtn.setPressedForeground(fg);
		
		optionsPanel = new JPanel();
		optionsPanel.add(new JScrollPane(optionsTxt));
		
		highScoreLabel = new JLabel();
		highScoreLabel.setForeground(fg);
		highScoreLabel.setFont(font);
				
		playBtn.addActionListener(e -> {
			uiManager.getOptions().setSettings(options);
			uiManager.sendLoad();
		});
		
		quitBtn.addActionListener(e -> {
			if (JOptionPane.showConfirmDialog(Menu.this, "Are you sure?", "Quit", 
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
				uiManager.sendQuit();	
		});
		
		optsBtn.addActionListener(e -> writeOptions());

		setLayout(new GridBagLayout());

		Box buttons = Box.createHorizontalBox();
		buttons.add(playBtn);
		buttons.add(Box.createHorizontalStrut(10));
		buttons.add(optsBtn);
		buttons.add(Box.createHorizontalStrut(10));
		buttons.add(quitBtn);
		buttons.setAlignmentX(Box.LEFT_ALIGNMENT);
		
		Box box = Box.createVerticalBox();
		box.add(welcomeLabel);
		box.add(Box.createVerticalStrut(10));
		box.add(buttons);
		box.add(Box.createVerticalStrut(10));
		box.add(highScoreLabel);
		
		box.setOpaque(true);
		box.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(box, new GridBagConstraints());
	}
	
	private void writeOptions() {
		if (JOptionPane.showConfirmDialog(Menu.this, optionsPanel, "Options", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION)
			options = optionsTxt.getText();
	}

}