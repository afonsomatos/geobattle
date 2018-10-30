package geobattle.triggers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TriggerMap {

	private Map<Object, LinkedList<Runnable>> map = new HashMap<Object, LinkedList<Runnable>>();
	
	public void call(Object key) {
		if (map.containsKey(key))
			map.get(key).forEach(Runnable::run);
	}
	
	public void add(Object key, Runnable runnable) {
		map.putIfAbsent(key, new LinkedList<Runnable>());
		map.get(key).addFirst(runnable);
	}
	
	public void clear(Object key) {
		if (map.containsKey(key))
			map.get(key).clear();
	}
	
	public void remove(Object key, Runnable runnable) {
		List<Runnable> set = map.get(key);
		if (set != null)
			set.remove(runnable);
	}
	
}
