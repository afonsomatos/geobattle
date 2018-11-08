package geobattle.living.bots;

import geobattle.core.Game;

public class BotFactory {

	public static Bot create(Game game, Class<? extends Bot> botClass) {
		
		Bot bot;
		if (botClass == Soldier.class)
			bot = new Soldier(game, 0, 0);
		else if (botClass == Slicer.class)
			bot = new Slicer(game, 0, 0);
		else if (botClass == Tower.class)
			bot = new Tower(game, 0, 0);
		else if (botClass == Slime.class)
			bot = new Slime(game, 0, 0);
		else if (botClass == Sentry.class)
			bot = new Sentry(game, 0, 0);
		else if (botClass == Fly.class)
			bot = new Fly(game, 0, 0);
		else if (botClass == Creeper.class)
			bot = new Creeper(game, 0, 0);
		else if (botClass == Bubble.class)
			bot = new Bubble(game, 0, 0);
		else if (botClass == Bomber.class)
			bot = new Bomber(game, 0, 0);
		else
			throw new IllegalArgumentException("Botclass not suported");
		
		return bot;
	}
}
