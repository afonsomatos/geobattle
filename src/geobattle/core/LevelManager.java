package geobattle.core;

import geobattle.living.Player;
import geobattle.living.bots.Bomber;
import geobattle.living.bots.Bot;
import geobattle.living.bots.BotFactory;
import geobattle.living.bots.BotSpawner;
import geobattle.living.bots.Bubble;
import geobattle.living.bots.Creeper;
import geobattle.living.bots.Fly;
import geobattle.living.bots.Sentry;
import geobattle.living.bots.Slicer;
import geobattle.living.bots.Slime;
import geobattle.living.bots.Soldier;
import geobattle.living.bots.Tower;
import geobattle.living.bots.Zombie;
import geobattle.schedule.Event;
import geobattle.util.Log;
import geobattle.util.Util;
import geobattle.util.WeightedRandomBag;

public class LevelManager {

	private Game game;

	private final static int WAVE_COUNT_DOWN = 3;
	private final static int WAVES_PER_LEVEL = 3;
	
	private int waveCountDown;
	private int wave;
	private int level;
	private int dead;
	
	private Runnable levelFinisher;
	
	private static class Spawn {
		final int weight;
		final int minLevel;
		final Class<? extends Bot> botClass;
		
		Spawn(int weight, int minLevel, Class<? extends Bot> botClass) {
			this.botClass = botClass;
			this.minLevel = minLevel;
			this.weight = weight;
		}
	}
	
	private static Spawn[] spawns = new Spawn[] {
		new Spawn(50,	1, 		Soldier.class),
		new Spawn(50, 	2, 		Creeper.class),
		new Spawn(50,	3,		Tower.class),
		new Spawn(50,	4,		Bubble.class),
		new Spawn(50, 	5, 		Slime.class),
		new Spawn(50,	6,		Sentry.class),
		new Spawn(50, 	7, 		Slicer.class),
		new Spawn(50, 	8, 		Fly.class),
		new Spawn(50, 	9, 		Bomber.class),
		new Spawn(50, 	10, 	Zombie.class)
	};
	
	public LevelManager(Game game) {
		this.game = game;
	}
	
	public void sendLevel(int level, Runnable levelFinisher) {
		this.level = level;
		this.levelFinisher = levelFinisher;
		wave = 0;
		sendNextWave();
	}
	
	private void loadWave(Runnable finish) {

		int width = game.getWidth();
		int height = game.getHeight();
		Player player = game.getPlayer();

		// Construct probability bag
		WeightedRandomBag<Spawn> bag = new WeightedRandomBag<Spawn>();
		for (Spawn s : spawns)
			if (level >= s.minLevel)
				bag.addEntry(s, s.weight);
		
		dead = 0;
		int n = wave + (level - 1);
		Bot[] bots = new Bot[n];
		for (int i = 0; i < bots.length; ++i) {
			Spawn s = bag.getRandom();
			Bot b = BotFactory.create(game, s.botClass);
			
			// Random location
			int x = Util.randomInteger(20, width - 20);
			int y = Util.randomInteger(20, height - 20);
			
			b.moveTo(x, y);
			b.setTag(Tag.Enemy);
			b.setTarget(player);
			
			b.getTriggerMap().addLast("die", () -> {
				// End wave when the last bot dies
				if (++dead >= bots.length)
					finishWave();
			});
			game.spawnGameObject(new BotSpawner(game, b, 3000));
		}
	}
	
	private void finishWave() {
		Log.i("Wave finished!");
		if (wave == WAVES_PER_LEVEL) {
			levelFinisher.run();
			return;
		}
		game.sendMessage(3000, "Wave cleared!");
		game.getSchedule().next(3000, this::sendNextWave);
	}
	
	private void sendNextWave() {
		waveCountDown = WAVE_COUNT_DOWN;
		
		Event event = new Event();
		game.sendMessage(1000, "New wave in " + waveCountDown);
		event.setRunnable(() -> {
			String msg;
			if (--waveCountDown == 0) {
				wave++;
				loadWave(this::finishWave);
				event.setOff(true);
				msg = "Go";
			} else {
				msg = "New wave in " + waveCountDown;
			}
			game.sendMessage(1000, msg);
		});
		event.setDelay(1000);
		event.setRepeat(true);
		game.getSchedule().add(event);
	}
	
	public int getWaveCountDown() {
		return waveCountDown;
	}

	public int getWave() {
		return this.wave;
	}

}
