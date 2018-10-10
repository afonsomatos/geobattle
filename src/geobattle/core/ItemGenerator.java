package geobattle.core;

import java.awt.Graphics2D;

import geobattle.item.AmmoItem;
import geobattle.item.HealthItem;
import geobattle.item.ShieldItem;
import geobattle.util.Util;

public class ItemGenerator extends GameObject {

	private final int MARGIN = 100;
	private int lastWave = 0;
	
	public ItemGenerator(Game game) {
		super(game);
		setHidden(true);
	}
	
	@Override
	public void update() {
		int wave = game.getLevelManager().getLevel();
		// Don't spawn at start or if it's not a new wave
		if (wave == 0 || wave == lastWave)
			return;

		lastWave = wave;

		int randX = Util.randomInteger(MARGIN, game.getWidth() - MARGIN);
		int randY = Util.randomInteger(MARGIN, game.getHeight() - MARGIN);

		if (wave % 3 == 0)
			game.spawnGameObject(new HealthItem(game, randX, randY, 300));
		else if (wave % 5 == 0)
			game.spawnGameObject(new AmmoItem(game, randX, randY, 100));
		else if (wave % 7 == 0)
			game.spawnGameObject(new ShieldItem(game, randX, randY, 200));
	}

	@Override
	public void render(Graphics2D gfx) {
		// Hidden
	}
	
}
