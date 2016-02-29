package cs378.hw2;

import java.util.Map;
import java.util.stream.Collectors;

public class LaplaceSmoothing {
	
	private double d_unseenLikelihood;
	private double d_alpha;
	
	public LaplaceSmoothing(double alpha)
	{
		d_alpha = alpha;
	}
	public void estimateMaximumLikelihoods(Unigram unigram){
		Map<String,Long> countMap = unigram.getCountMap();
		double t = d_alpha * countMap.size() + unigram.getTotalCount();
		Map<String,Double> map = countMap.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> (d_alpha + entry.getValue())/t));
		unigram.setLikelihoodMap(map);
		d_unseenLikelihood = d_alpha / t;
	}
	public void estimateMaximumLikelihoods(Bigram bigram)
	{
		Map<String,Unigram> unigramMap = bigram.getUnigramMap();
		
		for (Unigram unigram : unigramMap.values())
			unigram.estimateMaximumLikelihoods();
		
		d_unseenLikelihood = 1d / bigram.getWordSet().size();
	}
	public double getUnseenLikelihood()
	{
		return d_unseenLikelihood;
	}

}
