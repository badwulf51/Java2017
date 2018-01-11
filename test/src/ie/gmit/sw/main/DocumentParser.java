// Aron O' Malley 
// G00327019 
// Java Project 2017 

package ie.gmit.sw.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
// class for doc parser 
public class DocumentParser implements Runnable {
	// variables and blocking que 
	private BlockingQueue<Shingle>queue;
	private String file;
	private int shingleSize, k;
	private Deque<String> buffer = new LinkedList<>();
	private int docId;	
	// doc parser 
	public DocumentParser(String file, BlockingQueue<Shingle>q, int shingleSize, int k, int docId) {
		// takes file, shingle size
		this.file = file;
		this.shingleSize = shingleSize;
		this.k = k;
		this.docId = docId;
		// queue
		this.queue = q;
	}
	// run 
	public void run() {
		// message for showing that the thread has been started for file 
		System.out.println("DP: DocumentParser.run - Started thread for " + file);
		// try catch
		try {
			// buffered reader 
			BufferedReader br = new BufferedReader(new FileReader(file + ".txt"));
			
			String line = null;
			int linecounter = 0;
			// while loop
			while((line = br.readLine())!= null) {
				System.out.println("DP: [" + file + "] processing: " + line);
				String uLine = line.toUpperCase();
				String[] words = uLine.split(" ");
				addWordsToBuffer(words);
				// shingle here
				Shingle s;
				do
				{	// gets next shingle 
					s = getNextShingle();
					if(s != null)
					{
						queue.put(s);
					}
				} while (s != null);
				
				
			}
			// flushes buffer 
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

	// adds words to buffer 
	private void addWordsToBuffer(String [] words) {
		for(String s : words) {
			buffer.add(s);
		}
  
    }
	// gets next shingle
	private Shingle getNextShingle()
	{
		return getNextShingle(shingleSize);
	}
// code taken from offline, did not work originally but after a few tests I relised I forgot to set buffer.size in if 
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
		// output message for flushing buffer 
		System.out.println("DP: [" + file + "] Flushing buffer...");
		
		boolean flushing = true;
		while(flushing) {
			
			int size = shingleSize;
			if(buffer.size() < shingleSize)
			{
				size = buffer.size();
			}
			
			Shingle s = getNextShingle(size);
			// try catch 
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
