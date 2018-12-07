package geobattle.core;

class GameLoop {

	private Game game;

	private boolean running = false;

	private volatile int ups = 0;
	private volatile int fps = 0;

	private static final double NANOS_PER_SECOND = Math.pow(10, 9);
	private static final double NANOS_PER_MILLIS = Math.pow(10, 6);

	private static final double TICKS_PER_SECOND = 60.0;
	private static final double NANOS_PER_TICK =
			NANOS_PER_SECOND / TICKS_PER_SECOND;

	GameLoop(Game game) {
		this.game = game;
	}

	void stop() {
		running = false;
	}
	
	int getUps() {
		return ups;
	}
	
	int getFps() {
		return fps;
	}
	
	void start() {
		running = true;
		new Thread(this::controller).start();
		new Thread(this::renderer).start();
	}

	void end() {
		running = false;
	}

	void controller() {
		
		long lastTime = System.nanoTime();
		long lastSecond = lastTime;
		int updates = 0;
		double delta = 0;
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / NANOS_PER_TICK;
			lastTime = now;

			// Catch all the needed updates
			for (; delta >= 1; --delta) {
				game.tick();
				updates++;
			}

			// Reset updates each second
			if (now - lastSecond >= NANOS_PER_SECOND) {
				lastSecond = now;
				ups = updates; // Global updates per second
				updates = 0;
			}

			// Wait for next update
			double delay =
					((now + NANOS_PER_TICK) - System.nanoTime())
							/ NANOS_PER_MILLIS;
			if (delay > 0) {
				try {
					Thread.sleep((long) delay);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
	}
	
	void renderer() {

		final double targetFps = game.getSettings().getDouble("targetFps");
		final double nanosPerFrame = NANOS_PER_SECOND / targetFps;

		long lastSecond = System.nanoTime();
		int frames = 0;

		while (running) {
			long now = System.nanoTime();

			game.getUIManager().renderFrame(game);
			frames++;

			// Reset frames each second
			if (now - lastSecond >= NANOS_PER_SECOND) {
				lastSecond = System.nanoTime();
				fps = frames; // Global frames per second
				frames = 0;
			}
			
			// Wait for next update
			double delay =
					((now + nanosPerFrame) - System.nanoTime())
							/ NANOS_PER_MILLIS;
			
			if (delay > 0) {
				try {
					Thread.sleep((long) delay);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
	}

}
