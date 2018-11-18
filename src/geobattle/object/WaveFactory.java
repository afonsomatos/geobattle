package geobattle.object;

import java.awt.Color;
import geobattle.core.Game;

public class WaveFactory {

	private Color color = Color.RED;
	private int damage = 300;
	private int radius = 150;
	private int thickness = 30;
	private double speed = 0.04;
	
	public WaveFactory setThickness(int thickness) {
		this.thickness = thickness;
		return this;
	}
	public WaveFactory setDamage(int damage) {
		this.damage = damage;
		return this;
	}
	
	public WaveFactory setSpeed(double speed) {
		this.speed = speed;
		return this;
	}
	
	public WaveFactory setRadius(int radius) {
		this.radius = radius;
		return this;
	}

	public WaveFactory setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public Wave create(Game game) {
		Wave wave = new Wave(game);
		wave.setColor(color);
		wave.setSpeed(speed);
		wave.setDamage(damage);
		wave.setThickness(thickness);
		wave.setRadius(radius);
		return wave;
	}

}
