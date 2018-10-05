package geobattle.specials;

import java.awt.Point;

import geobattle.core.Game;
import geobattle.core.Tag;

public abstract class Special {
	
	protected Game game;
	protected Point pos;
	protected Tag tag = Tag.Neutral;
	
	public Special(Game game) {
		this(game, null);
	}
	
	public Special(Game game, Point pos) {
		this.pos = pos;
		this.game = game;
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