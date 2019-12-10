package main;

import java.io.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/log.txt")
public class ServletLogger extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public ServletLogger() {
		super();
	}
	
	public static void log(Object o, String msg) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		try {
			File f = new File(System.getProperty("user.dir") + "/WebContent/web/" + "log.txt");
			//System.out.println(f.exists() + " " + f.getAbsolutePath());
			PrintWriter out = new PrintWriter(new FileOutputStream(f, true)); 
			
			out.println(dtf.format(now) + " " + o.getClass().getSimpleName() + " -> " + msg);
			out.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");  
		
		ServletOutputStream out = response.getOutputStream();  
		FileInputStream fin = new FileInputStream(System.getProperty("user.dir") + "/WebContent/web/log.txt");
		
		BufferedInputStream bin = new BufferedInputStream(fin);  
		BufferedOutputStream bout = new BufferedOutputStream(out);  
		int ch =0; ;  
		while((ch=bin.read())!=-1)  
			bout.write(ch);  

		bin.close();  
		fin.close();  
		bout.close();  
		out.close(); 
	}
}
