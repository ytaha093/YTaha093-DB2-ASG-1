package relations;

public class Balance {

	private String accountNum;
	private int balance;

	public Balance(String accountNum, String balance) {
		this.accountNum = accountNum;
		this.balance = Integer.parseInt(balance);
	}

	// Just a bunch of getters and setters
	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return accountNum + "," + balance;
	}

}
