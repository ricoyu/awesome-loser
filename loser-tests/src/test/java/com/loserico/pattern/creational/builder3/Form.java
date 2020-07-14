package com.loserico.pattern.creational.builder3;

import java.util.Date;

/**
 * There is another form of the Builder Pattern other than what we have seen so far.
 * Sometimes there is an object with a long list of properties, and most of these
 * properties are optional. Consider an online form which needs to be filled in
 * order to become a member of a site. You need to fill all the mandatory fields but
 * you can skip the optional fields or sometimes it may look valuable to fill some
 * of the optional fields.
 * 
 * Please check the below Form class which contains long list of properties and some
 * of the properties are optional.
 * 
 * It is mandatory to have firstName, lastName, userName, and password in the form
 * but all others are optional fields.
 * 
 * The question is, what sort of constructor should we write for such a class? Well
 * writing a constructor with long list of parameters is not a good choice, this
 * could frustrate the client especially if the important fields are only a few.
 * This could increase the scope of error; the client may provide a value
 * accidentally to a wrong field.
 * 
 * Another way is to use telescoping constructors, in which you provide a
 * constructor with only the required parameters, another with a single optional
 * parameter, a third with two optional parameters, and so on, culminating in a
 * constructor with all the optional parameters.
 * 
 * <pre>
 * public Form(String firstName,String lastName){
 *		this(firstName,lastName,null,null);
 * }
	
 * public Form(String firstName,String lastName,String userName,String password){
 * 		this(firstName,lastName,userName,password,null,null);
 * }
	
 * public Form(String firstName,String lastName,String userName,String password,String address,Date dob){
 * 		this(firstName,lastName,userName,password,address,dob,null,null);
 * }
 * 
 * public Form(String firstName,String lastName,String userName,String password,String address,Date dob,String email,String backupEmail){
 * 		…
 * }
 * 
 * </pre>
 * 
 * When you want to create an instance, you use the constructor with the shortest
 * parameter list containing all the parameters you want to set.
 * 
 * The telescoping constructor works, but it is hard to write client code when there
 * are many parameters, and it is even harder to read it. The reader is left
 * wondering what all those values mean and must carefully count parameters to find
 * out. Long sequences of identically typed parameters can cause subtle bugs. If the
 * client accidentally reverses two such parameters, the compiler won’t complain,
 * but the program will misbehave at runtime.
 * 
 * A second alternative when you are faced with many constructor parameters is the
 * JavaBeans pattern, in which you call a parameter less constructor to create the
 * object and then call setter methods to set each required parameter and each
 * optional parameter of interest.
 * 
 * Unfortunately, the JavaBeans pattern has serious disadvantages of its own.
 * Because construction is split across multiple calls, a JavaBean may be in an
 * inconsistent state partway through its construction. The class does not have the
 * option of enforcing consistency merely by checking the validity of the
 * constructor parameters. Attempting to use an object when it’s in an inconsistent
 * state may cause failures that are far removed from the code containing the bug,
 * hence difficult to debug.
 * 
 * There is a third alternative that combines the safety of the telescoping
 * constructor pattern with the readability of the JavaBeans pattern. It is a form
 * of the Builder pattern. Instead of making the desired object directly, the client
 * calls a constructor with all of the required parameters and gets a builder
 * object. Then the client calls setter-like methods on the builder object to set
 * each optional parameter of interest. Finally, the client calls a parameter less
 * build method to generate the object.
 * 
 * @author Loser
 * @since Sep 14, 2016
 * @version
 *
 */
public class Form {

	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private String address;
	private Date dob;
	private String email;
	private String backupEmail;
	private String spouseName;
	private String city;
	private String state;
	private String country;
	private String language;
	private String passwordHint;
	private String securityQuestion;
	private String securityAnswer;

	public static class FormBuilder {

		private String firstName;
		private String lastName;
		private String userName;
		private String password;
		private String address;
		private Date dob;
		private String email;
		private String backupEmail;
		private String spouseName;
		private String city;
		private String state;
		private String country;
		private String language;
		private String passwordHint;
		private String securityQuestion;
		private String securityAnswer;

