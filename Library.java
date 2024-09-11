package com.library;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Library {
	
	static String url="jdbc:mysql://localhost:3306/library";
	static String un="root";
	static String pwd="Charan@119";
	static Connection con;
	static Statement stmt;
	static PreparedStatement pstmt;
	static ResultSet res;
	static Scanner sc=new Scanner(System.in); 
	
	
	
	public static void  displayAllBooks() {
		
		String query="select * from books";
		
		
		try {
			stmt=con.createStatement();
			res=stmt.executeQuery(query);
			
			while(res.next()){
				
				int book_id = res.getInt("book_id");
	            String title = res.getString("title");
	            String author = res.getString("author");
	            int copies = res.getInt("copies");
	            System.out.println(book_id + " " + title + " " + author + " " + copies);
				
			}
		} 
		catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public static void fetchBookById() throws Exception {
		
		String query="select * from books where book_id=?";
		
		System.out.println("Enter book id");
		int book_id=new Scanner(System.in).nextInt();
		
		
		pstmt=con.prepareStatement(query);
		pstmt.setInt(1,book_id);
		res=pstmt.executeQuery();
		
		if(res.next()) {
			
			System.out.println(res.getInt("book_id")+" "+res.getString("title")+" "+
				res.getString("author")+" "+res.getInt("copies"));
			
		}
		else {
			
			System.out.println("No records found");
		}
	
	}
	
	
	public static void addNewBook() throws SQLException {
		
		String query="insert into books(book_id,title,author,copies)values(?,?,?,?)";
		
		System.out.println("Enter the book_id");
		int book_id=sc.nextInt();
		sc.nextLine();
		
		System.out.println("Enter the title");
		String title=sc.nextLine();
		
		System.out.println("Enter the author");
		String author=sc.nextLine();
		
		System.out.println("Enter the copies");
		int copies=sc.nextInt();
		
		pstmt=con.prepareStatement(query);
		
		pstmt.setInt(1,book_id);
		pstmt.setString(2, title);
		pstmt.setString(3, author);
		pstmt.setInt(4, copies);
		
		int x=pstmt.executeUpdate();
		
		if(x>0) {
			
			System.out.println("Record added successfully");
		}
		else {
			System.out.println(" failed to add record");
		}
		
		
	}
	
	public static void getAllMembers() throws SQLException {
		String query="select * from members";
		
		stmt=con.createStatement();
		res=stmt.executeQuery(query);
		while(res.next()) {
			
			System.out.println(res.getInt("mem_id")+" "+res.getString("name")+" "+res.getInt("phn_num"));
			
		}
	}
	
	public static void addNewMember() throws SQLException {
		
		String query="insert into members(mem_id,name,phn_num)values(?,?,?)";
		
		System.out.println("Enter the mem_id");
		int mem_id=sc.nextInt();
		sc.nextLine();
		
		System.out.println("Enter the name");
		String name=sc.nextLine();
		
		System.out.println("Enter the phone number");
		int phn_num=sc.nextInt();
		
		pstmt=con.prepareStatement(query);
		pstmt.setInt(1, mem_id);
		pstmt.setString(2, name);
		pstmt.setInt(3, phn_num);
		int x=pstmt.executeUpdate();
		
		if(x>0) {
			System.out.println("New member added");
		}
		else {
			System.out.println("failed to add member");
		}
	}

	
	public static void deleteMember() throws SQLException {
		
		String checkMemeber="select * from members where mem_id=?";
		String deleteIssuedBooks="delete from issued_books where mem_id=?";
		String deleteMembers="delete from members where mem_id=? ";
		
		System.out.println("Enter memeber id");
		int mem_id=sc.nextInt();
		
		pstmt=con.prepareStatement(checkMemeber);
		pstmt.setInt(1,mem_id);
		res=pstmt.executeQuery();
		
		if(res.next()) {
			
			
			
			pstmt=con.prepareStatement(deleteIssuedBooks);
			pstmt.setInt(1, mem_id);
			pstmt.executeUpdate();
			
			pstmt=con.prepareStatement(deleteMembers);
			pstmt.setInt(1, mem_id);
			int x=pstmt.executeUpdate();
			
			if(x>0) {
				
				System.out.println("Memeber and Issued Books deleted successfully");
				
			}
			else {
				
				System.out.println("Failed to delete member");
			}
		}
		else {
			
			System.out.println("Member not found");
		}
	}
	
	
	public static void issueBook() throws SQLException{
		
		String checkAvailability="select availability,copies from books where book_id=?";
		String issueBook="insert into issued_books(mem_id,book_id,issue_date)values(?,?,?)";
		String updateBooksTable="update books set copies=? where book_id=?";
		
		System.out.println("Enter member id:");
		int mem_id=sc.nextInt();
		
		System.out.println("Enter book id:");
		int book_id=sc.nextInt();
		
		pstmt=con.prepareStatement(checkAvailability);
		pstmt.setInt(1, book_id);
		res=pstmt.executeQuery();
		
		if(res.next()) {
			
			int copies=res.getInt("copies");
			boolean isAvail=res.getBoolean("availability");
			
			if(isAvail && copies>0) {
				
				pstmt=con.prepareStatement(issueBook);
				pstmt.setInt(1, mem_id);
				pstmt.setInt(2, book_id);
				pstmt.setDate(3, new Date(System.currentTimeMillis()));
				int x=pstmt.executeUpdate();
				
				
			if(x>0) {
				
				pstmt=con.prepareStatement(updateBooksTable);
				pstmt.setInt(1, copies-x);
				pstmt.setInt(2, book_id);
				pstmt.executeUpdate();
				
				System.out.println("Book issued successfully.");
			}
			else {
				
				System.out.println("Failed to issue book.");
				}
			}
			else {
				System.out.println("Failed to issue book");
			}
			
		}
		else {
			System.out.println("Book not found");
		}
	}
	
	
	public static void returnBook() throws SQLException {
		
		String checkIssuedBooks="select * from issued_books where mem_id=? and book_id=?";
		String updateBookstable="update books set copies=copies+1 where  book_id=?";
		String deleteIssuedBook="delete from issued_books where mem_id=? and book_id=?";
		
		System.out.println("Enter member id");
		int mem_id=sc.nextInt();
		
		System.out.println("Enter book id");
		int book_id=sc.nextInt();
		
		
		pstmt=con.prepareStatement(checkIssuedBooks);
		pstmt.setInt(1, mem_id);
		pstmt.setInt(2, book_id);
		res=pstmt.executeQuery();
		
		if(res.next()) {
			
			pstmt=con.prepareStatement(updateBookstable);
			pstmt.setInt(1, book_id);
			pstmt.executeUpdate();
			
			pstmt=con.prepareStatement(deleteIssuedBook);
			pstmt.setInt(1, mem_id);
			pstmt.setInt(2, book_id);
			int x=pstmt.executeUpdate();
			
			if(x>0) {
				
				System.out.println("Book returned sucessfully");
			}
			else {
				System.out.println("Failed to return book");
				
			}
		}
		else {
			
			System.out.println("No record of book being issued to this member");
		}
	}
	
	
	
	

		
	public static void main(String[] args) throws SQLException {
		
	System.out.println("LIBRARY MANAGEMENT SYSTEM");
	System.out.println("-------------------------------");
		 
    try {	
	
	Class.forName("com.mysql.cj.jdbc.Driver");
	System.out.println("driver class loaded");
	
	con=DriverManager.getConnection(url,un,pwd);
	System.out.println("connection established");
	
	System.out.println("------------------------------");
	System.out.println("1. Display all books");
    System.out.println("2. Fetch book by ID");
    System.out.println("3. Add new book");
    System.out.println("4. Get all members details");
    System.out.println("5. Add new member");
    System.out.println("6. Delete a member");
    System.out.println("7. Issue book");
    System.out.println("8. Return book");
    
    System.out.println("Enter your choice:");
    
    int choice=sc.nextInt();
   
    
    switch(choice) {
    
    
    case 1:displayAllBooks();
    		break;
    
    case 2:fetchBookById();
    	   break;
    	   
    case 3:addNewBook();
    	   break;
    	  
    case 4:getAllMembers();
    	   break;
    	 
    case 5:addNewMember();
    	   break;
    	   
    case 6:deleteMember();
    	   break;
    	   
    case 7:issueBook();
    	   break;
    
    case 8:returnBook();
    	   break;
    	   
    default: System.out.println("Invalid choice");
   
    }
    
	}
    	catch(Exception e) {
		e.printStackTrace();
		System.out.println("problem");
    	}
    	
    }

	
}
	
    
    
    
	
	
	



