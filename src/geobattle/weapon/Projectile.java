package geobattle.weapon;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.infection.Infection;
import geobattle.infection.InfectionFactory;
import geobattle.living.Living;
import geobattle.util.Log;

public class Projectile extends GameObject {

	private int damage = 10;
	
	public Projectile(Game game, int x, int y, int width, int height) {
		super(game, x, y);
		setWidth(width);
		setHeight(height);
		setDamage(damage);
		setCollider(new Collider(this));
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void insertInfection(InfectionFactory infectionFactory, Tag tag) {
		setCollider(new Collider(this, tag) {
			@Override
			public void enterCollision(Collider other) {
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
	
	@Override
	public void update() {
		if (this.isOutOfBorders(-10))
			this.kill();
	}

	@Override
	protected void spawn() {
		
	}

	@Override
	protected void render(Graphics2D gfx) {
		
	}
}