		public FormBuilder(String firstName, String lastName, String userName, String password) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.userName = userName;
			this.password = password;
		}

		public FormBuilder address(String address) {
			this.address = address;
			return this;
		}

		public FormBuilder dob(Date dob) {
			this.dob = dob;
			return this;
		}

		public FormBuilder email(String email) {
			this.email = email;
			return this;
		}

		public FormBuilder backupEmail(String backupEmail) {
			this.backupEmail = backupEmail;
			return this;
		}

		public FormBuilder spouseName(String spouseName) {
			this.spouseName = spouseName;
			return this;
		}

		public FormBuilder city(String city) {
			this.city = city;
			return this;
		}

		public FormBuilder state(String state) {
			this.state = state;
			return this;
		}

		public FormBuilder country(String country) {
			this.country = country;
			return this;
		}

		public FormBuilder language(String language) {
			this.language = language;
			return this;
		}

		public FormBuilder passwordHint(String passwordHint) {
			this.passwordHint = passwordHint;
			return this;
		}

		public FormBuilder securityQuestion(String securityQuestion) {
			this.securityQuestion = securityQuestion;
			return this;
		}

		public FormBuilder securityAnswer(String securityAnswer) {
			this.securityAnswer = securityAnswer;
			return this;
		}

		public Form build() {
			return new Form(this);
		}
	}

	private Form(FormBuilder formBuilder) {

		this.firstName = formBuilder.firstName;
		this.lastName = formBuilder.lastName;
		this.userName = formBuilder.userName;
		this.password = formBuilder.password;
		this.address = formBuilder.address;
		this.dob = formBuilder.dob;
		this.email = formBuilder.email;
		this.backupEmail = formBuilder.backupEmail;
		this.spouseName = formBuilder.spouseName;
		this.city = formBuilder.city;
		this.state = formBuilder.state;
		this.country = formBuilder.country;
		this.language = formBuilder.language;
		this.passwordHint = formBuilder.passwordHint;
		this.securityQuestion = formBuilder.securityQuestion;
		this.securityAnswer = formBuilder.securityAnswer;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" First Name: ");
		sb.append(firstName);
		sb.append("\n Last Name: ");
		sb.append(lastName);
		sb.append("\n User Name: ");
		sb.append(userName);
		sb.append("\n Password: ");
		sb.append(password);

		if (this.address != null) {
			sb.append("\n Address: ");
			sb.append(address);
		}
		if (this.dob != null) {
			sb.append("\n DOB: ");
			sb.append(dob);
		}
		if (this.email != null) {
			sb.append("\n Email: ");
			sb.append(email);
		}
		if (this.backupEmail != null) {
			sb.append("\n Backup Email: ");
			sb.append(backupEmail);
		}
		if (this.spouseName != null) {
			sb.append("\n Spouse Name: ");
			sb.append(spouseName);
		}
		if (this.city != null) {
			sb.append("\n City: ");
			sb.append(city);
		}
		if (this.state != null) {
			sb.append("\n State: ");
			sb.append(state);
		}
		if (this.country != null) {
			sb.append("\n Country: ");
			sb.append(country);
		}
		if (this.language != null) {
			sb.append("\n Language: ");
			sb.append(language);
		}
		if (this.passwordHint != null) {
			sb.append("\n Password Hint: ");
			sb.append(passwordHint);
		}
		if (this.securityQuestion != null) {
			sb.append("\n Security Question: ");
			sb.append(securityQuestion);
		}
		if (this.securityAnswer != null) {
			sb.append("\n Security Answer: ");
			sb.append(securityAnswer);
		}

		return sb.toString();
	}

	/**
	 * As you can clearly see, now a client only needs to provide the mandatory
	 * fields and the fields which are important to him. To create the form object
	 * now, we need invoke the FormBuilder constructor which takes the mandatory
	 * fields and then we need to call the set of required methods on it and finally
	 * the build method to get the form object.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Form form = new FormBuilder("Dave", "Carter", "DavCarter", "DAvCaEr123").passwordHint("MyName").city("NY")
				.language("English").build();
		System.out.println(form);
	}

}
