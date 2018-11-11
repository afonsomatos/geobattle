package geobattle.living.bots.powerups;

import geobattle.living.bots.Bot;

public class HealthPowerup extends Powerup {

	private int extraHealth = 200;
	
	public HealthPowerup(int extraHealth) {
		this.extraHealth = extraHealth;
	}
	
	@Override
	public void apply(Bot bot) {
		bot.setHealth(bot.getHealth() + extraHealth);
	}
	
	@Override
	public String toString() {
		return "Health +" + extraHealth;
	}

}
