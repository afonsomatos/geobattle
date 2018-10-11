package geobattle.object;

import java.awt.Color;
import java.awt.Graphics2D;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.render.sprite.Sprite;
import geobattle.render.sprite.shapes.Cross;

public class MouseFollower extends GameObject {

	private static Sprite sprite = new Cross(10, 10, 5, Color.WHITE);
	
	public MouseFollower(Game game) {
		super(game);
		setSprite(sprite);
	}

	@Override
	protected void spawn() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void render(Graphics2D gfx) {
		// TODO Auto-generated method stub
	}

}
