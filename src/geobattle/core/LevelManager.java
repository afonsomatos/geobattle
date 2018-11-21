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
import geobattle.util.Interval;
import geobattle.util.Log;
import geobattle.util.Util;
import geobattle.util.WeightedRandomBag;

/**
 * Manages all the game levels and waves.
 */
public class LevelManager {

	private Game game;

	/**
	 * Formatted string message when wave is cleared. Must include the score
	 * (int) and elapsed time (double).
	 */
	private final static String WAVE_CLEARED_MESSAGE =
			"Wave cleared! +%d (%.2fs)";

	/**
	 * Score gained after killing an enemy.
	 */
	private final static int ENEMY_KILLED_SCORE = 10;

	/**
	 * Enemy killed message.
	 */
	private final static String ENEMY_KILLED_MESSAGE = "Enemy killed " +
			ENEMY_KILLED_SCORE;

	/**
	 * Lifetime in milliseconds of the "enemy killed" message.
	 */
	private final static int ENEMY_KILLED_MESSAGE_DELAY = 2000;

	/**
	 * Seconds for a wave to begin.
	 */
	private final static int WAVE_COUNT_DOWN = 3;

	/**
	 * Number of waves at a given level.
	 */
	private final static int WAVES_PER_LEVEL = 3;

	/**
	 * Milliseconds to wait between two enemies' spawns.
	 */
	private final static int WAIT_PER_SPAWN = 3000;

	/**
	 * Milliseconds to wait after a wave is cleared and before the next one
	 * begins.
	 */
	private final static int WAVE_CLEARED_PAUSE = 3000;

	/**
	 * Milliseconds to wait for an enemy to finish spawning.
	 */
	private final static int SPAWN_DELAY = 3000;

	/**
	 * Padding of the area where items will spawn.
	 */
	private final static int ITEM_SPAWN_MARGIN = 10;

	/**
	 * Padding of the area where bots will spawn.
	 */
	private final static int BOT_SPAWN_MARGIN = 20;

	/**
	 * Delay between item spawns.
	 */
	private final static Interval<Integer> ITEM_SPAWN_DELAY =
			new Interval<>(10000, 30000);

	/**
	 * Wraps an object (for example an enemy or item) and gives it the minimum
	 * required level to spawn and a relative weight used for probabilities.
	 */
	private static class Spawn<T extends GameObject> {
		final int weight;
		final int minLevel;
		final Supplier<T> supplier;

		Spawn(int weight, int minLevel, Supplier<T> supplier) {
			this.supplier = supplier;
			this.minLevel = minLevel;
			this.weight = weight;
		}
	}

	/**
	 * Countdown before a wave starts.
	 */
	private int waveCountDown;

	/**
	 * Current wave being played.
	 */
	private int wave;

	/**
	 * Current level being played.
	 */
	private int level;

	/**
	 * Current level score.
	 */
	private int score;

	/**
	 * Killed enemies for the current wave.
	 */
	private transient int dead;

	/**
	 * Spawned enemies for the current wave.
	 */
	private transient int spawned;

	/**
	 * Empty event to keep track of time played on the current wave.
	 */
	private Event waveEvent = new Event();

	/**
	 * Returns the quantity of bots to be spawned at the current state of the
	 * game.
	 */
	private Supplier<Integer> botQuantitySupplier = () -> wave + (level - 1);

	/**
	 * Returns extra score gained in the end of a wave.
	 */
	private Supplier<Integer> extraScoreSupplier = () -> {
		double elapsed = waveEvent.getElapsed() / 1000.0;
		int bestTime = botQuantitySupplier.get() * 5;
		int bestScore = bestTime * 10;
		return (int) Util.clamp(0, bestScore * bestTime / elapsed, bestScore);
	};

	/**
	 * List of all the items available for spawn.
	 */
	private List<Spawn<Item>> items = Arrays.asList(
			new Spawn<Item>(100, 1, () -> new AmmoItem(game, 25 * level)),
			new Spawn<Item>(100, 3, () -> new HealthItem(game, 50 * level)),
			new Spawn<Item>(50, 5, () -> new ShieldItem(game, 50 * level)));

	/**
	 * List of all the bots available for spawn.
	 */
	private List<Spawn<Bot>> spawns = Arrays.asList(
			new Spawn<Bot>(50, 1, () -> new Soldier(game)),
			new Spawn<Bot>(50, 2, () -> new Creeper(game)),
			new Spawn<Bot>(50, 3, () -> new Tower(game)),
			new Spawn<Bot>(50, 4, () -> new Bubble(game)),
			new Spawn<Bot>(50, 5, () -> new Slime(game)),
			new Spawn<Bot>(50, 6, () -> new Sentry(game)),
			new Spawn<Bot>(50, 7, () -> new Slicer(game)),
			new Spawn<Bot>(50, 8, () -> new Fly(game)),
			new Spawn<Bot>(50, 9, () -> new Bomber(game)),
			new Spawn<Bot>(50, 10, () -> new Zombie(game)));

