package geobattle.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import geobattle.core.Game;
import geobattle.core.Game.State;
import geobattle.io.IOManager;
import geobattle.io.KeyInput;
import geobattle.io.MouseInput;
import geobattle.render.Renderable;
import geobattle.util.Log;
import geobattle.util.Palette;

@SuppressWarnings("serial")
public class Play extends JPanel {
		
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

		// Ctrl-Q to quit
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK, false), "menu");
		getActionMap().put("menu", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// if (game.getState() != State.PLAYING) return;
				uiManager.sendPause(true);
				if (JOptionPane.showConfirmDialog(Play.this, "Are you sure?", "Back to Menu", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
				{
					uiManager.sendPause(false);
					uiManager.sendGameOver();
				}
				uiManager.sendPause(false);
				
			}				
		});
		
	}

	void renderFrame(Renderable renderable) {
		BufferStrategy bs = canvas.getBufferStrategy();
		Graphics2D gfx = (Graphics2D) canvas.getBufferStrategy().getDrawGraphics();
		
		renderable.render(gfx);
		
		gfx.dispose();
		bs.show();
	}
	
	void focusCanvas() {
		canvas.requestFocus();
	}
	
	void updateCanvas() {
		canvas.createBufferStrategy(3);
	}
	
}
