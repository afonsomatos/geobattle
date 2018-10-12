package geobattle.io;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import geobattle.core.Game;
import geobattle.util.Log;

public class Window extends Canvas {

	private static final long serialVersionUID = -2003953519044767223L;
	
	private String title = "Geometry Battle";
	private MouseInput mouseInput;
	
	private JPanel canvas;
	private ScorePanel scorePanel;
	
	private Game game;
	
	public Window(Game game) {
		this.game = game;
		int width = game.getWidth();
		int height = game.getHeight();
		JFrame container = new JFrame(title);
		
		JPanel panel = (JPanel) container.getContentPane();
		panel.setBackground(Color.BLACK);
		panel.setPreferredSize(new Dimension(width, height));
		panel.setLayout(null);
		
		scorePanel = new ScorePanel(width, height);
		
		// scorepanel here
		panel.add(scorePanel);
		
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
	
	public void showScorePanel(int score) {
		this.setVisible(false);
		scorePanel.setVisible(true);
		scorePanel.setScore(score);
	}
	
	private void saveScore(String name) {
		game.saveScore(name);
		setVisible(true);
		scorePanel.setVisible(false);
		requestFocus();
	}
	
	private class ScorePanel extends JPanel {
		
		JLabel scoreLabel = new JLabel();
		JLabel nameLabel = new JLabel("Enter your name: ");
		JTextField textField = new JTextField();
		
		ScorePanel(int width, int height) {
			setBounds(0, 0, width, height);
			setBackground(Color.BLACK);
			setVisible(false);
			
			setLayout(new GridBagLayout());
			GridBagConstraints gbc= new GridBagConstraints();
            gbc.weightx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            Font font = new Font("arial", Font.PLAIN, 16);
            
            gbc.insets = new Insets(10, 10, 0, 0);
            
			// Setup labels
			scoreLabel.setFont(font);
			scoreLabel.setForeground(Color.WHITE);
			add(scoreLabel, gbc);
            
			nameLabel.setFont(font);
			nameLabel.setForeground(Color.WHITE);
			add(nameLabel, gbc);
						
			// Setup name text field
			textField.setColumns(15);
			textField.setFont(new Font("arial", Font.PLAIN, 15));
			textField.setForeground(Color.WHITE);
			textField.setCaretColor(Color.WHITE);
			textField.setBackground(null);
			textField.setBorder(new LineBorder(Color.WHITE, 1));
			textField.setBorder(BorderFactory.createCompoundBorder(
			        textField.getBorder(), 
			        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			add(textField, gbc);
			
			
			@SuppressWarnings("serial")
			Action action = new AbstractAction()
			{
			    @Override
			    public void actionPerformed(ActionEvent e)
			    {
			        saveScore(textField.getText());
			    }
			};

			textField.addActionListener( action );
			
			gbc.weighty = 1;
			add(new JLabel(), gbc);
		}
		
		public void setScore(int score) {
			scoreLabel.setText("Your score: " + score);
			textField.grabFocus();
		}
	
	}
	
}
