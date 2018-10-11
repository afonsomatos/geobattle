package geobattle.living.enemies;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.render.sprite.shapes.Aura;
import geobattle.render.sprite.shapes.Circle;
import geobattle.schedule.Event;

public class EnemySpawner extends GameObject {
	
	private GameObject spawn;
	private Event event;
	private Runnable runnable;
	private long delay;
	
	public EnemySpawner(Game game, GameObject spawn, long delay, Runnable run) {
		super(game, spawn.getX(), spawn.getY());
		this.spawn = spawn;
		this.runnable = run;
		this.delay = delay;
	}

	@Override
	protected void spawn() {
		event = new Event(delay, false, () -> {
			this.kill();
			runnable.run();
			game.spawnGameObject(spawn);
		});
		game.getSchedule().add(event);
	}

	@Override
	protected void update() {
		double quota = (double) event.getElapsed() / delay;
		Color color = spawn.getColor();
		Color placeColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (quota * 100));
		setSprite(new Aura(30, 7, placeColor));
	}

	@Override
	protected void render(Graphics2D gfx) {
		// TODO Auto-generated method stub
	}
	
}
