package geobattle.particle;

import geobattle.core.Game;

/**
 * Renders game special effects.
 */
abstract class ParticleEffect {

	protected Game game;
	
	ParticleEffect(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	/**
	 * Start rendering particle effect.
	 */
	public abstract void start();
	
	/**
	 * Stops rendering particle effect.
	 */
	public abstract void destroy();
}
