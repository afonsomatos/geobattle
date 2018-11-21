package geobattle.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

import geobattle.living.Player;
import geobattle.object.Compass;
import geobattle.render.Renderable;
import geobattle.special.slot.SpecialSet;
import geobattle.special.slot.SpecialSlot;
import geobattle.util.Palette;
import geobattle.util.Util;
import geobattle.weapon.WeaponSet;
import geobattle.weapon.Weapon;

/**
 * Renders a HUD on top of the game. Responsible for status and info of the
 * player and game.
 */
class HUD implements Renderable {

	/**
	 * Empty space between the sides of the frame and the labels.
	 */
	private final static int PADDING = 10;

	/**
	 * Thickness of the involving border around the canvas.
	 */
	private final static int BORDER_THICKNESS = 5;

	private final static Color BORDER_COLOR = Palette.GREEN;

	/**
	 * Color of the layer that indicates that the player is out of the map.
	 */
	private final static Color EXITING_MAP_BG =
			Palette.alpha(Palette.BLACK, 100);

	/**
	 * Color of the layer that indicates the player is taking damage.
	 */
	private final static Color TAKING_DAMAGE_BG =
			Palette.alpha(Palette.RED, 100);

	private final static Color LABELS_COLOR = Palette.WHITE;
	private final static Font LABELS_FONT = new Font("arial", Font.PLAIN, 16);

	/**
	 * Top screen in-game message.
	 */
	private final static Font MESSAGE_FONT = new Font("arial", Font.BOLD, 20);

	private Game game;

	/**
	 * Compass that points to the position of the player when outside of the
	 * map. This is useful because the player might get loss.
	 */
	private Compass playerCompass;

	private int width, height;
	private Player player;

	private List<Renderable> drawers = Arrays.asList(
			this::border,
			this::gameOver,
			this::levelFinished,
			this::takingDamage,
			this::exitingMap,
			this::stats,
			this::pause,
			this::info,
			this::ammo,
			this::weaponSet,
			this::specialSet,
			this::message);

	HUD(Game game) {
		this.game = game;
		playerCompass = new Compass(game);
	}

	/**
	 * Draws topscreen game message.
	 * 
	 * @param gfx
	 */
	private void message(Graphics2D gfx) {
		gfx.setFont(MESSAGE_FONT);
		Util.Graphics.drawStringCentered(gfx, width / 2, PADDING + 60,
				game.getMessage());
	}

	/**
	 * Draws info related to this wave in the top left corner.
	 * 
	 * @param gfx
	 */
	private void stats(Graphics2D gfx) {
		int ascent = gfx.getFontMetrics().getMaxAscent();
		int fontsize = gfx.getFontMetrics().getFont().getSize();
		int spacing = 2;

		gfx.drawString("Health: " + player.getHealth(),
				PADDING, PADDING + ascent);
		gfx.drawString("Score: " + game.getLevelManager().getScore(),
				PADDING, PADDING + ascent + fontsize + spacing);
		gfx.drawString("Wave: " + game.getLevelManager().getWave(),
				PADDING, PADDING + ascent + 2 * (spacing + fontsize));
		gfx.drawString("Shield: " + (int) player.getShield(),
				PADDING, PADDING + ascent + 3 * (fontsize + spacing));
	}

	/**
	 * Draws technical info in the bottom left corner.
	 * 
	 * @param gfx
	 */
	private void info(Graphics2D gfx) {
		int fontsize = gfx.getFontMetrics().getFont().getSize();
		int spacing = 2;
		gfx.drawString("Ups: " + game.getUps(), PADDING,
				height - fontsize - PADDING - spacing);
		gfx.drawString("Fps: " + game.getFps(), PADDING, height - PADDING);
	}

	/**
	 * Draws weapon indicator in the bottom right corner.
	 * 
	 * @param gfx
	 */
	private void ammo(Graphics2D gfx) {
		Weapon weapon = player.getWeapon();

		// Empty slot
		if (weapon == null)
			return;

		// Render ammo
		int ammoSaved = weapon.getAmmoSaved();
		int ammoLoad = weapon.getAmmoLoad();

		String txt = (ammoLoad == Weapon.INFINITE_AMMO ? "∞" : ammoLoad) + "/" +
				(ammoSaved == Weapon.INFINITE_AMMO ? "∞" : ammoSaved);

		int fontsize = gfx.getFontMetrics().getFont().getSize();
		int spacing = 2;

		gfx.drawString(txt,
				width - gfx.getFontMetrics().stringWidth(txt) - PADDING,
				height - PADDING - fontsize - spacing);

		// Render weapon state
		txt = '[' + (weapon.isReloading()
				? "RELOADING"
				: weapon.getAmmoLoad() == 0
						? "NO AMMO"
						: "READY")
				+ ']';

		gfx.drawString(txt,
				width - gfx.getFontMetrics().stringWidth(txt) - PADDING,
				height - PADDING);
	}

	/**
	 * Draws a paused sign in the top of the screen.
	 * 
	 * @param gfx
	 */
	private void pause(Graphics2D gfx) {
		if (!game.isPaused())
			return;
		String txt = "[PAUSED]";
		Util.Graphics.drawStringCentered(gfx, width / 2, PADDING + 10, txt);
	}

	/**
	 * Draws the available weapons and indicator of selection - in the bottom
	 * center.
	 * 
	 * @param gfx
	 */
	private void weaponSet(Graphics2D gfx) {
		WeaponSet ars = player.getWeaponSet();
		int selected = ars.getSelected() + 1;
		int total = ars.size;
		String txt = "";

		for (int i = 1; i <= total; ++i) {
			if (i == selected)
				txt += "<" + i + ">";
			else
				txt += "[" + i + "]";
			txt += i == total ? "" : "   ";
		}

		Util.Graphics.drawStringCentered(gfx, width / 2, height - PADDING - 10,
				txt);
	}

