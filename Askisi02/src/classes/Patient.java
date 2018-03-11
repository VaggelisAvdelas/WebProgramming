package classes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Patient extends Users {

	Scanner scn = new Scanner(System.in);
	String AMKA;
	Appointment app = new Appointment();

	public Patient() {
		
	}
	
	public String getAMKA(){
		return this.AMKA;
	}
	
	public void setAMKA(String amka){
		this.AMKA = amka;
	}
	
	
	//Register method to insert a new patient in the database
	public String Register() {
		String registerStatement = "INSERT INTO patient VALUES("+getAMKA()+","+getUsername()+",crypt("+getPassword()+",gen_salt('bf',8)),"
				+getName()+","+getSurname()+")";
		return(registerStatement);
	}
	
	//Prints currently logged in patient's info
	public void PrintInfo(){
		System.out.println("Username: "+getUsername()+
					"\tPassword: "+getPassword()+
					"\tName: "+getName()+
					"\tSurname: "+getSurname()+
					"\tAMKA: "+AMKA);
	}
	
	//Two methods to schedule appointments, one based on the doctor's name and one based on his specialty
	//Checking if the doctor is available on these dates hasn't been implemented yet.
	public void AppointmentPerDoctor(){
		System.out.print("Please insert the name of the doctor you would like to schedule an appointment: ");
		String docName = scn.nextLine();
		System.out.println("Please insert the date you would like to schedule the appointment: ");
		String date = scn.nextLine();
		Doctor doc = new Doctor(docName);
		app.createAppointmentByName(doc, date);
	}
	public void AppointmentPerSpecialty(){
		System.out.print("Please insert the specialty of the doctor you would like to schedule an appointment: ");
		String docSpecialty = scn.nextLine();
		System.out.print("Please insert the date you would like to schedule the appointment: ");
		String date = scn.nextLine();
		app.createAppointmentBySpecialty(docSpecialty, date);
	}
	
	//Prints all appointments created by the current user.
	public void listAllAppointments() {
		for (int i=0;i<app.appointments.size();i++){
			System.out.println("Date: "+app.appointments.get(i).date+"\tDoctor: "+app.appointments.get(i).doc.name+"\tSpecialty: "+app.appointments.get(i).specialty);
		}
	}
	
	//Reading patient info from txt file.
	public void patientFromFile() throws IOException{
        BufferedReader inputStream = null;
        String[] patientText = null;
        
        try {
            inputStream = new BufferedReader (new FileReader ("src/mainpackage/Patients.txt"));
            String s;
            while((s = inputStream.readLine())!=null){
            	patientText = s.split(" ");
            }
            inputStream.close();
        }
        catch(FileNotFoundException e){
        	System.err.println("File Not Found");
        }
        
        this.name = patientText[0];
        this.surname = patientText[1];
        this.username = patientText[2];
        this.password = patientText[3];
	}
	
}