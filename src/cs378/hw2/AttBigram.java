package cs378.hw2;

public class AttBigram {
	String label;
	String attribute;
	double totalCount;
	double score;
	
	public AttBigram(String label, String attribute) {
		this.label = label;
		this.attribute = attribute;
		totalCount = 0;
		score = 0;
	}
	
	public void add(int count) {
		totalCount += count;
	}
	
	public double getCount(){
		return totalCount;
	}
}
