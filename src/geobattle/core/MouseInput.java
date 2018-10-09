package geobattle.core;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import geobattle.weapon.Weapon;

class MouseInput implements MouseListener, MouseMotionListener {

	private Game game;
	private GameObject mouseFollower;
	
	public MouseInput(Game game) {
		this.game = game;
		
		mouseFollower = new GameObject(game) {
			@Override
			public void update() {

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
		game.getSchedule().next(() -> game.getPlayer().fire());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		game.getPlayer().setFiring(true);
		updateMouseFollower(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		Weapon weapon = game.getPlayer().getWeapon();
		if (weapon == null)
			return;
		
		game.getPlayer().setFiring(false);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
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
		updateMouseFollower(e);	
	}
	
}