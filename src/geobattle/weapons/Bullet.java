package geobattle.weapons;

import geobattle.core.Game;

public class Bullet extends Projectile {
	
	public Bullet(Game game, int x, int y) {
		super(game, x, y);
		setWidth(8);
		setHeight(8);
	}
	
}
