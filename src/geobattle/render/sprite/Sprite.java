package geobattle.render.sprite;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import geobattle.render.Renderable;

public class Sprite implements Renderable {
	
	private static final GraphicsConfiguration GFX_CONFIG =
			GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration();

	private int centerX = 0;
	private int centerY = 0;
	
	private AlphaComposite alphaComp;
	private BufferedImage image;
	
	public Sprite(int width, int height, int centerX, int centerY) {
		image = GFX_CONFIG.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		this.centerX = centerX;
		this.centerY = centerY;
	}

	public void setAlpha(float alpha) {
		alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	}
	
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public int getCenterX() {
		return centerX;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public int getCenterY() {
		return centerY;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	@Override
	public void render(Graphics2D gfx) {
		
		if (alphaComp != null)
			gfx.setComposite(alphaComp);
		
		gfx.drawImage(image, -centerX, -centerY, null);
	}
	
	public void draw(int dx, int dy, Renderable renderer) {
		Graphics2D gfx = (Graphics2D) image.getGraphics();
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gfx.translate(dx, dy);
		renderer.render(gfx);
		gfx.dispose();	
	}
	
	public void draw(Renderable renderer) {
		draw(getCenterX(), getCenterY(), renderer);
	}
	
}
