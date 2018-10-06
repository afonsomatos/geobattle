package geobattle.core;

import java.util.LinkedList;
import java.util.Random;

import geobattle.core.Schedule.Event;
import geobattle.enemies.Bubble;
import geobattle.enemies.Creeper;
import geobattle.enemies.Enemy;
import geobattle.enemies.Soldier;
import geobattle.enemies.Tower;

class LevelManager {

	private Game game;

	private int level = 0;
	private boolean loadingLevel = false;
	private int levelCountDown;

	private Random rand = new Random();
	
	public LevelManager(Game game) {
		this.game = game;
	}
	
	public void sendNextLevel() {
		loadingLevel = true;
		levelCountDown = 3;

		Event event = new Event();
		event.setRunnable(() -> {
			if (--levelCountDown == 0) {
				level++;
				loadLevel();
				event.setOff(true);
				loadingLevel = false;
			}
		});
		event.setDelay(1000);
		event.setRepeat(true);

		game.getSchedule().add(event);
	}
	
	private void loadLevel() {
		int width = game.getWidth();
		int height = game.getHeight();
		Player player = game.getPlayer();
		
		LinkedList<Enemy> newEnemies = new LinkedList<Enemy>();
		
		for (int i = 0; i < 4; ++i) {	
			newEnemies.add(new Soldier(game, rand.nextInt(width), rand.nextInt(height), player));
			/*
			newEnemies.add(new Creeper(game, rand.nextInt(width), rand.nextInt(height), player));
			newEnemies.add(new Bubble(game, rand.nextInt(width), rand.nextInt(height)));
			newEnemies.add(new Tower(game, rand.nextInt(width), rand.nextInt(height), player));
			newEnemies.add(new Bubble(game, rand.nextInt(width), rand.nextInt(height)));
			*/
		}
		
		for (Enemy e : newEnemies)
			game.spawnGameObject(e);
	}
	
	public int getLevelCountDown() {
		return levelCountDown;
	}

	public boolean isLoadingLevel() {
		return this.loadingLevel;
	}
	
	public void setLoadingLevel(boolean loadingLevel) {
		this.loadingLevel = loadingLevel;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
}
