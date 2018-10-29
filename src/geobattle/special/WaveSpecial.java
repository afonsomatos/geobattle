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
	
	public WaveSpecial setDamage(int damage) {
		this.damage = damage;
		return this;
	}
	
	public WaveSpecial setSpeed(double speed) {
		this.speed = speed;
		return this;
	}
	
	public WaveSpecial setRadius(int radius) {
		this.radius = radius;
		return this;
	}

	public WaveSpecial setColor(Color color) {
		this.color = color;
		return this;
	}
	
	@Override
	public void send() {
		final Tag tag = getTag();
		final Game game = getGame();
		final Point pos = getPos();
		
		Wave wave = new Wave(game, pos.getX(), pos.getY(), getTag());
		wave.setColor(color);
		wave.setSpeed(speed);
		wave.setDamage(damage);
		wave.setRadius(radius);
		game.spawnGameObject(wave);
	}

}
