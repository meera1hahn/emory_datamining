package cs378.hw2;

import java.util.HashMap;
import java.util.Map;

public class Unigram {
	private Map<String,Double> m_likelihoods;
	private Map<String,Long>   m_counts;
	private long               t_counts;
	private LaplaceSmoothing         smoothing;
	
	public Unigram(double d_alpha)
	{
		m_counts = new HashMap<String,Long>();
		t_counts = 0;
		smoothing = new LaplaceSmoothing(d_alpha);
	}
	
	public void add(String attribute1, long count){
		long currCount;
		if(m_counts.get(attribute1)!=null){
			currCount = m_counts.get(attribute1);
			currCount += count;
			m_counts.put(attribute1, currCount);
		}
		else{
			m_counts.put(attribute1, count);
		}
		t_counts+=count;
	}
	public Map<String, Long> getCountMap() {
		return m_counts;
	}
	public Map<String, Double> getLikelihoodMap()
	{
		return m_likelihoods;
	}
	public void setTCount(long totalCount) {
		this.t_counts = totalCount;
	}
	public long getTotalCount() {
		return t_counts;
	}

	public void estimateMaximumLikelihoods(){
		smoothing.estimateMaximumLikelihoods(this);
	}
	


	public void setLikelihoodMap(Map<String, Double> map) {
		this.m_likelihoods = map;
		
	}

	public double getLikelihood(String attribute){
		if(m_likelihoods.get(attribute)!=null){
		return m_likelihoods.get(attribute);
		}
		return smoothing.getUnseenLikelihood();
	}
	
	public boolean contains(String attribute){
		if(m_likelihoods.containsKey(attribute)) return true;
		return false;
	}



}
