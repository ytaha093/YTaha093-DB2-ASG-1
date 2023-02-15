package dbms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import relations.Account;
import relations.Balance;
import relations.Customer;
import relations.Log;

/** @author Yaseen Taha */
public class Database {

	private HashMap<String, Customer> customers;
	private HashMap<String, Account> accounts;
	private HashMap<String, Balance> balances;
	private ArrayList<Log> logs;

	public Database() {
		// Initialize the hashmap's that will be the in memory rep for the relations.
		// I picked hashmap's to for the data structure since when you normally talk
		// about tuples u use keys to refer to them and with a hashmap you
		// can make the map key the PK so it works similarly to how a
		// Relational DB works with the PK being used to access a tuple
		this.customers = new HashMap<>();
		this.accounts = new HashMap<>();
		this.balances = new HashMap<>();
		// Using just an arrayList for the logs as i want to logs
		// to more structured like a list then DB
		this.logs = new ArrayList<>();
		// import all the csv data into java objects that go into the hashmap's
		importData();
	}

	public void runDemo() throws InterruptedException {
		System.out.println("Current account balances");
		printBalances();

		System.out.println("Current logs");
		printLog();

		System.out.println();

		try {
			System.out.println("Starting Transaction 1");
			// logs the before state of the transaction before anything else
			Balance checking = this.balances.get("312345c");
			Balance saving = this.balances.get("312345s");

			// I create a log for each tuple that is involved in the transaction
			// the log saves the before and after states
			Log checkingLog = createLog(1, checking.getClass().getSimpleName(), checking.toString());
			Log savingLog = createLog(1, checking.getClass().getSimpleName(), saving.toString());
			logTransactionState();

			// -100,000 from checking
			System.out.println("Subtracting $100,000 from checking account 312345C");
			checking.setBalance(checking.getBalance() - 100000);
			// set the after state in the log and set log status for
			// that part of the transaction to complete
			checkingLog.setAfterState(checking.toString());
			checkingLog.setStatusComplete();
			// save the log into secondary memory output_log.csv
			// also save this part of transaction into secondary mem
			// output_account-balance.csv
			logTransactionState();
			commitBalances();

			// +100,000 to savings
			System.out.println("Adding $100,000 to saving account 312345S");
			saving.setBalance(saving.getBalance() + 100000);
			// set the after state in the log and set log status for
			// that part of the transaction to complete
			savingLog.setAfterState(saving.toString());
			savingLog.setStatusComplete();
			// save the log into secondary memory output_log.csv
			// also save this part of transaction into secondary mem
			// output_account-balance.csv
			logTransactionState();
			commitBalances();

			System.out.println("Transaction 1 Complete");
			System.out.println("");
			System.out.println("Current account balances");
			printBalances();

			System.out.println("Current logs");
			printLog();
		} catch (Error e) {
			System.err.println("ERROR HAS OCCERED PREPAIRING ROLLBACK");

		}

		try {
			System.out.println("Starting Transaction 2");
			// logs the before state of the transaction before anything else
			Balance checking = this.balances.get("312345c");
			Balance saving = this.balances.get("312345s");

			// I create a log for each tuple that is involved in the transaction
			// the log saves the before and after states
			Log checkingLog = createLog(2, checking.getClass().getSimpleName(), checking.toString());
			Log savingLog = createLog(2, checking.getClass().getSimpleName(), saving.toString());
			logTransactionState();

			// -100,000 from checking
			System.out.println("Subtracting $100,000 from checking account 312345C");
			checking.setBalance(checking.getBalance() - 100000);
			// set the after state in the log and set log status for
			// that part of the transaction to complete
			checkingLog.setAfterState(checking.toString());
			checkingLog.setStatusComplete();
			// save the log into secondary memory output_log.csv
			// also save this part of transaction into secondary mem
			// output_account-balance.csv
			logTransactionState();
			commitBalances();

			// Stops the transaction after -100000 has bud deducted but not readded
			throw new Error();

		} catch (Error e) {
			System.err.println("ERROR HAS OCCERED PREPARING TO ROLLBACK INCOMPLETE TRANSACTIONS TO 'BEFORE' STATE");
			Thread.sleep(400);
			System.err.println("Current account balances with error");
			printBalances();
			System.err.println("Current Logs with error");
			printLog();
			revertIncompleteTransactions();
			System.err.println("Rollback Completed");
			System.err.println("Account balances after roolback");
			printBalances();
			System.err.println("Logs after roolback");
			printLog();
		}

	}

