package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

public class DynamicPages extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public DynamicPages() {
        super();
    }
    
    //Dynamic Pages for Admin
    
    //	Register Patient
    public void DynamicPageAddPatient(HttpServletResponse response) throws IOException{
    	response.setContentType("text/html; charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	PrintWriter out = response.getWriter();
    	
    	out.println("<html>");
    	out.println("<head>");
    	out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
    	out.println("<title>Register Patient</title>");
    	out.println("</head>");
    	out.println("<body>");
    	out.println("<head>");
    	out.println("<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Open+Sans:400,700'>");
    	out.println("</head>");
    	out.println("<div id='register'>");
    	out.println("<h1>Register Patient</h1>");
    	out.println("<form>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"firstname\" placeholder=\"Name\"/><br>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"lastname\" placeholder=\"Surname\"/><br>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"username\" placeholder=\"Username\"/><br>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"password\" placeholder=\"Password\"/><br>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"AMKA\" placeholder=\"AMKA\"/><br>");
    	out.println("<input type=\"submit\" formaction=\"./adduser\" formmethod=\"post\" name=\"requestType\" value=\"Register Patient\">");
    	out.println("</form>");
    	out.println("<form>");
    	out.println("<input type=\"submit\" formaction=\"./admin.html\" value=\"Go Back\">");
    	out.println("</form>");
    	out.println("</div>");
    	out.println("</body>");
    	out.println("</html>");
    }
    
    //	Register Doctor
    public void DynamicPageAddDoctor(HttpServletResponse response) throws IOException{
    	response.setContentType("text/html; charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	PrintWriter out = response.getWriter();
    	
    	out.println("<html>");
    	out.println("<head>");
    	out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
    	out.println("<title>Register Doctor</title>");
    	out.println("</head>");
    	out.println("<body>");
    	out.println("<head>");
    	out.println("<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Open+Sans:400,700'>");
    	out.println("</head>");
    	out.println("<div id='register'>");
    	out.println("<h1>Register Doctor</h1>");
    	out.println("<form>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"firstname\" placeholder=\"Name\"/><br>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"lastname\" placeholder=\"Surname\"/><br>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"username\" placeholder=\"Username\"/><br>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"password\" placeholder=\"Password\"/><br>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"specialty\" placeholder=\"Specialty\"/><br>");
    	out.println("<input type=\"submit\" formaction=\"./adduser\" formmethod=\"post\" name=\"requestType\" value=\"Register Doctor\">");
    	out.println("</form>");
    	out.println("<form>");
    	out.println("<input type=\"submit\" formaction=\"./admin.html\" value=\"Go Back\">");
    	out.println("</form>");
    	out.println("</div>");
    	out.println("</body>");
    	out.println("</html>");
    }
      
    //	Remove Doctor
    public void DynamicPageRemoveDoctor(HttpServletResponse response) throws IOException{
    	response.setContentType("text/html; charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	PrintWriter out = response.getWriter();
    	
    	out.println("<html>");
    	out.println("<head>");
    	out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
    	out.println("<title>Remove Doctor</title>");
    	out.println("</head>");
    	out.println("<body>");
    	out.println("<head>");
    	out.println("<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Open+Sans:400,700'>");
    	out.println("</head>");
    	out.println("<div id='register'>");
    	out.println("<h1>Remove Doctor</h1>");
    	out.println("<form>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"firstname\" placeholder=\"Name\"/><br>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"lastname\" placeholder=\"Surname\"/><br>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"username\" placeholder=\"Username\"/><br>");
    	out.println("<input type=\"text\" size=\"40\" maxlength=\"40\" name=\"specialty\" placeholder=\"Specialty\"/><br>");
    	out.println("<input type=\"submit\" formaction=\"./removeuser\" formmethod=\"post\" name=\"requestType\" value=\"Remove Doctor\">");
    	out.println("</form>");
    	out.println("<form>");
    	out.println("<input type=\"submit\" formaction=\"./admin.html\" value=\"Go Back\">");
    	out.println("</form>");
    	out.println("</div>");
    	out.println("</body>");
    	out.println("</html>");
    }

    //Dynamic Tables
    //userinfo
    public String HTMLRowInfoPatient(String amka, String username, String name, String surname) {
		String row = "<tr>";
		row  += "<td>" + amka + "</td>";
		row  += "<td>" + username + "</td>";
		row  += "<td>" + name + "</td>";
		row  += "<td>" + surname + "</td>";
		row +="</tr>";
		return row;
	}
  
    //userappointments
    public String HTMLRowAppsPatient(Timestamp date, String docname, String docsurname, String docspecialty, String status){
		String row = "<tr>";
		row += "<td>" + date + "</td>";
		row += "<td>" + docname + "</td>";
		row += "<td>" + docsurname + "</td>";
		row += "<td>" + docspecialty + "</td>";
		row += "<td>" + status + "</td>";
 		row += "</tr>";
		return row;
	}
    
    //	user_schedule
   	public String HTMLRowDocsPatient(String date, String docname, String docsurname) {
		String row = "<tr>";
		row += "<td><input type=\"radio\" name=\"date\" value=" + date + ">" + date.substring(0,10)+" "+date.substring(10,18) + "</td>";
		row += "<td>" + docname + "</td>";
		row += "<td>" + docsurname + "</td>";
		row += "</tr>";
		return row;
	}
    
   	
   	//	doc_info
   	public String HTMLRowInfoDoctor(String u, String n, String s, String sp) {
		String row = "<tr>";
		row  += "<td>" + u + "</td>";
		row  += "<td>" + n + "</td>";
		row  += "<td>" + s + "</td>";
		row  += "<td>" + sp + "</td>";
		row  += "</tr>";
		return(row);
	}
   	
   	
   	// see appointments as doc
    public String HTMLRowAppsDoctor(Timestamp date, String amka, String name, String surname){
		String row = "<tr>";
		row += "<td>" + date + "</td>";
		row += "<td>" + amka + "</td>";
		row += "<td>" + name + "</td>";
		row += "<td>" + surname + "</td>";
 		row += "</tr>";
		return row;
	}
    
    
    // remove appointments as doc 
    public String HTMLRowDeleteDoctor(String date, String amka, String name, String surname){
		String row = "<tr>";
		row += "<td><input type=\"radio\" name=\"date\" value=" + date + ">" + date.substring(0,10)+" "+date.substring(10) + "</td>";
		row += "<td>" + amka + "</td>";
		row += "<td>" + name + "</td>";
		row += "<td>" + surname + "</td>";
 		row += "</tr>";
		return row;
	}

    //Dynamic Pages for Everyone
    //Error Page + path
    public void DynamicPageError(HttpServletResponse response, String message, String path) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
		out.println("<title>Error</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<head>");
    	out.println("<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Open+Sans:400,700'>");
    	out.println("</head>");
		out.println("<div id=\"dynamic\">");
		out.println("<h1>" + message + "</h1>");
		out.println("<form action=" + path + ">");
		out.println("<input type=\"submit\" value=\"Go Back\"></input>");
		out.println("</form>");
		out.println("</div>");
    	out.println("</body>");
    	out.println("</html>");
    }
    
    public void DynamicPageSuccess(HttpServletResponse response, String message, String path) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
		out.println("<title>Success!</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<head>");
    	out.println("<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Open+Sans:400,700'>");
    	out.println("</head>");
		out.println("<div id=\"dynamic\">");
		out.println("<h1>" + message + "</h1>");
		out.println("<form action=" + path + ">");
		out.println("<input type=\"submit\" value=\"Go Back\"></input>");
		out.println("</form>");
		out.println("</div>");
    	out.println("</body>");
    	out.println("</html>");
    }
    
    public void DynamicPageLogout(HttpServletResponse response, String message) throws IOException{
    	response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
		out.println("<title>Hospital Website</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<head>");
    	out.println("<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Open+Sans:400,700'>");
    	out.println("</head>");
    	out.println("<div id=\"dynamic\">");
		out.println("<h1>" + message + "</h1>");
		out.println("<form action=\"./index.html\">");
		out.println("<input type=\"submit\" value=\"Return to main page\"></input>");
		out.println("</form>");
		out.println("</div>");
    	out.println("</body>");
    	out.println("</html>");
    }

}