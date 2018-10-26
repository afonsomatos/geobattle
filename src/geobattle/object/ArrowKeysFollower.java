package geobattle.object;

import java.util.HashMap;
import java.util.Map;

import com.sun.glass.events.KeyEvent;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.io.KeyInput;
import geobattle.living.Player;
import geobattle.util.Log;

public class ArrowKeysFollower extends GameObject {

	private int radius = 100;
	
	// Slice mechanism
	private int totalSlices 	= 100;
	private int sliceDir 		= 0;
	private double sliceSpeed	= 1.2;
	private double currentSlice	= 0;
	
	public ArrowKeysFollower(Game game) {
		super(game);
	}
	
	public int getSlice() {
		return Math.floorMod(
				(int) Math.round(currentSlice), totalSlices); 
	}
	
	private void handleArrowInput() {
		
		KeyInput ki = game.getWindow().getGameCanvas().getKeyInput();
		
		int quarter = totalSlices / 4;
		
		int[] quarters = new int[] {
				0,
				3 * quarter,
				2 * quarter,
				1 * quarter
		};
		
		int[] keys = new int[] {
				KeyEvent.VK_RIGHT,
				KeyEvent.VK_UP,
				KeyEvent.VK_LEFT,
				KeyEvent.VK_DOWN,
		};
		
		int[][] dirMap = new int[][] {
			/*R   U	  L	  D */
			{-1,  0,  0,  1 }, // first quarter
			{ 0,  0,  1, -1 }, // second quarter
			{ 0,  1, -1,  0 }, // thrid quarter
			{ 1, -1,  0,  0	}, // fourth quarter
			
			{ 0, -1,  0,  1 }, // right axis
			{-1,  0,  1,  0 }, // down axis
			{ 0,  1,  0, -1 }, // left axis
			{ 1,  0, -1,  0 }  // up axis
		};
		
		int slice = getSlice();
		int currQuarter = (int) slice / quarter;
		int dirs[];
		
		Log.i("Curr slice: " + slice);
		Log.i("Curr quarter: " + currQuarter);
		
		// Detect if axis or not
		if (slice % quarter == 0) {
			dirs = dirMap[4 + currQuarter];
			Log.i("Axis deteted!");
		} else {
			dirs = dirMap[currQuarter];
			Log.i("Quarter detected!");
		}
		
		int dir = 0, i;
		boolean moving = false;
		for (i = keys.length - 1; i >= 0; --i) {
			if (ki.isPressingKey(keys[i])) {
				dir = dirs[i];
				moving = true;
				break;
			}
		}
		
		if (i >= 0) {
			Log.i("new dir: " + dir);
			// Make a slice leap
			if (dir == 0)
				currentSlice = quarters[i];
			sliceDir = dir;
		} else {
			// No keys pressed, stop
			sliceDir = 0;
		}
		
	}
	
	@Override
	public void update() {
		
		// Each slice represents one angle
		double sliceAngle = 2 * Math.PI / totalSlices;
		
		handleArrowInput();
		
		// Get a rounded slice
		currentSlice += sliceSpeed * sliceDir;
		currentSlice %= totalSlices;
		int slice = getSlice();
		
		// Get its angle
		double theta = slice * sliceAngle;
		
		// Set position in relation with the player
		Player player = game.getPlayer();
		int x = (int) (player.getX() + Math.cos(theta) * radius);
		int y = (int) (player.getY() + Math.sin(theta) * radius);
		setX(x);
		setY(y);
	}

	
}
