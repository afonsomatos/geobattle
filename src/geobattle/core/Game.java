package geobattle.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import geobattle.collider.CollisionHandler;
import geobattle.item.ItemGenerator;
import geobattle.launcher.Launchable;
import geobattle.launcher.LauncherOption;
import geobattle.living.Player;
import geobattle.living.enemies.Enemy;
import geobattle.object.MouseFollower;
import geobattle.render.Renderable;
import geobattle.render.sprite.shapes.CircleCross;
import geobattle.schedule.Event;
import geobattle.schedule.Schedule;
import geobattle.ui.Window;
import geobattle.util.Counter;
import geobattle.util.Dispatcher;
import geobattle.util.Log;
import geobattle.util.Util;
import geobattle.weapon.Arsenal;
import geobattle.weapon.Rifle;
import geobattle.weapon.Shotgun;
import geobattle.weapon.Sniper;
import geobattle.weapon.Unlimited;
import geobattle.weapon.Weapon;

public class Game implements Launchable {

	private boolean RENDER_DEBUG = false;
	private Renderable debugRender;

	private Window window;
	private Schedule schedule = new Schedule();
	private Player player;
	private HUD hud;
	
	private LevelManager levelManager;
	private CollisionHandler collisionHandler;
	
	private int ups = 0;
	private int fps = 0;
	
	private int width;
	private int height;
	
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
	private boolean gameRunning = false;
	private int enemiesLeft;
	private int score;
	private int rounds = 0;
	private boolean godmode = false;
	
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
			if (l.equals("godmode")) {
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
		gameRunning 	= true;
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
		ars.store(0, new Shotgun(this, player, Tag.Player));
		ars.store(1, new Sniper(this, player, Tag.Player));
		ars.store(2, new Rifle(this, player, Tag.Player));
		//ars.store(3, new Unlimited(this, player, Tag.Player));
		ars.select(0);
		
		for (Weapon w : ars.getSlots())
			if (w != null)
				this.spawnGameObject(w);
		
		player.setTarget(window.getGameCanvas().getMouseInput().getMouseObject());
		spawnGameObject(player);

		spawnGameObject(new ItemGenerator(this));
		state = State.PLAYING;
		gameLoop();
	}
	
	public int getRounds() {
		return rounds;
	}
	
	public void gameLoop() {
		final double second = Math.pow(10, 9);
		new Thread(() -> {
			double ticksPerSecond = 60.0;
			double nanosPerTick = second / ticksPerSecond;
			
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
				
				render();
				frames++;
				
				if (lastTime - lastPrint >= second) {
					lastPrint = System.nanoTime();
					
					ups = updates;
					fps = frames;
					
					updates = 0;
					frames = 0;
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

		gfx.setColor(Color.BLACK);
		gfx.fillRect(0, 0, width, height);
		
		if (state == State.PLAYING) {
			LinkedList<GameObject> clone = new LinkedList<GameObject>(gameObjects);
			for (GameObject g : clone)
				if (!g.isHidden())
					g.render_(gfx);
	
			if (RENDER_DEBUG)
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

	public boolean isGameRunning() {
		return gameRunning;
	}

	public HUD getHud() {
		return hud;
	}

	public int getScore() {
		return score;
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
		width = opt.getWidth();
		height = opt.getHeight();
		
		// Open game in a new thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(opt.getScreen());
				window = new Window(Game.this, opt.getScreen(), opt.isFullScreen());
				open();
				dispatcher.dispatch();
			}
		}).start();
	}

}
