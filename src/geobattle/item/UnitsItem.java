package geobattle.item;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.util.Tank;
import geobattle.util.Util;

public class UnitsItem extends Item {
	
	private Tank unitsTank = new Tank();

	public UnitsItem(Game game, double x, double y, int units) {
		super(game, x, y);
		unitsTank.set(units);
	}
	
	public void setUnits(int units) {
		unitsTank.set(units);
	}
	
	public int getUnits() {
		return unitsTank.get();
	}
	
	@Override
	public void collected(GameObject collector) {
		if (unitsTank.empty())
			this.kill();
	}
	
	@Override
	public void render(Graphics2D superGfx) {
		super.render(superGfx);
		Graphics2D gfx = (Graphics2D) superGfx.create();
		
		String label = Integer.toString(unitsTank.get());
		int x = (int) (getX() - gfx.getFontMetrics().stringWidth(label) / 2.0);
		int y = (int) (getY() - getCollider().getHeight() / 2.0 - 10);
		gfx.setColor(Color.WHITE);
		gfx.drawString(label, x, y);
		gfx.dispose();
	}

}
