package ie.gmit.sw.main;

import java.util.Scanner;

public class Menu {
	
	public static void main() throws Exception {
	
		Scanner reader = new Scanner(System.in);
	
		String file1;
		String file2;
	
		System.out.println("Enter file 1: ");
		file1 = reader.nextLine();
		
		System.out.println("Enter file 2: ");
		file2 = reader.nextLine();
		
		reader.close();
		
		Launcher.Launch(file1, file2);	
	}
}
