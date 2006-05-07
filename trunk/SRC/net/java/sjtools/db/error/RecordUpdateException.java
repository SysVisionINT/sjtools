package net.java.sjtools.db.error;

public class RecordUpdateException extends Exception {
	private static final long serialVersionUID = -2289212150313242192L;
	
	public static final String UPDATE = "UPDATE"; 
	public static final String DELETE = "DELETE";
	
	private int changedRecords = 0;
	private int expectedChangedRecords = 0;
	private String operation = null;
	
	public RecordUpdateException(String operation, int expectedChangedRecords, int changedRecords) {
		super("Operation " + operation + " was expected to change " + expectedChangedRecords + " but changed " + changedRecords);
		
		this.changedRecords = changedRecords;
		this.expectedChangedRecords = expectedChangedRecords;
		this.operation = operation;
	}

	public int getChangedRecords() {
		return changedRecords;
	}

	public int getExpectedChangedRecords() {
		return expectedChangedRecords;
	}

	public String getOperation() {
		return operation;
	}	
}
