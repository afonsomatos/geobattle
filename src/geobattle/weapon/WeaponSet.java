package geobattle.weapon;

public class WeaponSet {
	
	private Weapon[] slots;
	private int selected;
	
	private Integer lastSelected = null;
	
	private double fireAngle = 0;

	public final int size;
	
	public WeaponSet(int len) {
		slots = new Weapon[len];
		size = len;
	}
	
	public Weapon[] getSlots() {
		return slots;
	}
	
	public void swap() {
		if (lastSelected != null)
			select(lastSelected);
	}
	
	public boolean store(int num, Weapon weapon) {
		if (num < 0 || num >= slots.length)
			return false;
		
		slots[num] = weapon;
		if (num != selected)
			weapon.setHidden(true);
		
		return true;
	}
	
	public boolean select(int num) {
		if (num < 0 || num >= slots.length)
			return false;
		
		lastSelected = selected;
		Weapon weapon = getSelectedWeapon();

		if (weapon != null) {	
			fireAngle = weapon.getFireAngle(); // Save firing direction
			weapon.setHidden(true);
		}
		
		selected = num;
		weapon = getSelectedWeapon();
		
		// If the slot isn't empty
		if (weapon != null) {
			weapon.setFireAngle(fireAngle);
			weapon.setHidden(false);
		}

		return true;
	}
	
	public int getSelected() {
		return selected;
	}
	
	public Weapon getSelectedWeapon() {
		return slots.length == 0 ? null : slots[selected];
	}
	
}
