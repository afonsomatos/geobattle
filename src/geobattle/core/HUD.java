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

class HUD implements Renderable {

	private final static int 	PADDING 			= 10;
	private final static int 	BORDER_THICKNESS 	= 5;
	private final static Color 	BORDER_COLOR 		= Palette.GREEN;
	private final static Color 	EXITING_MAP_BG 		= Palette.alpha(Palette.BLACK, 100);
	private final static Color 	TAKING_DAMAGE_BG 	= Palette.alpha(Palette.RED, 100);
	private final static Color 	LABELS_COLOR 		= Palette.WHITE;
	private final static Font 	LABELS_FONT 		= new Font("arial", Font.PLAIN, 16);
	private final static Font	MESSAGE_FONT		= new Font("arial", Font.BOLD, 25);
	
	private Game game;
	private Compass playerCompass;
	private int width, height;
	private Player player;
	
	private List<Renderable> drawers = Arrays.asList(
			this::border,
			this::gameOver,
			this::takingDamage,
			this::exitingMap,
			this::stats,
			this::waveStatus,
			this::pause,
			this::info,
			this::ammo,
			this::weaponSet,
			this::specialSet,
			this::message
			);
	
	HUD(Game game) {
		this.game = game;
		playerCompass = new Compass(game);
	}
	
	private void message(Graphics2D gfx) {
		gfx.setFont(MESSAGE_FONT);
		Util.Graphics.drawStringCentered(gfx, width / 2, PADDING + 60, game.getMessage());
	}
	
	private void waveStatus(Graphics2D gfx) {
		String txt = "";
		
		LevelManager levelManager = game.getLevelManager();
		boolean loading = levelManager.isLoadingLevel();
		int countDown = levelManager.getLevelCountDown();
		
		if (loading && countDown > 0)
			txt = "Next wave in " +levelManager.getLevelCountDown();
		else if (!loading)
			txt = "Enemies left: " + game.getEnemiesLeft();
	
		int ascent = gfx.getFontMetrics().getMaxAscent();
		
		gfx.drawString(txt, game.getWidth() - gfx.getFontMetrics().stringWidth(txt) - PADDING, PADDING + ascent);
	}
	
	private void stats(Graphics2D gfx) {
		int ascent 		= gfx.getFontMetrics().getMaxAscent();
		int fontsize 	= gfx.getFontMetrics().getFont().getSize();
		int spacing 	= 2;
		
		gfx.drawString("Health: " 	+ player.getHealth(),
				PADDING, PADDING + ascent);
		gfx.drawString("Score: " 	+ game.getScore(),
				PADDING, PADDING + ascent + fontsize + spacing);
		gfx.drawString("Wave: " 	+ game.getLevelManager().getLevel(),
				PADDING, PADDING + ascent + 2 * (spacing + fontsize));
		gfx.drawString("Shield: " 	+ (int) player.getShield(),
				PADDING, PADDING + ascent + 3 * (fontsize + spacing));
	}
	
	private void info(Graphics2D gfx) {
		int fontsize = gfx.getFontMetrics().getFont().getSize();
		int spacing = 2;
		gfx.drawString("Ups: " + game.getUps(), PADDING, height - fontsize - PADDING - spacing);
		gfx.drawString("Fps: " + game.getFps(), PADDING, height - PADDING);
	}
	
	private void ammo(Graphics2D gfx) {
		Weapon weapon = player.getWeapon();
		
		// Empty slot
		if (weapon == null)
			return;
		
		// Render ammo
		int ammoSaved 	= weapon.getAmmoSaved();
		int ammoLoad 	= weapon.getAmmoLoad();
		
		String txt = (ammoLoad == Weapon.INFINITE_AMMO ? "∞" : ammoLoad) + "/" +
				(ammoSaved == Weapon.INFINITE_AMMO ? "∞" : ammoSaved);
		
		int fontsize = gfx.getFontMetrics().getFont().getSize();
		int spacing = 2;
		
		gfx.drawString(txt, width - gfx.getFontMetrics().stringWidth(txt) - PADDING, height - PADDING - fontsize - spacing);	
	
		// Render weapon state
		txt = '[' + (weapon.isReloading()
					? "RELOADING"
					: weapon.getAmmoLoad() == 0
						? "NO AMMO"
						: "READY"
							) + ']';


		gfx.drawString(txt, width - gfx.getFontMetrics().stringWidth(txt) - PADDING, height - PADDING );
	}
	
