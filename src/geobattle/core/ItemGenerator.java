package geobattle.core;

import geobattle.item.AmmoItem;
import geobattle.item.HealthItem;
import geobattle.item.ShieldItem;
import geobattle.util.Util;

public class ItemGenerator extends GameObject {

	private final int MARGIN = 100;
	
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

			int randX = Util.randomInteger(MARGIN, game.getWidth() - MARGIN);
			int randY = Util.randomInteger(MARGIN, game.getHeight() - MARGIN);

			if (wave % 3 == 0) {
				game.spawnGameObject(new HealthItem(game, randX, randY, 300));
			} else if (wave % 5 == 0) {
				game.spawnGameObject(new AmmoItem(game, randX, randY, 100));
			} else if (wave % 7 == 0) {
				game.spawnGameObject(new ShieldItem(game, randX, randY, 200));
			}
			
		}
		
	}

}
