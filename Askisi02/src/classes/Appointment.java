package classes;

import java.util.ArrayList;
import java.util.Scanner;

public class Appointment {
	String date,specialty,docName;
	ArrayList<Appointment> appointments = new ArrayList<Appointment>();
	Doctor doc = new Doctor();
	
	Scanner scn = new Scanner(System.in);
	
	//Three constructors, one empty, one that gets as an argument a Doctor obj and one that gets as an argument the specialty.
	Appointment(){}
	Appointment(Doctor doctor, String date){
		this.doc = doctor;
		this.date = date;
		this.specialty = "Not Given";
		}
	Appointment(String specialty, String date){
		this.specialty = specialty;
		this.date = date;
		this.doc.name = "Not Given";
	}
	
	//Methods for creating appointments based on the previous constructors.
	public void createAppointmentByName(Doctor doctor, String date)	{
		this.date = date;
		this.doc = doctor;
		appointments.add(new Appointment(doc, date));
		System.out.println("Your appointment has been created successfully.");
	}
	public void createAppointmentBySpecialty(String specialty, String date){
		this.date = date;
		this.specialty = specialty;
		appointments.add(new Appointment(specialty, date));
		System.out.println("Your appointment has been created successfully");
	}
	
}
