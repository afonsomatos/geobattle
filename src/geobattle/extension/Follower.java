package geobattle.extension;

import geobattle.core.GameObject;
import geobattle.util.Interval;

public class Follower implements Controller {

	private GameObject target = null;
	
	private Interval<Integer> radar = new Interval<Integer>(null, null);
	
	private boolean following = false;
	private Runnable reached = null;
	
	private boolean active = true;
	
	public Follower() {
		
	}
	
	public Follower(GameObject target) {
		this.target = target;
	}
	
	public void setRadar(Interval<Integer> radar) {
		this.radar = radar;
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
	
	public void setReached(Runnable reached) {
		this.reached = reached;
	}
	
	@Override
	public void update(GameObject gameObject) {
		
		if (target == null) {
			gameObject.stop();
			return;
		}
		
		if (!active) return;

		boolean stopped = false;
		final double dist = gameObject.distance(target);
		
		// Point velocity vector to target
		boolean moving = gameObject.setDirection(target);
		stopped = !moving;
		
		int maxDistance = radar.end == null ? 0 : radar.end;
		int minDistance = radar.start == null ? 0: radar.start;
		
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
