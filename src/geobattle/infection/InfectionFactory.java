package geobattle.infection;

import java.awt.Color;
import geobattle.core.Game;
import geobattle.living.Living;

public class InfectionFactory {
	
	private Integer damage;
	private Integer delay;
	private Integer hits;
	private Integer spikes;
	private Color color;
	
	public InfectionFactory setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public InfectionFactory setSpikes(int spikes) {
		this.spikes = spikes;
		return this;
	}
	
	public InfectionFactory setDamage(int damage) {
		this.damage = damage;
		return this;
	}
	
	public InfectionFactory setDelay(int delay) {
		this.delay = delay;
		return this;
	}
	
	public InfectionFactory setHits(int hits) {
		this.hits = hits;
		return this;
	}
	
	public Infection create(Game game, Living target) {
		Infection infection = new Infection(game, target);
		
		if (damage 	!= null) infection.setDamage(damage);
		if (color	!= null) infection.setColor(color);
		if (delay	!= null) infection.setDelay(delay);
		if (hits	!= null) infection.setHits(hits);
		if (spikes	!= null) infection.setSpikes(spikes);
		
		return infection;
	}

}
