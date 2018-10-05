package geobattle.specials;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import geobattle.core.AliveObject;
import geobattle.core.CollisionHandler.CollisionMatrix;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.util.Counter;
import geobattle.util.Log;
import geobattle.util.Util;

public class WaveSpecial extends Special {

	private int startRadius = 1;
	private int endRadius = 300;
	private int waveThickness = 5;
	private Color color = Color.CYAN;
	private Tag tag = Tag.Player;
	
	public WaveSpecial(Game game) {
		super(game);
	}
	
	public WaveSpecial(Game game, Point pos) {
		super(game, pos);
	}
	
	@Override
	public void send() {
		game.spawnGameObject(new Wave(game, pos.getX(), pos.getY()));
	}
	
	private class Wave extends GameObject {

		private int damage = 300;
		
		private Counter counter = new Counter(startRadius, 2, endRadius) {
			@Override
			public void fire() {
				causeDamage();
				Wave.this.kill();
			}
		};
		
		public Wave(Game game, double x, double y) {
			super(game, x, y);
		}
		
		private void causeDamage() {
			CollisionMatrix colMatrix = game.getCollisionHandler().getCollisionMatrix();
			for (GameObject g : game.getGameObjects()) {
				if (g instanceof AliveObject && distance(g) <= endRadius && colMatrix.collidesWith(tag, g.getTag())) {
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
			final int alpha = 255 - (int) (255 * (counter.getValue() - startRadius) / (endRadius - startRadius));
			gfx.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
			gfx.setStroke(new BasicStroke(waveThickness));
			Util.drawCircle(gfx, (int) getX(), (int) getY(), (int) counter.getValue());
			gfx.dispose();
		}
		
	}

}
