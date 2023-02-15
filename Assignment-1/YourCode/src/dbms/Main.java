package dbms;

import java.io.FileNotFoundException;

/** @author Yaseen Taha */
public class Main {

	// this is just the main method refer to the Database Class and its comments
	// for documentation / explanations
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		Database db = new Database();
		db.runDemo();
	}

}
