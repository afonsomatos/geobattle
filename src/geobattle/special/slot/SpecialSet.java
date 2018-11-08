package geobattle.special.slot;

public class SpecialSet {

	private SpecialSlot[] specialSlots;
	private int selected = 0;
	
	public SpecialSet(int size) {
		specialSlots = new SpecialSlot[size];
	}
	
	public int size() {
		return specialSlots.length;
	}
	
	public void store(int index, SpecialSlot special) {
		specialSlots[index] = special;
	}
	
	public SpecialSlot getSelected() {
		return specialSlots[selected];
	}
	
	public SpecialSlot get(int index) {
		if (index >= specialSlots.length)
			return null;
		return specialSlots[index];
	}
	
	public void select(int index) {
		if (index >= specialSlots.length || index < 0)
			throw new RuntimeException("Index is out of bounds.");
		
		selected = index;
	}
	
}
