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
 * Servlet implementation class issue_servlet
 */
@WebServlet("/issue_servlet")
public class issue_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public issue_servlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String selectedPublication = request.getParameter("Publications");
		String selectedPriceRange = request.getParameter("priceRange");

		String selectedBookId = request.getParameter("selectedBookId");
		String selectedMemberId = request.getParameter("selectedMemberId");
		int Depsit=100;
		String availabilityStatus = "";
		String color = "";
		boolean submitDisabled = true;

		String memberName = "";
		String memberColor = "";
//        ------------------------------------Check Quantity-------------------------------------------------

		if (selectedBookId != null) {
			int bookId = Integer.parseInt(selectedBookId);
			try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
				String sql = "SELECT available_quantity FROM books WHERE id = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, bookId);
				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					int availableQuantity = rs.getInt("available_quantity");
					if (availableQuantity > 0) {
						availabilityStatus = "Available";
						color = "lightgreen";
						submitDisabled = false;
					} else {
						availabilityStatus = "NA";
						color = "red";
					}
				}
			} catch (SQLException e) {
				throw new ServletException("Error checking book availability", e);
			}
		}

		if (selectedMemberId != null) {
			int memberId = Integer.parseInt(selectedMemberId);
			try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
				String sql = "SELECT name,deposit FROM members WHERE id = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, memberId);
				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					memberName = rs.getString("name");
					Depsit = rs.getInt("deposit");
					memberColor = "lightgreen"; // Set color if member is found
				} else {
					memberColor = "red"; // Indicate invalid member ID
				}
			} catch (SQLException e) {
				throw new ServletException("Error fetching member name", e);
			}
		}

//		----------------------------------------------------------------------------------------------------------------

		System.out.println(
				"selectedPublication: " + selectedPublication + " -> " + "selectedPriceRange: " + selectedPriceRange);

		out.println("<!doctype html>");
		out.println("<html> <head> <title> Issue Book </title>"
				+ "<link rel='stylesheet' href='style.css' media='all' />"
				+ "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">"
				+ "<style>" + ".removea{" + "text-decoration: none; color:black;" + "}"

				+ ".availability { font-weight: bold; }" + ".publications{color:white;font-weight:600;}" + "</style>"
				+ "</head>");

		out.println("<body class='bg'>");
		out.println("<center>");
		out.println("<br>");
		out.println(
				"<center><h1 style='color:white; font-family: 'Lobster', sans-serif;'>Library Management System!!</h1></center>");
		out.println("<br>");
		out.println("<br>");
		out.println("<div class='outer-container'>");
		out.println("<h2 style='color: white;'>Issue Book</h2>");
