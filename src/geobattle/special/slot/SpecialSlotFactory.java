package geobattle.special.slot;

import java.util.function.Function;

import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.special.AsteroidSpecial;
import geobattle.special.BombSpecial;
import geobattle.special.GodModeSpecial;
import geobattle.special.Special;
import geobattle.special.TroopsSpecial;
import geobattle.special.WaveSpecial;
import geobattle.util.Palette;

public class SpecialSlotFactory {
	
	private final String name;
	private final Function<Game, SpecialSlot> func;
	
	private SpecialSlotFactory(String name, Function<Game, SpecialSlot> func) {
		this.name = name;
		this.func = func;
	}
	
	public String getName() {
		return name;
	}
	
	public SpecialSlot create(Game game) {
		return func.apply(game);
	}
	
	public final static SpecialSlotFactory GODMODE;
	public final static SpecialSlotFactory SENTRY_5S;
	public final static SpecialSlotFactory ASTEROID_15S;
	public final static SpecialSlotFactory BOMBS_X5;
	public final static SpecialSlotFactory WAVE;
	
	// Default specials
	static {
		
		GODMODE = new SpecialSlotFactory("Godmode", game -> {
			Special special = new GodModeSpecial(game, game.getPlayer(), 5000);
			return new TimedSpecialSlot(special, 15_000);
		});
		
		WAVE = new SpecialSlotFactory("Wave", game -> {
			Special special = new WaveSpecial(game, Tag.Player);
			return new TimedSpecialSlot(special, 15_000);		
		});
		
		SENTRY_5S = new SpecialSlotFactory("Sentry x5", game -> {
			Special special = new TroopsSpecial(game, Tag.Player, Tag.Enemy);
			return new UnitSpecialSlot(special, 5);		
		});
		
		ASTEROID_15S = new SpecialSlotFactory("Asteroid 15s", game -> {
			Special special = new AsteroidSpecial(game , game.getPlayer(), 5000);
			return new TimedSpecialSlot(special, 15_000);		
		});
		
		BOMBS_X5 = new SpecialSlotFactory("Bombs x5", game -> {
			BombSpecial special = new BombSpecial(game, Tag.Void);
			special.setColor(Palette.CYAN);
			return new UnitSpecialSlot(special, 5);
		});
	}
	
}