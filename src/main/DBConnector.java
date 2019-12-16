package main;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.io.*;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class DBConnector {
	private Properties prop;
	private StandardPBEStringEncryptor encryptor;
	private InputStream in;
	
	public DBConnector() throws SQLException {
		encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword("*&%Y(*$#&UHETDF)hjeoiwty3829(*"); 
		prop = new Properties();
		
		try {
			in = new FileInputStream("config.properties");
			prop.load(in);
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private void close(ResultSet rs,Statement stmt, Connection conn) {
		if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
	    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
	}
	private void close(Statement stmt, Connection conn) {
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
	    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
	}
	
	//Check if user is present in db
	public boolean passwordMatch(String user,String password) throws SQLException {
		ServletLogger.log(this,"Checking if " + user + " exists in the database.");
		String query = "select * from user where username='" + user + "' and password='" + password + "'";
		ServletLogger.log(this,"Query: " + query);
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/steganography","root",encryptor.decrypt(prop.getProperty("dbpass")));
		ServletLogger.log(this,"Successfully connected to database.");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		if (rs.next()) {
			ServletLogger.log(this,"Row found. Result: " + rs.getString("username"));
			close(rs,stmt,conn);
			return true;
		} else {
			ServletLogger.log(this,user + " does not exist.");
			close(rs,stmt,conn);
			return false;
		}
	}
	
	//Check if user is present in db
	public boolean userExists(String user) throws SQLException {
		ServletLogger.log(this,"Checking if " + user + " exists in the database.");
		String query = "select * from user where username='" + user + "'";
		ServletLogger.log(this,"Query: " + query);
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/steganography","root",encryptor.decrypt(prop.getProperty("dbpass")));
		ServletLogger.log(this,"Successfully connected to database.");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		if (rs.next()) {
			ServletLogger.log(this,"Row found. Result: " + rs.getString("username"));
			close(rs,stmt,conn);
			return true;
		} else {
			ServletLogger.log(this,user + " does not exist.");
			close(rs,stmt,conn);
			return false;
		}
	}
	
	//Add a new user to the db
	public boolean addUser(String user, String email, String password) throws SQLException {
		ServletLogger.log(this,"Adding " + user + " to the database.");
		
		if (!userExists(user)) {
			String query = "insert into user values ("
					+ "'" + user + "',"
					+ "'" +  email + "',"
					+ "'" +  password + "',"
					+ "default)";
			ServletLogger.log(this,"Query: " + query);
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/steganography","root",encryptor.decrypt(prop.getProperty("dbpass")));
			ServletLogger.log(this,"Successfully connected to database.");
			Statement stmt = conn.createStatement();
			
			
			int result = stmt.executeUpdate(query);
			
			if (userExists(user)) {
				ServletLogger.log(this,"Query executed successfully: " + result);
				close(stmt,conn);
				return true;
			} else {
				ServletLogger.log(this,"Query execution failed: " + result);
				close(stmt,conn);
			}
		} else {
			ServletLogger.log(this,"Query not executed, user already exists.");
		}
		return false;
	}
	
	//Removes user from db
	public boolean deleteUser(String user) throws SQLException {
		ServletLogger.log(this,"Deleting " + user + " from the database.");
		
		if (userExists(user)) {
			String query = "delete from user where username='" + user + "'";
			ServletLogger.log(this,"Query: " + query);
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/steganography","root",encryptor.decrypt(prop.getProperty("dbpass")));
			ServletLogger.log(this,"Successfully connected to database.");
			Statement stmt = conn.createStatement();
			int result = stmt.executeUpdate(query);
			
			if (!userExists(user)) {
				ServletLogger.log(this,"Query executed successfully: " + result);
				close(stmt,conn);
				return true;
			} else {
				ServletLogger.log(this,"Query execution failed: " + result);
				close(stmt,conn);
			}
		} else {
			ServletLogger.log(this,"Query not executed, user not found.");
		}
		return false;
	}
	
	//Check if user has this image
	public boolean imageExists(String user, String uniqueName) throws SQLException {
		ServletLogger.log(this,"Checking if " + user + " exists in the database.");
		
		String query = "select * from image where unique_name='" + uniqueName + "' and username='" + user + "'";
		ServletLogger.log(this,"Query: " + query);

		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/steganography","root",encryptor.decrypt(prop.getProperty("dbpass")));
		ServletLogger.log(this,"Successfully connected to database.");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		if (rs.next()) {
			ServletLogger.log(this,"Row found. Result: " + rs.getString("image_name"));
			close(rs,stmt,conn);
			return true;
		} else {
			ServletLogger.log(this,uniqueName + " associated with " + user + " does not exist.");
			close(rs,stmt,conn);
			return false;
		}
	}
	
	//Adds image to database, return unique file name
	public String addImage(String user, String fileName) throws SQLException {
		ServletLogger.log(this,"Adding " + fileName + " to the database.");
		
		String extension = fileName.split("\\.",0)[fileName.split("\\.",0).length - 1];
		SimpleDateFormat df = new SimpleDateFormat("ddMMyy-hhmmss.SSS.");
		
		String uniqueName = "file-" + df.format(new Date()) + extension;
		
		String query = "insert into image values ("
				+ "'" + fileName + "',"
				+ "'" +  uniqueName + "',"
				+ "'" +  user + "'"
				+ ")";
		ServletLogger.log(this,"Query: " + query);
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/steganography","root",encryptor.decrypt(prop.getProperty("dbpass")));
		ServletLogger.log(this,"Successfully connected to database.");
		Statement stmt = conn.createStatement();
		int result = stmt.executeUpdate(query);
		
		if (imageExists(user,uniqueName)) {
			ServletLogger.log(this,"Query executed successfully: " + result + ", file name: " + uniqueName);
			close(stmt,conn);
			return uniqueName;
		} else {
			ServletLogger.log(this,"Query execution failed: " + result);
			close(stmt,conn);
		}
		return "";
	}
	public boolean deleteImage(String user, String uniqueName) throws SQLException {
		ServletLogger.log(this,"Deleting " + uniqueName + " from the database.");
		
		if (imageExists(user, uniqueName)) {
			String query = "delete from image where unique_name='" + uniqueName + "' and username='" + user + "'";
			ServletLogger.log(this,"Query: " + query);
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/steganography","root",encryptor.decrypt(prop.getProperty("dbpass")));
			ServletLogger.log(this,"Successfully connected to database.");
			Statement stmt = conn.createStatement();
			int result = stmt.executeUpdate(query);
			
			if (!imageExists(user, uniqueName)) {
				ServletLogger.log(this,"Query executed successfully: " + result);
				close(stmt,conn);
				return true;
			} else {
				ServletLogger.log(this,"Query execution failed: " + result);
				close(stmt,conn);
			}
		} else {
			ServletLogger.log(this,"Query not executed, image not found.");
		}
		return false;
	}
	public static void main(String[] args)  {
		Properties prop = new Properties();
		
		try {
			FileInputStream in = new FileInputStream("config.properties");
			prop.load(in);
			DBConnector db = new DBConnector();
			System.out.println(db.userExists("Josh"));
		} catch(FileNotFoundException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
