package geobattle.core;

import java.util.LinkedList;
import java.util.Random;

import geobattle.living.Player;
import geobattle.living.enemies.Bomber;
import geobattle.living.enemies.Bubble;
import geobattle.living.enemies.Creeper;
import geobattle.living.enemies.Enemy;
import geobattle.living.enemies.EnemySpawner;
import geobattle.living.enemies.Fly;
import geobattle.living.enemies.Slime;
import geobattle.living.enemies.Soldier;
import geobattle.living.enemies.Slicer;
import geobattle.living.enemies.Tower;
import geobattle.schedule.Event;

public class LevelManager {

	private Game game;

	private int level = 0;
	private boolean loadingLevel = false;
	private int levelCountDown;
	
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
		
		Random rand = new Random();
		
		//newEnemies.add(new Bomber(game, 400, 400, player));
		
		/*
		for (int i = 1; i < level + 1; ++i) {
			if (i % 7 == 0)
				newEnemies.add(new Fly(game, rand.nextInt(width), rand.nextInt(height), player));
			else if (i % 6 == 0)
				newEnemies.add(new Slicer(game, 300, 300, player));
			else if (i % 5 == 0)
				newEnemies.add(new Tower(game, rand.nextInt(width), rand.nextInt(height), player));
			else if (i % 4 == 0)
				newEnemies.add(new Soldier(game, rand.nextInt(width), rand.nextInt(height), player));
			else if (i % 3 == 0)
				newEnemies.add(new Bubble(game, rand.nextInt(width), rand.nextInt(height)));
			else if (i % 2 == 0)
				newEnemies.add(new Slime(game, rand.nextInt(width), rand.nextInt(height), player));
			else if (i % 1 == 0)
				newEnemies.add(new Creeper(game, rand.nextInt(width), rand.nextInt(height), player));
		}
		
		for (Enemy e : newEnemies)
			game.spawnGameObject(new EnemySpawner(game, e, 3000, () -> loadingLevel = false ));
		*/
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
