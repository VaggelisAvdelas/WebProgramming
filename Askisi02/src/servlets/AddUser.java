package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class AddUser extends HttpServlet {
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
	
    public AddUser() {
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String requestType= request.getParameter("requestType");
		boolean error = true;
		
		if (requestType == null) {
			dynPage.DynamicPageError(response, "Invalid request type","./index.html");
		}
		
		if (requestType.equalsIgnoreCase("Register")){
			String name = request.getParameter("firstname");
			String surname = request.getParameter("lastname");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String amka = request.getParameter("AMKA");

			if(name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty()){
				dynPage.DynamicPageError(response, "You have to fill all the tabs.","./index.html");
				error = false;
			}
			else{
				try{
					Long.parseLong(amka);
				} catch(Exception e){
					error = false;
					dynPage.DynamicPageError(response,"AMKA must be a number.","./index.html");
				}
			}
			if(error){
				try {
					Connection con = datasource.getConnection();
						
					PreparedStatement stmt = con.prepareStatement("INSERT INTO patient VALUES(?,?,crypt(?,gen_salt('bf',8)),?,?)");
					stmt.setString(1,amka);
					stmt.setString(2, username);
					stmt.setString(3, password);
					stmt.setString(4, name);
					stmt.setString(5, surname);
						
					stmt.executeUpdate();
						
					HttpSession session = request.getSession();
					session.setAttribute("username", username);
					session.setAttribute("password", password);
						
					RequestDispatcher view = request.getRequestDispatcher("./patient.html");
					view.forward(request, response);
								
					con.close();
					
				}	catch(SQLException sqle) {
						dynPage.DynamicPageError(response, "Error inserting user in database, AMKA or Username may already exist.","./index.html");
				}		
			}		
			
		}	
		if (requestType.equalsIgnoreCase("Register Patient")){
			String name = request.getParameter("firstname");
			String surname = request.getParameter("lastname");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String amka = request.getParameter("AMKA");

			if(name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty()){
				dynPage.DynamicPageError(response, "You have to fill all the tabs.","./admin.html");
				error = false;
			}
			else{
				try{
					Long.parseLong(amka);
				} catch(Exception e){
					error = false;
					dynPage.DynamicPageError(response,"AMKA must be a number.","./admin.html");
				}
			}
			if(error){
				try {
					Connection con = datasource.getConnection();
						
					PreparedStatement stmt = con.prepareStatement("INSERT INTO patient VALUES(?,?,crypt(?,gen_salt('bf',8)),?,?)");
					stmt.setString(1,amka);
					stmt.setString(2, username);
					stmt.setString(3, password);
					stmt.setString(4, name);
					stmt.setString(5, surname);
						
					stmt.executeUpdate();
					
					dynPage.DynamicPageSuccess(response, "You have registered a patient successfully.","./admin.html");
					
					con.close();
					
				}	catch(SQLException sqle) {
						dynPage.DynamicPageError(response, "Error inserting user in database, AMKA or Username may already exist.","./admin.html");
				}
			}			
		}
		if (requestType.equalsIgnoreCase("Register Doctor")){
			String name = request.getParameter("firstname");
			String surname = request.getParameter("lastname");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String specialty = request.getParameter("specialty");
			
			if(name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty() || specialty.isEmpty()){
				dynPage.DynamicPageError(response, "You have to fill all the tabs.", "./admin.html");
				error = false;
			}
			if(error){
				try {
					Connection con = datasource.getConnection();
						
					PreparedStatement stmt = con.prepareStatement("INSERT INTO doctor VALUES(DEFAULT,?,crypt(?,gen_salt('bf',8)),?,?,?)");
					stmt.setString(1, username);
					stmt.setString(2, password);
					stmt.setString(3, name);
					stmt.setString(4, surname);
					stmt.setString(5, specialty);
						
					stmt.executeUpdate();
					
					dynPage.DynamicPageSuccess(response, "You have registered a doctor successfully.","./admin.html");

					con.close();
					
				}	catch(SQLException sqle) {
						dynPage.DynamicPageError(response, "Error inserting user in database, Username may already exist.","admin.html");
				}
			}			
		}
	}
}
