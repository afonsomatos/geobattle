package geobattle.living.bots.powerups;

import java.util.Locale;

import geobattle.living.bots.Bot;
import geobattle.particle.Track;
import geobattle.render.sprite.shapes.Rect;
import geobattle.util.Palette;

public class SpeedPowerup extends Powerup {

	private double speedup = 1.0;
	
	public SpeedPowerup(double speedup) {
		this.speedup = speedup;
	}
	
	@Override
	public void apply(Bot bot) {
		bot.setSpeed(bot.getSpeed() + speedup);
		Track track = new Track(bot.getGame(), bot);
		
		int width	= (int) (bot.getCollider().getWidth() * 0.7);
		int height 	= (int) (bot.getCollider().getHeight() * 0.7);
		
		track.setSprite(new Rect(width, height, Palette.ORANGE));
		track.start();
	}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "Speed +%.2f", speedup);
	}
	
}
