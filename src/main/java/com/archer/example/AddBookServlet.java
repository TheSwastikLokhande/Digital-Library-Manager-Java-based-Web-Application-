package com.archer.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class AddBookServlet
 */
@WebServlet("/AddBookServlet")
@MultipartConfig(location = "/tmp", maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 10, fileSizeThreshold = 1024)
public class AddBookServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");
		PrintWriter out = response.getWriter();
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
		out.println("<center>");
		
		out.println("<div class='outer-container'>");
		
		out.println("<h2 style='color:white;'>Add new Book</h2>");
		out.println("<form action='AddBookServlet' method='post' enctype='multipart/form-data'>");
		out.println("<input class='input-field' type='text' placeholder='Enter Name' name='title' id='title' required><br><br>");
		out.println("<input class='input-field' type='text' placeholder='Enter DIscription'  name='discription' id='discription' required><br><br>");
		
		out.println("<input class='input-field' type='text' placeholder='Enter Author' name='author'  id='author' required><br><br>");
		out.println("<input class='input-field' type='text' placeholder='Enter Publisher' name='publisher' id='publisher' required><br><br>");
		out.println("<input class='input-field' type='text' name='year' placeholder='Enter Year' required><br><br>");
		out.println("<input class='input-field' type='text' placeholder='Enter Total' name='total_quantity' required><br><br>");
		out.println("<input class='input-field' type='text' name='book_price' placeholder='Enter Book Price' required><br><br>");
		out.println("<input class='input-field' type='file' name='file' id='book_image' placeholder='Upload Image accept='image/*' required>");
		out.println("<br>");
		out.println("<br>");
		out.println("<input type='submit' value='Submit' style='background-color: #4CAF50; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px;'/>");
		
		out.println("</div>");
		
		
		out.println("<br>");
		out.println("<br>");
	
		
		
		out.println("<a href='BooksServlet' class='button-link'>Go to Home</a>");
		out.println("</center>");
		out.println("</form></body></html>");
		
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       System.out.println("in post");
      
       final String UPLOAD_DIRECTORY = "E:/archer info/java servlet/Issue_book_project/src/main/webapp/images/";
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
    	   
    	    uploadedImagePath = "images/" + fileName;
    	    
    	         } else {
           throw new ServletException("File upload failed. Ensure a file is selected.");
       }
       
       
       
		String title = request.getParameter("title");
		String discription = request.getParameter("discription");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        int year = Integer.parseInt(request.getParameter("year"));
        int totalQuantity = Integer.parseInt(request.getParameter("total_quantity"));
        int Book_Price= Integer.parseInt(request.getParameter("book_price"));
        
        
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
        	
        	System.out.println(title+","+ author+","+ publisher+","+ year+","+totalQuantity+","+ totalQuantity+","+Book_Price);
            String sql = "INSERT INTO books (title,discription, author, publisher, year, total_quantity, available_quantity,price,img) VALUES (?, ?, ?, ?, ?, ?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, discription);
            stmt.setString(3, author);
            stmt.setString(4, publisher);
            stmt.setInt(5, year);
            stmt.setInt(6, totalQuantity);
            stmt.setInt(7, totalQuantity);
            stmt.setInt(8, Book_Price);
            stmt.setString(9, uploadedImagePath);
            stmt.executeUpdate();
            response.sendRedirect("BooksServlet"); 
        } catch (SQLException e) {
            throw new ServletException("Error adding book", e);
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
