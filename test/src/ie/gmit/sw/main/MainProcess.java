// Aron O' Malley 
// G00327019 
// Java Project 2017 

package ie.gmit.sw.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// class the the process of implementing and using shingles and minhashes
public class MainProcess implements Runnable {

	private BlockingQueue<Shingle> q;
	private int k;
	private int[] minhashes;
	private Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
	private ExecutorService pool;
	// adds to pool 
	public MainProcess(BlockingQueue<Shingle> q, int k, int poolSize) {
		this.q = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		init();
	}
	// same as before 
	public void init(){
		Random random = new Random();
		minhashes = new int[k];
		
		for(int i=0; i < minhashes.length; i++){
			minhashes[i] = random.nextInt();
		}
	}
	
	//prints out avalible hashes 
	private void printHashes()
	{
		System.out.println("Hashes:");
		System.out.println("=======");
		for(int i = 0; i < map.size(); i++) 
		{
			System.out.println("DocId " + i + ": " + map.get(i).toString());
		}
	}
	// calculation used to show if hashes match 
	private void calculateSimilarity()
	{
		
		Set<Integer> set1 = new HashSet<Integer>();		
		Set<Integer> set2 = new HashSet<Integer>();
		// adds to map 
		set1.addAll(map.get(0));
		set2.addAll(map.get(1));
		
		Set<Integer> n = new TreeSet<Integer>(set1);
		n.retainAll(set2);
		// print statement for how many matching hashes there are
		System.out.println("Documents have " + n.size() + " matching minhashes");

	}
	// run 
	public void run() {
		int docCount = 2;
		// while doc is not 0 
		while(docCount > 0) {
			try {
				
				Shingle s = q.take();
				
				
				if(s.getHashcode() != 0){
					
					
					pool.execute(new Runnable() {
						// ovverrides previous 
						@Override
						public void run() {

							List<Integer>list = map.get(s.getDocId());
							
							for(int i=0; i<minhashes.length; i++){
								// CHANGED: 
								int value = s.getHashcode() ^ minhashes[i];
								
								if(list == null){
									list = new ArrayList<Integer>(Collections.nCopies(k, Integer.MAX_VALUE));
									map.put(s.getDocId(), list);
								}
								else {
									boolean newmin = false;
									
									if(list.get(i)>value)
									{
										
										list.set(i, value);
										newmin = true;
									}
									
									if(newmin)
									{
										System.out.println("MP:P: minhashes for docid:" + s.getDocId() + ": " + list.toString());
									}
								}
							}
						}
					});
					
					
					// useless 
				}else {
					docCount--;
					System.out.println("MP: Found poison.. waiting on " + docCount + " documents" );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		printHashes();
		
		calculateSimilarity();
	}
}

