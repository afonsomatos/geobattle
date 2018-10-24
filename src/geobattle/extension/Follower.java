package geobattle.extension;

import geobattle.core.GameObject;

public class Follower implements Extension {

	private GameObject target = null;
	private double maxDistance = 0;
	private double minDistance = 0;
	
	private boolean following = false;
	private Runnable reached = null;
	
	private boolean active = true;
	
	public Follower(GameObject target, double minDistance, double maxDistance) {
		this.target = target;
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
	}
	
	public Follower(GameObject target, double minDistance) {
		this(target, minDistance, 0);
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isFollowing() {
		return following;
	}
	
	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}
	
	public Follower(GameObject target) {
		this(target, 0, 0);
	}
	
	public void setReached(Runnable reached) {
		this.reached = reached;
	}
	
	@Override
	public void update(GameObject gameObject) {
		if (!active) return;

		boolean stopped = false;
		final double dist = gameObject.distance(target);
		
		// Point velocity vector to target
		boolean moving = gameObject.setDirection(target);
		stopped = !moving;
		
		// If target is too far away or too close
		if (maxDistance != 0 && dist > maxDistance
				|| Math.abs(minDistance - dist) <= gameObject.getVel()) {
			gameObject.stop();
			stopped = true;
			following = false;
		} else if (dist < minDistance) {
			// Step back
			gameObject.invertDirection();
		} else {
			following = true;
		}
		
		if (stopped && reached != null)
			reached.run();
	}
}
