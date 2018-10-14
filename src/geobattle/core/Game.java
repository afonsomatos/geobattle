package geobattle.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import geobattle.collider.CollisionHandler;
import geobattle.io.Window;
import geobattle.item.ItemGenerator;
import geobattle.living.Player;
import geobattle.living.enemies.Enemy;
import geobattle.object.MouseFollower;
import geobattle.render.Renderable;
import geobattle.schedule.Event;
import geobattle.schedule.Schedule;
import geobattle.ui.Launcher.Launchable;
import geobattle.ui.Launcher.LauncherOption;
import geobattle.util.Counter;
import geobattle.util.Dispatcher;
import geobattle.util.Log;
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
	
	private int width = 800;
	private int height = 600;
	
	private Event outOfBorderEvent;
	private boolean outOfBorders = false;
	private Counter outOfBorderCounter = new Counter(5, -1, 0, false) {
		@Override
		public void fire() {
			outOfBorderEvent.setOff(true);
			playerExitedMap();
		}
	};
	
	private Event gettingHitEvent;
	private boolean gettingHit = false;
	
	private boolean paused = false;
	private boolean gameRunning = false;
	private int enemiesLeft = 0;
	private int score;
	
	private int rounds = 0;
	
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
		player 				= new Player(this);
		hud 				= new HUD(this);
		levelManager 		= new LevelManager(this);
		window 				= new Window(this);
		collisionHandler 	= new CollisionHandler(this);
		debugRender 		= new Debug(this);

		outOfBorderEvent = new Event(1000, true, () -> outOfBorderCounter.tick());
		gettingHitEvent = new Event(500, false, () -> gettingHit = false);
	}
	
	public void open() {
		// Menu should appear first
		setup();
		
		state = State.MENU;

		window.setVisible(true);

		// Trace mouse input
		mouseFollower = new MouseFollower(this);
		window.getMouseInput().setGameObject(mouseFollower);
		spawnGameObject(mouseFollower);

		gameLoop();
	}
	
	public void end() {
		state = State.END;
		window.showScorePanel(score);
		rounds++;
	}
	
	public void start() {			
		score = 0;
		levelManager.setLevel(0);
		gameRunning = true;
		
		gameObjects.clear();
		
		Log.i("Game starting");
		
		levelManager.sendNextLevel();

		player.stop();
		player.restoreHealth();
		player.setX(width / 2);
		player.setY(height / 2);

		Arsenal ars = player.getArsenal();
		ars.store(0, new Shotgun(this, player, Tag.Player));
		ars.store(1, new Sniper(this, player, Tag.Player));
		ars.store(2, new Rifle(this, player, Tag.Player));
		ars.store(3, new Unlimited(this, player, Tag.Player));
		ars.select(0);
		
		for (Weapon w : ars.getSlots())
			this.spawnGameObject(w);
		
		player.setTarget(window.getMouseInput().getMouseObject());
		spawnGameObject(player);

		spawnGameObject(new ItemGenerator(this));
		state = State.PLAYING;
	}
	
	public int getRounds() {
		return rounds;
	}
	
	public void saveScore(String name) {
		if (state != State.END) return;
		Log.i("what");
		lastScores.add(new Score(name, score, rounds));
		state = State.MENU;
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
			
			while (true) {
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
				schedule.add(outOfBorderEvent);
			}
		}
	}
	
	private void playerExitedMap() {
		Log.i("Player exited the map");
	}
	
	public State getState() {
		return state;
	}
	
	public void tick() {
		if (state != State.PLAYING) return;
		schedule.tick();

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
	
	public void render() {
		Graphics2D gfx = (Graphics2D) window.getBufferStrategy().getDrawGraphics();
		
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		gfx.setColor(Color.BLACK);
		gfx.fillRect(0, 0, width, height);
		
		if (state == State.PLAYING) {
			for (GameObject g : gameObjects)
				if (!g.isHidden())
					g.render_(gfx);
	
			if (RENDER_DEBUG)
				debugRender.render(gfx);
		} else if (state == State.MENU) {

		}

		hud.render(gfx);
			 
		gfx.dispose();
		window.getBufferStrategy().show();
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
		end();
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

	@Override
	public void launch(LauncherOption opt, Dispatcher dispatcher) {
		width = opt.getWidth();
		height = opt.getHeight();
		
		// Open game in a new thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				open();
				dispatcher.dispatch();
			}
		}).start();
	}

}
