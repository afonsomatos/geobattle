package geobattle.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import geobattle.living.Player;
import geobattle.object.Compass;
import geobattle.render.Renderable;
import geobattle.special.slot.SpecialSet;
import geobattle.special.slot.SpecialSlot;
import geobattle.util.Palette;
import geobattle.util.Util;
import geobattle.weapon.Arsenal;
import geobattle.weapon.Weapon;

class HUD implements Renderable {

	private Game game;
	private Compass playerCompass;
	
	private final Font labelsFont = new Font("arial", Font.PLAIN, 16);
	private final Color labelsColor = Color.WHITE;

	private final Color gettingHitBackgroundColor = new Color(255, 0, 0, 100);
	private final Color playerOutOfMapBackgroundColor = new Color(0, 0, 0, 100);
	
	private final int padding = 8;
	
	public HUD(Game game) {
		this.game = game;
		playerCompass = new Compass(game, game.getWidth()/2, game.getHeight()/2 + 30, null);
	}
	
	private void renderTopRight(Graphics2D gfx) {
		String txt = "";
		
		boolean loading = game.getLevelManager().isLoadingLevel();
		int countDown = game.getLevelManager().getLevelCountDown();
		
		if (loading && countDown > 0)
			txt = "Next wave in " + game.getLevelManager().getLevelCountDown();
		else if (!loading)
			txt = "Enemies left: " + game.getEnemiesLeft();
	
		int ascent = gfx.getFontMetrics().getMaxAscent();
		
		gfx.drawString(txt, game.getWidth() - gfx.getFontMetrics().stringWidth(txt) - padding, padding + ascent);
	}
	
	private void renderTopLeft(Graphics2D gfx) {
		Player player = game.getPlayer();
	
		int x = padding;
		int ascent = gfx.getFontMetrics().getMaxAscent();
		int fontsize = gfx.getFontMetrics().getFont().getSize();
		int spacing = 2;
		
		gfx.drawString("Health: " + player.getHealth(), x, padding + ascent);
		gfx.drawString("Score: " + game.getScore(), x, padding + ascent + fontsize + spacing);
		gfx.drawString("Wave: " + game.getLevelManager().getLevel(), x, padding + ascent + spacing*2 + 2 * fontsize);
		gfx.drawString("Shield: " + (int) player.getShield(), x, padding + ascent + 3 * fontsize + spacing*3);
	}
	
	public void renderBottomLeft(Graphics2D gfx) {
		int height = game.getHeight();
		gfx.drawString("Ups: " + game.getUps(), 10, height - 30);
		gfx.drawString("Fps: " + game.getFps(), 10, height - 10);
	}
	
	public void renderBottomRight(Graphics2D gfx) {
		String txt;
		int width = game.getWidth();
		int height = game.getHeight();
		
		Weapon weapon = game.getPlayer().getWeapon();
		
		if (weapon == null)
			return;
		
		if (weapon.isReloading())
			txt = "RELOADING";
		else if (weapon.getAmmoLoad() == 0)
			txt = "NO AMMO";
		else
			txt = "READY";	
		txt = '[' + txt + ']';
		gfx.drawString(txt, width - gfx.getFontMetrics().stringWidth(txt) - 10, height - 10);
		
		int ammoSaved = weapon.getAmmoSaved();
		int ammoLoad = weapon.getAmmoLoad();
		
		txt = (ammoLoad == Weapon.INFINITE_AMMO ? "∞" : ammoLoad) + "/" +
				(ammoSaved == Weapon.INFINITE_AMMO ? "∞" : ammoSaved);
		gfx.drawString(txt, width - gfx.getFontMetrics().stringWidth(txt) - 10, height - 30);	
	}
	
	public void renderTopMiddle(Graphics2D gfx) {
		if (game.isPaused()) {
			String txt = "[PAUSED]";
			gfx.drawString(txt,
					game.getWidth() / 2 - gfx.getFontMetrics().stringWidth(txt) / 2, 20);
		}
	}
	
	public void renderBottomMiddle(Graphics2D gfx) {
		Arsenal ars = game.getPlayer().getArsenal();
		int selected = ars.getSelected();
		int total = ars.size;
		String txt;
		
		txt = "";
		for (int i = 0; i < total; ++i) {
			if (i == selected)
				txt += "<" + i + ">";
			else
				txt += "[" + i + "]";
			txt += i == total ? "" : "   ";
		}
		
		gfx.drawString(txt, game.getWidth()/2 - gfx.getFontMetrics().stringWidth(txt) / 2,
				game.getHeight()-20);
	}
	
