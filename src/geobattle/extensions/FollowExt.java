package geobattle.extensions;

import geobattle.core.GameObject;

public class FollowExt extends Extension {

	private GameObject target = null;
	private int maxDistance = 0;
	private int minDistance = 0;
	
	public FollowExt(GameObject target, int minDistance, int maxDistance) {
		this.target = target;
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
	}
	
	public FollowExt(GameObject target, int minDistance) {
		this(target, minDistance, 0);
	}
	
	public FollowExt(GameObject target) {
		this(target, 0, 0);
	}
	
	@Override
	public void tick(GameObject gameObject) {
		final double dist = gameObject.distance(target);
		
		// If target is too far away, stop
		if (maxDistance != 0 && dist > maxDistance) {
			gameObject.setVelX(0);
			gameObject.setVelY(0);
			return;
		}
		
		gameObject.setDirection(target);
		
		// If this object is too close to its target
		if (gameObject.distance(target) < minDistance) {
			// Step back
			gameObject.setVelX(-gameObject.getVelX());
			gameObject.setVelY(-gameObject.getVelY());
		}
	}

}
