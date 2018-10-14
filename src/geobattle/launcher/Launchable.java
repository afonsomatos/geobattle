package geobattle.launcher;

import geobattle.util.Dispatcher;

public interface Launchable {
	void launch(LauncherOption opt, Dispatcher dispatcher);
}