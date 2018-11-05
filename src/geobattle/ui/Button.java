package geobattle.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ButtonModel;
import javax.swing.JButton;

@SuppressWarnings("serial")
class Button extends JButton {
	  
	private Color pressedForeground = Color.BLACK;
	private Color pressedBackground = Color.WHITE;
	
    Button() {
    	this(null);
    }
    
    Button(String text) {
    	super(text);
    	super.setContentAreaFilled(false);
    	setForeground(Color.WHITE);
    	setBackground(Color.BLACK);
    	setFocusPainted(false);
    	setBorderPainted(false);
    }
    
	Color getPressedForeground() {
		return pressedForeground;
	}

	void setPressedForeground(Color pressedForeground) {
		this.pressedForeground = pressedForeground;
	}

	Color getPressedBackground() {
		return pressedBackground;
	}

	void setPressedBackground(Color pressedBackground) {
		this.pressedBackground = pressedBackground;
	}

	@Override
	public void paint(Graphics g) {
		Color oldFg, newFg;
		newFg = oldFg = getForeground();
		ButtonModel mod = getModel();
			
		if (mod.isPressed()) {
			newFg = pressedForeground;
			g.setColor(pressedBackground);
		} else {
			g.setColor(getBackground());
		}
		
		g.fillRect(0, 0, getWidth(), getHeight());
		setForeground(newFg);
		super.paintComponent(g);
		setForeground(oldFg);
	}
	
}