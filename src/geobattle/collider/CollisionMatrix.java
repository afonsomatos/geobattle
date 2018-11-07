package geobattle.collider;

import geobattle.core.Tag;

public final class CollisionMatrix {
	
	private final static int MAX_TAGS = 1000;
	
	private boolean[][] matrix = new boolean[MAX_TAGS][MAX_TAGS];
	
	private void verifyTags(Tag... tags) {
		for (Tag t : tags)
			if (t.id >= MAX_TAGS)
				throw new IllegalArgumentException("Tag id must be < " + MAX_TAGS);
	}
	
	public CollisionMatrix add(Tag tag1, Tag tag2) {
		verifyTags(tag1, tag2);
		int id1 = tag1.id;
		int id2 = tag2.id;
		matrix[id1][id2] = matrix[id2][id1] = true;
		return this;
	}
	
	public CollisionMatrix remove(Tag tag1, Tag tag2) {
		verifyTags(tag1, tag2);
		int id1 = tag1.id;
		int id2 = tag2.id;
		matrix[id1][id2] = matrix[id2][id1] = false;
		return this;
	}
	
	public boolean collidesWith(Tag tag1, Tag tag2) {
		verifyTags(tag1, tag2);
		return matrix[tag1.id][tag2.id];
	}

}