package cs378.hw2;

public class RunNaiveBayes {
    public static void main(String[] args) throws Exception {
		String trainPath = args[0]; //"/Users/meerahahn/Desktop/Emory_College/4Senior/2ndSemester/CS378/hw3/mushroom.training.txt"
		String testPath = args[1]; //"/Users/meerahahn/Desktop/Emory_College/4Senior/2ndSemester/CS378/hw3/mushroom.test.txt"
		String outputPath = args[2]; //"/Users/meerahahn/Desktop/Emory_College/4Senior/2ndSemester/CS378/hw3/output.txt"
        long time1 = System.currentTimeMillis();	
		NaiveBayes nb = new NaiveBayes(trainPath);
		nb.train();
		nb.test(testPath, outputPath);
        System.out.println("Total time (including) file i/o: " + ((double) (System.currentTimeMillis() - time1)/1000) + " seconds");
    }

}
