package cs378.hw2.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shureedkabir on 2/29/16.
 */
public class DecisionTreeNode {

    public static final int CLASS_LABEL = 0;
    public int ATTRIBUTE_SPLIT = -1;
    public char label;
    private HashMap<Character, DecisionTreeNode> children;
    private List<char[]> instancesPartition;
    double classInfo;

    public DecisionTreeNode(List<char[]> instancesPartition){
        this.instancesPartition = instancesPartition;
        classInfo = calculateInfo(instancesPartition, CLASS_LABEL);
    }


    public int getBestAttribute(boolean[] checked){
        int index = -1;
        double bestGainRatio = 0;
        double attributeInfo;
        double gainRatio;

        if (instancesPartition.size() < 1) {
            determineLabel();
            return -1;
        }

        int numberOfAttributes = instancesPartition.get(0).length;
        HashMap<Character, List<char[]>> partition;

        for (int i = 0; i < numberOfAttributes; i++) {

            if (checked[i])
                continue;

            partition = splitDataByAttribute(i);
            attributeInfo = 0;

            for (Map.Entry<Character, List<char[]>> entry : partition.entrySet())
                attributeInfo += ((double) entry.getValue().size() / instancesPartition.size()) * calculateInfo(entry.getValue(), CLASS_LABEL);

            gainRatio = (classInfo - attributeInfo) / calculateInfo(instancesPartition, i);

            if (gainRatio > bestGainRatio) {
                bestGainRatio = gainRatio;
                index = i;
            }
        }

        if (index < 0)
            determineLabel();

        return index;
    }


    private void determineLabel() {
        HashMap<Character, Integer> counts = new HashMap<>();

        for (char[] instance : instancesPartition)
            counts.merge(instance[CLASS_LABEL], 1, (x, y) -> x + y);

        label = counts.entrySet().stream().max((x, y) -> x.getValue() - y.getValue()).get().getKey();
    }


    private double calculateInfo(List<char[]> instances, int attribute){
        HashMap<Character, Integer> counts = new HashMap<>();
        double info = 0;

        for (char[] instance : instances)
            counts.merge(instance[attribute], 1, (x, y) -> x + y);

        double ratio;

        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            ratio = (entry.getValue().doubleValue()/instances.size());
            info -= ratio * logBase2(ratio);
        }

        return info;
    }


    private HashMap<Character, List<char[]>> splitDataByAttribute(int attribute){
        HashMap<Character, List<char[]>> partitions = new HashMap<>();

        for (char[] instance : instancesPartition)
            partitions.computeIfAbsent(instance[attribute], (x) -> new ArrayList<>())
                    .add(instance);

        return partitions;
    }


    public HashMap<Character, DecisionTreeNode> splitIntoChildren(int attribute){
        ATTRIBUTE_SPLIT = attribute;

        children = new HashMap<>();
        HashMap<Character, List<char[]>> partitions = splitDataByAttribute(attribute);


        for (Map.Entry<Character, List<char[]>> entry: partitions.entrySet())
            children.put(entry.getKey(), new DecisionTreeNode(entry.getValue()));

        instancesPartition = null; //clear for GC

        return children;
    }


    public DecisionTreeNode getChild(char[] fv){
        return children.get(fv[ATTRIBUTE_SPLIT]);
    }


    private static double logBase2(double x){
        if (x == 0) return 0;

        final double baseChange = Math.log(2);

        return Math.log(x)/baseChange;
    }
}
