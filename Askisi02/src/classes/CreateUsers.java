package classes;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CreateUsers {
	
	Scanner scn = new Scanner(System.in);

	public static void main(String[] args) {
		CreateUsers user = new CreateUsers();
		int menuChoice = user.Menu();
		if (menuChoice == 1){
			user.AsPatient();
		}
		else if(menuChoice == 2){
			user.AsDoctor();
		}
		else if(menuChoice == 3){
			user.AsAdmin();
		}
	}
	
	public int Menu(){
		int menuChoice;
		
		while(true){
			try{
				System.out.println("Options Menu\n------------");
				System.out.println("Press 1 if you are a Patient.\n"
						+ "Press 2 if you are a Doctor.\n"
						+ "Press 3 if you are an Admin.");
				menuChoice = scn.nextInt();
				if(menuChoice == 1 || menuChoice == 2 || menuChoice == 3) {
					break;
				}
				else {
					System.err.println("Incorrect Data");
					continue;
					}
				}
			catch(InputMismatchException e){
				System.err.println("Incorrect Data");
				scn.nextLine();
				continue;	
				}
			}
		scn.nextLine();
		return menuChoice;
		}		
	
	public void AsPatient(){
		int menuChoicePatient;
		boolean goBack = true;
		char response;
		Patient patient = new Patient();

		outerLoop:
		while(true){
			try{
				goBack = true;
				System.out.println("Patient Options\n---------------\n"
						+ "Press 1 to register as a new Patient\n"
						+ "Press 2 to view your details\n"
						+ "Press 3 to schedule an appointment with a specific doctor\n"
						+ "Press 4 to schedule an appointment with a doctor of specific specialty\n"
						+ "Press 5 to list all appointments scheduled\n"
						+ "Press 6 to insert a patient from a txt file");
				menuChoicePatient = scn.nextInt();
				
				if (menuChoicePatient == 1)	patient.Register();
				else if (menuChoicePatient == 2)	patient.PrintInfo();
				else if (menuChoicePatient == 3)	patient.AppointmentPerDoctor();
				else if (menuChoicePatient == 4)	patient.AppointmentPerSpecialty();
				else if (menuChoicePatient == 5)	patient.listAllAppointments();
				else if (menuChoicePatient == 6) 	patient.patientFromFile();
				else System.err.println("Incorrect Input");
				scn.nextLine();
				while(goBack){
					System.out.println("Do you want to go back? Y/N");
					response = scn.next().charAt(0);
					if (response == 'Y')	{
						goBack = false;
						continue outerLoop;
					}
					else if(response == 'N')	{
						goBack = false;
						break outerLoop;
					}
					else {
						System.err.println("Incorrect Input");
					}	
				}	
				scn.nextLine();
			}
			catch(IOException e){
				
			}
			catch(InputMismatchException e){
				System.err.println("Incorrect Data");
				scn.nextLine();
				continue;
				}
			}
		scn.nextLine();
		}
	public void AsDoctor(){
		int menuChoiceDoctor;
		boolean goBack = true;
		char response;
		String date;
		
		outerLoop:
			while(true){
				try{
					goBack = true;
					System.out.println("Doctor Options\n-------------\n"
							+ "Press 1 to view your schedule\n"
							+ "Press 2 to arrange your appointment accepting dates");
					menuChoiceDoctor = scn.nextInt();
					Doctor doctor = new Doctor();
					
					if (menuChoiceDoctor == 1)	doctor.showSchedule();
					else if (menuChoiceDoctor == 2)	{
						System.out.print("Insert the date you accept appointments: ");
						date = scn.nextLine();
						doctor.arrangeAvailableAppointments(date);
					}
					else System.err.println("Incorrect Input");
					scn.nextLine();
					while(goBack){
						System.out.println("Do you want to go back? Y/N");
						response = scn.next().charAt(0);
						if (response == 'Y')	{
							goBack = false;
							continue outerLoop;
						}
						else if(response == 'N')	{
							goBack = false;
							break outerLoop;
						}
						else {
							System.err.println("Incorrect Input");
						}	
					}	
					scn.nextLine();
					}
				catch(InputMismatchException e){
					System.err.println("Incorrect Data");
					scn.nextLine();
					continue;
				}
			}
	}
	public void AsAdmin(){
		int menuChoiceAdmin;
		char response;
		boolean goBack = true;
		
		outerLoop:
			while(true){
				try{
					goBack = true;
					System.out.println("Admin Options\n--------------\n"
							+ "Press 1 to add a new Doctor\n");
					menuChoiceAdmin = scn.nextInt();
					Admin admin = new Admin();
					if (menuChoiceAdmin == 1)	admin.AddDoctor();
					else System.err.println("Incorrect Input");
					scn.nextLine();
					while(goBack){
						System.out.println("Do you want to go back? Y/N");
						response = scn.next().charAt(0);
						if (response == 'Y')	{
							goBack = false;
							continue outerLoop;
						}
						else if(response == 'N')	{
							goBack = false;
							break outerLoop;
						}
						else {
							System.err.println("Incorrect Input");
						}	
					}	
					scn.nextLine();
				}
				catch(InputMismatchException e){
					System.err.println("Incorrect Data");
					scn.nextLine();
					continue;
					}
			}
	}
}
