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
 * Servlet implementation class ReturnBookServlet
 */
@WebServlet("/ReturnBookServlet")
public class ReturnBookServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	response.setContentType("text/html");
		PrintWriter out = response.getWriter();
    	
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
		
		out.println("<center>");
		out.println("<div class='outer-container'>");
		out.println("<h2 style='color:white;'>Return A Book</h2>");
		out.println("<form action='ReturnBookServlet' method='post'>");
		out.println("<span style='color:white;'>Issue Id: </span> <input type='text' name='issue_id' id='issue_id' required><br><br>");
		out.println("<span style='color:white;'>Book Id: </span> <input type='text' name='book_id' id='book_id' required><br><br>");
		
		out.println("<input type='submit' value='Submit' style='background-color: #1794f1; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;'/>");
		out.println("</form>");
		out.println("</div>");
		out.println("<br>");
		out.println("<br>");
		out.println("<a href='BooksServlet' class='button-link'>Go to Home</a>");
		out.println("</center>");

		out.println("</body></html>");
		
	}

    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int issueId = Integer.parseInt(request.getParameter("issue_id"));
        int bookId = Integer.parseInt(request.getParameter("book_id"));
        int issue_quantity = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
        	 String sql = "SELECT issued_quantity FROM books WHERE id=?";
             PreparedStatement stmt = conn.prepareStatement(sql);
             stmt.setInt(1, bookId);
             ResultSet rs = stmt.executeQuery();
             
             while(rs.next()) {
            	 issue_quantity=rs.getInt("issued_quantity");
             }
             
        } catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        if(issue_quantity>=0) {
        	 try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                 String returnSql = "UPDATE issued_books SET return_date = CURDATE(), status = 'Returned' WHERE issue_id = ?";
                 PreparedStatement returnStmt = conn.prepareStatement(returnSql);
                 returnStmt.setInt(1, issueId);
                 returnStmt.executeUpdate();

                 String updateSql = "UPDATE books SET available_quantity = available_quantity + 1, issued_quantity = issued_quantity - 1 WHERE id = ?";
                 PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                 updateStmt.setInt(1, bookId);
                 updateStmt.executeUpdate();

                 response.sendRedirect("BooksServlet");
             } catch (SQLException e) {
                 throw new ServletException("Book return error", e);
             }
        }else {
            response.sendRedirect("BooksServlet");

        }

        
       
    }
}