	public void renderPlayerExitingMap(Graphics2D _gfx) {
		String txt;
		Graphics2D gfx = (Graphics2D) _gfx.create();
		if (game.isOutOfBorders()) {
			gfx.setColor(playerOutOfMapBackgroundColor);
			gfx.fillRect(0, 0, game.getWidth(), game.getHeight());
			
			gfx.setColor(Color.RED);
			gfx.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
			
			int middle = game.getHeight()/2;
			txt = "You're out of borders!";
			gfx.drawString(txt,
					game.getWidth() / 2 - gfx.getFontMetrics().stringWidth(txt) / 2, middle - 40);
			
			txt = String.format("Exiting in %d seconds", (int)game.getOutOfBorderCounter().getValue());
			gfx.drawString(txt,
					game.getWidth() / 2 - gfx.getFontMetrics().stringWidth(txt) / 2, game.getHeight()/2 + 50);
		
			playerCompass.setY(middle);
			playerCompass.setTarget(game.getPlayer());
			playerCompass.update();
			playerCompass.render_(gfx);
		}

		gfx.dispose();
	}
	
	public void renderPlayerGettingHit(Graphics2D _gfx) {
		if (!game.isPlayerGettingHit()) return;
		Graphics2D gfx = (Graphics2D) _gfx.create();
		
		gfx.setColor(gettingHitBackgroundColor);
		gfx.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		gfx.dispose();
	}
	
	public void renderWarnings(Graphics2D gfx) {
		renderPlayerGettingHit(gfx);
		renderPlayerExitingMap(gfx);
	}

	public void renderBorder(Graphics2D superGfx) {
		Graphics2D gfx = (Graphics2D) superGfx.create();
		
		gfx.setColor(Palette.GREEN);
		gfx.setStroke(new BasicStroke(5));
		
		int width = game.getWidth();
		int height = game.getHeight();
		
		gfx.drawLine(0, 0, width, 0);
		gfx.drawLine(0, height, width, height);
		gfx.drawLine(0, 0, 0, height);
		gfx.drawLine(width, 0, width, height);
		
		gfx.dispose();
	}
	
	public void renderGameOver(Graphics2D superGfx) {
		if (!game.isGameOver()) return;
		
		Graphics2D gfx = (Graphics2D) superGfx.create();
		gfx.setColor(gettingHitBackgroundColor);
		gfx.fillRect(0, 0, game.getWidth(), game.getHeight());
		gfx.setFont(labelsFont.deriveFont(Font.BOLD, 40));

		String str = "Game Over";
		int h = gfx.getFontMetrics().getHeight();
		int w = gfx.getFontMetrics().stringWidth(str);
		int x = game.getWidth() / 2 - w/2;
		int y = game.getHeight() / 2 - h/2;
		
		gfx.setColor(Color.BLACK);
		Util.drawStringOutline(gfx, str, x, y);
		gfx.setColor(Color.WHITE);
		gfx.drawString(str, x, y);
		
		gfx.setFont(labelsFont.deriveFont(Font.BOLD, 24));
		str = "Your score was " + game.getScore();
		w = gfx.getFontMetrics().stringWidth(str);
		x = game.getWidth() / 2 - w/2;
		y = game.getHeight() / 2 + 30;
		
		gfx.setColor(Color.BLACK);
		Util.drawStringOutline(gfx, str, x, y);
		gfx.setColor(Color.WHITE);
		gfx.drawString(str, x, y);
		
		gfx.dispose();
	}
	
	private void renderSpecialSet(Graphics2D superGfx) {
		Graphics2D gfx = (Graphics2D) superGfx.create();
		
		String specialKeys = "ZXCVBNM";
		SpecialSet specialSet = game.getPlayer().getSpecialSet();
		
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
			gfx.drawString(txt, padding, (int) y + linePadding * i);
		}
		
		gfx.dispose();
	}
	
	@Override
	public void render(Graphics2D superGfx) {
		
		Graphics2D gfx = (Graphics2D) superGfx.create();
		gfx.setFont(labelsFont);
		gfx.setColor(labelsColor);
		
		renderBorder(gfx);
		
		renderGameOver(gfx);
		renderWarnings(gfx);
		renderPlayerExitingMap(gfx);
		renderTopLeft(gfx);
		renderTopRight(gfx);
		renderTopMiddle(gfx);
		renderBottomLeft(gfx);
		renderBottomRight(gfx);
		renderBottomMiddle(gfx);
		
		renderSpecialSet(gfx);
		
		gfx.dispose();
	}

}
