package cs378.hw2.Tree;

import cs378.hw2.AbstractClassifier;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shureedkabir on 2/29/16.
 */
public class DecisionTree extends AbstractClassifier {

    DecisionTreeNode root;
    String[][] instances;
    double info_d;
    HashMap<String, Double> classProbabilities;

    public DecisionTree(List<String[]> instances){
        this.instances = (String[][]) instances.toArray();
    }

    private void calculateInfo(){



    }

    @Override
    public String classify(String[] features) {
        return null;
    }

    @Override
    public void train(List<String[]> trainingData) {

    }



}
