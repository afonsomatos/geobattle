package geobattle.special;

import java.awt.Rectangle;

import geobattle.core.Game;
import geobattle.living.Living;
import geobattle.particle.Shield;
import geobattle.schedule.Event;
import geobattle.util.Palette;

public class GodModeSpecial extends Special {

	private Living owner;
	private Shield shield;
	
	private Event clear;
	
	public GodModeSpecial(Game game, Living owner, int duration) {
		super(game);
		this.owner = owner;
		
		clear = new Event(duration, false, this::clear);
		
		// Figure out appropriate shield size
		Rectangle rect = owner.getCollider().getBounds();
		int radius = (int) Math.max(rect.getWidth(), rect.getHeight());
		
		this.shield = new Shield(game, owner, radius,
				Palette.alpha(Palette.LIME, 220));
	}

	@Override
	public void send() {
		shield.start();
		owner.setGodmode(true);
		// Setup self destroy
		getGame().getSchedule().start(clear);
	}
	
	private void clear() {
		owner.setGodmode(false);
		shield.destroy();
	}

}
