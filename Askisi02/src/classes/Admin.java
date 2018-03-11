package classes;

import java.util.Scanner;

public class Admin extends Users {
	
	public Admin(){
		usersCounter++;
	}
		
	Scanner scn = new Scanner(System.in);		
	
	public void AddDoctor() {
		Doctor doc = new Doctor();
		String response;			
		System.out.print("Please Insert Doctor's Name:");
		response = scn.nextLine();
		doc.setName(response);
		System.out.print("Please Insert Doctor's Surname:");
		response = scn.nextLine();
		doc.setSurname(response);
		System.out.print("Please Insert Doctor's Username:");
		response = scn.nextLine();
		doc.setUsername(response);
		System.out.print("Please Insert Doctor's Password:");
		response = scn.nextLine();
		doc.setPassword(response);
		System.out.print("Please Insert Doctor's Specialty:");
		response = scn.nextLine();
		doc.setSpecialty(response);
		usersCounter++;
	}
}