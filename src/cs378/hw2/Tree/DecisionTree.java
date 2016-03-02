package cs378.hw2.Tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shureedkabir on 2/29/16.
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
     * @param args {trainingDataPath, testDataPath}
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

        runTest(tree, testInstances);

    }

    private static void runTest(DecisionTree tree, List<char[]> testInstances) {
        int totalCorrect = 0;

        for (char[] testInstance : testInstances) {
            if (tree.classify(testInstance) == testInstance[DecisionTreeNode.CLASS_LABEL])
                totalCorrect++;
        }

        double result = (double) totalCorrect/testInstances.size();

        System.out.println(totalCorrect+" out of "+testInstances.size() + " correct ("+ result +"). ");

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
