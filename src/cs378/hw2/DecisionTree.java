package cs378.hw2;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shureedkabir on 2/29/16. C4.5 implementation
 */
public class DecisionTree {

    private DecisionTreeNode root;
    boolean checked[];

    public DecisionTree(List<char[]> instances){
        root = new DecisionTreeNode(instances);
        checked = new boolean[instances.get(0).length];
        checked[DecisionTreeNode.CLASS_LABEL] = true;
    }

    public char classify(char[] features) {
        DecisionTreeNode current = root;

        while (current != null){
            if (current.ATTRIBUTE_SPLIT == -1)
                return current.label;

            current = current.getChild(features);
        }

        return ' ';
    }

    public void train() {
        int index;
        ArrayDeque<DecisionTreeNode> pq = new ArrayDeque<>();
        DecisionTreeNode current;

        pq.add(root);

        while (!pq.isEmpty()){
            current = pq.remove();
            index = current.getBestAttribute(checked);
            if (index < 0) continue;
            checked[index] = true;

            pq.addAll(current.splitIntoChildren(index).values());
        }
    }

    /**
     * @param args {trainingDataPath, testDataPath, outputPath}
     *
     * Trains then tests on given datasets. Class label must be first character (position 0)
     */

    public static void main(String[] args) throws IOException {
        List<char[]> trainingInstances = parseData(args[0]);
        List<char[]> testInstances = parseData(args[1]);

        System.out.println(trainingInstances.size()+" training instances");
        System.out.println(testInstances.size()+" test instances");

        DecisionTree tree = new DecisionTree(trainingInstances);
        tree.train();

        System.out.println();

        runTest(tree, testInstances, args[2]);



    }

    private static void runTest(DecisionTree tree, List<char[]> testInstances, String outputFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        int totalCorrect = 0;
        char prediction;

        for (char[] testInstance : testInstances) {
            prediction = tree.classify(testInstance);
            if (prediction == testInstance[DecisionTreeNode.CLASS_LABEL])
                totalCorrect++;

            writer.write("Gold: "+testInstance[DecisionTreeNode.CLASS_LABEL]+" Prediction: "+prediction);
            writer.newLine();
        }

        double result = (double) totalCorrect/testInstances.size();

        writer.write(totalCorrect+" out of "+testInstances.size() + " correct ("+ result*100 +"). ");
        writer.newLine();
        writer.flush();
        writer.close();

        System.out.println(totalCorrect+" out of "+testInstances.size() + " correct ("+ result*100 +"%). ");

    }

    private static List<char[]> parseData(String inputPath) throws IOException {
        List<char[]> instances = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(inputPath));
        String line;

        while ((line = reader.readLine()) != null){
            instances.add(line.replace("\t", "").toCharArray());
        }

        return instances;
    }
}
