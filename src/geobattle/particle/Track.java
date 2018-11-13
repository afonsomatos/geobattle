package geobattle.particle;

import java.awt.Graphics2D;
import java.awt.Point;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.extension.Controller;
import geobattle.render.Renderable;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Rect;
import geobattle.util.Palette;

public class Track extends ParticleEffect {

	private GameObject obj;
	private Sprite sprite = new Rect(30, 30, Palette.ORANGE);
	
	private Point[] lastPos;
	private int nextIndex = 0;
	
	private Renderable drawer;
	private Controller posTracker;
	
	private int trackLength = 20;
	
	public Track(Game game, GameObject obj) {
		super(game);
		this.obj = obj;
		
		// Record last positions
		setTrackLength(trackLength);
		posTracker = (gameObj) -> {
			lastPos[nextIndex] = gameObj.getPos();
			nextIndex = (nextIndex + 1) % trackLength;
		};
		
		// Draw last positions
		drawer = (superGfx) -> {
			for (Point p : lastPos) {
				// Don't draw if the track is not complete
				if (p == null) break;
				Graphics2D gfx = (Graphics2D) superGfx.create();
				gfx.translate(p.x, p.y);
				sprite.setAlpha(0.5f);
				sprite.render(gfx);
				gfx.dispose();
			}
		};
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void setTrackLength(int trackLength) {
		this.trackLength = trackLength;
		lastPos = new Point[trackLength];
	}
	
	@Override
	public void start() {
		obj.addController(posTracker);
		obj.addDrawerFirst(drawer);
		obj.getTriggerMap().add("kill", this::destroy);
	}

	@Override
	public void destroy() {
		obj.removeDrawer(drawer);
		obj.removeController(posTracker);
	}
	
}
