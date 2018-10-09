package geobattle.core;

import geobattle.item.AmmoItem;
import geobattle.item.HealthItem;
import geobattle.item.ShieldItem;
import geobattle.util.Util;

public class ItemGenerator extends GameObject {

	int lastWave = 0;
	
	public ItemGenerator(Game game) {
		super(game);
		setHidden(true);
	}
	
	@Override
	public void tick() {
		int wave = game.getLevelManager().getLevel();
		
		if (wave != 0 && wave != lastWave) {
			lastWave = wave;

			int randX = Util.randomInteger(100, game.getWidth());
			int randY = Util.randomInteger(100, game.getHeight());

			if (wave % 3 == 0) {
				game.spawnGameObject(new HealthItem(game, randX, randY, 300));
			} else if (wave % 5 == 0) {
				game.spawnGameObject(new AmmoItem(game, randX, randY, 00));
			} else if (wave % 7 == 0) {
				game.spawnGameObject(new ShieldItem(game, randX, randY, 200));
			}
			
		}
		
	}

}
