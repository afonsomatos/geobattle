package geobattle.io;

import geobattle.core.Game;

public class IOManager {

	private KeyInput keyInput;
	private MouseInput mouseInput;
	
	public IOManager(Game game) {
		this.keyInput = new KeyInput(game);
		this.mouseInput = new MouseInput(game);
	}
	
	public void disable() {
		keyInput.setActive(false);
		mouseInput.setActive(false);
	}
	
	public void enable() {
		keyInput.setActive(true);
		mouseInput.setActive(true);
	}
	
	public KeyInput getKeyInput() {
		return keyInput;
	}
	
	public MouseInput getMouseInput() {
		return mouseInput;
	}
	
}
