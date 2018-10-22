package geobattle.special;

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
import geobattle.util.Palette;
import geobattle.util.Util;

public class Bomb extends GameObject {

	private final static Sprite SPRITE = new Sprite(30, 40, 15, 25);
	
	static {
		SPRITE.draw(new Circle(15, Palette.GREY));
		SPRITE.draw(gfx -> {
			gfx.setColor(Palette.RED);
			gfx.fillRect(-5, -20, 5, 10);
		});
	}
	
	private final static Color FUSE_COLOR = Palette.BROWN;
	private final static int FUSE_THICKNESS = 3;
	private final static int FUSE_ARC_DEGREE = 100;
	
	private int damage = 500;
	private long delay = 2000;
	private WaveSpecial waveSpecial = new WaveSpecial(game);
	private Event explode = new Event(delay, false, this::explode);
	
	public Bomb(Game game, double x, double y, Tag tag) {
		super(game, x, y);
		setTag(tag);
		setSprite(SPRITE);
		
		waveSpecial.setDamage(damage);
		waveSpecial.setRadius(100);
		waveSpecial.setSpeed(0.03);
		waveSpecial.setColor(Palette.ORANGE);
	}
	
	void setDelay(int delay) {
		explode.setDelay(delay);
	}
	
	long getDelay() {
		return explode.getDelay();
	}
	
	void setDamage(int damage) {
		this.damage = damage;
	}

	private void explode() {
		waveSpecial.setTag(getTag());
		waveSpecial.setPos(new Point((int)getX(), (int)getY()));
		waveSpecial.send();
		this.kill();
	}
	
	@Override
	protected void spawn() {
		game.getSchedule().add(explode);
	}

	@Override
	protected void update() {

	}

	@Override
	protected void render(Graphics2D gfx) {
		Sprite sprite = new Sprite(30, 40, 15, 25);
		sprite.draw(new Circle(15, Palette.GREY));
		sprite.draw(g -> {
			g.rotate(0.3);
			g.setColor(Palette.GREY);
			g.fillRect(-5, -20, 10, 10);
		});
		
		setSprite(sprite);
		
		gfx.setColor(FUSE_COLOR);
		gfx.setStroke(new BasicStroke(FUSE_THICKNESS, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		gfx.drawArc((int)getX()+6, (int)getY()-28, 30, 30, 160, - (int) (FUSE_ARC_DEGREE* (1 - explode.getPercentage())));
	}

}