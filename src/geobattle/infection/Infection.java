package geobattle.infection;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.living.Living;
import geobattle.schedule.Event;
import geobattle.util.Palette;
import geobattle.util.Util;

public class Infection extends GameObject {
	
	private final static int COLLIDER_PADDING = 30;
	
	private final static Color COLOR = Palette.MAGENTA;
	private final static double ROTATION_SPEED = 0.03;
	
	public final static int INFINITE_HITS = -1;

	private Living host;
	private Event event;
	
	private boolean gone = false;
	
	private int spikes 	= 4;
	private int damage 	= 100;
	private int delay 	= 1000;
	private int radius 	= 30;
	private int hits 	= 3;
	
	public Infection(Game game, Living target) {
		super(game);
		this.host = target;
		event = new Event(delay, true, this::infect);
		setColor(COLOR);
		setRotationSpeed(ROTATION_SPEED);
		
		Collider col = host.getCollider();
		if (col != null)
			surround(col);
		
		host.getTriggerMap().add("die", this::destroy);
		getTriggerMap().add("spawn", this::spawned);
		addDrawer(this::draw);
	}
	
	public void surround(Collider collider) {
		Rectangle bounds = collider.getBounds();
		double len = Math.max(bounds.getWidth(), bounds.getHeight());
		setRadius((int) len / 2 + COLLIDER_PADDING);
	}
	
	public void setSpikes(int spikes) {
		this.spikes = spikes;
	}
	
	public void setHits(int hits) {
		this.hits = hits;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public boolean isGone() {
		return gone;
	}
	
	private void infect() {
		if (hits != INFINITE_HITS && hits <= 0) {
			destroy();
			return;
		}
		
		invertRotation();
		hits -= 1;
		host.suffer(damage);
	}
	
	public void destroy() {
		event.off();
		this.kill();
		gone = true;
	}
	
	private void spawned() {
		game.getSchedule().start(event);
	}
	
	private void draw(Graphics2D gfx) {
		final int x = (int) host.getX();
		final int y = (int) host.getY();
		
		gfx.setColor(getColor());
		gfx.translate(x, y);
		gfx.rotate(getRotation());

		final int thickness = radius / 8;
		final int spike = radius / 3;

		gfx.setStroke(new BasicStroke(thickness));
		Util.Graphics.drawCircle(gfx, 0, 0, radius);
	
		final int xs[] = {radius - spike, radius, radius};
		final int ys[] = {0, -spike / 2, spike / 2};
		
		for (int i = 0; i < spikes; ++i) {
			gfx.rotate(2 * Math.PI / spikes);
			gfx.fillPolygon(xs, ys, 3);
		}
	}

}
