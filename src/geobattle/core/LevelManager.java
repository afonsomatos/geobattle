package geobattle.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import geobattle.living.Living;
import geobattle.living.Player;
import geobattle.living.bots.Bomber;
import geobattle.living.bots.Bot;
import geobattle.living.bots.BotBuildHouse;
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
		
		Random rand = new Random();
		
		List<Bot> newEnemies = new ArrayList<Bot>();
		
		//newEnemies.add(new Zombie(game, 300, 300));
		
		Test.run("teamfight", game);
		boolean debug=true;
		
		if (!debug) {
			
		for (int i = 1; i < level + 1; ++i) {
			if (i % 9 == 0)
				newEnemies.add(new Zombie(game, rand.nextInt(width), rand.nextInt(height)));
			else if (i % 8 == 0)
				newEnemies.add(new Bomber(game, rand.nextInt(width), rand.nextInt(height)));
			else if (i % 7 == 0)
				newEnemies.add(new Fly(game, rand.nextInt(width), rand.nextInt(height)));
			else if (i % 6 == 0)
				newEnemies.add(new Slicer(game, rand.nextInt(width), rand.nextInt(height)));
			else if (i % 5 == 0)
				newEnemies.add(new Tower(game, rand.nextInt(width), rand.nextInt(height)));
			else if (i % 4 == 0)
				newEnemies.add(new Soldier(game, rand.nextInt(width), rand.nextInt(height)));
			else if (i % 3 == 0)
				newEnemies.add(new Bubble(game, rand.nextInt(width), rand.nextInt(height)));
			else if (i % 2 == 0)
				newEnemies.add(new Slime(game, rand.nextInt(width), rand.nextInt(height)));
			else if (i % 1 == 0)
				newEnemies.add(new Creeper(game, rand.nextInt(width), rand.nextInt(height)));
		}

		}
		
		for (Bot e : newEnemies) {
			e.setTarget(player);
			e.setTag(Tag.Enemy);
			game.spawnGameObject(new BotSpawner(game, e, 3000, () -> loadingLevel = false ));
		}
		
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
