package geobattle.io;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.stream.IntStream;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import geobattle.core.Game;
import geobattle.living.Player;
import geobattle.special.slot.SpecialSlot;
import geobattle.weapon.WeaponSet;

@SuppressWarnings("serial")
public class KeyInput extends KeyAdapter {

	private Game game;
	private boolean active = true;
	private int dirmask = 0;
	
	private HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
	
	public KeyInput(Game game) {
		this.game = game;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void bindAll() {
		
		bind("released P", game::togglePause, true);
		
		bind("released R", this::sendReload);
		bind("released Q", this::sendSwap);
		
		IntStream.range(1, 9).forEach(i ->
			bind("released " + i, () -> {
				WeaponSet wset = game.getPlayer().getWeaponSet();
				if (i - 1 >= wset.size) return;
				wset.select(i - 1);
			})
		);
		
		char specialSlots[] = {'Z', 'X', 'C', 'V', 'B'};
		for (int i = 0; i < specialSlots.length; ++i) {
			final int slot = i;
			char c = specialSlots[i];
			bind("released " + c, () -> {
				SpecialSlot s = game.getPlayer().getSpecialSet().get(slot);
				if (s != null)
					s.invoke();
			});
		}
		
		bind("released SPACE", () ->  game.getPlayer().setFiring(false));
		bind("pressed SPACE",  () ->  game.getPlayer().setFiring(true));
		
		char moveKeys[] = {'D', 'A', 'W', 'S'};
		for (int i = 0; i < moveKeys.length; ++i) {
			char c = moveKeys[i];
			final int mask = (int) Math.pow(2, i);
			
			bind("released " + c, () -> {
				dirmask &= ~mask;
				updateMovement();
			});
			
			bind("pressed " + c, () -> {
				int negMask = mask <= 2 ? 3 : 12;
				dirmask &= ~negMask;
				dirmask |= mask;
				updateMovement();
			});
			
		}
		
		bind("ctrl Q", game.getUIManager()::askQuit);
	}
	
	private void bind(String key, Runnable runnable) {
		bind(key, runnable, false);
	}
	
	private void bind(String key, Runnable runnable, boolean force) {

		game.getUIManager()
			.getInputMap()
			.put(KeyStroke.getKeyStroke(key), key);
		
		game.getUIManager()
			.getActionMap()
			.put(key, new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (active || force)
						runnable.run();
				}
			});
		
	}
	
	private void sendSwap() {
		game.getPlayer().getWeaponSet().swap();
	}
	
	private void sendReload() {
		game.getPlayer().sendReload();
	}
	
	public boolean isPressingKey(int keyCode) {
		return keys.containsKey(keyCode) && keys.get(keyCode);
	}
	
	private void updateMovement() {
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
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys.put(e.getKeyCode(), false);
	}
	
}