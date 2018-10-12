package geobattle.core;

public class Score {

	private String name;
	private int score;
	private int round;
	
	public Score(String name, int score, int round) {
		this.name = name;
		this.score = score;
		this.round = round;
	}
	
	public int getRound() {
		return round;
	}
	
	public String getName() {
		return name;
	}
	
	public int getScore() {
		return score;
	}
	
}
