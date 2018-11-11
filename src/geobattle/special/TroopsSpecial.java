package geobattle.special;

import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.living.bots.Sentry;
import geobattle.weapon.Weapon;

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
		Sentry sentry = new Sentry(game);
		sentry.moveTo(getPos().x, getPos().y);
		sentry.addTargetTag(targetTag);
		sentry.setTag(getTag());
		sentry.getWeapon().setAmmoSaved(Weapon.INFINITE_AMMO);
		game.spawnGameObject(sentry);
	}

}
