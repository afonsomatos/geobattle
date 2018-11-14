package geobattle.core;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import geobattle.item.AmmoItem;
import geobattle.item.HealthItem;
import geobattle.item.Item;
import geobattle.item.ShieldItem;
import geobattle.living.Player;
import geobattle.living.bots.Bomber;
import geobattle.living.bots.Bot;
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
	private final static int WAIT_PER_SPAWN = 3000;
	private final static int WAVE_CLEARED_PAUSE = 3000;
	private final static int SPAWN_DELAY = 3000;
	
	private final static int ITEM_SPAWN_MARGIN = 10;
	private final static int BOT_SPAWN_MARGIN = 20;
	
	private static class Spawn<T> {
		final int weight;
		final int minLevel;
		final Supplier<T> supplier;
		
		Spawn(int weight, int minLevel, Supplier<T> supplier) {
			this.supplier = supplier;
			this.minLevel = minLevel;
			this.weight = weight;
		}
	}
	
	private int waveCountDown;
	private int wave;
	private int level;
	private int dead;
	private int spawned;
	private int score;
	private long start;
	
	private Supplier<Integer> itemQuantitySupplier = () -> 1;
	private Supplier<Integer> botQuantitySupplier  = () -> wave + (level - 1);
	
	private List< Spawn<Item> > items = Arrays.asList(
		new Spawn<Item>(50, 1, () -> new AmmoItem(game, 50 * level)),
		new Spawn<Item>(50, 3, () -> new HealthItem(game, 50 * level)),
		new Spawn<Item>(50, 5, () -> new ShieldItem(game, 50 * level))
	);
	
	private List< Spawn<Bot> > spawns = Arrays.asList(
		new Spawn<Bot>(50,	1,	() -> new Soldier(game)),
		new Spawn<Bot>(50, 	2, 	() -> new Creeper(game)),
		new Spawn<Bot>(50,	3,  () -> new Tower(game)),
		new Spawn<Bot>(50,	4,  () -> new Bubble(game)),
		new Spawn<Bot>(50, 	5,  () -> new Slime(game)),
		new Spawn<Bot>(50,	6,  () -> new Sentry(game)),
		new Spawn<Bot>(50, 	7,  () -> new Slicer(game)),
		new Spawn<Bot>(50, 	8,  () -> new Fly(game)),
		new Spawn<Bot>(50, 	9,  () -> new Bomber(game)),
		new Spawn<Bot>(50, 	10, () -> new Zombie(game))
	);
	
	public LevelManager(Game game) {
		this.game = game;
	}
	
	public void sendLevel(int level) {
		this.level = level;
		wave = 0;
		score = 0;
		// FIXME: Pause ruins this
		start = System.currentTimeMillis();
		sendNextWave();
	}
	
	private void loadWave(Runnable finish) {
		spawnBots();
		spawnItems();
	}
	
	private void spawnItems() {
		// Construct probability bag
		WeightedRandomBag< Spawn<Item> > bag = new WeightedRandomBag< Spawn<Item> >();
		for (Spawn<Item> s : items)
			if (level >= s.minLevel)
				bag.addEntry(s, s.weight);

		int n = itemQuantitySupplier.get();
		while (--n >= 0) {
			Item item = bag.getRandom().supplier.get();
			item.moveTo(getRandomLocation(ITEM_SPAWN_MARGIN));
			game.spawnGameObject(item);
		}
	}
	
	private void spawnBots() {
		Player player = game.getPlayer();

		// Construct probability bag
		WeightedRandomBag< Spawn<Bot> > bag = new WeightedRandomBag< Spawn<Bot> >();
		for (Spawn<Bot> s : spawns)
			if (level >= s.minLevel)
				bag.addEntry(s, s.weight);
		
		dead = spawned = 0;
		
		int n = botQuantitySupplier.get();
		final Bot[] bots = new Bot[n];

		Runnable killed = () -> {
			game.sendMessage(2000, "Enemy killed +10");
			score += 10;
			// End wave when the last bot dies
			if (++dead >= bots.length)
				finishWave();
		};
		
		// Create all upcoming bots
		for (int i = 0; i < bots.length; ++i) {
			Spawn<Bot> s = bag.getRandom();
			Bot b = bots[i] = s.supplier.get();
			b.moveTo(getRandomLocation(BOT_SPAWN_MARGIN));
			b.setTag(Tag.Enemy);
			b.setTarget(player);
			// FIXME: This fails when new enemies are spawned (ex. slime)
			b.getTriggerMap().add("kill", killed);
		}
		
		// Spawn them in order
		Event spawnEvent = new Event(WAIT_PER_SPAWN, true);
		spawnEvent.setRunnable(() -> {
			if (spawned == n) {
				spawnEvent.setOff(true);
				return;
			}
			game.spawnGameObject(new BotSpawner(game, bots[spawned], SPAWN_DELAY));
			spawned++;
		});

		// Spawn first without delay
		spawnEvent.run();
		game.getSchedule().add(spawnEvent);
	}
	
	private Point getRandomLocation(int margin) {
		int randX = Util.randomInteger(margin, game.getWidth() - margin);
		int randY = Util.randomInteger(margin, game.getHeight() - margin);
		return new Point(randX, randY);
	}
	
	private void finishWave() {
		// In seconds
		double elapsed = getLevelTimeEllapsed() / 1000.0;
		
		// Time for which extra score is max
		double bestTime = (level + wave) * 5; 
		int bestScore	= (level + wave) * 50;
		int scoreGained = (int) Util.clamp(0, bestScore * elapsed / bestTime, bestScore);
		String msg = String.format("Wave cleared! +%d (%.2fs)", scoreGained, elapsed);
		
		score += scoreGained;
		game.sendMessage(WAVE_CLEARED_PAUSE, msg);
		game.getSchedule().next(WAVE_CLEARED_PAUSE, this::sendNextWave);

		if (wave == WAVES_PER_LEVEL)
			game.sendLevelFinished();
	}
	
	private void sendNextWave() {
		waveCountDown = WAVE_COUNT_DOWN;
		
		Log.i("Next wave!");
		Event event = new Event(1000, true);
		event.setRunnable(() -> {
			String msg;
			assert waveCountDown >= 0;
			if (waveCountDown == 0) {
				wave++;
				loadWave(this::finishWave);
				event.setOff(true);
				msg = "Go";
			} else {
				msg = "New wave in " + waveCountDown;
			}
			waveCountDown--;
			game.sendMessage(1000, msg);
		});
		
		event.run();
		game.getSchedule().add(event);
	}
	
	public int getWaveCountDown() {
		return waveCountDown;
	}
	
	public int getScore() {
		return score;
	}

	public int getWave() {
		return this.wave;
	}
	
	public long getLevelTimeEllapsed() {
		return System.currentTimeMillis() - start;
	}

}
