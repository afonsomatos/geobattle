package geobattle.core;

import java.awt.Graphics2D;

import geobattle.render.Renderable;

public abstract class Renderer implements Renderable {

	public abstract void render(Graphics2D gfx);

}