	/**
	 * Spawns items across levels.
	 */
	private Event spawnItemsEvent;

	public LevelManager(Game game) {
		this.game = game;
	}

	/**
	 * Loads the first wave of a current level and resets the score.
	 * 
	 * @param level
	 */
	public void sendLevel(int level) {
		this.level = level;
		wave = 0;
		score = 0;
		sendNextWave();
		spawnItems();
	}

	/**
	 * Loads the current wave.
	 */
	private void loadWave() {
		spawnBots();
		game.getSchedule().start(waveEvent);
	}

	/**
	 * Spawns all the items for the current level.
	 */
	private void spawnItems() {
		// Construct probability bag
		WeightedRandomBag<Spawn<Item>> bag =
				new WeightedRandomBag<Spawn<Item>>();
		for (Spawn<Item> s : items)
			if (level >= s.minLevel)
				bag.addEntry(s, s.weight);

		// Spawn items according to a timer
		spawnItemsEvent = new Event(Util.randomInteger(ITEM_SPAWN_DELAY), true,
				event -> {
					Log.i(event.getElapsed());
					Item item = bag.getRandom().supplier.get();
					item.moveTo(getRandomLocation(ITEM_SPAWN_MARGIN));
					game.spawnGameObject(item);
					event.setDelay(Util.randomInteger(ITEM_SPAWN_DELAY));
				});

		game.getSchedule().start(spawnItemsEvent);
	}

	/**
	 * Spawns all the bots for the current wave.
	 */
	private void spawnBots() {
		// Construct probability bag
		WeightedRandomBag<Spawn<Bot>> bag = new WeightedRandomBag<Spawn<Bot>>();
		for (Spawn<Bot> s : spawns)
			if (level >= s.minLevel)
				bag.addEntry(s, s.weight);

		dead = spawned = 0;

		int n = botQuantitySupplier.get();
		final Bot[] bots = new Bot[n];

		Runnable killed = () -> {
			game.sendMessage(ENEMY_KILLED_MESSAGE_DELAY, ENEMY_KILLED_MESSAGE);
			score += ENEMY_KILLED_SCORE;
			// End wave when the last bot dies
			if (++dead >= bots.length)
				finishWave();
		};

		Player player = game.getPlayer();
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
		Event spawnEvent = new Event(WAIT_PER_SPAWN, true, event -> {
			if (spawned == n) {
				event.off();
				return;
			}
			game.spawnGameObject(
					new BotSpawner(game, bots[spawned], SPAWN_DELAY));
			spawned++;
		});

		// Spawn first without delay
		spawnEvent.run();
		game.getSchedule().start(spawnEvent);
	}

	/**
	 * Returns a random position in the game arena with a certain
	 * padding/margin.
	 * 
	 * @param margin
	 * @return generated location
	 */
	private Point getRandomLocation(int margin) {
		int randX = Util.randomInteger(margin, game.getWidth() - margin);
		int randY = Util.randomInteger(margin, game.getHeight() - margin);
		return new Point(randX, randY);
	}

	private void finishLevel() {
		spawnItemsEvent.off();
		game.sendLevelFinished();
	}

	private void finishWave() {
		// In seconds
		double elapsed = waveEvent.getElapsed() / 1000.0;
		int scoreGained = extraScoreSupplier.get();
		String msg = String.format(WAVE_CLEARED_MESSAGE, scoreGained, elapsed);

		score += scoreGained;
		game.sendMessage(WAVE_CLEARED_PAUSE, msg);
		game.getSchedule().next(WAVE_CLEARED_PAUSE, this::sendNextWave);

		if (wave == WAVES_PER_LEVEL)
			finishLevel();
	}

	/**
	 * Sets the stage to load the next wave. Presents the wave countdown.
	 */
	private void sendNextWave() {
		final int second = 1000; // ms
		waveCountDown = WAVE_COUNT_DOWN;
		Event event = new Event(second, true, e -> {
			String msg;
			assert waveCountDown >= 0;
			if (waveCountDown == 0) {
				wave++;
				loadWave();
				e.setOff(true);
				msg = "Go";
			} else {
				msg = "New wave in " + waveCountDown;
			}
			waveCountDown--;
			game.sendMessage(second, msg);
		});
		event.run();
		game.getSchedule().start(event);
	}

	/**
	 * Returns the current countdown before a wave starts.
	 * 
	 * @return wave countdown number
	 */
	public int getWaveCountDown() {
		return waveCountDown;
	}

	/**
	 * Returns the current score for the current level.
	 * 
	 * @return current score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Returns the current wave that's being played.
	 * 
	 * @return current wave
	 */
	public int getWave() {
		return wave;
	}

}
