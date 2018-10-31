package geobattle.item;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.util.Tank;

abstract class UnitsItem extends Item {
	
	private Tank unitsTank = new Tank();
	private Color labelColor = Color.WHITE;
	
	public UnitsItem(Game game, double x, double y, int units) {
		super(game, x, y);
		unitsTank.set(units);
		addDrawer(this::draw);
		addController(this::killEmpty);
	}
	
	public void setLabelColor(Color labelColor) {
		this.labelColor = labelColor;
	}

	public void setUnits(int units) {
		unitsTank.set(units);
	}
	
	public int getUnits() {
		return unitsTank.get();
	}
	
	private void killEmpty(GameObject obj) {
		if (unitsTank.empty())
			this.kill();
	}
	
	private void draw(Graphics2D gfx) {
		String label = Integer.toString(unitsTank.get());
		int x = (int) (getX() - gfx.getFontMetrics().stringWidth(label) / 2.0);
		int y = (int) (getY() - getCollider().getHeight() / 2.0 - 10);
		gfx.setColor(labelColor);
		gfx.drawString(label, x, y);
	}

}
