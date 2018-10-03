package geobattle._test;

import java.awt.Color;

import geobattle.core.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
//import math.geom2d.Vector2D;

class Blackhole extends GameObject {
	
	private int radius = 600;
	private double dragAcc = 0.1;
	
	public Blackhole(Game game, double x, double y) {
		super(game, x, y);
		setWidth(radius);
		setColor(new Color(0, 255, 0, 100));
		setHeight(radius);
		
		Collider col = new Collider(this) {
			@Override
			public void handleCollision(Collider other) {
				GameObject obj = other.getGameObject();
				/*
				Vector2D vec = new Vector2D(Blackhole.this.getX() - obj.getX(), Blackhole.this.getY() - obj.getY()).normalize();
				vec = vec.times(dragAcc);
				obj.setAccX(vec.getX());
				obj.setAccY(vec.getY());
				*/
			}
		};
		
		col.setTag(Tag.Blackhole);
		setCollider(col);
		
	}
	
}
