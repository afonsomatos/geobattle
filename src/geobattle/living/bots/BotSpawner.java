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
	
	private Sprite sprite;
	private Bot spawn;
	private Event event;
	private Runnable runnable;
	private long delay;
	
	public BotSpawner(Game game, Bot spawn, long delay) {
		this(game, spawn, delay, null);
	}
	
	public BotSpawner(Game game, Bot spawn, long delay, Runnable run) {
		super(game);
		this.spawn 		= spawn;
		this.runnable 	= run;
		this.delay 		= delay;
		
		setSprite(new Aura(30, 7, spawn.getColor()));
		moveTo(spawn);
		getTriggerMap().add("spawn", this::setup);
		addController(this::updateSprite);
	}

	private void setup() {
		event = new Event(delay, false, () -> {
			this.kill();
			if (runnable != null)
				runnable.run();
			game.spawnGameObject(spawn);
		});
		game.getSchedule().add(event);
		
	}

	private void updateSprite(GameObject obj) {
		if (!hasSpawned()) return;
		float perc = (float) event.getPercentage();
		getSprite().setAlpha(perc / 3);
	}
	
}
