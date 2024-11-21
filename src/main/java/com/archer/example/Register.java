package com.archer.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	    private static final long serialVersionUID = 1L;
	    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
	    private static final String USER = "root";
	    private static final String PASSWORD = "root";
	       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<!doctype= 'html'>");
		out.println("<html> <head> <title> Library Management System </title>"
				+ "<link rel='stylesheet' href='style.css' media='all' />"
				+"<link rel='stylesheet' href='login.css' media='all' />"
				+ "<link href=\"https://fonts.googleapis.com/css2?family=Lobster&family=Playwrite+CU:wght@100..400&display=swap\" rel=\"stylesheet\">\r\n"
				+"<link rel='stylesheet' href='login.css' media='all' />"
				+ "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">"
				
				
				+ "</head>");
		out.println("<body class='bg'>");
		out.println("<br>");
		out.println(
				"<center><h1 style='color:white; font-family: 'Lobster', sans-serif;'>Library Management System!!</h1></center>");
		out.println("<br>");
		out.println("<br>");
		
		out.println("<center>");
		out.println("<div class=\"signup\">");
		out.println("    <h1>Register</h1>");
		out.println("<form onsubmit='return validatePasswords()' action='Register' method='post'>");
		out.println("    <input class=\"email\" name='name' id='name' type=\"text\" placeholder=\"&nbsp;&nbsp;Name\">");
		out.println("<br><br>");
		out.println("    <input class=\"email\" name='email' id='email' type=\"email\" placeholder=\"&nbsp;&nbsp;Email\">");
		out.println("<br><br>");
		
		
		out.println("    <input class=\"email\" type=\"password\" name='password' id='password' placeholder=\"&nbsp;&nbsp;password\">");
		out.println("<br><br>");
		out.println("    <input class=\"email\" type=\"password\" name='cpassword' id='cpassword' placeholder=\"&nbsp;&nbsp;Confirm Password\">");
		out.println("<br><br>");
		out.println("    <div class=\"login-btn\"><a href=\"\"><input class='login-btn' type='submit' value='signup'></a></div>");
		out.println("<br><br>");
		out.println("    <span class=\"gosign\">Already have an account ? <a href='Login'>Login</a></span>");
		
		out.println("</center>");
		

		 out.println("<script>");
		    out.println("function validatePasswords() {");
		    out.println("    var password = document.getElementById('password').value;");
		    out.println("    var confirmPassword = document.getElementById('cpassword').value;");
		    out.println("    if (password !== confirmPassword) {");
		    out.println("        alert('Passwords do not match!');");
		    out.println("        return false;"); // Prevent form submission
		    out.println("    }");
		    out.println("    return true;"); // Allow form submission
		    out.println("}");
		    out.println("</script>");
		
		
		
		
		
		out.println("</body></html>");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String Name= request.getParameter("name");
		String Email= request.getParameter("email");
		String Password= request.getParameter("password");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
        	 String sql = "INSERT INTO users (name,email, password) VALUES (?, ?, ?)";
             PreparedStatement stmt = conn.prepareStatement(sql);
             stmt.setString(1, Name);
             stmt.setString(2, Email);
             stmt.setString(3, Password);
             
             stmt.executeUpdate();
             
             
             
             response.sendRedirect("Login");
        	
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
