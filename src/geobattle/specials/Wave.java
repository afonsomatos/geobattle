package geobattle.specials;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.CollisionHandler.CollisionMatrix;
import geobattle.living.AliveObject;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.util.Counter;
import geobattle.util.Util;

class Wave extends GameObject {

	private int damage = 300;
	private int thickness = 30;
	private int radius = 300;
	private double speed = 0.08;
	
	private Counter counter = new Counter(0, speed * radius, radius) {
		@Override
		public void fire() {
			causeDamage();
			Wave.this.kill();
		}
	};
	
	public Wave(Game game, double x, double y, Tag tag) {
		super(game, x, y);
		setTag(tag);
	}
	
	@Override
	public void setSpeed(double speed) {
		counter.setStep(radius * speed);
	}
	
	void setThickness(int thickness) {
		this.thickness = thickness;
	}
	
	void setRadius(int radius) {
		this.radius = radius;
		counter.setEnd(radius);
	}
	
	void setDamage(int damage) {
		this.damage = damage;
	}
	
	private void causeDamage() {
		final Tag tag = getTag();
		final Game game = getGame();
		
		CollisionMatrix colMatrix = game.getCollisionHandler().getCollisionMatrix();
		for (GameObject g : game.getGameObjects()) {
			if (g instanceof AliveObject && distance(g) <= radius && colMatrix.collidesWith(tag, g.getTag())) {
				((AliveObject) g).suffer(damage);
			}
		}
	}
	
	@Override
	public void tick() {
		counter.tick();
	}
	
	@Override
	public void render(Graphics2D superGfx) {
		super.render(superGfx);
		Graphics2D gfx = (Graphics2D) superGfx.create();
		final int alpha = 255 - (int) (255 * counter.getValue() / radius);
		Color color = getColor();
		gfx.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
		gfx.setStroke(new BasicStroke(thickness));
		Util.drawCircle(gfx, (int) getX(), (int) getY(), (int) counter.getValue());
		gfx.dispose();
	}
	
}
