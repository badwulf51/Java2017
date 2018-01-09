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

	public DocumentParser(String file, BlockingQueue<Shingle>q, int shingleSize, int k) {
		this.queue = q;
	}
	
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file + ".txt"));
			
			String line = null;
			while((line = br.readLine())!= null) {
				String uLine = line.toUpperCase();
				String[] words = uLine.split(" ");
				addWordsToBuffer(words);
				Shingle s = getNextShingle();
				queue.put(s);
			}
			flushBuffer();
			br.close();
		} catch(FileNotFoundException FileNotFoundException) {
			
		}catch (IOException IOException){
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	private void addWordsToBuffer(String [] words) {
		for(String s : words) {
			buffer.add(s);
		}
  
    }

  	private Shingle getNextShingle() {
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		while(counter < shingleSize) {
			if(buffer.peek() != null) {
				sb.append(buffer.poll());
				counter++;
			}
		}  
		if (sb.length() > 0) {
			return(new Shingle(docId, sb.toString().hashCode()));
		}
		else {
			return(null);
		}
  	} // Next shingle
	

	private void flushBuffer() {
		while(buffer.size() > 0) {
			Shingle s = getNextShingle();
			try {
				if(s != null) {
					queue.put(s);
				}
				else {
					queue.put(new Poison(docId, 0));
				}
			} catch (Exception e) {
				
			}
		}
	}

        
  }// Document Parser