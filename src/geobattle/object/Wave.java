package geobattle.object;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.collider.CollisionMatrix;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.living.Living;
import geobattle.render.sprite.shapes.Aura;
import geobattle.util.Counter;
import geobattle.util.Log;

public class Wave extends GameObject {

	private int damage = 300;
	private int thickness = 30;
	private int radius = 300;
	private double speed = 0.08;
	
	private Counter counter = new Counter(0, speed * radius, radius) {
		@Override
		public void fire() {
			Wave.this.kill();
		}
	};
	
	public Wave(Game game) {
		super(game);
		getTriggerMap().add("spawn", this::causeDamage);
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
			if (g instanceof Living && distance(g) <= radius && colMatrix.collidesWith(tag, g.getTag())) {
				((Living) g).suffer(damage);
			}
		}
	}
	
	@Override
	public void update() {
		counter.tick();
		int currRadius = (int) counter.getValue();
		int alpha = 255 - (int) (255 * currRadius / radius);
		Color color = getColor();
		Color auraColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		setSprite(new Aura(currRadius, thickness, auraColor));
	}

}
