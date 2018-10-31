package geobattle.extension;

import geobattle.core.GameObject;

public class Orbit implements Controller {

	private GameObject object;
	
	private int radius;
	private double angVel;
	private double theta = 0;
			
	public Orbit(GameObject object, int radius, double angVel) {
		this.object = object;
		this.radius = radius;
		this.angVel = angVel;
	}
	
	@Override
	public void update(GameObject origin) {
		// Check if there is nothing orbiting around
		if (object == null)
			return;
		
		theta += angVel;
		theta %= Math.PI * 2;
		object.setX((int) (radius * Math.cos(theta) + origin.getX()));
		object.setY((int) (radius * Math.sin(theta) + origin.getY()));
	}
	
}
