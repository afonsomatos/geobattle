package geobattle.input;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Game.State;
import geobattle.weapon.Weapon;

public class MouseInput implements MouseListener, MouseMotionListener {

	private Game game;
	private GameObject mouseFollower;
	
	public MouseInput(Game game) {
		this.game = game;
		
		mouseFollower = new GameObject(game) {
			@Override
			public void update() {

			}
			@Override
			public void render(Graphics2D gfx) {
				
			}
			@Override
			protected void spawn() {
				// TODO Auto-generated method stub
				
			}
		};
		game.spawnGameObject(mouseFollower);		
	}
	
	public void updateMouseFollower(MouseEvent e) {
		mouseFollower.setX(e.getX());
		mouseFollower.setY(e.getY());
	}
	
	public GameObject getMouseFollower() {
		return mouseFollower;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (game.getState() != State.PLAYING) return;
		game.getSchedule().next(() -> game.getPlayer().fire());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (game.getState() != State.PLAYING) return;
		game.getPlayer().setFiring(true);
		updateMouseFollower(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (game.getState() != State.PLAYING) return;

		Weapon weapon = game.getPlayer().getWeapon();
		if (weapon == null)
			return;
		
		game.getPlayer().setFiring(false);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (game.getState() != State.PLAYING) return;
		updateMouseFollower(e);	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (game.getState() != State.PLAYING) return;
		updateMouseFollower(e);	
	}
	
}