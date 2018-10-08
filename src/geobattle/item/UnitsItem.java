package geobattle.items;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.util.Tank;

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

}