//------------------------------------------------------------------------------------------------------------------------------------------

		
		// issue book form
		out.println("<form action='issue_servlet' method='post'>");
		out.println(
				"<span style='color: white;'><label for='book_id '>Book Id:</lable></span> <input type='text' name='book_id' value='"
						+ (selectedBookId != null ? selectedBookId : "")
						+ "'book_id'  required > <span class='availability' id='availability'  style='color:" + color
						+ ";'>" + availabilityStatus + "</span>  <br><br>");

		out.println("<span style='color: white;'>Member Id:</span> " + "<input type='text' name='member_id' value='"
				+ (selectedMemberId != null ? selectedMemberId : "") + "' readonly> "
				+ "<span class='member-name' style='color:" + memberColor + "; font-weight: bold; '>" + memberName
				+ "</span><br><br>");

		out.println("<input oncick='lowdeposit()' type='submit' value='Submit' "
				+ "style='background-color: #1759f1; color: white; padding: 5px 40px; border: none; border-radius: 5px; cursor: pointer;' "
				+ (submitDisabled ? "disabled" : "") + ">");
		out.println("</form>");
		
		out.println("</div>");

		out.println("<br>");

		String errorMessage = (String) request.getAttribute("errorMessage");
		if (errorMessage != null) {
		    out.println("<span style='color:red;font-weight:600;font-size:20px;'>" + errorMessage + "</span><br>");
		}
		
		
		out.println("<br>");
		String msg=Depsit<50 ? "Low Diposit!!":"";
		
		out.println("<span style='color:red;font-weight:600;' >"+msg+"</span>");
		out.println("<br>");
		out.println("<br>");
		out.println("<a href='BooksServlet' class='button-link'>Go to Home</a>");
		out.println("<br>");

		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

			out.println("<div style='display:flex; justify-content:space-around;'>");

			out.println("<div style='display:flex;justify-content:center; flex-direction:column;'>");

			out.println("<h2 style='color:white;'>Available books</h2>");

			out.println(
					"<table  style='width: 55rem;'><tr><th>ID</th><th style='width: 50px;'>Title</th><th>Author</th><th>Total Quantity</th><th>Available Quantity</th><th>Issued Quantity</th> <th>Price</th> </tr>");

			String sql = "SELECT * FROM books";

			
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			
			while (rs.next()) {
				
				out.println("<tr>" + "<td><a href='issue_servlet?selectedBookId=" + rs.getInt("id")
						+ (selectedMemberId != null ? "&selectedMemberId=" + selectedMemberId : "")
						+ "'>" + rs.getInt("id") + "</a></td>" + "<td>" + rs.getString("title") + "</td>" + "<td>"
						+ rs.getString("author") + "</td>" + "<td>" + rs.getInt("total_quantity") + "</td>" + "<td>"
						+ rs.getInt("available_quantity") + "</td>" + "<td>" + rs.getInt("issued_quantity") + "</td>"
						+ "</td><td>" + rs.getInt("price") + "</td>"
						
						
						+ "</tr>");
			}

			out.println("</table>");

			out.println("</div>");

			out.println("<div style='display:flex;justify-content:center;'>");

			out.println("<div style='display:flex;flex-direction:column;align-items:center;'>");

			out.println("<h2 style='color:white;'> Members </h2>");

			out.println("<table>" + "<tr>" + "<th>ID</th>" + "<th>Name</th>" + "<th>Email</th>" + "<th>Contact</th>" + "<th>Deposite</th>"
					+ "</tr>");

			sql = "SELECT * FROM members";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				out.println("<tr>" + "<td><a href='issue_servlet?selectedMemberId=" + rs.getInt("id")
						+ (selectedBookId != null ? "&selectedBookId=" + selectedBookId : "")
						+ (selectedPublication != null && !selectedPublication.isEmpty()
								? "&Publications=" + selectedPublication
								: "")
						+ "'>" + rs.getInt("id") + "</a></td>" + "<td>" + rs.getString("name") + "</td>" + "<td>"
						+ rs.getString("email") + "</td>" 
						
						+"<td>" + rs.getString("contact") + "</td>" 
						
						+"<td>" + rs.getString("deposit") + "</td>" 
						
						
						+"</tr>");
			}
			out.println("</table>");

			out.println("</div>");

			out.println("</div>");
			out.println("</div>");

			out.println("<div style=' display:flex;justify-content:center;flex-direction:column;align-items:center;'>");
			out.println("<h2 style='color: white;'>Issued Books</h2>");

			out.println(
					"<table><tr><th>Issue Id</th><th>Book ID</th><th>Member Id</th><th>Issue Date</th><th>Return Date</th><th>Status</th></tr>");

			sql = "SELECT * FROM issued_books";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				out.println("<tr><td>" + rs.getInt("issue_id") + "</td><td>" + rs.getInt("book_id") + "</td><td>"
						+ rs.getInt("member_id") + "</td><td>" + rs.getDate("issue_date") + "</td><td>"
						+ rs.getDate("return_date") + "</td><td>" + rs.getString("status") + "</td></tr>");
			}
			out.println("</table>");

			out.println("</div>");

			out.println("</body></html>");
		} catch (SQLException e) {
			throw new ServletException("Error displaying books", e);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int bookId = Integer.parseInt(request.getParameter("book_id"));
		int memberId = Integer.parseInt(request.getParameter("member_id"));
		int Deposit = 0;

		
		// -------------------------------------------------------------------------------------------------------------------------------------------------------
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
			
			String getDepositSql ="SELECT deposit from members WHERE id=?";
			PreparedStatement getDepositStmt = conn.prepareStatement(getDepositSql);
			getDepositStmt.setInt(1, memberId);
			ResultSet getDepositRs = getDepositStmt.executeQuery();
			while(getDepositRs.next()) {
				Deposit = getDepositRs.getInt("deposit");
			}
			
			


			String brrowCheckSql = "SELECT COUNT(*) AS borrowed_count FROM issued_books WHERE member_id = ? AND status='Issued'";
			PreparedStatement borrowCheckStmt = conn.prepareStatement(brrowCheckSql);
			borrowCheckStmt.setInt(1, memberId);
			ResultSet borrowCheckRs = borrowCheckStmt.executeQuery();
			int bcount=0;
			while(borrowCheckRs.next()) {
				bcount=borrowCheckRs.getInt("borrowed_count");
			}
			if (bcount>=2) {

				// Borrow limit exceeded

				 request.setAttribute("errorMessage", "You cannot borrow a new book. Please return previously issued books.");
				    doGet(request, response);
				  return;
			}
			
			if (Deposit<1) {

				// Borrow limit exceeded

				 request.setAttribute("errorMessage", "You cannot borrow a new book. Please Add Deposit!!");
				    doGet(request, response);
				  return;
			}
			
			
			
//			if() {
//				
//			}

			String checkSql = "SELECT available_quantity FROM books WHERE id = ?";
			PreparedStatement checkStmt = conn.prepareStatement(checkSql);
			checkStmt.setInt(1, bookId);
			ResultSet rs = checkStmt.executeQuery();

			if (rs.next() && rs.getInt("available_quantity") > 0) {
				
				String issueSql = "INSERT INTO issued_books (book_id, member_id, issue_date, status) VALUES (?, ?, CURDATE(), 'Issued')";
				PreparedStatement issueStmt = conn.prepareStatement(issueSql);
				issueStmt.setInt(1, bookId);
				issueStmt.setInt(2, memberId);
				issueStmt.executeUpdate();

				String updateSql = "UPDATE books SET available_quantity = available_quantity - 1, issued_quantity = issued_quantity + 1 WHERE id = ?";
				PreparedStatement updateStmt = conn.prepareStatement(updateSql);
				updateStmt.setInt(1, bookId);
				updateStmt.executeUpdate();
				
				String updateMembersDeposit = "UPDATE members SET deposit = deposit-10 WHERE id= ?";
				PreparedStatement updateMemberStmt = conn.prepareStatement(updateMembersDeposit);
				updateMemberStmt.setInt(1, memberId);
				updateMemberStmt.executeUpdate();
				
				response.sendRedirect("issue_servlet");
			} else {
				
				  response.setContentType("text/html");
		            PrintWriter out = response.getWriter();
		            out.println("<!DOCTYPE html>");
		            out.println("<html><head><title>Book Unavailable</title></head><body>");
		            out.println("<h3 style='color: red;'>Sorry, this book is currently unavailable.</h3>");
		            out.println("<a href='issue_servlet'>Back to Issue Book</a>");
		            out.println("</body></html>");			}
					
		} catch (SQLException e) {
			throw new ServletException("Book issuance error", e);
		}

//     
	}

}
