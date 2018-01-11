package ie.gmit.sw.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class DocumentParser implements Runnable {
	private BlockingQueue<Shingle>queue;
	private String file;
	private int shingleSize, k;
	private Deque<String> buffer = new LinkedList<>();
	private int docId;	

	public DocumentParser(String file, BlockingQueue<Shingle>q, int shingleSize, int k, int docId) {
		// CHANGED
		this.file = file;
		this.shingleSize = shingleSize;
		this.k = k;
		this.docId = docId;
		
		this.queue = q;
	}
	
	public void run() {
		System.out.println("DP: DocumentParser.run - Started thread for " + file);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file + ".txt"));
			
			String line = null;
			int linecounter = 0;
			while((line = br.readLine())!= null) {
				System.out.println("DP: [" + file + "] processing: " + line);
				String uLine = line.toUpperCase();
				String[] words = uLine.split(" ");
				addWordsToBuffer(words);
				// CHANGED
				Shingle s;
				do
				{
					s = getNextShingle();
					if(s != null)
					{
						queue.put(s);
					}
				} while (s != null);
				
				//Thread.sleep(1000);
			}
			
			flushBuffer();
			br.close();
		} catch(FileNotFoundException e1) {
			e1.printStackTrace();
		}catch (IOException e2){
			e2.printStackTrace();
		} catch (InterruptedException e3) {
			e3.printStackTrace();
		}
	}


	private void addWordsToBuffer(String [] words) {
		for(String s : words) {
			buffer.add(s);
		}
  
    }
	
	private Shingle getNextShingle()
	{
		return getNextShingle(shingleSize);
	}

  	private Shingle getNextShingle(int shingleSize) {
  		
  		if(buffer.size() < shingleSize)
  		{
  			return null;
  		}
  		
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		while(counter < shingleSize) {
			if(buffer.peek() != null) {
				sb.append(buffer.poll());
				counter++;
			}
		}  
		if (sb.length() > 0) {
			int hash = sb.toString().hashCode();
			System.out.println("DP: [" + file + "] getNextShingle: hashing " + sb.toString() + " = " + hash);
			return(new Shingle(docId, hash));
		}
		else {
			return(null);
		}
  	} // Next shingle
	

	private void flushBuffer() {
		System.out.println("DP: [" + file + "] Flushing buffer...");
		
		boolean flushing = true;
		while(flushing) {
			
			int size = shingleSize;
			if(buffer.size() < shingleSize)
			{
				size = buffer.size();
			}
			
			Shingle s = getNextShingle(size);
			
			try {
				if(s != null) {
					queue.put(s);
				}
				else {
					queue.put(new Poison(docId, 0));
					flushing = false;
				}
			} catch (Exception e) {
				
			}
		}
	}

        
  }// Document Parser
