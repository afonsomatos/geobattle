package geobattle.launcher;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import geobattle.util.Dispatcher;

@SuppressWarnings("serial")
public class Launcher extends JFrame {

	private JLabel dimLabel 		= new JLabel("Dimensions: ");
	private JLabel screenLabel 		= new JLabel("Screen: ");
	private JTextField widthText	= new JTextField("1280");
	private JTextField heightText	= new JTextField("720");
	private JCheckBox fullScreen	= new JCheckBox("Fullscreen");
	private JButton startBtn		= new JButton("Start");
	private JButton cancelBtn		= new JButton("Cancel");
	private JLabel statusLabel		= new JLabel("Ready");
	
	private JComboBox<String> screenCombo;
	private GraphicsDevice[] gfxDevices;
	
	private Launchable launchable;
	
	public Launcher(Launchable launchable) {
		this.launchable = launchable;
		
		setTitle("Geometry Battle Game Launcher");
		setSize(400, 160);
		
		GridLayout layout = new GridLayout(3, 3, 10, 10);
		
		JPanel panel = new JPanel();
		panel.setLayout(layout);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		widthText.setHorizontalAlignment(JTextField.CENTER);
		heightText.setHorizontalAlignment(JTextField.CENTER);
		
		fullScreen.addActionListener((ActionEvent e) -> fullScreen());
		startBtn.addActionListener((ActionEvent e) -> start());
		cancelBtn.addActionListener((ActionEvent e) -> dispose());
		
		findScreens();
		
		panel.add(dimLabel);
		panel.add(widthText);
		panel.add(heightText);
		panel.add(screenLabel);
		panel.add(screenCombo);
		panel.add(fullScreen);
		panel.add(startBtn);
		panel.add(cancelBtn);
		panel.add(statusLabel);
		
		add(panel);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	private void findScreens() {
        gfxDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

        String[] screens = new String[gfxDevices.length];
        for (int i = 0; i < screens.length; ++i) {
        	screens[i] = i + "";
        	// Rectangle bounds = gfxDevices[i].getDefaultConfiguration().getBounds();
			// String.format("%d ~ %dx%d", i, (int) bounds.getWidth(), (int) bounds.getHeight());
        }
        
        screenCombo = new JComboBox<String>(screens);		
	}
	
	private void fullScreen() {
		boolean checked = fullScreen.isSelected();
		widthText.setEnabled(!checked);
		heightText.setEnabled(!checked);
		if (checked) {
			int screen = screenCombo.getSelectedIndex();
			Rectangle bounds = gfxDevices[screen].getDefaultConfiguration().getBounds();
			widthText.setText((int) bounds.getWidth() + "");
			heightText.setText((int) bounds.getHeight() + "");
		}
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
		opt.setFullScreen(fullScreen.isSelected());
		opt.setScreen(screenCombo.getSelectedIndex());
		opt.setWidth(width);
		opt.setHeight(height);

		changeStatus("Waiting");
		launchable.launch(opt, () -> {
			changeStatus("Launched");
			dispose();
		});
	}
	
}
