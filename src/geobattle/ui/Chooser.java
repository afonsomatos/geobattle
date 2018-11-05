package geobattle.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import geobattle.util.Interval;
import geobattle.util.Util;

@SuppressWarnings("serial")
class Chooser extends JPanel {

	private int value = 0;
	private String preStr;
	private Interval<Integer> inter;
	private JLabel label = new JLabel();
	
	private List<Runnable> valueChanged = new ArrayList<Runnable>();
	
	Chooser(UIStyle uiStyle, Interval<Integer> inter, String preStr) {
		this.inter = inter;
		this.preStr = preStr;
		
		Font font = uiStyle.getFont();
		
		setBackground(null);
		setLayout(new BorderLayout());
		
		label.setForeground(Color.WHITE);
		label.setFont(font);
		label.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel scroller = new JPanel();
		scroller.setBackground(null);
		scroller.setLayout(new GridLayout(2, 1));
		
		Button up = new Button("+");
		up.addActionListener(e -> setValue(value + 1));
		Button down = new Button("-");
		down.addActionListener(e -> setValue(value - 1));

		scroller.add(up);
		scroller.add(down);
		
		add(scroller, BorderLayout.EAST);
		add(label, BorderLayout.CENTER);
		
		setValue(inter.start);
	}
	
	int getValue() {
		return value;
	}
	
	void addChangedTrigger(Runnable trigger) {
		valueChanged.add(trigger);
	}
	
	void setValue(int value) {
		this.value = Util.clamp(inter.start, value, inter.end);
		updateLabel();
		valueChanged.forEach(Runnable::run);
	}
	
	void setInterval(Interval<Integer> inter) {
		this.inter = inter;
		setValue(value);
	}
	
	void updateLabel() {
		label.setText(preStr + value);
	}
	
}
