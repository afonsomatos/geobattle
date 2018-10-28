package geobattle.item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.CircleCross;
import geobattle.util.Palette;
import geobattle.util.Util;
import geobattle.weapon.Weapon;
import geobattle.weapon.WeaponFactory;

public class ItemGenerator extends GameObject {

	private final int MARGIN = 100;
	private int lastWave = 0;
	private List<Item> expiringItems = new ArrayList<Item>();
	
	public ItemGenerator(Game game) {
		super(game);
		setHidden(true);
	}
	
	public Point getRandomItemLocation() {
		int randX = Util.randomInteger(MARGIN, game.getWidth() - MARGIN);
		int randY = Util.randomInteger(MARGIN, game.getHeight() - MARGIN);
		return new Point(randX, randY);
	}
	
	@Override
	public void update() {
		int wave = game.getLevelManager().getLevel();
		
		// Don't spawn at start or if it's not a new wave
		if (wave == 0 || wave == lastWave)
			return;

		lastWave = wave;
		
		// Remove old items
		for (Item i : expiringItems)
			i.kill();
		
		Point loc;
		List<Item> newItems = new ArrayList<Item>();
		
		if (wave % 5 == 0) {
			loc = getRandomItemLocation();
			newItems.add(new HealthItem(game, loc.getX(), loc.getY(), 300));
		}
		
		if (wave % 5 == 0) {
			loc = getRandomItemLocation();
			newItems.add(new AmmoItem(game, loc.getX(), loc.getY(), 100));
		}
		
		if (wave % 3 == 0) {
			loc = getRandomItemLocation();
			newItems.add(new ShieldItem(game, loc.getX(), loc.getY(), 200));
		}
		
		if (wave % 2 == 0) {
			loc = getRandomItemLocation();

			// Create random weapon
			Weapon weapon = WeaponFactory.Unlimited.create(game, game.getPlayer(), Tag.Player);
			weapon.setAmmoSaved(10);
			weapon.setAmmoCapacity(10);
			weapon.fill();
			
			Color[] cols = Palette.randomWithout(2, Palette.BLACK);
			Sprite sprite = new CircleCross(20, cols[0], cols[1]);
			WeaponItem wi = new WeaponItem(game, loc.getX(), loc.getY(), sprite, weapon);
			newItems.add(wi);
			expiringItems.add(wi);
		}
		
		// Spawn new items
		for (Item i : newItems)
			game.spawnGameObject(i);
	}

	@Override
	public void spawn() {
		
	}

	@Override
	public void render(Graphics2D gfx) {

	}
	
}
