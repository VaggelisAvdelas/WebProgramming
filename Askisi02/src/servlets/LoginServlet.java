package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	private DataSource datasource = null;
	
	DynamicPages dynPage = new DynamicPages();
	
	//Arxikopoihsh sundeshs me vash dedomenwn
	public void init() throws ServletException {
		try {
			InitialContext ctx = new InitialContext();
			datasource = (DataSource)ctx.lookup("java:comp/env/jdbc/LiveDataSource");
		} catch(Exception e) {
			throw new ServletException(e.toString());
		}
	}
	
    public LoginServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String requestType= request.getParameter("requestType");	
		
		if (requestType == null) {
			dynPage.DynamicPageError(response, "Invalid request type","./index.html");
		}
		if (requestType.equalsIgnoreCase("Login")){
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			char user=' ';
			boolean checkUsername = true;
			boolean checkPassword = true;
			
			try{
				Connection con = datasource.getConnection();
				
				//Patient Table
				PreparedStatement stmt = con.prepareStatement("SELECT * FROM patient WHERE username=? AND password=crypt(?, password)");
				stmt.setString(1, username);
				stmt.setString(2, password);				
				ResultSet rs = stmt.executeQuery();
				while(rs.next()){
						checkUsername = false;
						checkPassword = false;
						user = 'p';
				}
				
				//Doctor Table
				stmt = con.prepareStatement("SELECT * FROM doctor WHERE username=? AND password=crypt(?, password)");
				stmt.setString(1, username);
				stmt.setString(2, password);				
				rs = stmt.executeQuery();
				while(rs.next()){
						checkUsername = false;
						checkPassword = false;
						user = 'd';
				}
				
				//Admin table
				stmt = con.prepareStatement("SELECT * FROM admin WHERE username=? AND password=crypt(?, password)");
				stmt.setString(1, username);
				stmt.setString(2, password);				
				rs = stmt.executeQuery();
				while(rs.next()){
						checkUsername = false;
						checkPassword = false;
						user = 'a';
				}
				
				if (checkUsername && checkPassword){
					dynPage.DynamicPageError(response,"This user doesn't exist","./index.html");
				}
				
				else{
					HttpSession session = request.getSession();
					session.setAttribute("username", username);
					
					if(user=='p'){
						RequestDispatcher view = request.getRequestDispatcher("./patient.html");
						view.forward(request, response);
					}
					else if(user=='d'){
						RequestDispatcher view = request.getRequestDispatcher("./doctor.html");
						view.forward(request, response);
					}
					else if(user=='a'){
						RequestDispatcher view = request.getRequestDispatcher("./admin.html");
						view.forward(request, response);
					}
				}
				
				rs.close();
				con.close();
				
			}	catch(SQLException sqle) {
				sqle.printStackTrace();
			}		
		}
	}
}
