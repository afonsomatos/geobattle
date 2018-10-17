package geobattle.item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.CircleCross;
import geobattle.util.Log;
import geobattle.util.Palette;
import geobattle.util.Util;
import geobattle.weapon.Unlimited;
import geobattle.weapon.Weapon;

public class ItemGenerator extends GameObject {

	private final int MARGIN = 100;
	private int lastWave = 0;
	private List<Item> expiringItems = new LinkedList<Item>();
	
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
		List<Item> newItems = new LinkedList<Item>();
		
		if (wave % 3 == 0) {
			loc = getRandomItemLocation();
			newItems.add(new HealthItem(game, loc.getX(), loc.getY(), 300));
		}
		
		if (wave % 5 == 0) {
			loc = getRandomItemLocation();
			newItems.add(new AmmoItem(game, loc.getX(), loc.getY(), 100));
		}
		
		if (wave % 7 == 0) {
			loc = getRandomItemLocation();
			newItems.add(new ShieldItem(game, loc.getX(), loc.getY(), 200));
		}
		
		if (wave % 2 == 0) {
			loc = getRandomItemLocation();
			
			// Create random weapon
			Weapon weapon = new Unlimited(game, game.getPlayer(), Tag.Player);
			weapon.setAmmoSaved(10);
			weapon.setProjectileColor(Color.GREEN);
			weapon.setAmmoCapacity(10);
			weapon.fill();
			
			Palette[] pal = Palette.randomUnique(2);
			Sprite sprite = new CircleCross(20, pal[0].getColor(), pal[1].getColor());
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
