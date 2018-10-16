package geobattle.item;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.CircleCross;
import geobattle.util.Util;
import geobattle.weapon.Shotgun;
import geobattle.weapon.Weapon;

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

		if (wave == 1) {
			Weapon weapon = new Shotgun(game, game.getPlayer(), Tag.Player);
			Color col = Util.randomColor();
			Sprite sprite = new CircleCross(20, Util.randomColor(), Util.randomColor());
			game.spawnGameObject(new WeaponItem(game, randX, randY, sprite, weapon));
		}
		
		
		/*
		if (wave % 3 == 0)
			game.spawnGameObject(new HealthItem(game, randX, randY, 300));
		else if (wave % 5 == 0)
			game.spawnGameObject(new AmmoItem(game, randX, randY, 100));
		else if (wave % 7 == 0)
			game.spawnGameObject(new ShieldItem(game, randX, randY, 200));
			*/
	}

	@Override
	public void spawn() {
		
	}

	@Override
	public void render(Graphics2D gfx) {

	}
	
}
