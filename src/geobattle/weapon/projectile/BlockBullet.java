package geobattle.weapon.projectile;

import java.awt.Graphics2D;

import geobattle.core.Game;

public class BlockBullet extends Projectile {

	public BlockBullet(Game game, int x, int y, int width, int height) {
		super(game, x, y);
		setWidth(width);
		setHeight(height);
	}

	@Override
	public void render(Graphics2D gfx) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void spawn() {
		// TODO Auto-generated method stub
		
	}

}
