package geobattle.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import geobattle.collider.CollisionHandler;
import geobattle.item.ItemGenerator;
import geobattle.launcher.Launchable;
import geobattle.launcher.LauncherOption;
import geobattle.living.Player;
import geobattle.living.enemies.Enemy;
import geobattle.object.MouseFollower;
import geobattle.render.Renderable;
import geobattle.render.sprite.Sprite;
import geobattle.schedule.Event;
import geobattle.schedule.Schedule;
import geobattle.special.Bomb;
import geobattle.special.WaveSpecial;
import geobattle.ui.Window;
import geobattle.util.Counter;
import geobattle.util.Dispatcher;
import geobattle.util.Log;
import geobattle.util.Palette;
import geobattle.util.Util;
import geobattle.weapon.Arsenal;
import geobattle.weapon.Weapon;
import geobattle.weapon.WeaponFactory;

public class Game implements Launchable {

	private boolean debug 	= false;
	private boolean godmode = false;
	
	private Renderable debugRender;

	private Schedule schedule = new Schedule();
	private Window window;
	private Player player;
	private HUD hud;
	
	private LevelManager levelManager;
	private CollisionHandler collisionHandler;
	
	private int ups = 0;
	private int fps = 0;
	private final int TARGET_FPS = 120;

	private int width = 1024;
	private int height = 576;
	private double ratio = 16.0 / 9.0;
	private double scale;
	
	private int score = 0;
	private int rounds = 0;
	
	private Event outOfBorderEvent;
	private boolean outOfBorders;
	private Counter outOfBorderCounter = new Counter(5, -1, 0, false) {
		@Override
		public void fire() {
			outOfBorderEvent.setOff(true);
			if (outOfBorders)
				playerExitedMap();
		}
	};
	
	private Event gettingHitEvent;
	private boolean gettingHit;
	private boolean gameOver;
	
	private boolean paused = false;
	private int enemiesLeft;
	
	private LinkedList<Score> lastScores = new LinkedList<Score>();
			
	public enum State {
		MENU,
		PLAYING,
		END
	};
	
	public State state = State.MENU;
	
	private LinkedList<GameObject> gameObjects = new LinkedList<GameObject>();
	
	// Follows the mouse input
	private MouseFollower mouseFollower;
	
	private void setup() {
		hud 				= new HUD(this);
		levelManager 		= new LevelManager(this);
		collisionHandler 	= new CollisionHandler(this);
		debugRender 		= new Debug(this);

		outOfBorderEvent = new Event(1000, true, () -> outOfBorderCounter.tick());
		gettingHitEvent = new Event(500, false, () -> gettingHit = false);
	}
	
	public void open() {
		setup();

		state = State.MENU;

		// Trace mouse input
		mouseFollower = new MouseFollower(this);
		window.getGameCanvas().getMouseInput().setGameObject(mouseFollower);
		spawnGameObject(mouseFollower);

		gameLoop();
	}
	
	public void end() {
		state = State.END;
		window.sendGameOver(score);
		rounds++;
	}
	
	private void parseOpts(String opts) {
		
		String[] lines = opts.split("\n");
		for(String l : lines) {
			if (l.equals("debug")) {
				debug = true;
				Log.i("debug activated.");
			} else if (l.equals("godmode")) {
				godmode = true;
				Log.i("godmode activated!");
			}
		}
	}
	
	public void start(String opts) {
		parseOpts(opts);
		
		score 			= 0;
		enemiesLeft 	= 0;
		outOfBorders 	= false;
		gettingHit 		= false;
		gameOver		= false;
		
		// Enable input
		window.getGameCanvas().getMouseInput().setActive(true);
		window.getGameCanvas().getKeyInput().setActive(true);
		
		outOfBorderCounter.reset();

		schedule.clear();
		gameObjects.clear();
		levelManager.setLevel(0);
		
		Log.i("Game starting");
		
		levelManager.sendNextLevel();

		player = new Player(this);

		if (godmode)
			player.setGodmode(true);
		
		player.stop();
		player.restoreHealth();
		player.setX(width / 2);
		player.setY(height / 2);

		Arsenal ars = player.getArsenal();
		
		ars.store(0, WeaponFactory.Shotgun.create(this, player, Tag.Player));
		ars.store(1, WeaponFactory.Sniper.create(this, player, Tag.Player));
		ars.store(2, WeaponFactory.Rifle.create(this, player, Tag.Player));
		ars.store(3, WeaponFactory.Unlimited.create(this, player, Tag.Player));

		for (Weapon w : ars.getSlots()) {
			if (w == null) continue;
			spawnGameObject(w);
		}
		
		//spawnGameObject(new Bomb(this, 500, 500));
		
		
		ars.select(0);

		player.setTarget(window.getGameCanvas().getMouseInput().getMouseObject());
		spawnGameObject(player);
		
		spawnGameObject(new ItemGenerator(this));
		state = State.PLAYING;
		gameLoop();
	}
	
