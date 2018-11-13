package geobattle.living.bots.powerups;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Locale;
import java.util.Random;

import geobattle.living.bots.Bot;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Rect;
import geobattle.schedule.Event;
import geobattle.util.Palette;
import geobattle.util.Util;

public class SpeedPowerup extends Powerup {

	private double speedup = 1.0;
	
	private int i = 0;
	private Point[] pos;
	private Sprite sprite = new Rect(7, 7, Palette.ORANGE);
	
	private Random rand = new Random();
	
	public SpeedPowerup(double speedup) {
		this.speedup = speedup;
	}
	
	@Override
	public void apply(Bot bot) {
		bot.setSpeed(bot.getSpeed() + speedup);
		
		// Record last positions
		pos = new Point[5];
		Event track = new Event(50, true, () -> {
			pos[i] = bot.getPos();
			i = (i + 1) % pos.length;
		});
		
		// Draw last positions
		bot.addDrawerFirst(superGfx -> {
			for (Point p : pos) {
				if (p == null) break;
				for (int i = 0; i < 15; ++i) {
					Graphics2D gfx = (Graphics2D) superGfx.create();
					gfx.translate(
							Util.randomInteger(p.x - 10, p.x + 10),
							Util.randomInteger(p.y - 10, p.y + 10));
					sprite.render(gfx);
					gfx.dispose();
				}
			}
		});

		bot.getGame().getSchedule().add(track);
	}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "Speed +%.2f", speedup);
	}
	
}
