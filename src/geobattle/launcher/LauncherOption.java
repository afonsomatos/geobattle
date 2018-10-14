package geobattle.launcher;

public class LauncherOption {
	
	private int width;
	private int height;
	private int screen;
	private boolean fullScreen;
	
	LauncherOption() {
		
	}
	
	void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}
	
	void setScreen(int screen) {
		this.screen = screen;
	}
	
	void setWidth(int width) {
		this.width = width;
	}
	
	void setHeight(int height) {
		this.height = height;
	}

	public boolean isFullScreen() {
		return fullScreen;
	}
	
	public int getScreen() {
		return screen;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
}