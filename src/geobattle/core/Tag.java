package geobattle.core;

import java.awt.Color;

import geobattle.util.Palette;

/**
 * Identifies a game object and is used for collision detecting, team building,
 * etc.
 */
public class Tag {

	private static int nextId = 0;

	/**
	 * Default Neutral tag.
	 */
	public final static Tag Neutral = new Tag();
	
	/**
	 * Default Enemy tag.
	 */
	public final static Tag Enemy = new Tag("Enemy");
	
	/**
	 * Default Void tag. Used to identify objects that are against the enemies
	 * and the player.
	 */
	public final static Tag Void = new Tag();
	
	/**
	 * Default Item tag.
	 */
	public final static Tag Item = new Tag();
	
	/**
	 * Default Player tag. Used to identify objects that are friendly towards
	 * the player.
	 */
	public final static Tag Player = new Tag("Friendly");

	private Color color = Palette.WHITE;
	private String name;

	/**
	 * Unique tag identifier. Starts at 0.
	 */
	public final int id;

	public Tag() {
		this("#" + nextId);
	}

	public Tag(String name) {
		id = nextId++;
		this.name = name;
	}

	/**
	 * Sets the color of the tag. A tag is rendered with this color.
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Returns the color of the tag.
	 * 
	 * @return color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the name of the tag. Used when rendering a tag.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of this tag.
	 * 
	 * @return name of the tag
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("[Tag #%d ~ %s]", id, name);
	}

}