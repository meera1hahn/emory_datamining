package cs378.hw2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayes {
	private Map<String,Bigram> feature_likelihoods;
	private Unigram            prior_likelihoods;
	private double             d_epsilon;
	
	public NaiveBayes(String train) throws IOException{
		prior_likelihoods   = new Unigram(.05);
		feature_likelihoods = new HashMap<String, Bigram>();
		d_epsilon = .0000000001;
		initializeData(train);
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
		
		//printStats(goldLabel, predictedLabel, outputPath);
	}

	private void printStats(List<String> goldLabel, List<String> predictedLabel, String outputPath2) {
		int i, correct = 0;
		for(i = 0; i < goldLabel.size(); i++) {
			System.out.println("Gold: " + goldLabel.get(i) + " Predicted: " + predictedLabel.get(i));
			if(goldLabel.get(i).equals(predictedLabel.get(i))) correct++;
		}
		double percentCorrect = (double) correct / (double) i;
		System.out.println("Percentage Correct: " + percentCorrect);
	}
	
	private String predict(String[] lineSplit) {
		List<ClassPrediction> pred = new ArrayList<>();
		for(Map.Entry<String, Double> entry: prior_likelihoods.getLikelihoodMap().entrySet()){
			pred.add(new ClassPrediction(entry.getKey(), entry.getValue()));
		}		
		Bigram temp;
		double score;
		
		for(int i = 1; i < lineSplit.length; i++) {
			temp = feature_likelihoods.get(lineSplit[i]);
			for(ClassPrediction p : pred){
				score = temp.getLikelihood(p.getLabel(), lineSplit[i]);
				if(score==0) score = d_epsilon;
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
	private void initializeData(String train) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(train)));
		String line;
		String[] lineSplit;
		while((line = br.readLine()) != null){
			lineSplit = line.split("\t");
			placeAttributes(lineSplit);
		}
		br.close();
	}
	private void placeAttributes(String[] line) {
		prior_likelihoods.add(line[0], 1); //line[0] is the class label
		Bigram temp;
		for(int i = 1; i < line.length; i++) {
			temp = feature_likelihoods.get(line[i]);
			if(temp!=null){
				temp.add(line[0], line[i], 1);
			} else{
				temp = new Bigram(.05);
				temp.add(line[0], line[i], 1);
				feature_likelihoods.put(line[i], temp);
			}
		}
	}
	
	public void train(){
		prior_likelihoods.estimateMaximumLikelihoods();
		for(Bigram bigram:feature_likelihoods.values()){
			bigram.estimateMaximumLikelihoods();
		}
	}
	

}
