package geobattle.weapon.projectile;

import geobattle.core.Game;

public class BlockBullet extends Projectile {

	public BlockBullet(Game game, int x, int y, int width, int height) {
		super(game, x, y);
		setWidth(width);
		setHeight(height);
	}

}
