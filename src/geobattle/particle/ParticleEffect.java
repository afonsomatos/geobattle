package geobattle.particle;

import geobattle.core.Game;

abstract class ParticleEffect {

	protected Game game;
	
	ParticleEffect(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public abstract void start();
	public abstract void destroy();
}
