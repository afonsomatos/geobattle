package geobattle.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import geobattle.core.Achievements;
import geobattle.core.Options;
import geobattle.core.Score;
import geobattle.special.slot.SpecialSlotFactory;
import geobattle.util.Interval;
import geobattle.util.Palette;
import geobattle.weapon.WeaponFactory;

@SuppressWarnings("serial")
class Load extends JPanel {
	
	private static final int MAX_SCORES = 10;

	private Chooser levelChooser;
	private UIManager uiManager;
	
	private JLabel highScoresLabel = new JLabel();
	
	private Picker weaponPicker;
	private Picker specialPicker;
	private Picker powerupPicker;
	
	Load(UIManager uiManager) {
		
		this.uiManager = uiManager;
		
		UIStyle uiStyle = uiManager.getUIStyle();
		Font font = uiStyle.getFont();
		Color fg = uiStyle.getForeground();
		
		JPanel left = new JPanel();
		left.setBackground(null);
		left.setLayout(new BorderLayout());
		
		// SMALL PICKERS
		JPanel smallPickers = new JPanel();
		smallPickers.setBackground(null);
		smallPickers.setLayout(new GridLayout(3, 1));
		
		JPanel levelPicker = new JPanel();
		levelPicker.setLayout(new BorderLayout());
		levelPicker.setBackground(null);
		
		JLabel title = new JLabel("Choose level");
		title.setFont(font);
		title.setPreferredSize(new Dimension(10, 30));
		title.setOpaque(true);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setBackground(Palette.TEAL);
		title.setForeground(Color.WHITE);
		
		// Level Chooser
		levelChooser = new Chooser(uiStyle, new Interval<Integer>(1, 10), "Level: "); 
		levelChooser.addChangedTrigger(this::levelSwitch);
		
		levelPicker.add(levelChooser, BorderLayout.CENTER);
		levelPicker.add(title, BorderLayout.NORTH);
		
		
		highScoresLabel.setFont(font);
		highScoresLabel.setForeground(fg);
		
		smallPickers.add(levelPicker);
		smallPickers.add(highScoresLabel);
		
		// NAVIGATION
		JPanel nav = new JPanel();
		nav.setLayout(new GridLayout(2, 1));
		
		Button cancel = new Button("Cancel");
		cancel.setFont(font);
		
		cancel.addActionListener(e -> uiManager.sendMenu());
		
		Button next = new Button("Next");
		next.setFont(font);
		nav.add(next);
		nav.add(cancel);
		
		weaponPicker = new Picker(uiStyle, "Weapons", Palette.BLUE);
		specialPicker = new Picker(uiStyle, "Specials", Palette.MAGENTA);
		powerupPicker = new Picker(uiStyle, "Powerups", Palette.ORANGE);
		
		next.addActionListener(e -> {
			Options opts = uiManager.getOptions();
			opts.setSpecials(specialPicker.getSelected());
			opts.setWeapons(weaponPicker.getSelected());
			opts.setPowerups(powerupPicker.getSelected());
			opts.setLevel(levelChooser.getValue());
			uiManager.sendPlay();
		});
		
		left.add(smallPickers, BorderLayout.CENTER);
		left.add(nav, BorderLayout.SOUTH);
		
		// GridPanel
		JPanel gridPanel = new JPanel();
		gridPanel.setBackground(null);
		GridLayout grid = new GridLayout(1, 4);
		gridPanel.setLayout(grid);
		grid.setHgap(10);
		
		gridPanel.add(left);
		gridPanel.add(specialPicker);
		gridPanel.add(weaponPicker);
		gridPanel.add(powerupPicker);
		
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout());
		setBackground(null);
		add(gridPanel);
	}
	
	void updateLoad() {
		levelChooser.setInterval(new Interval<Integer>(1, uiManager.getAchievements().getLevel()));
		levelSwitch();
		updateWeapons();
		updateSpecials();
		updatePowerups();
	}

	private void updatePowerups() {
		Achievements ach = uiManager.getAchievements();
		powerupPicker.setMax(ach.getPowerupSlots());
		powerupPicker.setSlots(
				ach
					.getPowerups()
					.stream()
					.map(Object::toString)
					.toArray(x -> new String[x])
					);
	}
	
	private void updateSpecials() {
		Achievements ach = uiManager.getAchievements();
		specialPicker.setMax(ach.getSpecialSlots());
		specialPicker.setSlots(
				ach
					.getSpecials()
					.stream()
					.map(SpecialSlotFactory::getName)
					.toArray(x -> new String[x])
					);
	}
	
	private void updateWeapons() {
		Achievements ach = uiManager.getAchievements();
		weaponPicker.setMax(ach.getWeaponSlots());
		weaponPicker.setSlots(
				ach
					.getWeapons()
					.stream()
					.map(WeaponFactory::getName)
					.toArray(x -> new String[x])
					);
	}
	
	void levelSwitch() {
		int level = levelChooser.getValue();
		List<Score> scores = uiManager.getScores(level);
		scores.sort((s1, s2) -> s2.score - s1.score);

		String highScoreTxt = "<html>Times played: " + scores.size();
		
		for (int i = 0; i < MAX_SCORES; ++i) {
			if (scores.size() <= i) {
				highScoreTxt += "<br>";
				continue;
			}
			Score s = scores.get(i);
			String txt = String.format("%dº %s ~ %d", i + 1, s.name, s.score);
			highScoreTxt += "<br>" + txt;
		}
		
		highScoresLabel.setText(highScoreTxt);
	}
	
}
