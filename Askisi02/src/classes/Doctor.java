package classes;

import java.util.ArrayList;
import java.util.Scanner;

public class Doctor extends Users {
	Scanner scn = new Scanner(System.in);
	String specialty;
	ArrayList<String> availableAppointments = new ArrayList<String>();			//This list keeps track of the dates a doctor is available.

	Doctor(){}
	Doctor(String name) {
		this.name = name;
	}
	//Getter and Setter for Specialty.
	public String getSpecialty(){
		return this.specialty;
	}
	public void setSpecialty(String specialty){
		this.specialty = specialty;
	}
	
	public void showSchedule() {
		//Appointment app = new Appointment();
		//for (int i=0;i<app.appointments.size();i++){ 												Shows all appointments from the appointments list
		//	System.out.println("You have an appointment for "+app.appointments.get(i).date);		In this case we don't have any appointments because they get deleted every time we
		//}																							execute the program
		System.out.println("You have an appointment for some date");
	}
	public void arrangeAvailableAppointments(String date) {				//The doctor inserts the dates he is free for appointments.
		String dateAccepting;											//Later on, we will compare the availableAppointments list with the appointments list
		dateAccepting = date;											//to see if a doctor is available on a selected date.
		availableAppointments.add(dateAccepting);
	}
}
