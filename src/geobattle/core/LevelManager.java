package geobattle.core;

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
		
		/*
		Tag afonso = new Tag("Afonso");
		Tag tomas	= new Tag("Tomás");
		Tag manuel = new Tag("Manuel");
		
		game.rivalTags(afonso, tomas, manuel);
		
		int quant = 15;
		//team1
		LinkedList<Bot> team1 = new LinkedList<Bot>();
		for (int i = 0; i < quant; ++i)
			team1.add(new Sentry(game, rand.nextInt(width), rand.nextInt(height)));
		LinkedList<Bot> team2 = new LinkedList<Bot>();
			for (int i = 0; i < quant; ++i)
				team2.add(new Sentry(game, rand.nextInt(width), rand.nextInt(height)));
		LinkedList<Bot> team3 = new LinkedList<Bot>();
			for (int i = 0; i < quant; ++i)
				team3.add(new Sentry(game, rand.nextInt(width), rand.nextInt(height)));

		for (Bot t1 : team1)
			t1.setTag(afonso);
		
		for (Bot t2 : team2)
			t2.setTag(tomas);
	
		for (Bot t3 : team3)
			t3.setTag(manuel);
		
		
		for (int i = 0; i < quant; ++i) {
			game.spawnGameObject(team1.get(i));
			game.spawnGameObject(team2.get(i));
			game.spawnGameObject(team3.get(i));
		}*/
		
		/*
		int quant = 10;
		int radius = 300;
		
		Sentry[] sentries 	= new Sentry[quant];
		Tag[] tags 			= new Tag[quant];

		double cx = game.getWidth() / 2;
		double cy = game.getHeight() / 2;
		double theta = Math.PI * 2 * (1.0 / quant);
		
		for (int i = 0; i < quant; ++i) {
			tags[i] = new Tag();
			int x = (int) (cx + Math.cos(theta * i) * radius);
			int y = (int) (cy + Math.sin(theta * i) * radius);
			sentries[i] = new Sentry(game, x, y);
			sentries[i].setTag(tags[i]);
			sentries[i].setHealth(3000);
		}

		game.rivalTags(tags);
		
		for (int i = 0; i < quant; ++i) {
			sentries[i].setTarget(sentries[(i + 1) % quant]);
			game.spawnGameObject(sentries[i]);
		}
		*/
		
		BotBuildHouse slicerBuildHouse = new BotBuildHouse(game, 200, 200, Slicer.class, player);
		slicerBuildHouse.setTag(Tag.Enemy);
		game.spawnGameObject(slicerBuildHouse);
		
		BotBuildHouse soldierBuildHouse = new BotBuildHouse(game, 400, 400, Soldier.class, player);
		soldierBuildHouse.setTag(Tag.Enemy);
		game.spawnGameObject(soldierBuildHouse);
		
		List<Bot> newEnemies = new LinkedList<Bot>();

		boolean debug=true;
		
		if (!debug) {
			
		for (int i = 1; i < level + 1; ++i) {
			if (i % 8 == 0)
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
	
			for (Bot e : newEnemies) {
				e.setTarget(player);
				e.setTag(Tag.Enemy);
				game.spawnGameObject(new BotSpawner(game, e, 3000, () -> loadingLevel = false ));
			}
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
