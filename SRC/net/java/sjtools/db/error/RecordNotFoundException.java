package net.java.sjtools.db.error;

public class RecordNotFoundException extends Exception {	
	private static final long serialVersionUID = -3323525947679423620L;

	public RecordNotFoundException(String query) {
		super("Unable to find record " + query);
	}
}
