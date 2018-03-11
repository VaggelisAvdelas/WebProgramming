package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class PatientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    	
	private DataSource datasource = null;
	
	DynamicPages dynPage = new DynamicPages();
	
	public void init() throws ServletException {
		try {
			InitialContext ctx = new InitialContext();
			datasource = (DataSource)ctx.lookup("java:comp/env/jdbc/LiveDataSource");
		} catch(Exception e) {
			throw new ServletException(e.toString());
		}
	}
	
    public PatientServlet() {
        super();
        
        }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
	    response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String requestType= request.getParameter("requestType");
		
		HttpSession session=request.getSession(false);
		String LoginUsername = (String)session.getAttribute("username");
		String LoginAmka = SessionAMKA(LoginUsername,response);
		
		if (requestType == null) {
			dynPage.DynamicPageError(response, "Invalid request type","./patient.html");
		}
		
		//Shows all patient appointments, previous and scheduled
		if (requestType.equalsIgnoreCase("View Appointments")){
			String apps;
			Timestamp currentDate = new Timestamp(System.currentTimeMillis());
			String status="";
			
			try{
				Connection con = datasource.getConnection();
				
				apps = "<table id=\"table\" border=\"1\">"
						+ "<tr>"
						+ "<th>Date</th>"
						+ "<th>Doctor's Name</th>"
						+ "<th>Doctor's Surname</th>"
						+ "<th>Doctor's Specialty</th>"
						+ "<th>Status</th>"
						+ "</tr>";
				
				PreparedStatement stmt = con.prepareStatement("SELECT date,name,surname,specialty FROM appointments,doctor"
						+ " WHERE appointments.patientamka in (SELECT amka FROM patient WHERE username=?)"
						+ " AND appointments.doc_id = doctor.id ORDER BY date");
				stmt.setString(1, LoginUsername);
				
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()){
					Timestamp date = rs.getTimestamp("date");
					if(date.after(currentDate))	status = "Scheduled";
					else if(date.before(currentDate))	status = "Passed";
					String name = rs.getString("name");
					String surname = rs.getString("surname");
					String specialty = rs.getString("specialty");
					String addRow = dynPage.HTMLRowAppsPatient(date,name,surname,specialty,status);
					apps += addRow;
				}
				
				request.setAttribute("apps", apps);
		        request.getRequestDispatcher("/viewappointments.jsp").forward(request, response);
		        
		        rs.close();
				con.close();
				
				}	catch(SQLException sqle){
					dynPage.DynamicPageError(response, "Error while connecting to the database","./patient.html");
					}
		}		
		
		//Check available appointments based on doctor's specialty
		if (requestType.equalsIgnoreCase("Search")) {
			String specialty = request.getParameter("specialty");
			String table;
			Timestamp currentDate = new Timestamp(System.currentTimeMillis());

			try {
				Connection con = datasource.getConnection();
				
				table = "<form action=\"./patientservlet\" method=\"post\">"
						+ "<table id=\"table\" border=\"1\">"
						+ "<tr>"
						+ "<th>Date</th>"
						+ "<th>Doctor's Name</th>"
						+ "<th>Doctor's Surname</th>"
						+ "</tr>";
				
				
				PreparedStatement stmt = con.prepareStatement("SELECT date,doctor.name,doctor.surname FROM appointments JOIN doctor"
						+ " ON appointments.doc_id = doctor.id" 
						+ " WHERE doctor.specialty=? AND appointments.patientamka IS NULL"
						+ " AND appointments.date >=? ORDER BY date");
				stmt.setString(1, specialty);
				stmt.setTimestamp(2, currentDate);
				
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					Timestamp ts = rs.getTimestamp("date");
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedOption = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").format(date);
					String name = rs.getString("name");
					String surname = rs.getString("surname");
					formattedOption += name+","+surname;
					String addRow  = dynPage.HTMLRowDocsPatient(formattedOption,name,surname);
					table += addRow;
				}
				
				table += "<input type=\"submit\" name=\"requestType\" value=\"Schedule Appointment\"></input><br><br>";
				table += "</form>";
				
				request.setAttribute("table", table);
		        request.getRequestDispatcher("/appointmentoptions.jsp").forward(request, response);
		        
		        rs.close();
		        con.close();
		        
			}	catch(SQLException sqle) {
				sqle.printStackTrace();
				dynPage.DynamicPageError(response, "Error while connecting to the database","./patient.html");
				}
		}

		//Cancel an appointment
		if (requestType.equalsIgnoreCase("Cancel Appointment")) {
			String table;
			Timestamp currentDate = new Timestamp(System.currentTimeMillis());
			
			try {
				Connection con = datasource.getConnection();
				
				table = "<form action=\"./patientservlet\" method=\"post\">"
						+ "<table id=\"table\" border=\"1\">"
						+ "<tr>"
						+ "<th>Date</th>"
						+ "<th>Doctor's Name</th>"
						+ "<th>Doctor's Surname</th>"
						+ "</tr>";
				
				PreparedStatement stmt = con.prepareStatement("SELECT date,doctor.name,doctor.surname FROM appointments JOIN doctor"
						+ " ON appointments.doc_id = doctor.id" 
						+ " WHERE appointments.patientamka=?"
						+ " AND appointments.date >= (cast(? as timestamp) + interval '3 days') ORDER BY date");
				stmt.setString(1, LoginAmka);
				stmt.setTimestamp(2, currentDate);
				
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					Timestamp ts = rs.getTimestamp("date");
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedOption = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").format(date);
					String name = rs.getString("name");
					String surname = rs.getString("surname");
					formattedOption += name+","+surname;
					String addRow  = dynPage.HTMLRowDocsPatient(formattedOption,name,surname);
					table += addRow;
				}
				
				table += "<input type=\"submit\" name=\"requestType\" value=\"Remove Appointment\"></input><br><br>";
				table += "</form>";
				
				request.setAttribute("table", table);
		        request.getRequestDispatcher("/appointmentoptions.jsp").forward(request, response);
		        
		        rs.close();
		        con.close();
		        
			}	catch(SQLException sqle) {
				sqle.printStackTrace();
				dynPage.DynamicPageError(response, "Error while connecting to the database","./patient.html");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String requestType= request.getParameter("requestType");	
	
		HttpSession session=request.getSession(false);
		String LoginUsername = (String)session.getAttribute("username");
		String LoginAmka = SessionAMKA(LoginUsername, response);
		
		if (requestType == null) {
			dynPage.DynamicPageError(response, "Invalid request type.","./patient.html");
		}
		
		if (requestType.equalsIgnoreCase("View Info")){
			String table;
			try{
				Connection con = datasource.getConnection();
				
				table = "<table id=\"table\" border=\"1\">"
						+ "<tr>"
						+ "<th>AMKA</th>"
						+ "<th>Username</th>"
						+ "<th>Name</th>"
						+ "<th>Surname</th>"
						+ "</tr>";
				
				PreparedStatement stmt = con.prepareStatement("SELECT * FROM patient WHERE username=?");
				stmt.setString(1, LoginUsername);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()){
					String amka = rs.getString("amka");
					String username = rs.getString("username");
					String name = rs.getString("name");
					String surname = rs.getString("surname");
					String addRow = dynPage.HTMLRowInfoPatient(amka,username,name,surname);
					table += addRow;
				}
				
				request.setAttribute("table", table);
		        request.getRequestDispatcher("./info.jsp").forward(request, response);
		        
		        rs.close();
				con.close();
		        
			}	catch(SQLException sqle){
				dynPage.DynamicPageError(response, "Error while connecting to the database","./patient.html");
				}
		}
		
		//Schedule an appointment
		if (requestType.equalsIgnoreCase("Schedule Appointment")) {		
			String appDate = request.getParameter("date");
					
			if (appDate==null) {
				dynPage.DynamicPageError(response, "You have to choose a date", "./patient.html");
			}
			else {
				String doc[] = appDate.substring(18).split(",");
				String docName = doc[0];
				String docSurname = doc[1];
				appDate = appDate.substring(0,10)+" "+appDate.substring(10,18);
				
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parsedDate = sdf.parse(appDate);
					Timestamp tsappDate = new Timestamp(parsedDate.getTime());
									
					Connection con  = datasource.getConnection();
									
					PreparedStatement stmt = con.prepareStatement("UPDATE appointments SET patientamka=? WHERE date=? AND doc_id IN"
							+ " (SELECT id FROM doctor WHERE name=? AND surname=?)");
					stmt.setString(1, LoginAmka);
					stmt.setTimestamp(2, tsappDate);
					stmt.setString(3, docName);
					stmt.setString(4, docSurname);
					stmt.executeUpdate();
									
					dynPage.DynamicPageSuccess(response, "You have scheduled an appointment successfully","./patient.html");
							
					con.close();
									
				} catch(Exception e) {
					e.printStackTrace();
					}
			}
		}
		
		if (requestType.equalsIgnoreCase("Remove Appointment")) {
			String appDate = request.getParameter("date");
			
			if (appDate==null) {
				dynPage.DynamicPageError(response, "You have to choose a date", "./patient.html");
			}
			else {
				String doc[] = appDate.substring(18).split(",");
				String docName = doc[0];
				String docSurname = doc[1];
				appDate = appDate.substring(0,10)+" "+appDate.substring(10,18);
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parsedDate = sdf.parse(appDate);
					Timestamp tsappDate = new Timestamp(parsedDate.getTime());
					
					Connection con  = datasource.getConnection();
					
					PreparedStatement stmt = con.prepareStatement("UPDATE appointments SET patientamka=NULL WHERE date=? AND doc_id IN"
							+ " (SELECT id FROM doctor WHERE name=? AND surname=?)");
					stmt.setTimestamp(1, tsappDate);
					stmt.setString(2, docName);
					stmt.setString(3, docSurname);
					stmt.executeUpdate();
							
					dynPage.DynamicPageSuccess(response, "You have canceled an appointment successfully","./patient.html");
					
					con.close();
							
				} catch(Exception e) {
					e.printStackTrace();
					}
			}
		}
		
		if (requestType.equalsIgnoreCase("Logout")){
			session.invalidate();
			dynPage.DynamicPageLogout(response, "You have logged out successfully.");
		}
	}
	
	
	private String SessionAMKA(String username,HttpServletResponse response) throws IOException{
		String amka="";
		try {
			Connection con = datasource.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT amka FROM patient WHERE username=?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				amka = rs.getString("amka");
			}
			
			rs.close();
			con.close();
			
		}	catch(SQLException sqle) {
			dynPage.DynamicPageError(response, "Can't access patient's AMKA", "./patient.html");
		}
		return(amka);
	}
}
