package geobattle.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

public class Test {
	
	public interface GameRunnable {
		public void run(Game game);
	}
	
	private static HashMap<String, GameRunnable> map = new HashMap<String, GameRunnable>();
	
	public static void run(String name, Game game) {
		map.get(name).run(game);
	}
	
	static {
		
		map.put("levelmanager", game -> {
			Bot b = new Bubble(game, 200, 200);
			b.setTag(Tag.Enemy);
			b.setTarget(game.getPlayer());
			game.spawnGameObject(new BotSpawner(game, b, 3000, () -> game.getLevelManager().setLoadingLevel(false) ));
		});
		
		map.put("spawnx200", game -> {
			Random rand = new Random();
			int width = game.getWidth();
			int height = game.getHeight();
			
			List<Bot> creepers = new ArrayList<>(200);
			for (int i = 0; i < 200; ++i)
				creepers.add(new Creeper(game, rand.nextInt(width), rand.nextInt(height)));
			
			for (Bot b : creepers) {
				b.setTag(Tag.Enemy);
				b.setTarget(game.getPlayer());
				game.spawnGameObject(new BotSpawner(game, b, 3000, () -> game.getLevelManager().setLoadingLevel(false) ));
			}

		});
		
		map.put("showcase", game -> {
			Random rand = new Random();
			int x = game.getWidth() / 2;
			int y = game.getHeight() / 2;
			int level = game.getLevelManager().getLevel();
			
			Bot newBot;
			
			int i = level % 10;
			
			if (i == 9)
				newBot = new Zombie(game, x, y);
			else if (i == 8)
				newBot =(new Bomber(game, x, y));
			else if (i == 7)
				newBot =(new Fly(game, x, y));
			else if (i == 6)
				newBot =(new Slicer(game, x, y));
			else if (i == 5)
				newBot =(new Tower(game, x, y));
			else if (i == 4)
				newBot =(new Soldier(game, x, y));
			else if (i == 3)
				newBot =(new Bubble(game, x, y));
			else if (i == 2)
				newBot =(new Slime(game, x, y));
			else
				newBot =(new Creeper(game, x, y));
			
			newBot.setTarget(game.getPlayer());
			newBot.setTag(Tag.Enemy);
			game.spawnGameObject(new BotSpawner(game, newBot, 3000, () -> game.getLevelManager().setLoadingLevel(false) ));
			
		});
		
		map.put("buildhouse", game -> {
			Player player = game.getPlayer();
			BotBuildHouse slicerBuildHouse = new BotBuildHouse(game, 200, 200, Slicer.class, player);
			slicerBuildHouse.setTag(Tag.Enemy);
			game.spawnGameObject(slicerBuildHouse);
			
			BotBuildHouse soldierBuildHouse = new BotBuildHouse(game, 400, 400, Soldier.class, player);
			soldierBuildHouse.setTag(Tag.Enemy);
			game.spawnGameObject(soldierBuildHouse);
		});
		
		map.put("teamfight", (Game game) -> {
			Random rand = new Random();
			int width = game.getWidth();
			int height = game.getHeight();
			Tag afonso = new Tag("Afonso");
			Tag tomas	= new Tag("Tomás");
			Tag manuel = new Tag("Manuel");
			
			game.rivalTags(afonso, tomas, manuel);
			
			int quant = 100;
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
			}	
		});
		
		map.put("sentries", (Game game) -> {
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
				sentries[i].setFocused(true);
			}

			game.rivalTags(tags);
			
			for (int i = 0; i < quant; ++i) {
				sentries[i].setTarget(sentries[(i + 1) % quant]);
				game.spawnGameObject(sentries[i]);
			}
		});
		
	}
	
}
