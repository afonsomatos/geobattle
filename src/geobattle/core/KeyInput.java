package geobattle.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import geobattle.util.Util;
import geobattle.weapons.Arsenal;

class KeyInput implements KeyListener {

	private Game game;
	
	private final Integer[] movementKeys = {
			KeyEvent.VK_W,
			KeyEvent.VK_S,
			KeyEvent.VK_A,
			KeyEvent.VK_D
	};
	
	private boolean up 		= false;
	private boolean down 	= false;
	private boolean left 	= false;
	private boolean right 	= false;
	
	private Integer lastSelected = null;
	
	public KeyInput(Game game) {
		this.game = game;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		Player player = game.getPlayer();

		if (Util.contains(movementKeys, keyCode)) {
			// Movement keys stop all momentum
			player.setAccX(0);
			player.setAccY(0);
			
			double speed = player.getSpeed();
			
			switch (keyCode) {
				case KeyEvent.VK_W:
					up = true;
					player.setVelY(-speed);
					break;
				case KeyEvent.VK_S:
					down = true;
					player.setVelY(speed);
					break;
				case KeyEvent.VK_A:
					left = true;
					player.setVelX(-speed);
					break;
				case KeyEvent.VK_D:
					right = true;
					player.setVelX(speed);
					break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		Player player = game.getPlayer();
		Arsenal ars = player.getArsenal();
		
		switch (keyCode) {
						
			// Switch to last used weapon
			case KeyEvent.VK_Q:
				if (lastSelected != null) {
					int aux = ars.getSelected();
					ars.select(lastSelected);
					lastSelected = aux;
				}
				break;
				
			// Reloading
			case KeyEvent.VK_R:
				player.getWeapon().reload();
				break;
				
			// Pausing
			case KeyEvent.VK_P:
				game.togglePaused();
				break;
				
			// Movement
			case KeyEvent.VK_W:
				up = false;
				break;
				
			case KeyEvent.VK_S:
				down = false;
				break;
			
			case KeyEvent.VK_A:
				left = false;
				break;
				
			case KeyEvent.VK_D:
				right = false;
				break;
				
			default:
				// Slot selecting
				if (keyCode >= '0' && keyCode <= '9') {
					lastSelected = ars.getSelected();
					ars.select(keyCode - '0');
				}
		}

		if (!up && !down) player.setVelY(0);
		if (!left && !right) player.setVelX(0);
	}
	
}