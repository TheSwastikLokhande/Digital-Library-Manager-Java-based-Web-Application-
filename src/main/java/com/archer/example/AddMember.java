package com.archer.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class AddMember
 */
@WebServlet("/AddMember")
@MultipartConfig(location = "/tmp", maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 10, fileSizeThreshold = 1024)
public class AddMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
	    private static final String USER = "root";
	    private static final String PASSWORD = "root";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMember() {
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
    	
    	out.println("<!doctype html>");
		out.println("<html> <head> <title> Add Member  </title>"
				+ "<link rel='stylesheet' href='style.css' media='all' />"
    			+"<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">"
				+ "</head>");
		out.println("<body class='bg'>");
		out.println("<center>");
		out.println("<div class='outer-container'>");
		out.println("<h2 style='color: white;'>Add Member </h2>");
		out.println("<form action='AddMember' method='post' enctype='multipart/form-data'>");
		out.println("<span style='color: white;'>Name:</span> <input  type='text' name='name' id='name' required/><br><br>");
		out.println("<span style='color: white;'>Email:</span> <input  type='text' name='email' id='email' required/><br><br>");
		out.println("<span style='color: white;'>Contact:</span> <input type='text' name='contact' id='contact' required/><br><br>");
		out.println("<span style='color: white;'>Deposit:</span> <input type='text' name='deposit' id='deposit' required/><br><br>");
		out.println(" <input value='Upload File' type='file' name='file' id='member_image' accept='image/*' required /><span style='color:white;'> <br>Upload Book Image</span>");
		out.println("<br>");
		out.println("<br>");
		out.println("<input type='submit' value='Submit' style='background-color: #1794f1; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;'/>");
		out.println("</div>");
		out.println("<br>");
		out.println("<br>");
		out.println("<a href='BooksServlet' class='button-link'>Go to Home</a>");
		out.println("<br>");
		out.println("<h2 style='color: white;'>Members</h2>");
		
		 try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
	            String sql = "SELECT * FROM members";
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            ResultSet rs = stmt.executeQuery();
	            out.println("<table><tr><th>ID</th><th>Name</th><th>Email</th><th>Contact</th>");
	            while (rs.next()) {
	                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("email") + "</td><td>" + rs.getString("contact") + "</tr>");
	            }
	            out.println("</table>");
		 
		 
		 
		 
		 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		out.println("</center>");
		out.println("</form></body></html>");
		
	}

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	       final String UPLOAD_DIRECTORY = "E:/archer info/java servlet/Issue_book_project/src/main/webapp/user_images/";
	       String uploadedImagePath="";
	       File uploadDir = new File(UPLOAD_DIRECTORY);
	       if (!uploadDir.exists()) {
	           uploadDir.mkdir();
	       }
	      
	       Part filePart = request.getPart("file");
	       String fileName = getFileName(filePart);
	       
	       if (filePart != null && fileName != null && !fileName.isEmpty()) {
	           // Save the file to the upload directory
	    	   String filePath = UPLOAD_DIRECTORY + fileName;
	    	   filePart.write(filePath);
	    	   
	    	    uploadedImagePath = "user_images/" + fileName;
	    	    
	    	         } else {
	           throw new ServletException("File upload failed. Ensure a file is selected.");
	       }
		
		
		String name = request.getParameter("name");
        String email = request.getParameter("email");
        String contact = request.getParameter("contact");
        int deposit = Integer.parseInt(request.getParameter("deposit"));
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
        
        	String sql = "INSERT INTO members (name,email,contact,deposit,img) VALUES (?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, contact);
            stmt.setInt(4, deposit);
            stmt.setString(5, uploadedImagePath);
            stmt.executeUpdate();
            
            response.sendRedirect("BooksServlet");
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
	}
	
	private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 2, token.length() - 1);
            }
        }
        return "";
    }

}
