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
 * Servlet implementation class BooksServlet
 */
@WebServlet("/BooksServlet")
public class BooksServlet extends HttpServlet {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
			String sql = "SELECT * FROM books";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

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
//			------------=--------------------------------------------------------------------------------------------
			out.println("<div style='display:flex;align-items:center;justify-content: space-around;'>");

			out.println("<a class='btn btn-primary' href='AddBookServlet' style='margin-right:20px;'>ADD BOOK</a>");
			out.println("<a class='btn btn-primary' href='AddMember' style='margin-right:20px;'>ADD MEMBER</a>");
			out.println("<a class='btn btn-primary' href='issue_servlet' style='margin-right:20px;'>Issue BOOK</a>");
			out.println("<a class='btn btn-primary' href='ReturnBookServlet' style='margin-right:20px;'>Return BOOK</a>");
			out.println("<a class='btn btn-primary' href='Required_book_data' style='margin-right:20px;'>BOOK Display Filter</a>");
			out.println("<a class='btn btn-primary' href='members_display' style='margin-right:20px;'>Members Display</a>");
			
			
			out.println("</div>");
			out.println("<br>");
			out.println("<br>");
//           -----------------------------------------------------------------------------------------

			out.println("<div style='display: flex; justify-content: space-around;'>");

			out.println("<div style='display:flex;flex-direction:column;align-items:center;'>");
			out.println("<h2 style='color:white;'>Available books</h2>");

			out.println(
					"<table><tr><th>ID</th><th>Title</th><th>Author</th><th>Total Quantity</th><th>Available Quantity</th><th>Issued Quantity</th> <th>Price</th></tr>");
			while (rs.next()) {
				out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("title") + "</td><td>"
						+ rs.getString("author") + "</td><td>" + rs.getInt("total_quantity") + "</td><td>"
						+ rs.getInt("available_quantity") + 
						"</td><td>" + rs.getInt("issued_quantity") + "</td>"
						+"</td><td>" + rs.getInt("price") + "</td>"

						+ "</tr>");
			}
			out.println("</table>");
			out.println("</div>");

			out.println("<br>");
			out.println("<br>");

			sql = "SELECT * FROM members";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			out.println("<div style='display:flex;flex-direction:column;align-items:center;'>");

			out.println("<h2 style='color:white;'> Members </h2>");
			out.println("<table>"
					+ "<tr>"
					+ "<th>ID</th>"
					+ "<th>Name</th>"
					+ "<th>Email</th>"
					+ "<th>Contact</th>"
					+ "</tr>");
			
			while (rs.next()) {
				System.out.println("ID: " + rs.getInt("id"));
			    System.out.println("Name: " + rs.getString("name"));
			    System.out.println("Email: " + rs.getString("email"));
			    System.out.println("Contact: " + rs.getString("contact"));
				out.println("<tr>"
						+ "<td>" + rs.getInt("id") + "</td>"
						+ "<td>" + rs.getString("name") + "</td>"
						+ "<td>" + rs.getString("email") + "</td>"
						+ "<td>" + rs.getString("contact") + "</td>"
						
						+ "</tr>");
			}
			out.println("</table>");
			out.println("</div>");

//------------------
			out.println("</div>");

			out.println("<br>");
			out.println("<br>");
			out.println("<div style='display:flex;flex-direction:column;align-items:center;'>");
			out.println("<h2 style='color:white;'> Issued Books </h2>");

			sql = "SELECT * FROM issued_books";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			out.println(
					"<table border='1'><tr><th>Issue Id</th><th>Book ID</th><th>Member Id</th><th>Issue Date</th><th>Return Date</th><th>Status</th>");
			while (rs.next()) {
				out.println("<tr><td>" + rs.getInt("issue_id") + "</td><td>" + rs.getInt("book_id") + "</td><td>"
						+ rs.getInt("member_id") + "</td><td>" + rs.getDate("issue_date") + "</td><td>"
						+ rs.getDate("return_date") + "</td><td>" + rs.getString("status") + "</tr>");
			}
			out.println("</table>");
			out.println("</div>");

			out.println("<br>");
			out.println("<br>");
			
			
			out.println("</body></html>");

			// response.sendRedirect("BooksServlet");
		} catch (SQLException e) {
			throw new ServletException("Error displaying books", e);
		}
	}
}
