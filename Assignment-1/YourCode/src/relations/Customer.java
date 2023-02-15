package relations;

/** @author Yaseen Taha */
public class Customer {

	private int id;
	private String lastName;
	private String firstName;
	private String address;
	private String city;
	private int age;

	public Customer(String id, String lastName, String firstName, String address, String city, String age) {
		this.id = Integer.parseInt(id);
		this.lastName = lastName;
		this.firstName = firstName;
		this.address = address;
		this.city = city;
		this.age = Integer.parseInt(age);
	}

	// Just a bunch of getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return id + "," + lastName + "," + firstName + "," + address + "," + city + "," + age;
	}

}
