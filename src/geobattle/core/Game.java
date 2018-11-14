package geobattle.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import geobattle.collider.CollisionHandler;
import geobattle.io.IOManager;
import geobattle.launcher.Launchable;
import geobattle.launcher.LauncherOption;
import geobattle.living.Player;
import geobattle.living.bots.Bot;
import geobattle.living.bots.powerups.Powerup;
import geobattle.object.ArrowKeysFollower;
import geobattle.object.MouseFollower;
import geobattle.render.Renderable;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Aura;
import geobattle.schedule.Event;
import geobattle.schedule.Schedule;
import geobattle.special.slot.SpecialSet;
import geobattle.special.slot.SpecialSlot;
import geobattle.special.slot.SpecialSlotFactory;
import geobattle.ui.UIManager;
import geobattle.util.Counter;
import geobattle.util.Log;
import geobattle.util.Palette;
import geobattle.weapon.Weapon;
import geobattle.weapon.WeaponFactory;
import geobattle.weapon.WeaponSet;

public class Game implements Launchable, Renderable {

	// Cross arrow
	private final static Sprite CROSS_ARROW_SPRITE = new Aura(15, 2, Palette.RED);
	private GameObject crossArrow = null;

	private Settings settings = new Settings();
	
	private Renderable debugRender;

	private Schedule schedule = new Schedule();
	private UIManager uiManager;
	private IOManager ioManager;
	private Player player;
	private HUD hud;
	
	private LevelManager levelManager;
	private CollisionHandler collisionHandler;
	
	private int ups = 0;
	private int fps = 0;

	private int width = 1280;
	private int height = 720;
	private double ratio = 16.0 / 9.0;
	private double scale;
	
	private boolean outOfBorders;
	private Counter outOfBorderCounter = new Counter(5, -1, 0, false) {
		@Override
		public void fire() {
			outOfBorderEvent.setOff(true);
			if (outOfBorders)
				playerExitedMap();
		}
	};
	
	private Event outOfBorderEvent 	= new Event(1000, true, outOfBorderCounter::tick);
	private Event gettingHitEvent 	= new Event(500, false, () -> gettingHit = false);
	
	private boolean gettingHit;
	private boolean gameOver;
	
	private boolean paused = false;
	
	private List<GameObject> gameObjects 	= new ArrayList<GameObject>(500);
	private List<Score> scores  			= new ArrayList<Score>();
			
	public enum State {
		MENU,
		PLAYING,
		END
	};
	
	public State state = State.MENU;
	
	private String message = "";
	private Event hideMessageEvent = new Event(0, false, () -> message = "");
	
	private Options options;
	private Achievements achievements = new Achievements();
	
	private int level;
	private boolean levelFinished;
	
	public Game() {
		ioManager			= new IOManager(this);
		hud 				= new HUD(this);
		levelManager 		= new LevelManager(this);
		collisionHandler 	= new CollisionHandler(this);
		debugRender 		= new Debug(this);
		
		rivalTags(Tag.Enemy, Tag.Player);
		rivalTags(Tag.Item, Tag.Player);
		rivalTags(Tag.Enemy, Tag.Void);
		rivalTags(Tag.Player, Tag.Void);
	}
	
	public Achievements getAchievements() {
		return achievements;
	}
	
	public UIManager getUIManager() {
		return uiManager;
	}
	
	public void togglePause() {
		paused = !paused;
		if (paused)
			schedule.pause();
		else
			schedule.unpause();
	}
	
	public void setPause(boolean paused) {
		// Check for a switch
		if (this.paused ^ paused)
			togglePause();
		else
			this.paused = paused;
	}
	
	String getMessage() {
		return message;
	}
	
