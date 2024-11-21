package com.archer.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Required_book_data
 */
@WebServlet("/Required_book_data")
public class Required_book_data extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
	private static final String USER = "root";
	private static final String PASSWORD = "root";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Required_book_data() {
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
        String[] selectedOptions = request.getParameterValues("selectedOptions");

		out.println("<!doctype= 'html'>");
		out.println("<html> <head> <title> Library Management System </title>"
				+ "<link rel='stylesheet' href='style.css' media='all' />"
				+ "<link href=\"https://fonts.googleapis.com/css2?family=Lobster&family=Playwrite+CU:wght@100..400&display=swap\" rel=\"stylesheet\">\r\n"

				+ "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">"
				+ "</head>");
		out.println("<body class='bg'>");
		out.println("<br>");
		out.println(
				"<center><h1 style='color:white; font-family: 'Lobster', sans-serif;'>Library Management System!!</h1></center>");
		out.println("<br>");
		out.println("<br>");
		out.println("</center>");
		
		out.println("<center>"
				+ "<h2 style='color:white;'>Available Books</h2>"
				
				+"<form action='Required_book_data' method='GET'>"
				
				+" <label>"
				+ "            <input type='checkbox' name='selectedOptions' value='id'>"
				+ "            <scpan style='color:white;'>ID"
				
				+ "            <input type='checkbox' name='selectedOptions' value='title'>"
				+ "            <scpan style='color:white;'>Name"
				
				+ "            <input type='checkbox' name='selectedOptions' value='price'>"
				+ "            <scpan style='color:white;'>Price"
				
				+ "            <input type='checkbox' name='selectedOptions' value='discription'>"
				+ "            Discription"
				
				+ "            <input type='checkbox' name='selectedOptions' value='img'>"
				+ "            <scpan style='color:white;'>Img"
				
				
				+ "        </label>"
				
				
				+"			<input type='submit' value='submit' onclick='getSelectedCheckboxes()'>"
				
				+"</form>"
				
				
				+ "</center>");
		
		
		if (selectedOptions != null && selectedOptions.length > 0) {
			 try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
	                // Construct the SELECT query based on selected options
	                String selectedOptionsString = String.join(", ", selectedOptions);
	                String sql = "SELECT " + selectedOptionsString + " FROM Books;";
	                PreparedStatement stmt = conn.prepareStatement(sql);
	                ResultSet rs = stmt.executeQuery();

	                // Display the results in a table
	                out.println("<br><center><table  class='table table-bordered' style='width:fit-content; height:400px;'>");
	                out.println("<thead><tr>");
	                for (String option : selectedOptions) {
	                    out.println("<th>" + option.toUpperCase() + "</th>");
	                }
	                out.println("</tr></thead><tbody>");
	                
	                // Populate the table rows with data from the result set
	                while (rs.next()) {
	                    out.println("<tr>");
	                    for (String option : selectedOptions) {
	                    	
	                    	if(option.equals("img")){
	                    	    String imgPath = rs.getString(option);
	                    	    if (imgPath != null && !imgPath.trim().isEmpty()) {
	                    	        out.println("<td><img src='" + imgPath + "' width='70' height='70'> </td>");
	                    	    } else {
	                    	        out.println("<td>No Image</td>");
	                    	    }
	                    	}else
	                    	{
	                        out.println("<td>" + rs.getString(option) + "</td>");
	                    	}
	                    }
	                    out.println("</tr>");
	                }
	                out.println("</tbody></table></center>");
	            } catch (SQLException e) {
	                e.printStackTrace();
	                out.println("<h3>Error accessing the database</h3>");
	            }
	            
		}else {
            out.println("<h3 style='color:white;'>Please select at least one option to display data.</h3>");

		}
		  
		out.println("<a href='BooksServlet' class='button-link'>Go to Home</a>");

		
		out.println("</body></html>");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//            
	}

}
