package geobattle.living.bots;

import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.schedule.Event;
import geobattle.core.Tag;
import geobattle.render.sprite.Sprite;
import geobattle.special.BombSpecial;
import geobattle.util.Interval;
import geobattle.util.Palette;
import geobattle.util.Util;

public class Bomber extends Bot {

	private final static Sprite SPRITE = new Sprite(90, 60, 50, 30);
	private final static int HEALTH = 1000;
	private final static int MARGIN = 100;

	static {
		SPRITE.draw(g -> {
			g.setColor(Palette.NAVY);
			int side = 50;
			int[] xs = { 0, -side, -side };
			int[] ys = { 0, side, -side };

			g.translate(-30, 0);
			g.fillPolygon(xs, ys, 3);

			g.translate(30, 0);
			g.setColor(Palette.MINT);
			g.fillRect(-15, 10, 30, 20);

			Ellipse2D.Double body = Util.Graphics.getEllipse(0, 0, 70, 50);
			g.setColor(Palette.GREY);
			g.fill(body);
			g.setStroke(new BasicStroke(3));
			g.setColor(Palette.NAVY);
			g.draw(body);

		});
	}

	private boolean onMap = true;
	private int reappearDelay = 2000;

	private BombSpecial bombSpecial;

	private Interval<Integer> attackInterval = new Interval<Integer>(750, 1500);

	private Event attackEvent =
			new Event(Util.randomInteger(attackInterval), true, event -> {
				bombSpecial.setPos(getPos());
				bombSpecial.send();
				event.setDelay(Util.randomInteger(attackInterval));
			});

	public Bomber(Game game) {
		super(game);
		bombSpecial = new BombSpecial(game, Tag.Enemy);
		setSprite(SPRITE);
		setHealth(HEALTH);
		getCollider().surround(SPRITE);
		setSpeed(6);
		setVelX(6);

		getTriggerMap().add("die", attackEvent::off);
		getTriggerMap().add("spawn", this::resetEvent);

		addController(this::travel);
	}

	public void resetEvent() {
		if (attackEvent != null)
			attackEvent.off();

		game.getSchedule().start(attackEvent);
	}

	private void travel(GameObject obj) {
		// make him reappear
		if (!this.isOutOfBorders(-MARGIN)) {
			if (!onMap) {
				resetEvent();
			}
			onMap = true;
		} else if (onMap) {
			// wait a little bit
			onMap = false;
			attackEvent.off();
			game.getSchedule().next(reappearDelay, () -> {
				stop();
				int z = Util.randomInteger(0, 3);
				if (z <= 1) {
					double x =
							Util.randomDouble(MARGIN, game.getWidth() - MARGIN);
					setX(x);
					if (z == 1) {
						// up
						setY(-MARGIN);
						// go down
						setVelY(getSpeed());
					} else if (z == 0) {
						// down
						setY(game.getHeight() + MARGIN);
						// go up
						setVelY(-getSpeed());
					}
				} else if (z <= 3) {
					double y = Util.randomDouble(MARGIN,
							game.getHeight() - MARGIN);
					setY(y);
					if (z == 2) {
						// left
						setX(-MARGIN);
						// go right
						setVelX(getSpeed());
					} else if (z == 3) {
						// right
						setX(game.getWidth() + MARGIN);
						// go left
						setVelX(-getSpeed());
					}
				}
			});
		}

	}

}
