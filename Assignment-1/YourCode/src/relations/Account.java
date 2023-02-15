package relations;

public class Account {

	private int id;
	private String checkingAccount;
	private String savingAccount;

	public Account(String id, String checkingAccount, String savingAccount) {
		this.id = Integer.parseInt(id);
		this.checkingAccount = checkingAccount;
		this.savingAccount = savingAccount;
	}

	// Just a bunch of getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCheckingAccount() {
		return checkingAccount;
	}

	public void setCheckingAccount(String checkingAccount) {
		this.checkingAccount = checkingAccount;
	}

	public String getSavingAccount() {
		return savingAccount;
	}

	public void setSavingAccount(String savingAccount) {
		this.savingAccount = savingAccount;
	}

	@Override
	public String toString() {
		return id + "," + checkingAccount + "," + savingAccount;
	}

}
