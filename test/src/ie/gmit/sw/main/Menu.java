// Aron O' Malley 
// G00327019 
// Java Project 2017 

package ie.gmit.sw.main;

import java.util.Scanner;
// Main menu class
public class Menu {
	
	public static void main() throws Exception {
	
		Scanner reader = new Scanner(System.in);
		
		String file1;
		String file2;
		// System asks user to enter the name of file 1, in this case enter test1 or test2 
		System.out.println("Enter file 1: ");
		file1 = reader.nextLine();
		// System asks user to enter the name of file 1, in this case enter test1 or test2 
		System.out.println("Enter file 2: ");
		file2 = reader.nextLine();
		
		reader.close();
		//launcher gets files and puts them over 
		Launcher.Launch(file1, file2);	
	}
}
