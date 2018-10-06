package geobattle.specials;

import java.awt.Point;

import geobattle.core.Game;
import geobattle.core.Tag;

public abstract class Special {
	
	private Game game;
	private Point pos;
	private Tag tag = Tag.Neutral;
	
	public Special(Game game) {
		this(game, null);
	}
	
	public Special(Game game, Point pos) {
		this.pos = pos;
		this.game = game;
	}
	
	public Point getPos() {
		return pos;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setPos(Point pos) {
		this.pos = pos;
	}
	
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	public Tag getTag() {
		return tag;
	}
	
	public abstract void send();
}