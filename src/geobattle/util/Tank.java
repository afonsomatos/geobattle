package geobattle.util;

public class Tank {
	
	private int minQuantity = 0;
	private int quantity = 0;
	private int maxQuantity = 0;

	public Tank() {
		
	}
	
	public Tank(int quantity) {
		this.maxQuantity = this.quantity = quantity;
	}
	
	public void fill() {
		quantity = maxQuantity;
	}
	
	public int fill(int quant) {
		final int filled = Math.min(maxQuantity - quantity, quant);
		set(filled + quantity);
		return quant - filled;
	}
	
	public int take(int quant) {
		final int taken = Math.min(quantity - minQuantity, quant);
		set(quantity - taken);
		return quant - taken;
	}
	
	public void set(int charge) {
		if (charge > maxQuantity)
			maxQuantity = charge;
		
		this.quantity = Math.max(charge, 0);	
	}
	
	public int free() {
		return maxQuantity - quantity;
	}
	
	public int get() {
		return quantity;
	}
	
	public int max() {
		return maxQuantity;
	}
	
	public int min() {
		return minQuantity;
	}
	
	public boolean empty() {
		return quantity == 0;
	}
	
	public boolean full() {
		return quantity >= maxQuantity;
	}
}