	private void pause(Graphics2D gfx) {
		if (!game.isPaused()) return;
		String txt = "[PAUSED]";
		Util.Graphics.drawStringCentered(gfx, width / 2, PADDING + 10, txt);
	}
	
	private void weaponSet(Graphics2D gfx) {
		WeaponSet ars = player.getArsenal();
		int selected = ars.getSelected();
		int total = ars.size;
		String txt = "";
		
		for (int i = 0; i < total; ++i) {
			if (i == selected)
				txt += "<" + i + ">";
			else
				txt += "[" + i + "]";
			txt += i == total - 1 ? "" : "   ";
		}
		
		Util.Graphics.drawStringCentered(gfx, width / 2, height - PADDING - 10, txt);
	}
	
	private void exitingMap(Graphics2D gfx) {
		if (!game.isOutOfBorders()) return;
		
		gfx.setColor(EXITING_MAP_BG);
		gfx.fillRect(0, 0, width, height);
		
		gfx.setColor(Palette.RED);
		gfx.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		
		int middle = height / 2;
		String txt = "You're out of borders!";
		Util.Graphics.drawStringCentered(gfx, width / 2, middle - 50, txt);
		
		txt = String.format("Exiting in %d seconds", (int) game.getOutOfBorderCounter().getValue());
		Util.Graphics.drawStringCentered(gfx, width / 2, middle + 50, txt);
	
		playerCompass.moveTo(width / 2, height / 2);
		playerCompass.setTarget(player);
		playerCompass.update();
		playerCompass.render(gfx);
	}
	
	private void takingDamage(Graphics2D gfx) {
		if (!game.isPlayerGettingHit()) return;
		gfx.setColor(TAKING_DAMAGE_BG);
		gfx.fillRect(0, 0, width, height);
	}
	

	private void border(Graphics2D gfx) {
		gfx.setColor(BORDER_COLOR);
		gfx.setStroke(new BasicStroke(BORDER_THICKNESS));
		
		gfx.drawRect(
				BORDER_THICKNESS/2,
				BORDER_THICKNESS/2,
				width - BORDER_THICKNESS,
				height - BORDER_THICKNESS);
	}
	
	private void gameOver(Graphics2D gfx) {
		if (!game.isGameOver()) return;
		
		gfx.setColor(TAKING_DAMAGE_BG);
		gfx.fillRect(0, 0, width, height);
		gfx.setFont(LABELS_FONT.deriveFont(Font.BOLD, 40));

		String str = "Game Over";
		int h = gfx.getFontMetrics().getHeight();
		int w = gfx.getFontMetrics().stringWidth(str);
		int x = game.getWidth() / 2 - w/2;
		int y = game.getHeight() / 2 - h/2;
		
		gfx.setColor(Palette.BLACK);
		Util.drawStringOutline(gfx, str, x, y);
		gfx.setColor(Palette.WHITE);
		gfx.drawString(str, x, y);
		
		gfx.setFont(LABELS_FONT.deriveFont(Font.BOLD, 24));
		str = "Your score was " + game.getScore();
		w = gfx.getFontMetrics().stringWidth(str);
		x = game.getWidth() / 2 - w/2;
		y = game.getHeight() / 2 + 30;
		
		gfx.setColor(Palette.BLACK);
		Util.drawStringOutline(gfx, str, x, y);
		gfx.setColor(Palette.WHITE);
		gfx.drawString(str, x, y);
	}
	
	private void specialSet(Graphics2D gfx) {
		String specialKeys = "ZXCVBNM";
		SpecialSet specialSet = player.getSpecialSet();
		
		int linePadding = 30;
		int setSize = specialSet.size();
		int fontSize = gfx.getFontMetrics().getHeight();
		
		double y =
				game.getHeight() / 2.0 -
				(setSize * fontSize + (setSize - 1) * linePadding) / 2.0;
		
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
