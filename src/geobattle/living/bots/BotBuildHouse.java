package geobattle.living.bots;

import java.awt.Color;

import geobattle.core.Game;
import geobattle.living.Living;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Circle;
import geobattle.render.sprite.shapes.Diamond;
import geobattle.schedule.Event;
import geobattle.util.Log;
import geobattle.util.Palette;

public class BotBuildHouse extends Living {
	
	private static final int HEALTH = 10000;
	
	private Sprite sprite;
	
	private Class<? extends Bot> botClass;
	private Living target;
	private Event event;
	private long delay = 7000;
	
	public <T extends Bot> BotBuildHouse(Game game, int x, int y, Class<T> botClass, Living target) {
		super(game);
		this.botClass = botClass;
		this.target = target;
		moveTo(x, y);
		setHealth(HEALTH);
		
		customizeHouse();

		setSprite(sprite);
		getCollider().surround(sprite);
		
		getTriggerMap().add("spawn", this::setup);
		getTriggerMap().add("die", this::remove);
	}
	
	private void customizeHouse() {
		Color color = Palette.BLUE;
		
		if (botClass == Soldier.class) {
			color = Palette.RED;
		} else if (botClass == Slicer.class) {
			color = Palette.ORANGE;
		}
		
		sprite = new Sprite(110, 110, 55, 55);
		sprite.draw(new Circle(45, color));
		sprite.draw(new Diamond(110, 110, Palette.WHITE));
		sprite.draw(new Diamond(100, 100, color));
		sprite.draw(new Diamond(90,90, Palette.WHITE));
		sprite.draw(new Diamond(83,83, Palette.BLACK));
	}
	
	private void remove() {
		event.setOff(true);
		kill();
	}
	
	private Bot buildBot() {
		if (botClass == Soldier.class) {
			return new Soldier(game);
		} else if (botClass == Slicer.class) {
			return new Slicer(game);
		} else 
			throw new RuntimeException("Can't build bots of type " + botClass);
	}
	
	private void setup() {
		Log.i("setup");
		event = new Event(delay, true, () -> {
			Log.i("ho?");
			Bot bot = buildBot();
			bot.setTag(getTag());
			bot.moveTo(this);
			bot.setTarget(target);
			game.spawnGameObject(bot);
		});
		game.getSchedule().add(event);
	}

}
