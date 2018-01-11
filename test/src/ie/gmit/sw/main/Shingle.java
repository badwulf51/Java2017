// Aron O' Malley 
// G00327019 
// Java Project 2017 

// Pakcages and imports
package ie.gmit.sw.main;
// basic getters and setters class 
public class Shingle {
	private int docId;
	private int hashcode;
	// super class 
	public Shingle(int docId, int hashcode) {
		super();
		this.docId = docId;
		this.hashcode = hashcode;
	}
	// gets docID
	public int getDocId() {
		return docId;
	}
	// sets docID
	public void setDocId(int docId) {
		this.docId = docId;
	}
	// gets hashcode
	public int getHashcode() {
		return hashcode;
	}
	
	// Sets hashcode 
	public void setHashcode(int hashcode) {
		this.hashcode = hashcode;
	}

	
}
