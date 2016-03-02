package cs378.hw2;

public class ClassPrediction {
	private String label;
	private double score;
	
	
	
	public ClassPrediction(String l, double s){
		this.label = l;
		this.score = s;
	}
	public void addScore(double s){
		this.score *=s;
	}
	public ClassPrediction(ClassPrediction p)
	{
		setLabel(p.getLabel());
		setScore(p.getScore());
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public int compareTo(ClassPrediction o)
	{
		return (int)Math.signum(score - o.score);
	}
	
	public String toString()
	{
		return label+":"+score;
	}
	
}