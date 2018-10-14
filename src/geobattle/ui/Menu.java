package geobattle.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

class Menu extends JPanel {
	
	Menu(Runnable starter) {
		setBackground(Color.BLACK);

		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		String text = "<html>Welcome to Geometry Battle!";
		text += "<br>Please press [ENTER] to start playing!";
		
		JLabel label = new JLabel(text);
		label.setForeground(Color.WHITE);

		Action start = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("hey");
				starter.run();
			}
		};
		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "enter");
		getActionMap().put("enter", start);
		
		add(label);
	}
	
}
