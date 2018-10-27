package geobattle.collider;

import java.util.HashMap;
import java.util.Map;

import geobattle.core.Tag;
import geobattle.util.Log;

public final class CollisionMatrix {
	
	// TODO: Longs have at most 64 bits, so the current implementation fails
	// when there are more than 64 tags.
	private Map<Integer, Long> matrix = new HashMap<Integer, Long>();
	
	public CollisionMatrix add(Tag tag1, Tag tag2) {
		int id1 = tag1.id;
		int id2 = tag2.id;
		matrix.put(id1, matrix.getOrDefault(id1, 0L) | (1 << id2));
		matrix.put(id2, matrix.getOrDefault(id2, 0L) | (1 << id1));
		return this;
	}
	
	public CollisionMatrix remove(Tag tag1, Tag tag2) {
		int id1 = tag1.id;
		int id2 = tag2.id;
		matrix.put(id1, matrix.getOrDefault(id1, 0L) & ~(1 << id2));
		matrix.put(id2, matrix.getOrDefault(id2, 0L) & ~(1 << id1));	
		return this;
	}
	
	public boolean collidesWith(Tag tag1, Tag tag2) {
		return 0 != (matrix.getOrDefault(tag1.id, 0L) & (1 << tag2.id));
	}

}