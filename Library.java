

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	
	public  static  void getAllMembers() throws SQLException  {
		
		String query="select * from members";
		
		Statement stmt = con.createStatement();
		ResultSet res = stmt.executeQuery(query);
		
		while(res.next()) {
			
			
			System.out.println(res.getInt("mem_id")+" "+res.getString("name")+" "+
					" "+res.getInt("phone")+" "+res.getString("membership"));
			
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
	
	System.out.println("--------------------");
	System.out.println("1. Display all books");
    System.out.println("2. Fetch book by ID");
    System.out.println("3. Add new book");
    System.out.println("4. Get all members");
   
    
    System.out.println("Enter your choice: ");
    
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
   
    default: System.out.println("Invalid choice");
   
    }
    
	}
    	catch(Exception e) {
		e.printStackTrace();
		System.out.println("problem");
    	}
    	
    }
}
	
    
    
    
	
	
	



