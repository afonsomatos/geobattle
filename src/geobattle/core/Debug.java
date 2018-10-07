package geobattle.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

class Debug implements Renderer {

	private Game game;
	
	public Debug(Game game) {
		this.game = game;
	}
	
	@Override
	public void render(Graphics2D gfx) {
		for(GameObject g : game.getGameObjects()) {
			if (g.isHidden()) continue;
			gfx.setColor(Color.RED);
			gfx.setStroke(new BasicStroke(1));
			gfx.drawLine(
					(int) g.getX() - 50,
					(int) g.getY(),
					(int) g.getX() + 50,
					(int) g.getY()
					);
			gfx.drawLine(
					(int) g.getX(),
					(int) g.getY() - 50,
					(int) g.getX(),
					(int) g.getY() + 50
					);
		}
	}

}
