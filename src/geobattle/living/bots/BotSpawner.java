package geobattle.living.bots;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Aura;
import geobattle.render.sprite.shapes.Circle;
import geobattle.schedule.Event;
import geobattle.util.Log;

public class BotSpawner extends GameObject {
	
	private Bot spawn;
	private Event event;
	private Runnable runnable;
	private long delay;
	
	public BotSpawner(Game game, Bot spawn, long delay) {
		this(game, spawn, delay, null);
	}
	
	public BotSpawner(Game game, Bot spawn, long delay, Runnable run) {
		super(game, spawn.getX(), spawn.getY());
		this.spawn = spawn;
		this.runnable = run;
		this.delay = delay;
		getTriggerMap().add("spawn", this::setup);
	}

	private void setup() {
		event = new Event(delay, false, () -> {
			this.kill();
			if (runnable != null)
				runnable.run();
			game.spawnGameObject(spawn);
		});
		game.getSchedule().add(event);
		setSprite(new Aura(30, 7, spawn.getColor()));
	}

	@Override
	protected void update() {
		if (!hasSpawned()) return;
		double quota = event.getPercentage();
		Color color = spawn.getColor();
		Color placeColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (quota * 100));
		// TODO: Too slow, find a way to change transparency of Sprite.getImage()
		// to make this more efficient.
		// setSprite(new Aura(30, 7, placeColor));
	}
	
}
