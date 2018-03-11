package servlets;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;


public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings("unused")
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
	
    public AdminServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String requestType= request.getParameter("requestType");
		
		if (requestType == null) {
			dynPage.DynamicPageError(response, "Invalid request type.","./admin.html");
		}
		if (requestType.equalsIgnoreCase("Add Patient")){
			dynPage.DynamicPageAddPatient(response);
		}
		if (requestType.equalsIgnoreCase("Add Doctor")){
			dynPage.DynamicPageAddDoctor(response);
		}
		if (requestType.equalsIgnoreCase("Remove Doctor")){
			dynPage.DynamicPageRemoveDoctor(response);
		}
		if (requestType.equalsIgnoreCase("Logout")){
			HttpSession session = request.getSession(false);
			session.invalidate();
			dynPage.DynamicPageLogout(response, "You have logged out successfully.");
		}
	}

}
