package geobattle.launcher;

public interface Launchable {
	void launch(LauncherOption opt, Runnable dispatcher);
}