	// search the logs to find the transaction ID with an incomplete status, then
	// reverts all actions with that transaction ID
	private void revertIncompleteTransactions() {
		// finds the transactionID with incomplete status
		int transactionID = -1;
		ArrayList<Log> stuffToRevert = new ArrayList<>();
		for (Log log : logs) {
			if (log.getStatus().equals("incomplete")) {
				transactionID = log.getTransationID();
				break;
			}
		}
		// once we know what transaction failed get logs for logs
		// involved in the transaction
		for (Log log : logs) {
			if (log.getTransationID() == transactionID) {
				stuffToRevert.add(log);
			}
		}
		// now we revert to transaction
		for (Log log : stuffToRevert) {
			switch (log.getEntityType()) {
			case "Balance":
				String[] vals = log.getBeforeState().split(",");
				Balance balance = new Balance(vals[0], vals[1]);
				this.balances.put(vals[0], balance);
				log.setStatusReverted();
				break;
			case "Account":
				String[] vals1 = log.getBeforeState().split(",");
				Account account = new Account(vals1[0], vals1[1], vals1[2]);
				this.accounts.put(vals1[0], account);
				log.setStatusReverted();
				break;
			case "Customer":
				String[] vals2 = log.getBeforeState().split(",");
				Customer customer = new Customer(vals2[0], vals2[1], vals2[2], vals2[1], vals2[4], vals2[5]);
				this.customers.put(vals2[0], customer);
				log.setStatusReverted();
				break;
			}
		}
	}

	// creates and adds a log to the log list
	private Log createLog(int transationID, String entityType, String beforeState) {
		Log log = new Log((this.logs.size() + 1), transationID, entityType, beforeState);
		this.logs.add(log);
		return log;
	}

	private void printLog() {
		if (this.logs.isEmpty()) {
			System.out.println("No loggs yet");
			return;
		}
		for (Log log : this.logs) {
			System.out.println("LogID:" + log.getLogID() + "  TransationID:" + log.getTransationID() + "  Entity Type:"
					+ log.getEntityType() + "  Before:" + log.getBeforeState() + "  After:" + log.getAfterState()
					+ "  Status:" + log.getStatus());
		}
		System.out.println();
	}

	private void printBalances() {
		System.out.println();
		for (Balance balance : this.balances.values()) {
			System.out.println("Account Num:" + balance.getAccountNum() + "  Balance:" + balance.getBalance());
		}
		System.out.println();
	}

	// Writes the content of the in memory log to secondary memory
	// output_log.csv
	private void logTransactionState() {
		File logs = new File("output_log.csv");
		try {
			FileWriter writer = new FileWriter(logs);
			for (Log log : this.logs) {
				writer.write(log.toString() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Error encountered saving transation log");
			e.printStackTrace();
		}
	}

	// Writes the content of the in memory customers map to secondary memory
	// output_customer.csv
	private void commitCustomers() {
		File customers = new File("output_customer.csv");
		try {
			FileWriter writer = new FileWriter(customers);
			for (Customer customer : this.customers.values()) {
				writer.write(customer.toString() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Error encountered commiting customer table");
			e.printStackTrace();
		}
	}

	// Writes the content of the in memory accounts map to secondary memory
	// output_account.csv
	private void commitAccounts() {
		File accounts = new File("output_account.csv");
		try {
			FileWriter writer = new FileWriter(accounts);
			for (Account account : this.accounts.values()) {
				writer.write(account.toString() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Error encountered commiting customer table");
			e.printStackTrace();
		}
	}

	// Writes the content of the in memory balances map to secondary memory
	// output_account-balance.csv
	private void commitBalances() {
		File balances = new File("output_account-balance.csv");
		try {
			FileWriter writer = new FileWriter(balances);
			for (Balance balance : this.balances.values()) {
				writer.write(balance.toString() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Error encountered commiting customer table");
			e.printStackTrace();
		}
	}

	// imports all the data from the CSV's
	private void importData() {
		try {
			// imports the customers
			Scanner scn = new Scanner(new File("../../Assignment-1/Data-Assignment-1/csv/customer.csv"));
			while (scn.hasNext()) {
				// scans a row splitting the row's values up in an array
				String[] vals = scn.nextLine().split(",");
				// create a customer obj with the row values then add it to the map
				Customer customer = new Customer(vals[0], vals[1], vals[2], vals[3], vals[4], vals[5]);
				this.customers.put(vals[0], customer);
			}
			scn.close();
			// imports the accounts
			scn = new Scanner(new File("../../Assignment-1/Data-Assignment-1/csv/account.csv"));
			while (scn.hasNext()) {
				// scans a row splitting the row's values up in an array
				String[] vals = scn.nextLine().split(",");
				// create a account obj with the row values then add it to the map
				Account account = new Account(vals[0], vals[1], vals[2]);
				this.accounts.put(vals[0], account);
			}
			scn.close();
			// imports the balances
			scn = new Scanner(new File("../../Assignment-1/Data-Assignment-1/csv/account-balance.csv"));
			while (scn.hasNext()) {
				// scans a row splitting the row's values up in an array
				String[] vals = scn.nextLine().split(",");
				// create a balance obj with the row values then add it to the map
				Balance balance = new Balance(vals[0], vals[1]);
				this.balances.put(vals[0], balance);
			}
			scn.close();
		} catch (FileNotFoundException e) {
			System.out.println("cannot find / access CSV in specified location");
			e.printStackTrace();
		}

	}

}