	public void sendMessage(long delay, String msg) {
		message = msg;
		hideMessageEvent.setDelay(delay);
		schedule.add(hideMessageEvent);
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public boolean areTagsFriends(Tag t1, Tag t2) {
		return !collisionHandler.getCollisionMatrix().collidesWith(t1, t2);
	}
	
	public void rivalTags(Tag... tags) {
		for (int i = 0; i < tags.length; ++i)
			for (int j = i + 1; j < tags.length; ++j)
				collisionHandler.getCollisionMatrix().add(tags[i], tags[j]);
	}
	
	public void friendTags(Tag... tags) {
		for (int i = 0; i < tags.length; ++i)
			for (int j = i + 1; j < tags.length; ++j)
				collisionHandler.getCollisionMatrix().remove(tags[i], tags[j]);
	}
	
	public void open() {
		uiManager.sendOpen();
		state = State.MENU;
	}
	
	public void end() {
		state = State.END;
		String name = uiManager.sendScoreEnter();
		if (name != null)
			saveScore(name, levelManager.getScore(), level);
		uiManager.sendLoad();
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	private void loadSettings(String opts) {
		try {
			settings.loadDefault();
			settings.load(new ByteArrayInputStream(opts.getBytes("UTF-8")));
		} catch (IOException ioe) {
			Log.e("Failed loading settings");
			ioe.printStackTrace();
		}
	}
	
	private void loadCrossArrow() {
		// Trace mouse/arrow input
		if (settings.getBoolean("arrows")) {
			crossArrow = new ArrowKeysFollower(this);
		} else {
			crossArrow = new MouseFollower(this);
			ioManager.getMouseInput().setGameObject(crossArrow);
		}
		crossArrow.setSprite(CROSS_ARROW_SPRITE);
		crossArrow.setZindex(100); // Be on top of all objects
		spawnGameObject(crossArrow);
	}
	
	private void loadPlayerPowerups() {
		int[] choices = options.getPowerups();
		List<Powerup> powerups = achievements.getPowerups();
		for (int i : choices)
			powerups.get(i).apply(player);
	}
	
	private void loadPlayerSpecialSet() {
		int[] choices = options.getSpecials();
		int slots = Math.min(choices.length, achievements.getSpecialSlots());
		List<SpecialSlotFactory> specials = achievements.getSpecials();
		SpecialSet specialSet = new SpecialSet(slots);
		
		for (int i = 0; i < slots; ++i) {
			SpecialSlot s = specials.get(choices[i]).create(this);
			specialSet.store(i, s);
		}
		
		player.setSpecialSet(specialSet);
	}
	
	private void loadPlayerWeaponSet() {
		int[] choices = options.getWeapons();
		int slots = Math.min(choices.length, achievements.getWeaponSlots());
		List<WeaponFactory> weapons = achievements.getWeapons();
		WeaponSet weaponSet = new WeaponSet(slots);
		
		for (int i = 0; i < slots; ++i) {
			Weapon w = weapons.get(choices[i]).create(this, player, Tag.Player);
			weaponSet.store(i, w);
			player.getTriggerMap().add("die", w::kill);
			// TODO: Construct a dependency mechanism to handle spawning and killing weapons
			spawnGameObject(w);
		}

		if (slots >= 0)
			weaponSet.select(0);
		
		player.setWeaponSet(weaponSet);
	}
	
	boolean isLevelFinished() {
		return levelFinished;
	}
	
	private void loadPlayer() {
		player = new Player(this);

		if (settings.getBoolean("godmode"))
			player.setGodmode(true);
		
		player.restoreHealth();
		player.moveTo(width / 2, height / 2);
		player.setTarget(crossArrow);
		spawnGameObject(player);

		loadPlayerWeaponSet();
		loadPlayerSpecialSet();
		loadPlayerPowerups();
	}
	
	private void reset() {
		outOfBorders 	= false;
		gettingHit 		= false;
		gameOver		= false;
		paused			= false;
		
		outOfBorderCounter.reset();
		schedule.clear();
		gameObjects.clear();
	}
	
	public void start(Options opts) {
		this.options = opts;

		// NOTE: If loading settings fails, the behavior is unexpected
		loadSettings(opts.getSettings());
		
		reset();
		
		// Enable input
		ioManager.enable();
		
		loadCrossArrow();
		loadPlayer();
		
		level = opts.getLevel();
		levelFinished = false;
		levelManager.sendLevel(opts.getLevel());

		state = State.PLAYING;
		gameLoop();
	}
	
	public HUD getHUD() {
		return hud;
	}
	
	void sendLevelFinished() {
		levelFinished = true;
		Log.i("Level " + level + " finished");
		schedule.next(3000, () -> {
			if (achievements.getLevel() == level)
				achievements.unlockLevel();
			end();
		});
	}
	
	public double getRatio() {
		return ratio;
	}
	
	public void gameLoop() {
		final long NANOS_PER_SECOND = (long) Math.pow(10, 9);
		final long NANOS_PER_MILLIS = (long) Math.pow(10, 6);
		final long rateLimit = NANOS_PER_SECOND / settings.getInt("targetFps");
		
		new Thread(() -> {
			double ticksPerSecond = 60.0;
			double nanosPerTick = NANOS_PER_SECOND / ticksPerSecond;
			
			long lastTime = System.nanoTime();
			long lastPrint = System.nanoTime();
			
			int updates = 0;
			int frames = 0;
			double delta = 0;
			
			
			while (state == State.PLAYING) {
				long now = System.nanoTime();
				delta += (now - lastTime) / nanosPerTick;
				lastTime = now;
				
				while (delta >= 1) {
					tick();
					updates++;
					delta--;
				}
				
				uiManager.renderFrame(this);
				frames++;
				
				if (lastTime - lastPrint >= NANOS_PER_SECOND) {
					lastPrint = System.nanoTime();
					
					ups = updates;
					fps = frames;
					
					updates = 0;
					frames = 0;
				}
				
			   final long delayms =  ((now + rateLimit) - System.nanoTime()) / NANOS_PER_MILLIS;
			    if (delayms > 0) {
			        // more than a millisecond wait, do it....
			    	try {
			    		Thread.sleep(delayms);
			    	} catch (InterruptedException ie) {
			    		// ignore.
			    	}
			    }
			}
			
		}).start();
	}
	
	public List<Score> getScores() {
		return scores;
	}
	
	public int getUps() {
		return ups;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public boolean isOutOfBorders() {
		return outOfBorders;
	}

	public Counter getOutOfBorderCounter() {
		return outOfBorderCounter;
	}

	public boolean isPlayerGettingHit() {
		return gettingHit;
	}
	
	public void playerGotHit() {
		gettingHit = true;
		schedule.add(gettingHitEvent);
	}
	
	private void handleOutOfBorders() {
		// Check for switch
		if (outOfBorders ^ (outOfBorders = player.isOutOfBorders()) && outOfBorders) {
			outOfBorderCounter.reset();
			schedule.add(outOfBorderEvent);
		}
	}
	
	private void playerExitedMap() {
		Log.i("Player exited the map");
		player.die();
	}
	
	public State getState() {
		return state;
	}
	
	public void tick() {
		// Must always be recording time
		schedule.tick();

		if (paused) return;
		if (state != State.PLAYING) return;

		handleOutOfBorders();
		
		getGameObjects()
			.stream()
			.filter(GameObject::isActive)
			.forEach(GameObject::update);

		collisionHandler.handleCollisions();
	}
	
	@Override
	public void render(Graphics2D gfx) {

		gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		gfx.scale(scale, scale); // This is the key to scaling
		
		gfx.setColor(Color.BLACK);
		gfx.fillRect(0, 0, width, height);
		
		if (state == State.PLAYING) {
			
			getGameObjects()
				.stream()
				.filter(obj -> !obj.isHidden())
				.forEach(obj -> obj.render(gfx));

			if (settings.getBoolean("debug"))
				debugRender.render(gfx);
		}
		
		hud.render(gfx);
	}
	
	public void spawnGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
		
		// Sort by layer
		Collections.sort(gameObjects, new Comparator<GameObject>() {
			@Override
			public int compare(GameObject o1, GameObject o2) {
				return o1.getZindex() - o2.getZindex();
			}
		});
		
		gameObject.spawn();
	}
	
	public void killGameObject(GameObject gameObject) {
		gameObjects.remove(gameObject);
	}
	
	public List<GameObject> getGameObjects() {
		// Create a clone so the original list doesn't get modified while iterating
		return new ArrayList<GameObject>(gameObjects);
	}
	
	public void sendPlayerDead() {
		gameOver = true;
		
		// Player is not out of borders
		player.moveTo(0, 0);
		
		// Disable all input
		ioManager.disable();
		
		// Remove player from sight
		player.setActive(false);
		player.kill();
		
		// Wait a little bit before ending
		schedule.next(3000, this::end);
	}

	public IOManager getIOManager() {
		return ioManager;
	}
	
	public int getFps() {
		return fps;
	}

	public Player getPlayer() {
		return player;
	}

	public CollisionHandler getCollisionHandler() {
		return collisionHandler;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public HUD getHud() {
		return hud;
	}

	public double getScale() {
		return scale;
	}
	
	public LevelManager getLevelManager() {
		return levelManager;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void saveScore(String name, int score, int level) {
		scores.add(new Score(name, score, level));
		Log.i(name + " had score " + score + " in level " + level);
	}
	
	@Override
	public void launch(LauncherOption opt, Runnable dispatcher) {
		// width = opt.getWidth();
		// height = opt.getHeight();
		
		scale = opt.getWidth() / (double) width;
		
		// Open game in a new thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				uiManager = new UIManager(Game.this, new Dimension(opt.getWidth(), opt.getHeight()), opt.getScreen(), opt.isFullScreen());
				ioManager.getKeyInput().bindAll();
				open();
				dispatcher.run();
			}
		}).start();
	}

}
