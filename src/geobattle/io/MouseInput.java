package geobattle.io;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Game.State;
import geobattle.living.Player;

public class MouseInput extends MouseAdapter {

	private Game game;
	private GameObject gameObject = null;
	
	public MouseInput(Game game) {
		this.game = game;
	}
	
	public void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}
	
	public void updateMouseObject(MouseEvent e) {
		if (gameObject != null) {
			gameObject.setX(e.getX());
			gameObject.setY(e.getY());
		}
	}
	
	public GameObject getMouseObject() {
		return gameObject;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (game.getState() == State.PLAYING) {
			game.getPlayer().fire();
			// Prevents concurrent modification
			// game.getSchedule().next(() -> game.getPlayer().fire());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
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
		updateMouseObject(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (gameObject != null)
			gameObject.setHidden(false);
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		if (gameObject != null)
			gameObject.setHidden(true);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		updateMouseObject(e);	
	}
	
}