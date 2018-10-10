package geobattle.item;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.SpriteRenderer;
import geobattle.util.Tank;

public class UnitsItem extends Item {
	
	private Tank unitsTank = new Tank();
	private Color labelColor = Color.WHITE;
	private SpriteRenderer srend = new SpriteRenderer();
	
	public UnitsItem(Game game, double x, double y, int units) {
		super(game, x, y);
		unitsTank.set(units);
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
	
	public void setSprite(Sprite sprite) {
		srend.setSprite(sprite);
		getCollider().surround(sprite);
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void collected(GameObject collector) {
		if (unitsTank.empty())
			this.kill();
	}
	

	@Override
	public void render(Graphics2D superGfx) {
		Graphics2D gfx = (Graphics2D) superGfx.create();
		srend.render((int)getX(), (int)getY(), superGfx);
		String label = Integer.toString(unitsTank.get());
		int x = (int) (getX() - gfx.getFontMetrics().stringWidth(label) / 2.0);
		int y = (int) (getY() - getCollider().getHeight() / 2.0 - 10);
		gfx.setColor(labelColor);
		gfx.drawString(label, x, y);
		gfx.dispose();
	}

}
