package geobattle.living.bots.powerups;

import java.util.Locale;

import geobattle.living.bots.Bot;

public class SpeedPowerup extends Powerup {

	private double speedup = 1.0;
	
	public SpeedPowerup(double speedup) {
		this.speedup = speedup;
	}
	
	@Override
	public void apply(Bot bot) {
		bot.setSpeed(bot.getSpeed() + speedup);
	}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "Speed +%.2f", speedup);
	}
	
}
