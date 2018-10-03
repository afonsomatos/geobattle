package geobattle.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import geobattle.weapons.Arsenal;
import geobattle.weapons.Weapon;

class HUD implements Renderable {

	private Game game;
	
	public HUD(Game game) {
		this.game = game;
	}
	
	private void renderTopRight(Graphics2D gfx) {
		String txt;
		
		if (game.getLevelManager().isLoadingLevel())
			txt = "Next wave in " + game.getLevelManager().getLevelCountDown();
		else
			txt = "Enemies left: " + game.getEnemiesLeft();
	
		gfx.drawString(txt, game.getWidth() - gfx.getFontMetrics().stringWidth(txt) - 10, 20);
	}
	
	private void renderTopLeft(Graphics2D gfx) {
		Player player = game.getPlayer();
	
		gfx.drawString("Health: " + player.getHealth(), 10, 20);
		gfx.drawString("Score: " + game.getScore(), 10, 40);
		gfx.drawString("Wave: " + game.getLevelManager().getLevel(), 10, 60);
		gfx.drawString("Shield: " + (int) player.getShield(), 10, 80);
	}
	
	public void renderBottomLeft(Graphics2D gfx) {
		gfx.drawString("FPS: " + game.getFps(), 10, game.getHeight() - 10);
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
	
	@Override
	public void render(Graphics2D gfx) {
		gfx.setFont(new Font("arial", Font.PLAIN, 16));
		gfx.setColor(Color.WHITE);
		
		renderTopLeft(gfx);
		renderTopRight(gfx);
		renderTopMiddle(gfx);
		
		renderBottomLeft(gfx);
		renderBottomRight(gfx);
		renderBottomMiddle(gfx);
	}

}
