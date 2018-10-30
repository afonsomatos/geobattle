package geobattle.special;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.extension.Orbit;
import geobattle.object.Asteroid;
import geobattle.schedule.Event;

public class AsteroidSpecial extends Special {

	private final static int UNLIMITED_TIME = -1;
	
	private GameObject owner;
	private long time;
	private Event destroyEvent = null;
	
	public AsteroidSpecial(Game game, GameObject owner, long time) {
		super(game);
		this.owner = owner;
		this.time = time;
	}

	@Override
	public void send() {
		Game game = getGame();
		// Setup asteroid
		Asteroid asteroid = new Asteroid(game);
		asteroid.setTag(owner.getTag());
		game.spawnGameObject(asteroid);
		// Create an orbit
		Orbit orbit = new Orbit(asteroid, 100, 0.1);
		owner.addExtension(orbit);
		// When the owner dies the asteroid dies
		owner.getTriggerMap().add("kill", () -> {
			asteroid.kill();
			if (destroyEvent != null)
				destroyEvent.setOff(true);
		});
		// Setup self destroy
		if (time != UNLIMITED_TIME) {
			destroyEvent = new Event();
			destroyEvent.setDelay(time);
			destroyEvent.setRunnable(asteroid::kill);
			game.getSchedule().add(destroyEvent);
		}
	}

}
