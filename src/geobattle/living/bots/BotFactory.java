package geobattle.living.bots;

import geobattle.core.Game;

public class BotFactory {

	public static Bot create(Game game, Class<? extends Bot> botClass) {
		
		Bot bot;
		if (botClass == Soldier.class)
			bot = new Soldier(game);
		else if (botClass == Slicer.class)
			bot = new Slicer(game);
		else if (botClass == Tower.class)
			bot = new Tower(game);
		else if (botClass == Slime.class)
			bot = new Slime(game);
		else if (botClass == Sentry.class)
			bot = new Sentry(game);
		else if (botClass == Fly.class)
			bot = new Fly(game);
		else if (botClass == Creeper.class)
			bot = new Creeper(game);
		else if (botClass == Bubble.class)
			bot = new Bubble(game);
		else if (botClass == Bomber.class)
			bot = new Bomber(game);
		else if (botClass == Zombie.class)
			bot = new Zombie(game);
		else
			throw new IllegalArgumentException("Botclass not suported");
		
		return bot;
	}
}
