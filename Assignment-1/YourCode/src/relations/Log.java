package relations;

/** @author Yaseen Taha */
public class Log {

	private int logID;
	private int transationID;
	private String entityType;
	private String beforeState;
	private String afterState;
	private String status;

	public Log(int logID, int transationID, String entityType, String beforeState) {
		this.logID = logID;
		this.transationID = transationID;
		this.entityType = entityType;
		this.beforeState = beforeState;
		this.afterState = "";
		this.status = "incomplete";
	}

	public int getLogID() {
		return logID;
	}

	public void setLogID(int logID) {
		this.logID = logID;
	}

	public int getTransationID() {
		return transationID;
	}

	public void setTransationID(int transationID) {
		this.transationID = transationID;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getBeforeState() {
		return beforeState;
	}

	public void setBeforeState(String beforeState) {
		this.beforeState = beforeState;
	}

	public String getAfterState() {
		return afterState;
	}

	public void setAfterState(String afterState) {
		this.afterState = afterState;
	}

	public String getStatus() {
		return status;
	}

	public void setStatusComplete() {
		this.status = "complete";
	}

	public void setStatusIncomplete() {
		this.status = "incomplete";
	}

	public void setStatusReverted() {
		this.status = "reverted";
	}

	@Override
	public String toString() {
		return logID + "," + transationID + "," + entityType + "," + beforeState.replace(",", ":") + ","
				+ afterState.replace(",", ":") + "," + status;
	}

}
