package geobattle.weapon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.infection.Infection;
import geobattle.infection.InfectionFactory;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Square;
import geobattle.util.Log;

public class Projectile extends GameObject {
	
	private int damage = 10;
	
	private InfectionFactory infectionFactory = null;
	
	Projectile(Game game) {
		super(game);
		setDamage(damage);
		setCollider(new Collider(this) {
			@Override
			public void enterCollision(Collider other) {
				if (infectionFactory == null) return;
				GameObject obj = other.getGameObject();
				// only the living get infected
				if (!(obj instanceof Living)) return;
				Living target = (Living) obj;
				Infection infection = infectionFactory.create(game, target);
				infection.surround(other);
				game.spawnGameObject(infection);	
			}
		});
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setInfectionFactory(InfectionFactory infectionFactory) {
		this.infectionFactory = infectionFactory;
	}
	
	@Override
	public void update() {
		
		if (this.isOutOfBorders(-10))
			this.kill();
	}

	@Override
	protected void spawn() {
		double velX = getVelX();
		double velY = getVelY();
		double theta = Math.atan(velY / velX);
		if (velX < 0) theta += Math.PI;
		setRotation(theta);
	}

	@Override
	protected void render(Graphics2D gfx) {
		
	}
}
