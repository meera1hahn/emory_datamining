package cs378.hw2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NaiveBayes {
	String testPath;
	String outputPath;
	
	public NaiveBayes(String train, String test, String out) throws IOException{
		initializeData(train);
		testPath = test;
		outputPath = out;
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
	}
}
