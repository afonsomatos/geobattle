package geobattle.extension;

import geobattle.core.GameObject;

public abstract class Extension<T extends GameObject> {
	
	public abstract void tick(T gameObject);

}