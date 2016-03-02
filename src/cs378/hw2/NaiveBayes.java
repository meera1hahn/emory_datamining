

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NaiveBayes {
	private Map<String,AttBigram> feature_likelihoods;
	private Map<String, Integer>    unigram;
	private Set<String> labels;
	private int totalTrans;
	private int totalAtt;
	
	public NaiveBayes(String train) throws IOException{
		feature_likelihoods = new HashMap<String, AttBigram>();
		unigram = new HashMap<String, Integer>();
		labels = new HashSet<String>();
		totalTrans = 0;
		totalAtt = 0;
		initializeData(train);
	}
	
	private void initializeData(String train) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(train)));
		String line;
		String[] lineSplit;
		while((line = br.readLine()) != null){
			totalTrans++;
			lineSplit = line.split("\t");
			labels.add(lineSplit[0]);
			placeAttributes(lineSplit);
		}
		br.close();
	}
	private void placeAttributes(String[] line) {
		int count = 0;
		//line[0] is the class label
		if(unigram.containsKey(line[0])) count = unigram.get(line[0]);
		unigram.put(line[0], ++count); 
		
		for(int i = 1; i < line.length; i++) {
			totalAtt++;
			count = 0;
			if(unigram.containsKey(line[i])) count = unigram.get(line[i]);
			unigram.put(line[i], ++count); 
			AttBigram b;
			if(feature_likelihoods.containsKey(line[0]+line[i])) {
				b = feature_likelihoods.get(line[0]+line[i]);
				b.add(1);
			}
			else {
				b = new AttBigram(line[0], line[i]);
				b.add(1);
				feature_likelihoods.put(line[0]+line[i], b);
			}
		}
	}
	
	public void test(String testPath, String outputPath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(testPath)));
		String line;
		String[] lineSplit;
		List<String> goldLabel = new ArrayList<String>();
		List<String> predictedLabel = new ArrayList<String>();

		while((line = br.readLine()) != null){
			lineSplit = line.split("\t");
			goldLabel.add(lineSplit[0]);
			String predicted = predict(lineSplit);
			predictedLabel.add(predicted);
		}

		br.close();
		
		printStats(goldLabel, predictedLabel, outputPath);
	}
	
	private String predict(String[] lineSplit) {
		List<ClassPrediction> pred = new ArrayList<>();
		for(String label: labels){

			pred.add(new ClassPrediction(label, (double) unigram.get(label) / (double) totalTrans));
		}		
		double score;
		
		for(int i = 1; i < lineSplit.length; i++) {
			score = (double) unigram.get(lineSplit[i]) / (double) totalAtt;
			AttBigram temp;
			for(ClassPrediction p : pred){
				temp = feature_likelihoods.get(p.getLabel()+lineSplit[i]);
				if(temp==null){
					p.addScore(.0005);
					continue;
				}
				score = temp.getCount() / (double) unigram.get(lineSplit[i]);
				p.addScore(score);
			}
		}
		return getPrediction(pred);

	}
	private String getPrediction(List<ClassPrediction> pred) {
		String bestClass = "";
		double max = Double.MIN_VALUE;
		for(ClassPrediction p : pred){
			if(p.getScore() > max){
				max = p.getScore();
				bestClass = p.getLabel();
			}
		}
		return bestClass;
	}
	


	private void printStats(List<String> goldLabel, List<String> predictedLabel, String outputPath) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputPath)));
		int i, correct = 0;
		for(i = 0; i < goldLabel.size(); i++) {
			bw.write("Gold: " + goldLabel.get(i) + " Predicted: " + predictedLabel.get(i) + "\n");
			if(goldLabel.get(i).equals(predictedLabel.get(i))) correct++;
		}
		double percentCorrect = (double) correct / (double) i;
		bw.write("Percentage Correct: " + percentCorrect + "\n");
		bw.flush();
		bw.close();
	}

}
