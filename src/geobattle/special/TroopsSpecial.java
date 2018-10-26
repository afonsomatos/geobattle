package geobattle.special;

import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.living.bots.Sentry;

public class TroopsSpecial extends Special {

	private Tag targetTag;
	
	public TroopsSpecial(Game game, Tag teamTag, Tag targetTag) {
		super(game);
		setTag(teamTag);
		this.targetTag = targetTag;
	}

	@Override
	public void send() {
		Game game = getGame();
		Sentry sentry = new Sentry(game, getPos().x, getPos().y);
		sentry.addTargetTag(targetTag);
		sentry.setTag(getTag());
		game.spawnGameObject(sentry);
	}

}