	public int getRounds() {
		return rounds;
	}
	
	public HUD getHUD() {
		return hud;
	}
	
	public double getRatio() {
		return ratio;
	}
	
	public void gameLoop() {
		final long NANOS_PER_SECOND = (long) Math.pow(10, 9);
		final long NANOS_PER_MILLIS = (long) Math.pow(10, 6);
		final long rateLimit = NANOS_PER_SECOND / TARGET_FPS; 
		
		new Thread(() -> {
			double ticksPerSecond = 60.0;
			double nanosPerTick = NANOS_PER_SECOND / ticksPerSecond;
			
			long lastTime = System.nanoTime();
			long lastPrint = System.nanoTime();
			
			int updates = 0;
			int frames = 0;
			double delta = 0;
			
			long elapsed;
			
			while (state == State.PLAYING) {
				long now = System.nanoTime();
				delta += (now - lastTime) / nanosPerTick;
				elapsed = now - lastTime;
				lastTime = now;
				
				while (delta >= 1) {
					tick();
					updates++;
					delta--;
				}
				
				render();
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
	
	public LinkedList<Score> getScores() {
		return new LinkedList<Score>(lastScores);
	}
	
	public Window getWindow() {
		return window;
	}
	
	public int getUps() {
		return ups;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public void togglePaused() {
		paused = !paused;
		if (paused) schedule.pause();
		else schedule.unpause();
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
		// check for switch
		if (outOfBorders ^ (outOfBorders = player.isOutOfBorders())) {
			outOfBorderEvent.setOff(false);
			if (outOfBorders) {
				outOfBorderCounter.reset();
				System.out.println(outOfBorderCounter.getValue());
				schedule.add(outOfBorderEvent);
			}
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
		// must always be recording time
		schedule.tick();

		if (paused) return;
		if (state != State.PLAYING) return;

		handleOutOfBorders();
		
		for (GameObject g : getGameObjects()) {
			if (g.isActive())
				g.tick();
		}

		collisionHandler.handleCollisions();
		
		if (!levelManager.isLoadingLevel() && enemiesLeft == 0)
			levelManager.sendNextLevel();
		/*
		 */
	}
	
	public synchronized void render() {

		BufferStrategy bfs = window.getGameCanvas().getBufferStrategy();
		Graphics2D gfx = (Graphics2D) bfs.getDrawGraphics();
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		gfx.scale(scale, scale); // this is the key to scaling
		
		gfx.setColor(Color.BLACK);
		gfx.fillRect(0, 0, width, height);
		
		if (state == State.PLAYING) {
			LinkedList<GameObject> clone = new LinkedList<GameObject>(gameObjects);
			for (GameObject g : clone)
				if (!g.isHidden())
					g.render_(gfx);
	
			if (debug)
				debugRender.render(gfx);
		}

		
		hud.render(gfx);

		gfx.dispose();
		bfs.show();
	}
	
	public void spawnGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
		gameObject.spawn();
		
		if (gameObject instanceof Enemy)
			enemiesLeft++;
	}
	
	public void killGameObject(GameObject gameObject) {
		gameObjects.remove(gameObject);
		
		if (gameObject instanceof Enemy) {
			score += 10;
			enemiesLeft--;
		}
	}
	
	public List<GameObject> getGameObjects() {
		// Create a clone so the original list doesn't get modified while iterating
		return new ArrayList<GameObject>(gameObjects);
	}
	
	public void sendPlayerDead() {
		gameOver = true;
		
		// Player is not out of borders
		player.setX(0);
		player.setY(0);
		
		// Disable all input
		window.getGameCanvas().getMouseInput().setActive(false);
		window.getGameCanvas().getKeyInput().setActive(false);
		
		// Remove player from sight
		player.getWeapon().setHidden(true);
		player.setActive(false);
		player.kill();
		
		// Wait a little bit before ending
		schedule.next(3000, this::end);
	}

	public int getEnemiesLeft() {
		return enemiesLeft;
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

	public int getScore() {
		return score;
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

	public void saveScore(String name, int score) {
		lastScores.add(new Score(name, score, rounds));
		System.out.printf("%s had %d score in round %d", name, score, rounds);
	}
	
	@Override
	public void launch(LauncherOption opt, Dispatcher dispatcher) {
		// width = opt.getWidth();
		// height = opt.getHeight();
		
		scale = opt.getWidth() / (double) width;
		
		Log.i("scale: " + scale);
		
		// Open game in a new thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				window = new Window(Game.this, opt.getScreen(), opt.isFullScreen(), opt.getWidth(), opt.getHeight());
				open();
				dispatcher.dispatch();
			}
		}).start();
	}

}
