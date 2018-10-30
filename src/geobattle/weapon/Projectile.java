package geobattle.weapon;

import java.awt.Graphics2D;

import geobattle.collider.Collider;
import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.infection.Infection;
import geobattle.infection.InfectionFactory;
import geobattle.living.Living;
import geobattle.special.Wave;
import geobattle.special.WaveFactory;

public class Projectile extends GameObject {
	
	private int damage = 10;
	
	private WaveFactory waveFactory = null;
	private InfectionFactory infectionFactory = null;
	
	Projectile(Game game) {
		super(game);
		setDamage(damage);
		setCollider(new Collider(this) {
			@Override
			public void enterCollision(Collider other) {
				GameObject obj = other.getGameObject();
				
				// Infect living targets
				if (obj instanceof Living && infectionFactory != null) {
					Living target = (Living) obj;
					Infection infection = infectionFactory.create(game, target);
					infection.surround(other);
					game.spawnGameObject(infection);
				}
				
				// Bombwave
				if (obj instanceof Living && waveFactory != null) {
					Wave wave = waveFactory.create(game);
					wave.moveTo(Projectile.this);
					wave.setTag(getTag());
					game.spawnGameObject(wave);
				}
				
				// Self destroy if penetrating a living thing
				if (obj instanceof Living)
					kill();
			}
		});
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setWaveFactory(WaveFactory waveFactory) {
		this.waveFactory = waveFactory;
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
