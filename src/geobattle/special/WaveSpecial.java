package geobattle.special;

import java.awt.Color;
import java.awt.Point;

import geobattle.core.Game;
import geobattle.core.Tag;

public class WaveSpecial extends Special {

	private Color color = Color.RED;
	private int damage = 300;
	private int radius = 300;
	private double speed = 0.08;
	
	public WaveSpecial(Game game) {
		super(game);
	}
	
	public WaveSpecial(Game game, Tag tag) {
		super(game);
		setTag(tag);
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public void send() {
		final Tag tag = getTag();
		final Game game = getGame();
		final Point pos = getPos();
		
		Wave wave = new Wave(game, pos.getX(), pos.getY(), getTag());
		wave.setColor(color);
		wave.setTag(tag);
		wave.setSpeed(speed);
		wave.setDamage(damage);
		wave.setRadius(radius);
		game.spawnGameObject(wave);
	}

}
