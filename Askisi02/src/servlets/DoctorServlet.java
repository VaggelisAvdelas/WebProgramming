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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class DoctorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	DynamicPages dynPage = new DynamicPages();
	
	private DataSource datasource = null;
	
	public void init() throws ServletException {
		try {
			InitialContext ctx = new InitialContext();
			datasource = (DataSource)ctx.lookup("java:comp/env/jdbc/LiveDataSource");
		} catch(Exception e) {
			throw new ServletException(e.toString());
		}
	}
	
    public DoctorServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String requestType= request.getParameter("requestType");
		
		HttpSession session=request.getSession(false);
		String LoginUsername = (String)session.getAttribute("username");
		int LoginID = SessionId(LoginUsername, response);
		
		if (requestType == null) {
			dynPage.DynamicPageError(response, "Invalid request type","./doctor.html");
		}
		if (requestType.equalsIgnoreCase("Plan Appointments")) {
			Timestamp currentDate = new Timestamp(System.currentTimeMillis());
			Date date = new Date();
			date.setTime(currentDate.getTime());
			String formattedDate = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").format(date);
			
			int currentDay = Integer.parseInt(formattedDate.substring(8,10));
			int day = Integer.parseInt(request.getParameter("day"));
			
			String hour_start = request.getParameter("hour_start");
			int sHour = Integer.parseInt(hour_start.substring(0,2));
			String hour_end = request.getParameter("hour_end");
			int eHour = Integer.parseInt(hour_end.substring(0,2));
			int hour_ascending = Integer.parseInt(hour_start.substring(0,2));
					
			try {
				Connection con = datasource.getConnection();
				
				if (currentDay >= day ) {
					dynPage.DynamicPageError(response, "Chosen date must be after current date", "./doctor.html");
				}
				else {
					for (int i=0; i<=eHour-sHour; i++) {
						try {
							String appDate = formattedDate.substring(0,8)+Integer.toString(day)+" "+hour_start+":00";
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date parsedDate = sdf.parse(appDate);
							Timestamp tsappDate = new Timestamp(parsedDate.getTime());
							
							PreparedStatement stmt = con.prepareStatement("INSERT INTO appointments VALUES(DEFAULT,?,?,NULL)");
							stmt.setTimestamp(1,tsappDate);
							stmt.setInt(2, LoginID);
							stmt.executeUpdate();
							
							hour_ascending++;
							hour_start = Integer.toString(hour_ascending)+":00";
						}	catch(Exception e) {
							e.printStackTrace();
							dynPage.DynamicPageError(response, "Error inserting appointment", "./doctor.html");
						}
					}
				}
			
				RequestDispatcher view = request.getRequestDispatcher("./appointmentplans.html");
				view.forward(request, response);
				
				con.close();
				
			}	catch(SQLException sqle) {
				dynPage.DynamicPageError(response, "Error accessing database", "./doctor.html");
			}
		}
		if (requestType.equalsIgnoreCase("View Appointments")) {
			String apps;
			Timestamp currentDate = new Timestamp(System.currentTimeMillis());
			
			try{
				Connection con = datasource.getConnection();
				
				apps = "<table id=\"table\" border=\"1\">"
						+ "<tr>"
						+ "<th>Date</th>"
						+ "<th>Patient's AMKA</th>"
						+ "<th>Patient's Name</th>"
						+ "<th>Patient's Surname</th>"
						+ "</tr>";
				
				PreparedStatement stmt = con.prepareStatement("SELECT date,amka,name,surname FROM appointments JOIN patient ON appointments.patientamka = patient.amka"
						+ " WHERE appointments.doc_id in (SELECT id FROM doctor WHERE username=?)"
						+ " AND appointments.date >= ? ORDER BY date");
				stmt.setString(1, LoginUsername);
				stmt.setTimestamp(2, currentDate);
				
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()){
					Timestamp date = rs.getTimestamp("date");
					String amka = rs.getString("amka");
					String name = rs.getString("name");
					String surname = rs.getString("surname");
					String addRow = dynPage.HTMLRowAppsDoctor(date,amka,name,surname);
					apps += addRow;
				}
				
				request.setAttribute("apps", apps);
		        request.getRequestDispatcher("/viewappointments.jsp").forward(request, response);
		        
		        rs.close();
				con.close();
				
				}	catch(SQLException sqle){
					dynPage.DynamicPageError(response, "Error while connecting to the database","./doctor.html");
					}
			}
		if (requestType.equalsIgnoreCase("Cancel Appointment")) {
			String table;
			Timestamp currentDate = new Timestamp(System.currentTimeMillis());
			
			try {
				Connection con = datasource.getConnection();
				
				table = "<form action=\"./doctorservlet\" method=\"get\">"
						+ "<table id=\"table\" border=\"1\">"
						+ "<tr>"
						+ "<th>Date</th>"
						+ "<th>Patient's AMKA</th>"
						+ "<th>Patient's Name</th>"
						+ "<th>Patient's Surname</th>"
						+ "</tr>";
				
				PreparedStatement stmt = con.prepareStatement("SELECT date,amka,name,surname FROM appointments JOIN patient ON appointments.patientamka = patient.amka"
						+ " WHERE appointments.doc_id in (SELECT id FROM doctor WHERE username=?)"
						+ " AND appointments.date >= (cast(? as timestamp) + interval '3 days') ORDER BY date");
				stmt.setString(1, LoginUsername);
				stmt.setTimestamp(2, currentDate);
				
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()){
					Timestamp ts = rs.getTimestamp("date");
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").format(date);
					String amka = rs.getString("amka");
					String name = rs.getString("name");
					String surname = rs.getString("surname");
					String addRow = dynPage.HTMLRowDeleteDoctor(formattedDate,amka,name,surname);
					table += addRow;
				}
				
				table += "<input type=\"submit\" name=\"requestType\" value=\"Remove Appointment\"></input><br><br>";
				table += "</form>";
				
				request.setAttribute("table", table);
		        request.getRequestDispatcher("/appointmentoptions.jsp").forward(request, response);
		        
		        rs.close();
				con.close();
				
				}	catch(SQLException sqle){
					dynPage.DynamicPageError(response, "Error while connecting to the database","./doctor.html");
					}		
		}
		if (requestType.equalsIgnoreCase("Remove Appointment")) {
			String appDate = request.getParameter("date");
			appDate = appDate.substring(0,10)+" "+appDate.substring(10);
			System.out.println(appDate);
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parsedDate = sdf.parse(appDate);
				Timestamp tsappDate = new Timestamp(parsedDate.getTime());
				
				Connection con  = datasource.getConnection();
				
				PreparedStatement stmt = con.prepareStatement("DELETE FROM appointments WHERE date=? AND doc_id=?");
				stmt.setTimestamp(1, tsappDate);
				stmt.setInt(2, LoginID);
				stmt.executeUpdate();
						
				dynPage.DynamicPageSuccess(response, "You have canceled an appointment successfully","./doctor.html");
				con.close();
						
			} catch(Exception e) {
				e.printStackTrace();
				dynPage.DynamicPageError(response, "Appointment couldn't be deleted", "./doctor.html");
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
		
		if (requestType == null) {
			dynPage.DynamicPageError(response, "Invalid request type.","./doctor.html");
		}
		if (requestType.equalsIgnoreCase("View Info")){
			String table;
			try{
				Connection con = datasource.getConnection();
				
				table = "<table id=\"table\" border=\"1\">"
						+ "<tr>"
						+ "<th>Username</th>"
						+ "<th>Name</th>"
						+ "<th>Surname</th>"
						+ "<th>Specialty</th>"
						+ "</tr>";
				
				PreparedStatement stmt = con.prepareStatement("SELECT username,name,surname,specialty FROM doctor WHERE username=?");
				stmt.setString(1, LoginUsername);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()){
					String username = rs.getString("username");
					String name = rs.getString("name");
					String surname = rs.getString("surname");
					String specialty = rs.getString("specialty");
					String addRow = dynPage.HTMLRowInfoDoctor(username,name,surname,specialty);
					table += addRow;
				}
				
				request.setAttribute("table", table);
		        request.getRequestDispatcher("./info.jsp").forward(request, response);
		        
		        rs.close();
				con.close();
		        
			}	catch(SQLException sqle){
				dynPage.DynamicPageError(response, "Error while connecting to the database","./doctor.html");
				}
		}
		if (requestType.equalsIgnoreCase("Logout")){
			session.invalidate();
			dynPage.DynamicPageLogout(response, "You have logged out successfully.");
		}
	}

	private int SessionId(String username,HttpServletResponse response) throws IOException{
		int id=0;
		try {
			Connection con = datasource.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM doctor WHERE username=?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				id = rs.getInt("id");
			}
			
			rs.close();
			con.close();
			
		}	catch(SQLException sqle) {
			dynPage.DynamicPageError(response, "Can't access doctor's ID", "./doctor.html");
		}
		return(id);
	}
}
