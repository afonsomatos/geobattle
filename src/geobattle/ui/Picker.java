package geobattle.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
class Picker extends JPanel {
	
	private final static int TITLE_HEIGHT = 30;
	private final static int BUTTON_WIDTH = 50;
	
	private List<Integer> selected = new ArrayList<Integer>();
	
	private int max;
	private boolean[] btns;
	
	private JLabel title = new JLabel();

	Picker(UIStyle uiStyle, String name, Color titleColor, String[] slots, int max) {
		this.max = max;
		
		setName(name);
		
		Font font = uiStyle.getFont();

		btns = new boolean[slots.length];
		
		JPanel col = new JPanel();
		col.setBackground(null);
		col.setBorder(null);
		col.setLayout(new GridLayout(slots.length, 1));

		for (int i = 0; i < slots.length; ++i) {
			
			JPanel slot = new JPanel();
			slot.setLayout(new BorderLayout());
			slot.setBackground(null);
			
			final JLabel label = new JLabel(slots[i]);
			label.setFont(font);
			label.setForeground(Color.WHITE);

			Button btn = new Button("+");
			btn.setPreferredSize(new Dimension(BUTTON_WIDTH, 0));
			
			final int x = i;
			btn.addActionListener(e -> {
				if (!toggle(x)) return;
				btns[x] = !btns[x];
				if (btns[x]) {
					btn.setText("-");
					label.setForeground(titleColor);
				} else {
					btn.setText("+");
					label.setForeground(Color.WHITE);
				}
				updateTitle();
			});
			
			slot.add(label, BorderLayout.CENTER);
			slot.add(btn, BorderLayout.EAST);
			col.add(slot);
		}
		
		title.setFont(font);
		title.setPreferredSize(new Dimension(0, TITLE_HEIGHT));
		title.setOpaque(true);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Color.WHITE);
		title.setBackground(titleColor);
		
		setBackground(null);
		setLayout(new BorderLayout());
		
		add(title, BorderLayout.NORTH);
		add(col, BorderLayout.CENTER);	
		
		updateTitle();
	}
	
	int[] getSelected() {
		return selected.stream().mapToInt(i -> i).toArray();
	}
	
	private boolean toggle(int i) {
		if (selected.contains(i)) {
			selected.remove((Integer) i);
			return true;
		} else if (selected.size() < max) {
			selected.add(i);
			return true;
		}
		return false;
	}
	
	private void updateTitle() {
		title.setText(getName() + " " + selected.size() + "/" + max);
	}
	
}
