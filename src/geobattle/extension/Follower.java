package geobattle.extension;

import geobattle.core.GameObject;
import geobattle.util.Log;

public class Follower implements Extension {

	private GameObject target = null;
	private int maxDistance = 0;
	private int minDistance = 0;
	
	private boolean finished = true;
	private Runnable reached = null;
	
	public Follower(GameObject target, int minDistance, int maxDistance) {
		this.target = target;
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
	}
	
	public Follower(GameObject target, int minDistance) {
		this(target, minDistance, 0);
	}
	
	public Follower(GameObject target) {
		this(target, 0, 0);
	}
	
	public void setReached(Runnable reached) {
		this.reached = reached;
	}
	
	@Override
	public void update(GameObject gameObject) {
		
		if (!target.isActive()) {
			gameObject.stop();
			return;
		}
		
		final double dist = gameObject.distance(target);
		
		// Point velocity vector to target
		gameObject.setDirection(target);
		
		// If target is too far away or too close
		if (maxDistance != 0 && dist > maxDistance
				|| Math.abs(minDistance - dist) <= gameObject.getVel()) {
			gameObject.stop();
		} else if (dist < minDistance) {
			// Step back
			gameObject.invertDirection();
		} 
		
		// In case there is no velocity, it means it reached its destination
		if (gameObject.getVel() == 0 ^ finished) {
			finished = !finished;
			if (finished)
				reached.run();
		}
		
	}
}
