package geobattle.special;

import geobattle.core.Game;
import geobattle.core.Tag;

public class BombSpecial extends Special {

	private int damage = 500;
	
	public BombSpecial(Game game, Tag tag) {
		super(game);
		setTag(tag);
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	@Override
	public void send() {
		Game game = getGame();
		Bomb bomb = new Bomb(game, getPos().getX(), getPos().getY(), getTag());
		bomb.setDamage(damage);
		game.spawnGameObject(bomb);
	}

}
