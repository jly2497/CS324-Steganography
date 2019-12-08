package main;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/account_hdlr")
public class Account_hdlr extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Account_hdlr() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DBConnector db;
		
		if (request.getParameter("submit").equals("Log In")) {
			String userName = request.getParameter("Username");
			ServletLogger.log(this,"User sign-in detected, processing user:" + userName);
			
			try {
				db = new DBConnector();
				
				if (db.userExists(userName)) {
					ServletLogger.log(this,"User sign-in accepted, logging in...");
					HttpSession session = request.getSession(true);
					
					session.setAttribute("Username", userName);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} else if (request.getParameter("submit").equals("Register")) {
			ServletLogger.log(this,"User registration accepted, processing account...");
			
			try {
				db = new DBConnector();
				String hashPassword = hash(request.getParameter("Password"));
				
				db.addUser(request.getParameter("Username"), request.getParameter("Email"), hashPassword);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		} else {
			ServletLogger.log(this,"User input validation failed- redirecting to home.");
			response.sendRedirect(request.getContextPath());
			return;
		}
	}
	private String hash(String password) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
	}
}
