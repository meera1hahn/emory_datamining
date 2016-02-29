package cs378.hw2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NaiveBayes {
	private Map<String,Bigram> feature_likelihoods;
	private Unigram            prior_likelihoods;
	private double             d_epsilon;
	String outputPath;
	
	public NaiveBayes(String train, String out) throws IOException{
		initializeData(train);
		outputPath = out;
		prior_likelihoods   = new Unigram(.05);
		feature_likelihoods = new HashMap<>();
		d_epsilon = 0000000001;
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
	
	public void test(String test){
		testPath = test;
	}
}
