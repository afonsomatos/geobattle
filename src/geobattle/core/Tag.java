package geobattle.core;

import java.awt.Color;

import geobattle.util.Log;
import geobattle.util.Palette;

public class Tag {

	private static final int MAX_TAGS = 64;
	private static int nextId = 0;
	
	// Common tags
	public final static Tag Enemy	= new Tag();
	public final static Tag Neutral = new Tag();
	public final static Tag Item	= new Tag();
	public final static Tag Player	= new Tag("Friendly");

	private Color color = Palette.WHITE;
	private String name;
	
	public final int id;
	
	public Tag() {
		this("#" + nextId);
	}
	
	public Tag(String name) {
		id = nextId++;
		nextId %= MAX_TAGS;
		this.name = name;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}