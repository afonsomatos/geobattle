package geobattle.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import geobattle.util.Log;

public class UIStyle {

	private Color foreground = Color.WHITE;
	private Color background = Color.BLACK;
	private Font font		 = new Font("arial", Font.PLAIN, 16);
	
	public Font getFont() {
		return font;
	}

	public Color getForeground() {
		return foreground;
	}
	
	public Color getBackground() {
		return background;
	}
	
	public void setBackground(Color background) {
		this.background = background;
	}
	
}
