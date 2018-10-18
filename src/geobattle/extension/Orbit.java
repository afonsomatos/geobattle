package geobattle.extension;

import geobattle.core.GameObject;

public class Orbit implements Extension {

	private GameObject object;
	
	private int radius = 100;
	private double angVel = 0.15;
	private double theta = 0;
			
	public Orbit(GameObject object) {
		this.object = object;
	}
	
	@Override
	public void update(GameObject origin) {
		if (object == null) return;
		theta = (theta + angVel) % (Math.PI * 2);
		int x = (int) (radius * Math.cos(theta) + origin.getX());
		int y = (int) (radius * Math.sin(theta) + origin.getY());
		object.setX(x);
		object.setY(y);
	}
}
