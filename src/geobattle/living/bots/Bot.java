package geobattle.living.bots;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import geobattle.core.Game;
import geobattle.core.Tag;
import geobattle.living.Living;
import geobattle.util.Palette;
import geobattle.util.Util;

public abstract class Bot extends Living {
	
	// Space between tag and lower bound of collider
	private final static int TAG_COLLIDER_MARGIN = 20;
	
	// Current target
	private Living target = null;
	
	// Focus on the current target
	private boolean focused = false;
	
	// Objects to target
	private List<Tag> targetTags = new ArrayList<Tag>();
	
	public Bot(Game game, int x, int y) {
		super(game, x, y);
		getTriggerMap().add("die", this::kill);
		addDrawer(this::renderTag);
	}
	
	private void renderTag(Graphics2D superGfx) {
		Tag tag = getTag();
		if (tag == null || tag.getName() == null) return;
		
		Graphics2D gfx = (Graphics2D) superGfx.create();
			
		gfx.setColor(tag.getColor());
		String txt = tag.getName();
		
		int x = (int) getX();
		int y = (int) (getY() + getCollider().getHeight() / 2) + TAG_COLLIDER_MARGIN;
		Util.Graphics.drawStringCentered(gfx, x, y, txt);
		gfx.dispose();
	}
	
	public void addTargetTag(Tag targetTag) {
		targetTags.add(targetTag);
		getTriggerMap().call("newTargetTag");
	}
	
	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	public boolean isFocused() {
		return focused;
	}
	
	public void setTarget(Living target) {
		this.target = target;
		getTriggerMap().call("newTarget");
	}
	
	public Living getTarget() {
		return this.target;
	}

}

