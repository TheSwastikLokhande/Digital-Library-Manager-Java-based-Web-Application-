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
 * Servlet implementation class members_display
 */
@WebServlet("/members_display")
public class members_display extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
	private static final String USER = "root";
	private static final String PASSWORD = "root";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public members_display() {
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
        
        // Default memberId, for example
        int memberId = 1; 

        // Get the memberId from request (if any)
        if (request.getParameter("memberId") != null) {
            memberId = Integer.parseInt(request.getParameter("memberId"));
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Fetch member details
            String sql = "SELECT * FROM members WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            out.println("<!doctype html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Library Management System</title>");
            out.println("<link rel='stylesheet' href='style.css' media='all' />");
            out.println("<link href='https://fonts.googleapis.com/css2?family=Lobster&family=Playwright+CU:wght@100..400&display=swap' rel='stylesheet'>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet' integrity='sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH' crossorigin='anonymous'>");
            out.println("</head>");
            out.println("<body class='bg'>");

            out.println("<br>");
            out.println("<center><h1 style='color:white; font-family: 'Lobster', sans-serif;'>Library Management System</h1></center>");
            out.println("<br><br>");

            // Form for submitting memberId
            out.println("<form id='memberForm' action='members_display' method='post'>");
            out.println("<input type='hidden' id='memberIdInput' name='memberId' value='" + memberId + "'>");
            out.println("</form>");

            if (rs.next()) {
                out.println("<center>");
                out.println("<div class='outer-container'>");
                out.println("<div class='member-details'>");
                out.println("<span class='color-white'>Member Img: <img src='" + rs.getString("img") + "' width='70' height='70'></span><br>");
                out.println("<span class='color-white'>Member Id: " + rs.getInt("id") + "</span>");
                out.println("<span class='color-white'>Name: " + rs.getString("name") + "</span>");
                out.println("<span class='color-white'>Email: " + rs.getString("email") + "</span>");
                out.println("<span class='color-white'>Contact: " + rs.getString("contact") + "</span>");
                out.println("<span class='color-white'>Deposit: " + rs.getString("deposit") + "</span>");
                out.println("</div>");
               
                out.println("</div>");
                
                out.println("<span class='color-white' style='color'>Books</span><br>");

                // Fetch issued books for the member
                sql = "SELECT books.img, issued_books.issue_id, books.title AS BookTitle, issued_books.issue_date, issued_books.return_date, issued_books.status " +
                      "FROM issued_books " +
                      "JOIN books ON issued_books.book_id = books.id " +
                      "WHERE issued_books.member_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, memberId);
                rs = stmt.executeQuery();

                out.println("<table style='height:fit-content;'>");
                out.println("<tr><th>Img</th><th>Issue ID</th><th>Book Title</th><th>Issue Date</th><th>Return Date</th><th>Status</th></tr>");
                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td><img src='" + rs.getString("img") + "' width='50' height='50'></td>");
                    out.println("<td>" + rs.getInt("issue_id") + "</td>");
                    out.println("<td>" + rs.getString("BookTitle") + "</td>");
                    out.println("<td>" + rs.getString("issue_date") + "</td>");
                    out.println("<td>" + rs.getString("return_date") + "</td>");
                    out.println("<td>" + rs.getString("status") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }

            out.println("<br><br>");
            out.println("<div class='button-container'>");
            out.println("<button type='button' id='firstButton' onclick='getFirst()' class='nav-button' title='First'>&#x23EE;</button>");
            out.println("<button type='button' id='previousButton' onclick='getPrevious()' class='nav-button' title='Previous'>&#x23F4;</button>");
            out.println("<input class='nav-button' id='inputfield'  type='text' placeholder='Enter the ID' />");
            out.println("<button type='button' id='nextButton' onclick='getNext()' class='nav-button' title='Next'>&#x23F5;</button>");
            out.println("<button type='button' id='lastButton' onclick='getLast()' class='nav-button' title='Last'>&#x23ED;</button>");
            out.println("</div>");
            
    		out.println("<a href='BooksServlet' class='button-link'>Go to Home</a>");

            out.println("</center>");

            // JavaScript code to handle button clicks and form submission
            out.println("<script>");
            out.println("let currentMemberId = " + memberId + ";");  // Track the current memberId
            int lastMemberId =0;

            sql = "SELECT id FROM members ORDER BY id DESC LIMIT 1";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
           if (rs.next()) {
                lastMemberId = rs.getInt("id");
               System.out.println("Last Member ID: " + lastMemberId);
               // You can use this lastMemberId as needed
           }
           
            out.println("let lastMemberId = " + lastMemberId + ";");
            

          
            out.println("function getFirst() {");
            out.println("  currentMemberId = 1;"); // First member's ID
            out.println("  document.getElementById('memberIdInput').value = currentMemberId;");
            out.println("  document.getElementById('memberForm').submit();");
            out.println("}");

            out.println("function getPrevious() {");
            out.println("  if (currentMemberId > 1) { currentMemberId--; }");
            out.println("  document.getElementById('memberIdInput').value = currentMemberId;");
            out.println("  document.getElementById('memberForm').submit();");
            out.println("}");

            out.println("function getNext() {");
            out.println("if(currentMemberId<lastMemberId){");
            out.println("  currentMemberId++;"); // Logic assumes next ID is valid
            out.println("}");
            out.println("  document.getElementById('memberIdInput').value = currentMemberId;");
            out.println("  document.getElementById('memberForm').submit();");
            out.println("}");

            out.println("function getLast() {");
            
            
           
            
            
            out.println("  currentMemberId ="+lastMemberId); // Replace with last ID logic if needed
            out.println("  document.getElementById('memberIdInput').value = currentMemberId;");
            
            out.println("if (currentMemberId === lastMemberId) {");
            out.println("  document.getElementById('nextButton').disabled = true;");
            out.println("  document.getElementById('lastButton').disabled = true;");
            out.println("}");
           
           
            out.println("  document.getElementById('memberForm').submit();");
            out.println("}");
            
            
            
            
            
            out.println("function updateMemberIdFromInput() {");
            out.println("  const inputField = document.getElementById('inputfield');");
            out.println("  const enteredId = parseInt(inputField.value);");
            out.println("  if (enteredId >= 1 && enteredId <= lastMemberId) {");
            out.println("    currentMemberId = enteredId;");
            out.println("    document.getElementById('memberIdInput').value = currentMemberId;");
            out.println("    document.getElementById('memberForm').submit();");
            out.println("  } else {");
            out.println("    alert('Please enter a valid member ID');");
            out.println("  }");
            out.println("}");

            // Bind the function to input field (e.g., pressing Enter)
            out.println("document.getElementById('inputfield').addEventListener('keydown', function(event) {");
            out.println("  if (event.key === 'Enter') {");
            out.println("    updateMemberIdFromInput();");
            out.println("  }");
            out.println("});");

            out.println("</script>");

            out.println("</body></html>");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		
		doGet(req, resp);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
}
