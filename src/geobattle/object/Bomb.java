package geobattle.object;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Tag;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Circle;
import geobattle.schedule.Event;
import geobattle.util.Log;
import geobattle.util.Palette;
import geobattle.util.Util;

public class Bomb extends GameObject {

	private final static Sprite SPRITE = new Sprite(30, 40, 15, 20);

	static {
		SPRITE.draw(new Circle(15, Palette.GREY));
		SPRITE.draw(g -> {
			g.rotate(0.3);
			g.setColor(Palette.GREY);
			g.fillRect(-5, -20, 10, 10);
		});
	}
	
	private final static Color FUSE_COLOR = Palette.BROWN;
	private final static int FUSE_THICKNESS = 3;
	private final static int FUSE_ARC_DEGREE = 100;
	
	private int damage = 500;
	private long delay = 2000;
	private WaveFactory waveFactory;
	private Event explode = new Event(delay, false, this::explode);
	
	public Bomb(Game game, Tag tag) {
		super(game);
		setTag(tag);
		setSprite(SPRITE);
		setColor(FUSE_COLOR);
		
		waveFactory = new WaveFactory()
				.setDamage(damage)
				.setRadius(100)
				.setSpeed(0.03)
				.setColor(Palette.ORANGE);
		
		addDrawer(this::draw);
		getTriggerMap().add("spawn", this::spawned);
	}
	
	void setDelay(int delay) {
		explode.setDelay(delay);
	}
	
	long getDelay() {
		return explode.getDelay();
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}

	private void explode() {

		Wave wave = waveFactory.create(game);
		wave.moveTo(this);
		wave.setTag(getTag());
		game.spawnGameObject(wave);
		
		this.kill();
	}
	
	private void spawned() {
		game.getSchedule().start(explode);
	}

	private void draw(Graphics2D gfx) {
		gfx.setColor(getColor());
		gfx.setStroke(new BasicStroke(FUSE_THICKNESS, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		gfx.drawArc((int)getX()+6, (int)getY()-28, 30, 30, 160, - (int) (FUSE_ARC_DEGREE* (1 - explode.getPercentage())));
	}

}