package geobattle.collider;

import geobattle.core.Tag;

public class CollisionMatrix {
	
	private boolean[][] matrix;
	
	public CollisionMatrix() {
		int len = Tag.values().length;
		matrix = new boolean[len][len];
	}
	
	public CollisionMatrix add(Tag tag1, Tag tag2) {
		int p1 = tag1.ordinal();
		int p2 = tag2.ordinal();
		matrix[p1][p2] = true;
		matrix[p2][p1] = true;
		return this;
	}
	
	public boolean collidesWith(Tag tag1, Tag tag2) {
		return matrix[tag1.ordinal()][tag2.ordinal()];
	}

}