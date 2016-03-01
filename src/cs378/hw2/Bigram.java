package cs378.hw2;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Bigram {
	private Map<String,Unigram> m_unigrams;
	private LaplaceSmoothing smoothing;
	private double d_alpha;
	
	public Bigram(double d_alpha)
	{
		this.d_alpha = d_alpha;
		m_unigrams  = new HashMap<>();
		smoothing = new LaplaceSmoothing(d_alpha);
	}
	
	public void add(String attribute1, String attribute2, long count){
		Unigram temp;
		
		if((temp=m_unigrams.get(attribute1))!=null){
			temp.add(attribute2, count);
		}
		else{
			temp = new Unigram(d_alpha);
			temp.add(attribute2, count);
			m_unigrams.put(attribute1, temp);
		}
	}
	
	public Map<String,Unigram> getUnigramMap()
	{
		return m_unigrams;
	}
	
	public void estimateMaximumLikelihoods(){
		smoothing.estimateMaximumLikelihoods(this);
	}
	
	public boolean contains(String attribute1, String attribute2)
	{
		Unigram unigram = m_unigrams.get(attribute1);
		return (unigram != null) && unigram.contains(attribute2); 
	}

	public double getLikelihood(String attribute1, String attribute2)
	{
		Unigram unigram = m_unigrams.get(attribute1);
		if (unigram != null) {
			System.out.println(unigram.getLikelihood(attribute2));
			return unigram.getLikelihood(attribute2);
		
		}
		return smoothing.getUnseenLikelihood();
	}
	
	public Set<String> getWordSet() {
		Set<String> set = new HashSet<>();
		
		for (Entry<String,Unigram> entry : m_unigrams.entrySet())
		{
			set.add(entry.getKey());
			set.addAll(entry.getValue().getCountMap().keySet());
		}
		
		return set;
	}

}
