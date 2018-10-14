package geobattle.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import geobattle.util.Dispatcher;

public class Launcher extends JFrame {

	private JLabel dimLabel 		= new JLabel("Dimensions: ");
	private JTextField widthText	= new JTextField("800");
	private JTextField heightText	= new JTextField("600");
	private JButton startBtn		= new JButton("Start");
	private JButton cancelBtn		= new JButton("Cancel");
	private JLabel statusLabel		= new JLabel("Ready");
	
	private Launchable launchable;
	
	public Launcher(Launchable launchable) {
		this.launchable = launchable;
		
		setTitle("Geometry Battle Game Launcher");
		setSize(350, 130);
		
		GridLayout layout = new GridLayout(2, 3, 10, 10);
		
		JPanel panel = new JPanel();
		panel.setLayout(layout);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		widthText.setHorizontalAlignment(JTextField.CENTER);
		heightText.setHorizontalAlignment(JTextField.CENTER);
		
		startBtn.addActionListener((ActionEvent e) -> start());
		cancelBtn.addActionListener((ActionEvent e) -> dispose());
		
		panel.add(dimLabel);
		panel.add(widthText);
		panel.add(heightText);
		panel.add(startBtn);
		panel.add(cancelBtn);
		panel.add(statusLabel);
		
		add(panel);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	private void changeStatus(String status) {
		statusLabel.setText(status);
	}
	
	private void start() {
		int width, height;
		
		try {
			width = Integer.parseInt(widthText.getText());
			height = Integer.parseInt(heightText.getText());
			if (width <= 0 || height <= 0)
				throw new NumberFormatException();
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "Invalid size", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		LauncherOption opt = new LauncherOption();
		opt.setWidth(width);
		opt.setHeight(height);

		changeStatus("Waiting");
		launchable.launch(opt, () -> {
			changeStatus("Launched");
			dispose();
		});
	}
	
	public interface Launchable {
		void launch(LauncherOption opt, Dispatcher dispatcher);
	}

	public class LauncherOption {
		
		private int width;
		private int height;
		
		public void setWidth(int width) {
			this.width = width;
		}
		
		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
		
	}
	
}
