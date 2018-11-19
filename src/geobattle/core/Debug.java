package geobattle.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.collider.Collider;
import geobattle.render.Renderable;
import geobattle.util.Palette;

/**
 * Renders useful artifacts for each active game object.
 */
class Debug implements Renderable {

	private Game game;

	private final static int VELOCITY_VECTOR_SIZE = 50;
	private final static Color VELOCITY_VECTOR_COLOR = Palette.GREEN;
	private final static Color VELOCITY_LABEL_COLOR = Palette.GREEN;

	private final static int POSITION_CROSS_SIZE = 50;
	private final static Color POSITION_CROSS_COLOR = Palette.RED;
	private final static Color POSITION_LABEL_COLOR = Palette.RED;

	/**
	 * Color of the collision box drawn around a game object
	 */
	private final static Color COLISION_BOX_COLOR = Palette.PURPLE;

	public Debug(Game game) {
		this.game = game;
	}

	/**
	 * Renders velocity vector.
	 * 
	 * @param obj
	 * @param superGfx
	 */
	public void renderVelocity(GameObject obj, Graphics2D superGfx) {
		double vel = obj.getVel();
		int vx = (int) (VELOCITY_VECTOR_SIZE * obj.getVelX() / vel);
		int vy = (int) (VELOCITY_VECTOR_SIZE * obj.getVelY() / vel);

		Graphics2D gfx = (Graphics2D) superGfx.create();
		gfx.setColor(VELOCITY_VECTOR_COLOR);
		int x = (int) obj.getX();
		int y = (int) obj.getY();
		gfx.drawLine(x, y, x + vx, y + vy);
		gfx.setColor(VELOCITY_LABEL_COLOR);
		gfx.drawString(String.format("v=%.3f", vel), x + 5, y - 5);
		gfx.dispose();
	}

	/**
	 * Renders position indicator.
	 * 
	 * @param obj
	 * @param superGfx
	 */
	public void renderPosition(GameObject obj, Graphics2D superGfx) {
		Graphics2D gfx = (Graphics2D) superGfx.create();
		gfx.setColor(POSITION_CROSS_COLOR);
		gfx.setStroke(new BasicStroke(1));
		int x = (int) obj.getX();
		int y = (int) obj.getY();
		gfx.drawLine(
				x - POSITION_CROSS_SIZE / 2,
				y,
				x + POSITION_CROSS_SIZE / 2,
				y);
		gfx.drawLine(
				x,
				y - POSITION_CROSS_SIZE / 2,
				x,
				y + POSITION_CROSS_SIZE / 2);
		gfx.setColor(POSITION_LABEL_COLOR);
		gfx.drawString(String.format("(%d, %d)", x, y), x + 5, y + 20);
		gfx.dispose();
	}

	/**
	 * Renders collision box.
	 * 
	 * @param obj
	 * @param superGfx
	 */
	public void renderCollider(GameObject obj, Graphics2D superGfx) {
		Collider col = obj.getCollider();
		if (col != null) {
			Graphics2D gfx = (Graphics2D) superGfx.create();
			gfx.setColor(COLISION_BOX_COLOR);
			gfx.draw(col.getBounds());
			gfx.dispose();
		}
	}

	@Override
	public void render(Graphics2D gfx) {
		for (GameObject g : game.getGameObjects()) {
			if (!g.isActive())
				continue;
			renderPosition(g, gfx);
			renderCollider(g, gfx);
			renderVelocity(g, gfx);
		}
	}

}
