package geobattle.object;

import com.sun.glass.events.KeyEvent;

import geobattle.core.Game;
import geobattle.core.GameObject;
import geobattle.core.Settings;
import geobattle.io.KeyInput;
import geobattle.living.Player;

public class ArrowKeysFollower extends GameObject {

	private final static int PRECISION_KEY = KeyEvent.VK_SHIFT;
	
	// Slice mechanism
	private int totalSlices		= 64;
	private int radius			= 100;
	private int sliceDir 		= 0;
	private double currentSlice	= 0;
	
	private double sliceSpeed 	= 1;
	private double precision 	= 0.1;
	
	private final static int A = -1; 	// Rotate anti-clockwise
	private final static int C = 1;		// Rotate clock-wise
	private final static int L = 0;		// Stop and make leap
	private final static int X = 2;		// Don't do anything
	
	private final static int[] KEYS = new int[] {
		KeyEvent.VK_RIGHT,
		KeyEvent.VK_UP,
		KeyEvent.VK_LEFT,
		KeyEvent.VK_DOWN,
	};
	
	private final static int[][] DIR_MAP_V1 = new int[][] {
		/*R   U	  L	  D */
		{ A, L, L, C }, // first quarter
		{ L, L, C, A }, // second quarter
		{ L, C, A, L }, // thrid quarter
		{ C, A, L, L }, // fourth quarter
		
		{ L, A, L, C }, // right axis
		{ A, L, C, L }, // down axis
		{ L, C, L, A }, // left axis
		{ C, L, A, L }  // up axis
	};
	
	private final static int[][] DIR_MAP_V2 = new int[][] {
		/*R   U	  L	  D */
		{ A, A, C, C }, // first quarter
		{ A, C, C, A }, // second quarter
		{ C, C, A, A }, // thrid quarter
		{ C, A, A, C }, // fourth quarter
		
		{ L, A, A, C }, // right axis
		{ A, C, C, L }, // down axis
		{ C, C, L, A }, // left axis
		{ C, L, A, C }  // up axis
	};
	
	private final static int[][] DIR_MAP_V3 = new int[][] {
		/*R   U	  L	  D */
		{ C, X, A, X }, // first quarter
		{ C, X, A, X }, // second quarter
		{ C, X, A, X }, // thrid quarter
		{ C, X, A, X }, // fourth quarter
		     
		{ C, X, A, X }, // right axis
		{ C, X, A, X }, // down axis
		{ C, X, A, X }, // left axis
		{ C, X, A, X }  // up axis
	};
	
	public enum ArrowMap {
		V1(DIR_MAP_V1),
		V2(DIR_MAP_V2),
		V3(DIR_MAP_V3);
		
		public final int[][] map;
		
		ArrowMap(int[][] map) {
			this.map = map;
		}
	}
	
	// Direction map to use
	public ArrowMap arrowMap;  
	
	public ArrowKeysFollower(Game game) {
		super(game);
		
		Settings settings = game.getSettings();
		setSliceSpeed(settings.getDouble("arrows.speed"));
		setArrowMap(ArrowMap.values()[settings.getInt("arrows.mode") - 1]);
	}
	
	public void setSliceSpeed(double speed) {
		this.sliceSpeed = speed;
	}
	
	public void setArrowMap(ArrowMap arrowMap) {
		this.arrowMap = arrowMap;
	}
	
	public int getSlice() {
		return Math.floorMod(
				(int) Math.round(currentSlice), totalSlices); 
	}
	
	private void handleArrowInput() {
		
		KeyInput ki = game.getIOManager().getKeyInput();
		
		int quarter = totalSlices / 4;
		
		int[] quarters = new int[] {
			0,
			3 * quarter,
			2 * quarter,
			1 * quarter
		};
		
		int[][] dirMap = arrowMap.map;
		
		int slice = getSlice();
		int currQuarter = (int) slice / quarter;
		int dirs[];
		
		if (slice % quarter == 0)
			dirs = dirMap[4 + currQuarter];
		else
			dirs = dirMap[currQuarter];
		
		int dir = 0, i;
		for (i = KEYS.length - 1; i >= 0; --i)
			if (ki.isPressingKey(KEYS[i])) {
				dir = dirs[i];
				break;
			}
		
		if (i >= 0) {
			if (dir == 0)
				currentSlice = quarters[i];
			if (dir != X)
				sliceDir = dir;
		} else
			// No keys pressed, stop
			sliceDir = 0;
		
	}
	
	private double handlePrecisionInput() {
		boolean usingPrecision =
				game.getIOManager()
				.getKeyInput()
				.isPressingKey(PRECISION_KEY);
		
		if (!usingPrecision)
			return sliceSpeed;
		
		return precision * sliceSpeed;
	}
	
	@Override
	public void update() {
		
		handleArrowInput();
		double currSpeed = handlePrecisionInput();
		
		// Get a rounded slice
		currentSlice += currSpeed * sliceDir;
		currentSlice %= totalSlices;
		int slice = getSlice();
		
		double theta =  2 * Math.PI * slice / totalSlices;
		
		// Set position in relation with the player
		Player player = game.getPlayer();
		int x = (int) (player.getX() + Math.cos(theta) * radius);
		int y = (int) (player.getY() + Math.sin(theta) * radius);
		setX(x);
		setY(y);
	}

	
}
