package geobattle.collider;

import geobattle.core.Tag;

/**
 * Keeps track of which {@code Tag}s collide with eachother. This is used
 * by {@link CollisionManager} to check if two collider's can collide based on
 * their game object's tag.
 */
public final class CollisionMatrix {

	private final static int MAX_TAGS = 1000;

	private boolean[][] matrix = new boolean[MAX_TAGS][MAX_TAGS];

	private void verifyTags(Tag... tags) {
		for (Tag t : tags)
			if (t.id >= MAX_TAGS)
				throw new IllegalArgumentException(
						"Tag id must be < " + MAX_TAGS);
	}

	/**
	 * Makes {@code tag1} collidable with {@code tag2}.
	 * 
	 * @param tag1
	 * @param tag2
	 */
	public void add(Tag tag1, Tag tag2) {
		verifyTags(tag1, tag2);
		int id1 = tag1.id;
		int id2 = tag2.id;
		matrix[id1][id2] = matrix[id2][id1] = true;
	}
	
	/**
	 * Makes {@code tag1} not collidable with {@code tag2}.
	 * 
	 * @param tag1
	 * @param tag2
	 */
	public void remove(Tag tag1, Tag tag2) {
		verifyTags(tag1, tag2);
		int id1 = tag1.id;
		int id2 = tag2.id;
		matrix[id1][id2] = matrix[id2][id1] = false;
	}
	
	/**
	 * Checks whether two tags are collidable.
	 * 
	 * @param tag1
	 * @param tag2
	 * @return whether {@code tag1} and {@code tag2} collide.
	 */
	public boolean collidesWith(Tag tag1, Tag tag2) {
		verifyTags(tag1, tag2);
		return matrix[tag1.id][tag2.id];
	}

}