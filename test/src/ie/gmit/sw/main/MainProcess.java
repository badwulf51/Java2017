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

public class MainProcess implements Runnable {

	private BlockingQueue<Shingle> q;
	private int k;
	private int[] minhashes;
	private Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
	private ExecutorService pool;
	
	public MainProcess(BlockingQueue<Shingle> q, int k, int poolSize) {
		this.q = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		init();
	}
	
	public void init(){
		Random random = new Random();
		minhashes = new int[k];
		
		for(int i=0; i < minhashes.length; i++){
			minhashes[i] = random.nextInt();
		}
	}
	
	// CHANGED
	private void printHashes()
	{
		System.out.println("Hashes:");
		System.out.println("=======");
		for(int i = 0; i < map.size(); i++) 
		{
			System.out.println("DocId " + i + ": " + map.get(i).toString());
		}
	}
	
	private void calculateSimilarity()
	{
		
		Set<Integer> set1 = new HashSet<Integer>();		
		Set<Integer> set2 = new HashSet<Integer>();
		
		set1.addAll(map.get(0));
		set2.addAll(map.get(1));
		
		Set<Integer> n = new TreeSet<Integer>(set1);
		n.retainAll(set2);
		
		System.out.println("Documents have " + n.size() + " matching minhashes");

	}
	
	public void run() {
		int docCount = 2;
		
		while(docCount > 0) {
			try {
				
				Shingle s = q.take();
				
				
				if(s.getHashcode() != 0){
					
					
					pool.execute(new Runnable() {
						
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


