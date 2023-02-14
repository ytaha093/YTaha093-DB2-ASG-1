package dbms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import relations.Account;
import relations.Balance;
import relations.Customer;

public class Database {

	private HashMap<String, Customer> customers;
	private HashMap<String, Account> accounts;
	private HashMap<String, Balance> balances;

	public Database() {
		// convert all the csv data into java objects, then organize them into hasmap's
		this.customers = new HashMap<>();
		this.accounts = new HashMap<>();
		this.balances = new HashMap<>();
		importData();
	}

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
			System.out.println("cannot find CSV in specified location");
			e.printStackTrace();
		}

	}

}
