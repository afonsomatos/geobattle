package geobattle.io;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import geobattle.core.Game;
import geobattle.core.Game.State;
import geobattle.living.Player;
import geobattle.util.Util;
import geobattle.weapon.Arsenal;

public class KeyInput extends KeyAdapter {

	private Game game;
	
	private final static Integer[] MOVEMENT_KEYS = {
			KeyEvent.VK_W,
			KeyEvent.VK_S,
			KeyEvent.VK_A,
			KeyEvent.VK_D
	};
	
	private Integer lastSelected = null;
	private int dirmask = 0;
	
	public KeyInput(Game game) {
		this.game = game;
	}
	
	public void updateMovement() {
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

		player.setVelX(newVelX);
		player.setVelY(newVelY);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		State state = game.getState();
		
		if (state == State.MENU) {
			if (keyCode == KeyEvent.VK_ENTER) {
				game.start();
			}
		} else if (state == State.PLAYING) {
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