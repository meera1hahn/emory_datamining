package cs378.hw2;

import java.util.List;

/**
 * Created by shureedkabir on 2/29/16.
 */
public abstract class AbstractClassifier {

    public abstract String classify(String[] features);
    public abstract void train(List<String[]> trainingData);

}
