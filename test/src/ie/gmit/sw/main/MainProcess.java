package ie.gmit.sw.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
	
	public void run() {
		int docCount = 2;
		
		while(docCount > 0) {
			try {
				Shingle s = q.take();
				
				if(s.getHashcode() != 48){
					pool.execute(new Runnable() {
						
						@Override
						public void run() {
							List<Integer>list = map.get(s.getDocId());
							
							for(int i=0; i<minhashes.length; i++){
								int value = s.getHashcode() + minhashes[i];
								
								if(list == null){
									list = new ArrayList<Integer>(Collections.nCopies(k, Integer.MAX_VALUE));
									map.put(s.getDocId(), list);
								}
								else {
									if(list.get(i)>value){
										list.set(i, value);
									}
								}
							}
						}
					});
				}else {
					docCount--;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}


