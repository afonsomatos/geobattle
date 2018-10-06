package geobattle.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Player;

public class HealthBonus extends Item {

	private int quantity;
	
	public HealthBonus(Game game, double x, double y, int quantity) {
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
		gfx.setColor(Color.GREEN);
		gfx.drawLine(0, -height/2, 0, height/2);
		gfx.drawLine(-width/2, 0, width/2, 0);
		gfx.dispose();
	}

	@Override
	public void collected(GameObject collector) {
		if (collector instanceof Player) {
			Player p = (Player) collector;
			quantity = p.giveHealth(quantity);
		}
		if (quantity == 0)
			kill();
	}

}
