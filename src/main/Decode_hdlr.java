package main;

import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/decode_hdlr")
public class Decode_hdlr extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Decode_hdlr() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		
		ServletLogger.log(this,"Entering Decode_hdlr... Checking image for encrypted messages.");
		
		if (!request.getPart("UploadFile").getSubmittedFileName().equals("")) {
			Part filePart = request.getPart("UploadFile");
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
			ServletLogger.log(this,"Handling POST AJAX request: " + fileName);
			
			Steganography decoder = new Steganography();
			
		} else {
			ServletLogger.log(this,"Warning, not image detected in request parameter, redirecting to home.");
			response.sendRedirect(request.getContextPath());
			return;
		}
	}

}
