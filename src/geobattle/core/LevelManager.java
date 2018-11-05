package geobattle.core;

import geobattle.living.Player;
import geobattle.living.bots.Bot;
import geobattle.living.bots.BotSpawner;
import geobattle.living.bots.Soldier;
import geobattle.schedule.Event;
import geobattle.util.Log;
import geobattle.util.Util;

public class LevelManager {

	private Game game;

	private int waveCountDown;
	private int wave;
	private int level;
	private int dead;
	
	private Runnable levelFinisher;
	
	public LevelManager(Game game) {
		this.game = game;
	}
	
	public void sendLevel(int level, Runnable levelFinisher) {
		this.level = level;
		this.levelFinisher = levelFinisher;
		sendNextWave();
	}
	
	private void loadWave(Runnable finish) {

		int width = game.getWidth();
		int height = game.getHeight();
		Player player = game.getPlayer();
		
		int n = wave * level;
		dead = 0;
		Bot[] bots = new Bot[n];
		for (int i = 0; i < bots.length; ++i) {
			int x = Util.randomInteger(20, width - 20);
			int y = Util.randomInteger(20, height - 20);
			Soldier s = new Soldier(game, x, y);
			s.setTag(Tag.Enemy);
			s.setTarget(player);
			s.getTriggerMap().addLast("die", () -> {
				if (++dead >= bots.length)
					finishWave();
			});
			game.spawnGameObject(new BotSpawner(game, s, 3000));
		}
	}
	
	private void finishWave() {
		Log.i("Wave finished!");
		if (wave == 10) {
			levelFinisher.run();
			return;
		}
		
		game.sendMessage(3000, "Wave cleared!");
		game.getSchedule().next(3000, this::sendNextWave);
	}
	
	private void sendNextWave() {
		waveCountDown = 10;

		Event event = new Event();
		game.sendMessage(1000, "New wave in " + waveCountDown);
		event.setRunnable(() -> {
			if (--waveCountDown == 0) {
				wave++;
				loadWave(this::finishWave);
				event.setOff(true);
				game.sendMessage(1000, "Go!");
			} else {
				game.sendMessage(1000, "New wave in " + waveCountDown);
			}
		});
		event.setDelay(1000);
		event.setRepeat(true);
		game.getSchedule().add(event);
	}
	
	public int getWaveCountDown() {
		return waveCountDown;
	}

	public int getWave() {
		return this.wave;
	}

}
