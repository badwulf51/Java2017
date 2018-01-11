package ie.gmit.sw.main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {
	
	public static void Launch(String f1, String f2) {
		
		BlockingQueue<Shingle> q = new LinkedBlockingQueue<>(100);
		// threadPoolSize	
		
		Thread t1 = new Thread(new DocumentParser(f1, q, 5, 5, 0), "T1");
		Thread t2 = new Thread(new DocumentParser(f2, q, 5, 5, 1), "T2");
		Thread t3 = new Thread(new MainProcess(q, 5, 5), "T3");
		
		t1.start();
		t2.start();
		t3.start();
		
		try {
			t1.join();
			System.out.println("Launcher: Thread 1 exited...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			t2.join();
			System.out.println("Launcher: Thread 2 exited...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
