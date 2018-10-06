package geobattle.specials;

import java.awt.Color;
import java.awt.Point;

import geobattle.core.Game;
import geobattle.core.Tag;

public class WaveSpecial extends Special {

	private Color color = Color.RED;
	
	public WaveSpecial(Game game, Tag tag) {
		super(game);
		setTag(tag);
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public void send() {
		final Tag tag = getTag();
		final Game game = getGame();
		final Point pos = getPos();
		
		Wave wave = new Wave(game, pos.getX(), pos.getY(), getTag());
		wave.setColor(color);
		wave.setTag(tag);
		game.spawnGameObject(wave);
	}

}
