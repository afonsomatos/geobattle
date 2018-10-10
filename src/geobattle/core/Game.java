package geobattle.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import geobattle.collider.CollisionHandler;
import geobattle.core.Schedule.Event;
import geobattle.living.Player;
import geobattle.living.enemies.Enemy;
import geobattle.render.Renderer;
import geobattle.util.Counter;
import geobattle.util.Log;
import geobattle.weapon.Arsenal;
import geobattle.weapon.Rifle;
import geobattle.weapon.Shotgun;
import geobattle.weapon.Sniper;
import geobattle.weapon.Unlimited;
import geobattle.weapon.Weapon;

public class Game {

	private boolean RENDER_DEBUG = false;
	private Renderer debugRender;

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
	
	private LinkedList<GameObject> gameObjects = new LinkedList<GameObject>();
	
	public Game() {
		hud = new HUD(this);
		levelManager = new LevelManager(this);
		window = new Window(this);
		collisionHandler = new CollisionHandler(this);
		debugRender = new Debug(this);

		outOfBorderEvent = new Event(1000, true, () -> outOfBorderCounter.tick());
		gettingHitEvent = new Event(500, false, () -> gettingHit = false);
	}
	
	public void start() {
		score = 0;
		gameRunning = true;
		
		Log.i("Game starting");
		
		levelManager.sendNextLevel();
		
		Player player = new Player(this);
			
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
		
		player.setTarget(window.getMouseInput().getMouseFollower());
		spawnGameObject(player);
		this.player = player;

		//spawnGameObject(new HealthItem(this, 400, 400, 300));
		//spawnGameObject(new ShieldItem(this, 200, 200, 200));
		
		spawnGameObject(new ItemGenerator(this));
		
		window.setVisible(true);
		
		gameLoop();	
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
	
	public Window getWindow() {
		return window;
	}
	
	public int getUps() {
		return ups;
	}
	
	public boolean isPaused() {
		return paused;
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
	
	public void tick() {
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
		
		Color oldColor = gfx.getColor();
		gfx.setColor(Color.BLACK);
		gfx.fillRect(0, 0, width, height);
		gfx.setColor(oldColor);
		
		for (GameObject g : gameObjects)
			if (!g.isHidden())
				g.render_(gfx);

		hud.render(gfx);

		if (RENDER_DEBUG)
			debugRender.render(gfx);
		 
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

}