	/**
	 * Draws warning that player is outside of the map.
	 * 
	 * @param gfx
	 */
	private void exitingMap(Graphics2D gfx) {
		if (!game.isOutOfBorders())
			return;

		gfx.setColor(EXITING_MAP_BG);
		gfx.fillRect(0, 0, width, height);

		gfx.setColor(Palette.RED);
		gfx.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));

		int middle = height / 2;
		String txt = "You're out of borders!";
		Util.Graphics.drawStringCentered(gfx, width / 2, middle - 50, txt);

		txt = String.format("Exiting in %d seconds",
				(int) game.getOutOfBorderCounter().getValue());
		Util.Graphics.drawStringCentered(gfx, width / 2, middle + 50, txt);

		playerCompass.moveTo(width / 2, height / 2);
		playerCompass.setTarget(player);
		playerCompass.update();
		playerCompass.render(gfx);
	}

	/**
	 * Draws warning that player is taking damage.
	 * 
	 * @param gfx
	 */
	private void takingDamage(Graphics2D gfx) {
		if (!game.isPlayerGettingHit())
			return;
		gfx.setColor(TAKING_DAMAGE_BG);
		gfx.fillRect(0, 0, width, height);
	}

	/**
	 * Draws game border.
	 * 
	 * @param gfx
	 */
	private void border(Graphics2D gfx) {
		gfx.setColor(BORDER_COLOR);
		gfx.setStroke(new BasicStroke(BORDER_THICKNESS));

		gfx.drawRect(
				BORDER_THICKNESS / 2,
				BORDER_THICKNESS / 2,
				width - BORDER_THICKNESS,
				height - BORDER_THICKNESS);
	}

	/**
	 * Draws message that the level has been completed.
	 * 
	 * @param gfx
	 */
	private void levelFinished(Graphics2D gfx) {
		if (!game.isLevelFinished())
			return;

		gfx.setFont(LABELS_FONT.deriveFont(Font.BOLD, 40));

		String str = "Level cleared";
		int h = gfx.getFontMetrics().getHeight();
		int w = gfx.getFontMetrics().stringWidth(str);
		int x = game.getWidth() / 2 - w / 2;
		int y = game.getHeight() / 2 - h / 2;

		gfx.setColor(Palette.BLACK);
		Util.drawStringOutline(gfx, str, x, y);
		gfx.setColor(Palette.WHITE);
		gfx.drawString(str, x, y);

		gfx.setFont(LABELS_FONT.deriveFont(Font.BOLD, 24));
		str = "Your score was " + game.getLevelManager().getScore();
		w = gfx.getFontMetrics().stringWidth(str);
		x = game.getWidth() / 2 - w / 2;
		y = game.getHeight() / 2 + 30;

		gfx.setColor(Palette.BLACK);
		Util.drawStringOutline(gfx, str, x, y);
		gfx.setColor(Palette.WHITE);
		gfx.drawString(str, x, y);
	}

	/**
	 * Draws message that the player has lost.
	 * 
	 * @param gfx
	 */
	private void gameOver(Graphics2D gfx) {
		if (!game.isGameOver())
			return;

		gfx.setColor(TAKING_DAMAGE_BG);
		gfx.fillRect(0, 0, width, height);
		gfx.setFont(LABELS_FONT.deriveFont(Font.BOLD, 40));

		String str = "Game Over";
		int h = gfx.getFontMetrics().getHeight();
		int w = gfx.getFontMetrics().stringWidth(str);
		int x = game.getWidth() / 2 - w / 2;
		int y = game.getHeight() / 2 - h / 2;

		gfx.setColor(Palette.BLACK);
		Util.drawStringOutline(gfx, str, x, y);
		gfx.setColor(Palette.WHITE);
		gfx.drawString(str, x, y);

		gfx.setFont(LABELS_FONT.deriveFont(Font.BOLD, 24));
		str = "Your score was " + game.getLevelManager().getScore();
		w = gfx.getFontMetrics().stringWidth(str);
		x = game.getWidth() / 2 - w / 2;
		y = game.getHeight() / 2 + 30;

		gfx.setColor(Palette.BLACK);
		Util.drawStringOutline(gfx, str, x, y);
		gfx.setColor(Palette.WHITE);
		gfx.drawString(str, x, y);
	}

	/**
	 * Draws info about the available special slots on the left center.
	 * 
	 * @param gfx
	 */
	private void specialSet(Graphics2D gfx) {
		String specialKeys = "ZXCVBNM";
		SpecialSet specialSet = player.getSpecialSet();

		int linePadding = 30;
		int setSize = specialSet.size();
		int fontSize = gfx.getFontMetrics().getHeight();

		double y =
				game.getHeight() / 2.0 -
						(setSize * fontSize + (setSize - 1) * linePadding)
								/ 2.0;

		for (int i = 0; i < setSize; ++i) {
			SpecialSlot s = specialSet.get(i);
			char c = specialKeys.charAt(i);
			String txt = c + ": " + s.getIndicator();
			gfx.drawString(txt, PADDING, (int) y + linePadding * i);
		}
	}

	@Override
	public void render(Graphics2D superGfx) {
		width = game.getWidth();
		height = game.getHeight();
		player = game.getPlayer();

		Graphics2D gfx = (Graphics2D) superGfx.create();
		gfx.setFont(LABELS_FONT);
		gfx.setColor(LABELS_COLOR);

		drawers.forEach(d -> {
			Graphics2D g = (Graphics2D) gfx.create();
			d.render(g);
			g.dispose();
		});

		gfx.dispose();
	}

}
