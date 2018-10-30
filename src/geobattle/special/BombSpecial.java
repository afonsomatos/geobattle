package geobattle.special;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.object.Bomb;
import geobattle.util.Palette;

public class BombSpecial extends Special {

	private int damage = 500;
	private Color color = Palette.BROWN;
	
	public BombSpecial(Game game, Tag tag) {
		super(game);
		setTag(tag);
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	@Override
	public void send() {
		Game game = getGame();
		Bomb bomb = new Bomb(game, getPos().getX(), getPos().getY(), getTag());
		bomb.setColor(color);
		bomb.setDamage(damage);
		game.spawnGameObject(bomb);
	}

}
