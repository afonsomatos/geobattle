package geobattle.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import geobattle.io.IOManager;
import geobattle.io.KeyInput;
import geobattle.io.MouseInput;
import geobattle.render.Renderable;

@SuppressWarnings("serial")
class Play extends JPanel {
		
	private Canvas canvas;
	
	Play(UIManager uiManager) {
		
		Color bg = uiManager.getUIStyle().getBackground();
		
		Dimension size = uiManager.getSize();
		canvas = new Canvas();
		canvas.setSize(size.width, size.height);
		canvas.setBackground(bg);
		
		setLayout(null);
		add(canvas);
		
		IOManager ioManager = uiManager.getIOManager();
		KeyInput keyInput = ioManager.getKeyInput();
		MouseInput mouseInput = ioManager.getMouseInput();
		
		canvas.addKeyListener(keyInput);
		canvas.addMouseListener(mouseInput);
		canvas.addMouseMotionListener(mouseInput);
		
		setPreferredSize(size);

		// No need to see the OS cursor
		hideCursor();
	}

	private void hideCursor() {
		setCursor(getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(),
                null));
	}
	
	void renderFrame(Renderable renderable) {
		BufferStrategy bs = canvas.getBufferStrategy();
		Graphics2D gfx = (Graphics2D) canvas.getBufferStrategy().getDrawGraphics();
		
		renderable.render(gfx);
		
		gfx.dispose();
		bs.show();
	}
	
	void getReady() {
		canvas.createBufferStrategy(3);
		canvas.requestFocus();
	}
	
}
