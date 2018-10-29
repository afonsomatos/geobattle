package geobattle.io;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Game.State;
import geobattle.living.Player;
import geobattle.util.Log;

public class MouseInput extends MouseAdapter {

	private Game game;
	private GameObject gameObject = null;
	private boolean active = true;
	
	public MouseInput(Game game) {
		this.game = game;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}
	
	public void updateMouseObject(MouseEvent e) {
		if (gameObject != null) {
			gameObject.setX(e.getX() / game.getScale());
			gameObject.setY(e.getY() / game.getScale());
		}
	}
	
	public GameObject getMouseObject() {
		return gameObject;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!active) return;
		if (game.getState() == State.PLAYING) {
			game.getPlayer().fire();
			// Prevents concurrent modification
			// game.getSchedule().next(() -> game.getPlayer().fire());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!active) return;
		if (game.getState() == State.PLAYING) {
			Player player = game.getPlayer();
			if (player != null) {
				player.setFiring(true);
			}
		}
		updateMouseObject(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!active) return;
		if (game.getState() == State.PLAYING) {
			Player player = game.getPlayer();
			if (player != null) {
				player.setFiring(false);
			}
		}
		updateMouseObject(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!active) return;
		updateMouseObject(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (!active) return;
		updateMouseObject(e);
		if (gameObject != null)
			gameObject.setHidden(false);
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		if (!active) return;
		if (gameObject != null)
			gameObject.setHidden(true);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!active) return;
		updateMouseObject(e);	
	}
	
}