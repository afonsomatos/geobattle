package geobattle.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Player;
import geobattle.util.Log;
import geobattle.weapons.Weapon;

public class AmmoBonus extends Item {

	private int quantity;
	
	public AmmoBonus(Game game, double x, double y, int quantity) {
		super(game, x, y);
		this.quantity = quantity;
		setWidth(20);
		setHeight(20);
	}

	@Override
	public void render(Graphics2D superGfx) {
		Graphics2D gfx = (Graphics2D) superGfx.create();
		gfx.setStroke(new BasicStroke(10));
		gfx.translate(getX(), getY());
		final int width = getWidth();
		final int height = getHeight();
		gfx.setColor(Color.ORANGE);
		gfx.rotate(Math.PI / 4);
		gfx.fillRect(-width/2, -height/2, width, height);
		gfx.dispose();
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Player) {
			Player p = (Player) collector;
			Weapon weapon = p.getWeapon();
			quantity = weapon.fillAmmo(quantity);
		}
		Log.i(quantity + "bullets left");
		if (quantity == 0)
			kill();
	}

}
