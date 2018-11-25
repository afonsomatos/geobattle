package geobattle.particle;

import java.awt.Color;
import java.awt.Rectangle;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.render.Renderable;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Circle;
import geobattle.util.Palette;

/**
 * Renders a circle on top of an object.
 */
public class Shield extends ParticleEffect {

	/**
	 * Object on top of which the shield will be drawn.
	 */
	private GameObject obj;

	private Renderable drawer;
	
	/**
	 * Shield sprite.
	 */
	private Circle sprite;
	
	/**
	 * Creates a shield effect (circle sprite) on top of a certain object with
	 * a certain color. It's automatically destroyed when the object is killed.
	 * Opaque colors will hide the object in most cases.
	 * 
	 * @param game
	 * @param obj
	 * @param col
	 */
	public Shield(Game game, GameObject obj, int radius, Color col) {
		super(game);
		this.obj = obj;
		transform(radius, col);
		drawer = (gfx) -> {
			gfx.translate(obj.getX(), obj.getY());
			sprite.render(gfx);
		};
	}

	@Override
	public void start() {
		obj.addDrawer(drawer);
		obj.getTriggerMap().add("kill", this::destroy);
	}

	@Override
	public void destroy() {
		obj.removeDrawer(drawer);
		
	}
	
	/**
	 * Changes the current's shield radius and color.
	 * 
	 * @param radius
	 * @param col
	 */
	public void transform(int radius, Color col) {
		sprite = new Circle(radius, col);
	}
	
}
