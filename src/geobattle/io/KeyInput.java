package geobattle.io;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import geobattle.core.Game;
import geobattle.core.Game.State;
import geobattle.living.Player;
import geobattle.object.ArrowKeysFollower;
import geobattle.util.Log;
import geobattle.util.Util;
import geobattle.weapon.Arsenal;

public class KeyInput extends KeyAdapter {

	private Game game;
	private boolean active = true;
	
	private final static Integer[] MOVEMENT_KEYS = {
			KeyEvent.VK_W,
			KeyEvent.VK_S,
			KeyEvent.VK_A,
			KeyEvent.VK_D
	};
	
	private int dirmask = 0;
	
	private HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public KeyInput(Game game) {
		this.game = game;
	}
	
	public boolean isPressingKey(int keyCode) {
		return keys.containsKey(keyCode) && keys.get(keyCode);
	}
	
	public void updateMovement() {
		if (!active) return;
				
		Player player = game.getPlayer();
		double speed = player.getSpeed();
		double newVelX, newVelY;
		
		if ((dirmask & 3) == 0)
			newVelX = 0;
		else if ((dirmask & 1) != 0)
			newVelX = speed;
		else
			newVelX = -speed;
		
		if ((dirmask & 12) == 0)
			newVelY = 0;
		else if ((dirmask & 8) != 0)
			newVelY = speed;
		else
			newVelY = -speed;
		
		if (newVelX != 0 && newVelY != 0) {
			// In case of a diagonal movement, distribute speed between axis
			newVelX /= Math.sqrt(2);
			newVelY /= Math.sqrt(2);
		}
		
		player.setVelX(newVelX);
		player.setVelY(newVelY);
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys.put(e.getKeyCode(), true);
		if (!active) return;
		
		int keyCode = e.getKeyCode();
		State state = game.getState();
		
		if (state == State.PLAYING) {
			if (Util.contains(MOVEMENT_KEYS, keyCode)) {
				if (keyCode == KeyEvent.VK_D)
					dirmask = (dirmask & ~2) | 1;
				else if (keyCode == KeyEvent.VK_A)
					dirmask = (dirmask & ~1) | 2;
				else if (keyCode == KeyEvent.VK_W)
					dirmask = (dirmask & ~8) | 4;
				else if (keyCode == KeyEvent.VK_S)
					dirmask = (dirmask & ~4) | 8;
				
				updateMovement();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys.put(e.getKeyCode(), false);
		
		int keyCode = e.getKeyCode();
		State state = game.getState();
		
		if (state == State.PLAYING) {
			Player player = game.getPlayer();
			Arsenal ars = player.getArsenal();
			// Special attack
			if (keyCode == KeyEvent.VK_Z) {
				player.sendSpecial();
				// Prevent concurrent exception
				// game.getSchedule().next(() -> player.sendSpecial());
			} else if (keyCode == KeyEvent.VK_R) {
				player.sendReload();
			} else if (keyCode == KeyEvent.VK_P) {
				game.togglePaused();
			} else if (keyCode == KeyEvent.VK_Q) {
				ars.swap();
			} else if (keyCode >= '0' && keyCode <= '9') {
				ars.select(keyCode - '0');
			} else if (Util.contains(MOVEMENT_KEYS, keyCode)) {
				if (keyCode == KeyEvent.VK_D)
					dirmask &= ~1;
				else if (keyCode == KeyEvent.VK_A)
					dirmask &= ~2;
				else if (keyCode == KeyEvent.VK_W)
					dirmask &= ~4;
				else if (keyCode == KeyEvent.VK_S)
					dirmask &= ~8;
				
				updateMovement();
			}
		}
	}
	
}