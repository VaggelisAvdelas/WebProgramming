package classes;

import java.util.Scanner;

public abstract class Users {

	Scanner scn = new Scanner(System.in);
	static int usersCounter = 0;
	String username,password,name,surname;
	
	// Constructors
	
	Users() { 
	}
	
	// Getters
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getSurname() {
		return this.surname;
	}
	
	// Setters
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	//Typical Login and Logout methods.
	public String login(String un, String pw){
		String loginStatement = "SELECT * FROM doctor,patient WHERE username="+un+" AND password="+pw;
		return(loginStatement);
	}
	
	public void logout(){
		System.out.println("You have successfuly logged off.");
	}
}
	