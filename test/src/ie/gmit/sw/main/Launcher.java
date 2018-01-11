// Aron O' Malley 
// G00327019 
// Java Project 2017 

package ie.gmit.sw.main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
// class for launcher which assists with shingles 
public class Launcher {
	// file 1 and 2 
	public static void Launch(String f1, String f2) {
		// attempt at blocking queue for shingls and hashes
		BlockingQueue<Shingle> q = new LinkedBlockingQueue<>(100);
		// threadPoolSize	
		
		Thread t1 = new Thread(new DocumentParser(f1, q, 5, 5, 0), "T1");
		Thread t2 = new Thread(new DocumentParser(f2, q, 5, 5, 1), "T2");
		Thread t3 = new Thread(new MainProcess(q, 5, 5), "T3");
		
		t1.start();
		t2.start();
		t3.start();
		// message for when thread 1 is exited
		try {
			t1.join();
			System.out.println("Launcher: Thread 1 exited...");
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}
		// message for when thread 2 is exited
		try {
			t2.join();
			System.out.println("Launcher: Thread 2 exited...");
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		try {
			t3.join();
			System.out.println("Launcher: MainProcess exited...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
