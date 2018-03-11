package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class RemoveUser extends HttpServlet {
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
	
    public RemoveUser() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String requestType= request.getParameter("requestType");
		boolean error = true;
		
		if (requestType == null) {
			dynPage.DynamicPageError(response, "Invalid request type.","./admin.html");
		}
		
		if (requestType.equalsIgnoreCase("Remove Doctor")){
			String name = request.getParameter("firstname");
			String surname = request.getParameter("lastname");
			String username = request.getParameter("username");
			String specialty = request.getParameter("specialty");
			
			if(name.isEmpty() || surname.isEmpty() || username.isEmpty() || specialty.isEmpty()){
				dynPage.DynamicPageError(response, "You have to fill all the tabs.","./admin.html");
				error = false;
			}
			if(error){
				try {
					Connection con = datasource.getConnection();
						
					PreparedStatement stmt = con.prepareStatement("DELETE FROM doctor WHERE username=? AND name=? AND surname=? AND specialty=?");
					stmt.setString(1, username);
					stmt.setString(2, name);
					stmt.setString(3, surname);
					stmt.setString(4, specialty);
					
					stmt.executeUpdate();
					
					dynPage.DynamicPageSuccess(response, "You have removed a doctor successfully.","./admin.html");
					
					con.close();
					
					}	catch(SQLException sqle){
						dynPage.DynamicPageError(response, "Error removing doctor. This doctor might not exist","./admin.html");
					}
			}
		}
	}
}
