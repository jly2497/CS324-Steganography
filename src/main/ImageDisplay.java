package main;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/out.png")
public class ImageDisplay extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ImageDisplay() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/jpeg");  
		response.setContentType("image/jpg");  
		response.setContentType("image/png");  
		
		String image = request.getParameter("image");
		
		ServletOutputStream out = response.getOutputStream();  
		FileInputStream fin;
		
		/*if (image.equals("text")) {
			fin = new FileInputStream(System.getProperty("user.dir") + "/WebContent/web/images/tmp/tmp.png");
		} else if (image.equals("image")){
			fin = new FileInputStream(System.getProperty("user.dir") + "/WebContent/web/images/tmp/tmp2.png");
		} else {*/
			fin = new FileInputStream(System.getProperty("user.dir") + "/WebContent/web/images/tmp/out.png");
		//}

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
