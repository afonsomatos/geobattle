package geobattle.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.collider.Collider;
import geobattle.render.Renderable;

class Debug implements Renderable {

	private Game game;
	
	public Debug(Game game) {
		this.game = game;
	}
	
	public void renderPositionFx(Graphics2D superGfx) {
		Graphics2D gfx = (Graphics2D) superGfx.create();
		
		// Position
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
		
		gfx.dispose();
	}
	
	public void renderColliderFx(Graphics2D superGfx) {
		Graphics2D gfx = (Graphics2D) superGfx.create();
		
		// Draw collision boxes
		for (GameObject g : game.getGameObjects()) {
			Collider col = g.getCollider();
			if (col == null) continue;
			gfx.setColor(new Color(255, 0, 255));
			gfx.draw(col.getBounds());
		}
		
		gfx.dispose();
	}
	
	@Override
	public void render(Graphics2D gfx) {
		renderPositionFx(gfx);
		renderColliderFx(gfx);
	}

}
