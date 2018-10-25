package geobattle.object;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Cross;
import geobattle.render.sprite.shapes.Square;
import geobattle.util.Log;

public class MouseFollower extends GameObject {

	private final static Sprite SPRITE = new Square(10, 10, Color.WHITE);
	
	public MouseFollower(Game game) {
		super(game);
		setSprite(SPRITE);
	}
	
}
