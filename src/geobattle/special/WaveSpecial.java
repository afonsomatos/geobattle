package geobattle.special;

import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.object.Wave;
import geobattle.object.WaveFactory;

public class WaveSpecial extends Special {

	public WaveSpecial(Game game, Tag tag) {
		super(game);
		setTag(tag);
	}

	@Override
	public void send() {
		Wave wave = new WaveFactory().create(getGame());
		wave.moveTo(getPos().x, getPos().y);
		getGame().spawnGameObject(wave);
	}

}
